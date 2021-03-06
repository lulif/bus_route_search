package com.gdxx.search.dto.common;

import lombok.Data;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-15 21:56
 * @description:
 **/
@Data
public class LineDTO {
    /**
     * 线路名
     */
    private String name;
    /**
     * 价格
     */
    private String price;
    /**
     * 运行时间描述
     */
    private String runTimeDesc;
    /**
     * 班次信息描述
     */
    private String frequencyDesc;
}
