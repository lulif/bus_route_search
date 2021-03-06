package com.gdxx.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gdxx.dao.entity.LineStation;

import java.util.List;

/**
 * @author lulif
 * @since 2021-02-07
 */
public interface LineStationMapper extends BaseMapper<LineStation> {
    /**
     * 批量插入线路站点信息
     *
     * @param list
     */
    void batchInsert(List<LineStation> list);

    /**更新name-classifyId更新站点经纬度信息
     * @param station
     */
    void updateLatLngByNameAndClassifyId(LineStation station);

}
