package com.gdxx.crawler.pojo;

import lombok.Data;

/**
 * @program: grab
 * @author: lulif
 * @create: 2021-02-06 22:45
 * @description:
 **/
@Data
public class LineNode {
    /**
     * 线路详情 跳转链接 拼接
     */
    private String href;
    /**
     * 线路名
     */
    private String name;
}
