package com.gdxx.search.controller;

import com.gdxx.dao.entity.LineStation;
import com.gdxx.search.common.Response;
import com.gdxx.search.constant.IRoute;
import com.gdxx.search.dto.route.RouteDTO;
import com.gdxx.search.enums.ResEnum;
import com.gdxx.search.redis.RedisService;
import com.gdxx.search.service.LineService;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-27 22:05
 * @description:
 **/
@RestController
public class RouteController {

    @Autowired
    private LineService lineService;

    @Autowired
    private RedisService redisService;

    /**
     * 站点下拉提示
     *
     * @return
     */
    @GetMapping(IRoute.ROUTE_STATION_AUTO_COMPLETE)
    public Response<Set<String>> routeStationSupport(String stationName) {
        if (StringUtils.isEmpty(stationName)) {
            return Response.newSuccessResponse(Sets.newHashSet());
        }
        List<LineStation> allStation = redisService.getAllLineStation();
        Set<String> stationNameList = allStation.stream().filter(lineStation -> lineStation.getName().contains(stationName)).map(LineStation::getName).collect(Collectors.toSet());
        return Response.newSuccessResponse(stationNameList);
    }

    /**
     * 路线规划
     */
    @GetMapping(IRoute.ROUTE_PLAN_GET)
    public Response<RouteDTO> getRoutePlan(String origin, String terminal) {
        if (!checkStation(origin, terminal)) {
            return Response.newFailedResponse(ResEnum.NOT_FIND_STATION);
        }
        return null;
    }

    /**
     * 起/终点校验
     */
    private boolean checkStation(String origin, String terminal) {
        if (StringUtils.isEmpty(origin) || StringUtils.isEmpty(terminal)) {
            return false;
        }
        List<LineStation> allStation = redisService.getAllLineStation();
        boolean isFindOrigin = false;
        boolean isFindTerminal = false;
        for (LineStation lineStation : allStation) {
            if (!isFindOrigin || !isFindTerminal) {
                if (Objects.equals(lineStation.getName(), origin)) {
                    isFindOrigin = true;
                }
                if (Objects.equals(lineStation.getName(), terminal)) {
                    isFindTerminal = true;
                }
            } else {
                return true;
            }
        }
        return false;
    }


}
