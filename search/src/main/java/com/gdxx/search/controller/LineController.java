package com.gdxx.search.controller;

import com.gdxx.search.common.Response;
import com.gdxx.search.constant.ILine;
import com.gdxx.search.dto.common.StationDTO;
import com.gdxx.search.dto.nearby.NearByLineDTO;
import com.gdxx.search.dto.nearby.NearByStationLineDTO;
import com.gdxx.search.enums.ResEnum;
import com.gdxx.search.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-12 18:42
 * @description:
 **/
@RestController
public class LineController {

    @Autowired
    private LineService lineService;

    /**
     * 获取附近站点及其线路
     *
     * @return
     */
    @GetMapping(ILine.NEARBY_STATION_LINE_GET)
    public Response<List<NearByStationLineDTO>> getNearbyStationAndLine(Double currentLat,
                                                                        Double currentLng) {
        if (Objects.isNull(currentLat) || Objects.isNull(currentLng)) {
            return Response.newFailedResponse(ResEnum.LOCATION_FAIL);
        }
        return Response.newSuccessResponse(lineService.getNearbyStationAndLine(currentLat, currentLng));
    }

    /**
     * 根据站名获取线路信息
     *
     * @param stationName
     * @return
     */
    @GetMapping(ILine.LINE_GET_BY_STATION)
    public Response<List<NearByLineDTO>> getLineByStation(String stationName) {
        if (StringUtils.isEmpty(stationName)) {
            return Response.newFailedResponse(ResEnum.PARAM_ERROR);
        }
        return Response.newSuccessResponse(lineService.getLineByStation(stationName));
    }

    /**
     * 根据classifyId获取站点信息
     *
     * @param classifyId
     * @return
     */
    @GetMapping(ILine.STATION_GET_BY_LINE)
    public Response<List<StationDTO>> getLineStationList(Long classifyId) {
        if (Objects.isNull(classifyId)) {
            return Response.newFailedResponse(ResEnum.PARAM_ERROR);
        }
        return Response.newSuccessResponse(lineService.getLineStationList(classifyId));
    }




}
