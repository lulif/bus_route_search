package com.gdxx.search.redis;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gdxx.dao.entity.LineMajor;
import com.gdxx.dao.entity.LineStation;
import com.gdxx.dao.mapper.LineMajorMapper;
import com.gdxx.dao.mapper.LineStationMapper;
import com.gdxx.search.constant.RedisKey;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-15 22:40
 * @description:
 **/
@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private LineMajorMapper lineMajorMapper;
    @Autowired
    private LineStationMapper lineStationMapper;


    public List<LineMajor> getAllLineMajor() {
        String key = RedisKey.GDXX_ROUTE_LINE_MAJOR_ALL;
        if (redisTemplate.hasKey(key)) {
            return (List<LineMajor>) redisTemplate.opsForValue().get(key);
        } else {
            List<LineMajor> lineMajorList = lineMajorMapper.selectList(Wrappers.lambdaQuery());
            redisTemplate.opsForValue().set(key, lineMajorList, 8, TimeUnit.HOURS);
            return lineMajorList;
        }
    }

    public List<LineStation> getLineStationLatLng() {
        String key = RedisKey.GDXX_ROUTE_LINE_STATION_LATLNG;
        if (redisTemplate.hasKey(key)) {
            return (List<LineStation>) redisTemplate.opsForValue().get(key);
        } else {
            List<LineStation> lineStationList = lineStationMapper.selectList(Wrappers.<LineStation>lambdaQuery().isNotNull(LineStation::getLat).isNotNull(LineStation::getLng));
            redisTemplate.opsForValue().set(key, lineStationList, 8, TimeUnit.HOURS);
            return lineStationList;
        }
    }

    public List<LineStation> getAllLineStation() {
        String key = RedisKey.GDXX_ROUTE_LINE_STATION_ALL;
        if (redisTemplate.hasKey(key)) {
            return (List<LineStation>) redisTemplate.opsForValue().get(key);
        } else {
            List<LineStation> lineStationList = lineStationMapper.selectList(Wrappers.lambdaQuery());
            redisTemplate.opsForValue().set(key, lineStationList,8, TimeUnit.HOURS);
            return lineStationList;
        }
    }

    public void searchHistoryAdd(String keyword) {
        String key = RedisKey.GDXX_ROUTE_SEARCH_HISTORY;
        redisTemplate.opsForZSet().add(key, keyword, System.currentTimeMillis());
    }

    public Set<String> getSearchHistory() {
        String key = RedisKey.GDXX_ROUTE_SEARCH_HISTORY;
        Long size = redisTemplate.opsForZSet().size(key);
        Set<String> set = Sets.newLinkedHashSet();
        if (size > 0) {
            set = redisTemplate.opsForZSet().reverseRange(key, 0, size);
        }
        return set;
    }

    public Boolean cleanSearchHistory() {
        String key = RedisKey.GDXX_ROUTE_SEARCH_HISTORY;
        return redisTemplate.delete(key);
    }
}
