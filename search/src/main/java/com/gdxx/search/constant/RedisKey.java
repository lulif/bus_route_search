package com.gdxx.search.constant;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-13 15:37
 * @description:
 **/
public interface RedisKey {
    /**
     * 所有线路主要信息
     */
    String GDXX_ROUTE_LINE_MAJOR_ALL = "gdxx:route:line:major:all";
    /**
     * 所有站点信息
     */
    String GDXX_ROUTE_LINE_STATION_ALL = "gdxx:route:line:station:all";
    /**
     * 有经纬度的站点信息
     */
    String GDXX_ROUTE_LINE_STATION_LATLNG = "gdxx:route:line:station:latlng";
    /**
     * 历史搜索记录
     */
    String GDXX_ROUTE_SEARCH_HISTORY = "gdxx:route:search:history";
}
