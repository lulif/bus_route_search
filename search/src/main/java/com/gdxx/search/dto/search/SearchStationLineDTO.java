package com.gdxx.search.dto.search;

import com.gdxx.search.dto.common.StationDTO;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-15 21:40
 * @description:
 **/
@Data
public class SearchStationLineDTO {
    /**
     * 站点集合
     */
    private List<StationDTO> stationList;
    /**
     * 线路集合
     */
    private Set<String> lineList;
}
