package com.gdxx.search.dto.nearby;

import com.gdxx.search.dto.common.LineClassifyDTO;
import lombok.Data;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-13 20:12
 * @description:
 **/
@Data
public class NearByLineClassifyDTO extends LineClassifyDTO {
    /**
     * classify的Id
     */
    private Long id;
}
