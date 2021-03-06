package com.gdxx.search.dto.search;

import com.gdxx.search.dto.common.LineClassifyDTO;
import com.gdxx.search.dto.common.StationDTO;
import lombok.Data;

import java.util.List;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-15 21:54
 * @description:
 **/
@Data
public class SearchLineClassifyDTO extends LineClassifyDTO {
    private List<StationDTO> stationList;
}
