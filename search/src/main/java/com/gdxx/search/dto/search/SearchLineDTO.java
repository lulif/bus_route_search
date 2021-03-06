package com.gdxx.search.dto.search;

import com.gdxx.search.dto.common.LineDTO;
import lombok.Data;

import java.util.List;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-15 21:49
 * @description:
 **/
@Data
public class SearchLineDTO extends LineDTO {

    private List<SearchLineClassifyDTO> classifyDTOList;

}
