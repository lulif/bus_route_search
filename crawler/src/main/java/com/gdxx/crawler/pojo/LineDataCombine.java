package com.gdxx.crawler.pojo;


import com.gdxx.dao.entity.LineClassify;
import com.gdxx.dao.entity.LineMajor;
import com.gdxx.dao.entity.LineStation;
import com.google.common.collect.Multimap;
import lombok.Data;

import java.util.List;

/**
 * @program: grab
 * @author: lulif
 * @create: 2021-02-07 23:56
 * @description:
 **/
@Data
public class LineDataCombine {

    private LineMajor lineMajor;

    private List<LineClassify> lineClassifyList;
    /**
     * key为lineClassifyList的下标
     */
    private Multimap<Integer, LineStation> classify2StationMap;
}
