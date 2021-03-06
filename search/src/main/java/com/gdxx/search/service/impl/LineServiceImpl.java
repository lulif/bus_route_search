package com.gdxx.search.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gdxx.dao.entity.LineClassify;
import com.gdxx.dao.entity.LineMajor;
import com.gdxx.dao.entity.LineStation;
import com.gdxx.dao.mapper.LineClassifyMapper;
import com.gdxx.dao.mapper.LineMajorMapper;
import com.gdxx.dao.mapper.LineStationMapper;
import com.gdxx.search.dto.common.StationDTO;
import com.gdxx.search.dto.nearby.NearByLineClassifyDTO;
import com.gdxx.search.dto.nearby.NearByLineDTO;
import com.gdxx.search.dto.nearby.NearByStationLineDTO;
import com.gdxx.search.dto.search.SearchLineClassifyDTO;
import com.gdxx.search.dto.search.SearchLineDTO;
import com.gdxx.search.dto.search.SearchStationLineDTO;
import com.gdxx.search.redis.RedisService;
import com.gdxx.search.service.LineService;
import com.gdxx.search.utils.CalculateUtil;
import com.google.common.collect.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-12 18:58
 * @description:
 **/
@Service
public class LineServiceImpl implements LineService {

    private final static double NEARBY_DISTANCE = 1.0d;

    @Autowired
    private LineMajorMapper lineMajorMapper;
    @Autowired
    private LineStationMapper lineStationMapper;
    @Autowired
    private LineClassifyMapper lineClassifyMapper;
    @Autowired
    private RedisService redisService;

    @Override
    public List<NearByStationLineDTO> getNearbyStationAndLine(Double currentLat, Double currentLng) {
        List<LineMajor> lineMajorList = redisService.getAllLineMajor();
        Map<Long, String> majorId2NameMap = lineMajorList.stream().collect(Collectors.toMap(LineMajor::getId, LineMajor::getName));
        Map<String, NearByStationLineDTO> name2DTOMap = Maps.newHashMap();
        List<LineStation> lineStationList = redisService.getLineStationLatLng();
        lineStationList.forEach(station -> {
            double distance = CalculateUtil.getDistance(currentLat, currentLng, station.getLat(), station.getLng());
            if (distance <= NEARBY_DISTANCE) {
                String stationName = station.getName();
                Set<String> lineNameSet;
                if (!name2DTOMap.containsKey(stationName)) {
                    lineNameSet = Sets.newHashSet(majorId2NameMap.get(station.getMajorId()));
                    NearByStationLineDTO nearByStationLineDTO = new NearByStationLineDTO();
                    BeanUtils.copyProperties(station, nearByStationLineDTO);
                    nearByStationLineDTO.setDistance(distance);
                    nearByStationLineDTO.setLineNameList(lineNameSet);
                    name2DTOMap.put(stationName, nearByStationLineDTO);
                } else {
                    name2DTOMap.get(stationName).getLineNameList().add(majorId2NameMap.get(station.getMajorId()));
                }
            }
        });
        List<NearByStationLineDTO> sortList = Lists.newArrayList(name2DTOMap.values());
        sortList = sortList.stream().sorted(Comparator.comparingDouble(NearByStationLineDTO::getDistance)).collect(Collectors.toList());
        return sortList;
    }

    @Override
    public List<NearByLineDTO> getLineByStation(String stationName) {
        List<NearByLineDTO> resDTOList = Lists.newArrayList();
        List<LineStation> lineStationList = lineStationMapper.selectList(Wrappers.<LineStation>lambdaQuery().eq(LineStation::getName, stationName));
        List<Long> majorIdList = lineStationList.stream().filter(distinctByKey(s -> s.getMajorId())).map(LineStation::getMajorId).collect(Collectors.toList());
        List<LineMajor> lineMajorList = CollectionUtils.isEmpty(majorIdList) ? Lists.newArrayList() :
                lineMajorMapper.selectList(Wrappers.<LineMajor>lambdaQuery().in(LineMajor::getId, majorIdList));
        List<LineClassify> lineClassifyList = CollectionUtils.isEmpty(majorIdList) ? Lists.newArrayList() :
                lineClassifyMapper.selectList(Wrappers.<LineClassify>lambdaQuery().in(LineClassify::getMajorId, majorIdList));
        Multimap<Long, LineClassify> majorId2ClassifyMap = ArrayListMultimap.create();
        lineClassifyList.forEach(classify -> majorId2ClassifyMap.put(classify.getMajorId(), classify));
        lineMajorList.forEach(major -> {
            NearByLineDTO nearByLineDTO = new NearByLineDTO();
            BeanUtils.copyProperties(major, nearByLineDTO);
            List<NearByLineClassifyDTO> classifyDTOS = Lists.newArrayList();
            majorId2ClassifyMap.get(major.getId()).forEach(classify -> {
                NearByLineClassifyDTO classifyDTO = new NearByLineClassifyDTO();
                BeanUtils.copyProperties(classify, classifyDTO);
                classifyDTOS.add(classifyDTO);
            });
            nearByLineDTO.setClassifyDTOList(classifyDTOS);
            resDTOList.add(nearByLineDTO);
        });
        return resDTOList;
    }

