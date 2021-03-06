package com.gdxx.search.dto.nearby;

import com.gdxx.search.dto.common.StationDTO;
import lombok.Data;

import java.util.Set;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-13 15:16
 * @description:
 **/
@Data
public class NearByStationLineDTO extends StationDTO {

    /**
     * 距离
     */
    private Double distance;

    /**
     * 线路名集合
     */
    private Set<String> lineNameList;
}
