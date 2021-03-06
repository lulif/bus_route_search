package com.gdxx.crawler.constant;

/**
 * @program: grab
 * @author: lulif
 * @create: 2021-02-06 22:01
 * @description:
 **/
public interface Url {
    /**
     * 线路首字母种类获取
     */
     String LINES_TYPE_URL = "https://hangzhou.8684.cn/list1";

    /**
     * 线路号获取
     */
    String LINES_URL = "https://hangzhou.8684.cn/list";

    /**
     * 线路详情
     */
    String LINE_DETAIL_URL = "https://hangzhou.8684.cn";

    /**
     * 百度地图ak
     */
     String AK = "KGPOhDf6okL4OYQZoQjnGFyzCnASS5E6";
    /**
     * 百度地图杭州区域码
     */
     String REGION_CODE = "179";

    /**
     * 百度地图API 获取站点经纬度信息
     */
     String BAIDU_API_LAT_LNG_URL = "http://api.map.baidu.com/place/v2/search?ak="
            + AK + "&region=" + REGION_CODE + "&output=json" + "&page_size=20";
}