    @Override
    public List<StationDTO> getLineStationList(Long classifyId) {
        List<StationDTO> resDTOList = Lists.newArrayList();
        List<LineStation> lineStationList = lineStationMapper.selectList(Wrappers.<LineStation>lambdaQuery()
                .eq(LineStation::getClassifyId, classifyId).orderByAsc(LineStation::getId));
        lineStationList.forEach(station -> {
            StationDTO nearByStationDTO = new StationDTO();
            BeanUtils.copyProperties(station, nearByStationDTO);
            resDTOList.add(nearByStationDTO);
        });
        return resDTOList;
    }

    @Override
    public SearchStationLineDTO searchStationLine(String keyword) {
        redisService.searchHistoryAdd(keyword);
        SearchStationLineDTO resDTO = new SearchStationLineDTO();
        List<LineStation> stationList = redisService.getAllLineStation();
        Set<String> stationNameSet = Sets.newHashSet();
        List<StationDTO> resStationList = Lists.newArrayList();
        stationList.forEach(item -> {
            if (item.getName().contains(keyword) && !stationNameSet.contains(item.getName())) {
                stationNameSet.add(item.getName());
                StationDTO stationDTO = new StationDTO();
                BeanUtils.copyProperties(item, stationDTO);
                resStationList.add(stationDTO);
            }
        });
        resDTO.setStationList(resStationList);

        Set<String> lineNameSet = Sets.newHashSet();
        List<LineMajor> lineList = redisService.getAllLineMajor();
        lineList.forEach(item -> {
            if (item.getName().contains(keyword)) {
                lineNameSet.add(item.getName());
            }
        });
        resDTO.setLineList(lineNameSet);
        return resDTO;
    }

    @Override
    public SearchLineDTO getSearchLineDetail(String lineName) {
        SearchLineDTO searchLineDTO = new SearchLineDTO();
        LineMajor lineMajor = lineMajorMapper.selectOne(Wrappers.<LineMajor>lambdaQuery().eq(LineMajor::getName, lineName));
        Long majorId = lineMajor.getId();
        BeanUtils.copyProperties(lineMajor, searchLineDTO);
        List<LineClassify> classifyList = lineClassifyMapper.selectList(Wrappers.<LineClassify>lambdaQuery().eq(LineClassify::getMajorId, majorId));
        List<LineStation> stationList = lineStationMapper.selectList(Wrappers.<LineStation>lambdaQuery().eq(LineStation::getMajorId, majorId).orderByAsc(LineStation::getId));
        List<SearchLineClassifyDTO> resClassifyDTOList = Lists.newLinkedList();
        classifyList.forEach(classify -> {
            SearchLineClassifyDTO classifyDTO = new SearchLineClassifyDTO();
            BeanUtils.copyProperties(classify, classifyDTO);
            List<StationDTO> resStationList = Lists.newArrayList();
            stationList.forEach(station -> {
                if (Objects.equals(station.getClassifyId(), classify.getId())) {
                    StationDTO stationDTO = new StationDTO();
                    BeanUtils.copyProperties(station, stationDTO);
                    resStationList.add(stationDTO);
                }
            });
            classifyDTO.setStationList(resStationList);
            resClassifyDTOList.add(classifyDTO);
        });
        searchLineDTO.setClassifyDTOList(resClassifyDTOList);
        return searchLineDTO;
    }

    /**
     * 实体类根据某个属性去重
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = Maps.newConcurrentMap();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Override
    public Set<String> getSearchHistory() {
        return redisService.getSearchHistory();
    }

    @Override
    public Boolean cleanSearchHistory() {
        return redisService.cleanSearchHistory();
    }
}
