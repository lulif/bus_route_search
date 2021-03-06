package com.gdxx.search.dto.common;

import lombok.Data;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-15 21:57
 * @description:
 **/
@Data
public class StationDTO {
    /**
     * 站名
     */
    private String name;
    /**
     * 纬度
     */
    private Double lat;
    /**
     * 经度
     */
    private Double lng;
}
