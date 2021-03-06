package com.gdxx.crawler.constant;

/**
 * @program: grab
 * @author: lulif
 * @create: 2021-02-06 22:04
 * @description:
 **/
public interface Xpath {
    /**
     * 线路首字母种类获取
     */
    String LINES_TYPE_XPATH = "//div[@class='layout layout--728-250']/div[@class='layout-left']/div[@id='ccContentTooltip']/div[@class='tooltip-context']/div[@class='tooltip-inner']";

    /**
     * 线路号获取
     */
    String LINES_XPATH = "//div[@class='layout layout--728-250']/div[@class='layout-left']/div[@class='cc-content']/div[@class='list clearfix']";

    /**
     * 线路详情 (运行时间,票价,公交公司,最后更新时间) 获取
     */
    String LINE_DETAIL_TIME_XPATH = "//div[@class='layout layout--728-250']/div[@class='layout-left']/div[@class='bus-lzinfo mb20']/div[@class='info']/ul[@class='bus-desc']";

    /**
     * 线路详情 (发车信息) 获取
     */
    String LINE_DETAIL_FREQUENCY_XPATH = "//div[@class='layout layout--728-250']/div[@class='layout-left']/div[@class='change-info mb20']";

    /**
     * 线路详情 (区间信息) 获取
     */
    String LINE_DETAIL_SECTION_XPATH = "//div[@class='layout layout--728-250']/div[@class='layout-left']/div[@class='bus-excerpt mb15']/div[@class='excerpt fl']/div[@class='trip']";

    /**
     * 线路详情 (站点信息) 获取
     */
    String LINE_DETAIL_STATION_XPATH = "//div[@class='layout layout--728-250']/div[@class='layout-left']/div[@class='bus-lzlist mb15']";

}
