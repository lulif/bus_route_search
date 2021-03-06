package com.gdxx.crawler.grab;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdxx.crawler.constant.Url;
import com.gdxx.dao.entity.LineMajor;
import com.gdxx.dao.entity.LineStation;
import com.gdxx.dao.mapper.LineMajorMapper;
import com.gdxx.dao.mapper.LineStationMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: grab
 * @author: lulif
 * @create: 2021-02-10 14:02
 * @description:
 **/
@Slf4j
@Component
public class LatLngGrab {

    private RestTemplate restTemplate = new RestTemplate();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private LineStationMapper lineStationMapper;

    @Autowired
    private LineMajorMapper lineMajorMapper;

    private boolean findOrNot = false;

    private int findLevel = 0;

    public void stationLatLngGet() {
        //站名-分类Id Map
        List<LineStation> stationList = lineStationMapper.selectList(Wrappers.<LineStation>lambdaQuery().isNull(LineStation::getLat).isNull(LineStation::getLng));
        Map<String, Long> nameClassify2MajorMap = Maps.newHashMap();
        for (LineStation lineStation : stationList) {
            nameClassify2MajorMap.put(lineStation.getName() + "-" + lineStation.getClassifyId(), lineStation.getMajorId());
        }
//       //测试开关
//        nameClassify2MajorMap.clear();
//        nameClassify2MajorMap.put("珠儿潭-147",147L);

        //主信息Id-线路名 Map
        List<LineMajor> majorList = CollectionUtils.isEmpty(nameClassify2MajorMap.values()) ? Lists.newArrayList() :
                lineMajorMapper.selectList(Wrappers.<LineMajor>lambdaQuery().in(LineMajor::getId, new HashSet<>(nameClassify2MajorMap.values())));
        Map<Long, String> major2NameMap = majorList.stream().collect(Collectors.toMap(LineMajor::getId, LineMajor::getName, (oldValue, newValue) -> newValue));
        nameClassify2MajorMap.forEach((k, v) -> {

            String[] str = k.split("-");
            //query附加词: (公交站)/(公交车站)/无/
            String query = str[0];
            //通过 站名 获取到 线路名
            String lineName = major2NameMap.get(v);
            String url = Url.BAIDU_API_LAT_LNG_URL + "&query={query}";
            Map<String, String> param = Maps.newHashMap();
            param.put("query", query);
            ResponseEntity<String> res = restTemplate.getForEntity(url, String.class, param);
            if (Objects.equals(res.getStatusCode(), HttpStatus.OK)) {
                Map<String, Object> resMap = Maps.newHashMap();
                try {
                    resMap = objectMapper.readValue(res.getBody(), Map.class);
                } catch (Exception e) {
                    log.error("{}解析站点数据失败", param.get("query"));
                }
                try {
                    List<Map> results = (List<Map>) resMap.get("results");
                    findOrNot = false;
                    findLevel = 0;
                    findStationLatLng(results, lineName, k);
                } catch (Exception e) {
                    log.error("{}经纬度数据解析失败", k);
                }

            }
        });
    }

    private void writeDB(Double lat, Double lng, String name, String classifyId, Integer findLevel) {
        LineStation lineStation = new LineStation();
        lineStation.setLat(lat);
        lineStation.setLng(lng);
        lineStation.setName(name);
        lineStation.setClassifyId(Long.valueOf(classifyId));
        lineStation.setFindLevel(findLevel);
        findOrNot = true;
        lineStationMapper.updateLatLngByNameAndClassifyId(lineStation);
        log.info("{}-{}经纬度数据更新成功", name, classifyId);
    }

    private void findStationLatLng(List<Map> results, String lineName, String key) {
        for (Map<String, Object> resultMap : results) {
            String address = (String) resultMap.get("address");
            String name = (String) resultMap.get("name");
            Map<String, Double> locationMap = (Map<String, Double>) resultMap.get("location");
            if (!findOrNot && !StringUtils.isEmpty(address) && Objects.nonNull(locationMap)) {
                String[] strArr = key.split("-");
                Double lat = locationMap.get("lat");
                Double lng = locationMap.get("lng");
                if (lineName.contains("路") || lineName.contains("快")
                        || lineName.contains("早") || lineName.contains("晚")
                        ||lineName.contains("外")||lineName.contains("内")
                        ||lineName.contains("路内环")) {
                    lineName = lineName.substring(0, lineName.length() - 1);
                }
                if (findLevel == 0 && address.contains(lineName)) {
                    writeDB(lat, lng, strArr[0], strArr[1], findLevel);
                }
                if (findLevel == 1 && Objects.equals(strArr[0], name)) {
                    writeDB(lat, lng, strArr[0], strArr[1], findLevel);
                }
                if (findLevel == 2 && name.contains(strArr[0])) {
                    writeDB(lat, lng, strArr[0], strArr[1], findLevel);
                }
            }
        }
        if (!findOrNot) {
            ++findLevel;
            if (findLevel > 2) {
                return;
            }
            findStationLatLng(results, lineName, key);
        }
    }

}
