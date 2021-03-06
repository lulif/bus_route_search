package com.gdxx.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gdxx.dao.entity.LineClassify;


import java.util.List;

/**
 * @author lulif
 * @since 2021-02-07
 */
public interface LineClassifyMapper extends BaseMapper<LineClassify> {
    /**
     * 批量插入线路分类信息
     * @param list
     */
    void batchInsert(List<LineClassify> list);
}
