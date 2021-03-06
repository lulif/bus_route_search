package com.gdxx.search.controller;

import com.gdxx.search.common.Response;
import com.gdxx.search.constant.ISearch;
import com.gdxx.search.dto.search.SearchLineDTO;
import com.gdxx.search.dto.search.SearchStationLineDTO;
import com.gdxx.search.enums.ResEnum;
import com.gdxx.search.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-27 22:01
 * @description:
 **/
@RestController
public class SearchController {

    @Autowired
    private LineService lineService;

    /**
     * 站点/线路 关键字模糊搜索
     *
     * @param keyword
     * @return
     */
    @GetMapping(ISearch.STATION_LINE_SEARCH)
    public Response<SearchStationLineDTO> searchStationLine(String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return Response.newFailedResponse(ResEnum.PARAM_ERROR);
        }
        return Response.newSuccessResponse(lineService.searchStationLine(keyword));
    }

    /**
     * 用户历史搜索记录获取
     *
     * @return
     */
    @GetMapping(ISearch.SEARCH_HISTORY_GET)
    public Response<Set<String>> getSearchHistory() {
        return Response.newSuccessResponse(lineService.getSearchHistory());
    }


    /**
     * 用户历史搜索记录清空
     *
     * @return
     */
    @PostMapping(ISearch.SEARCH_HISTORY_CLEAN)
    public Response cleanSearchHistory() {
        return Response.newSuccessResponse(lineService.cleanSearchHistory());
    }

    /**
     * 搜索的路线详情
     */
    @GetMapping(ISearch.SEARCH_LINE_DETAIL)
    public Response<SearchLineDTO> searchLineDetail(String lineName) {
        if (StringUtils.isEmpty(lineName)) {
            return Response.newFailedResponse(ResEnum.PARAM_ERROR);
        }
        return Response.newSuccessResponse(lineService.getSearchLineDetail(lineName));
    }
}
