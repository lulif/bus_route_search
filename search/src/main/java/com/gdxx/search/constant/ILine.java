package com.gdxx.search.constant;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-13 15:32
 * @description:
 **/
public interface ILine {
    /**
     * 获取附近站点及其线路
     *
     * @return
     */
    String NEARBY_STATION_LINE_GET = "/nearby/station/line/get";
    /**
     * 根据站点获取线路信息
     */
    String LINE_GET_BY_STATION = "/line/get/by/station";

    /**
     * 根据classifyId获取站点信息
     */
    String STATION_GET_BY_LINE = "/station/get/by/line";

}
