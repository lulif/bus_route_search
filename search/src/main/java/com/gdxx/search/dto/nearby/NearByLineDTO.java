package com.gdxx.search.dto.nearby;

import com.gdxx.search.dto.common.LineDTO;
import lombok.Data;

import java.util.List;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-13 19:46
 * @description:
 **/
@Data
public class NearByLineDTO extends LineDTO {
    /**
     * 线路分类
     */
    private List<NearByLineClassifyDTO> classifyDTOList;
}
