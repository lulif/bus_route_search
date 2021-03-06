package com.gdxx.search.constant;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-27 22:03
 * @description:
 **/
public interface ISearch {

    /**
     * 站点/线路 关键字模糊搜索
     */
    String STATION_LINE_SEARCH = "/station/line/search";

    /**
     * 历史搜索记录获取
     */
    String SEARCH_HISTORY_GET = "/search/history/get";

    /**
     * 历史搜索记录清空
     */
    String SEARCH_HISTORY_CLEAN = "/search/history/clean";

    /**
     * 搜索的路线详情
     */
    String SEARCH_LINE_DETAIL = "/search/line/detail";
}
