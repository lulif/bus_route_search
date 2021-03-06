package com.gdxx.search.dto.common;

import lombok.Data;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-15 22:00
 * @description:
 **/
@Data
public class LineClassifyDTO {
    /**
     * 区间
     */
    private String section;
    /**
     * 线路站数
     */
    private Integer stationNum;
}
