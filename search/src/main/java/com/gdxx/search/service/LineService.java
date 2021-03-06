package com.gdxx.search.service;

import com.gdxx.search.dto.common.StationDTO;
import com.gdxx.search.dto.nearby.NearByLineDTO;
import com.gdxx.search.dto.nearby.NearByStationLineDTO;
import com.gdxx.search.dto.search.SearchLineDTO;
import com.gdxx.search.dto.search.SearchStationLineDTO;

import java.util.List;
import java.util.Set;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-12 18:58
 * @description:
 **/
public interface LineService {

    List<NearByStationLineDTO> getNearbyStationAndLine(Double currentLat, Double currentLng);

    List<NearByLineDTO> getLineByStation(String stationName);

    List<StationDTO> getLineStationList(Long classifyId);

    SearchStationLineDTO searchStationLine(String keyword);

    Set<String> getSearchHistory();

    Boolean cleanSearchHistory();

    SearchLineDTO getSearchLineDetail(String lineName);
}
