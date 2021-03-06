package com.gdxx.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author lulif
 * @since 2021-02-07
 */
@Data
public class LineStation implements Serializable {

    private static final long serialVersionUID = -6191174707373240046L;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 线路主要信息(line_major)的id
     */
    @TableField("majorId")
    private Long majorId;

    /**
     * 线路开往方向分类(line_classify)的id
     */
    @TableField("classifyId")
    private Long classifyId;

    /**
     * 站点名称
     */
    private String name;

    /**
     * 记录创建时间
     */
    @TableField("createTime")
    private Date createTime;

    /**
     * 记录更新时间
     */
    @TableField("updateTime")
    private Date updateTime;

    /**
     * 纬度
     */
    private Double lat;
    /**
     * 经度
     */
    private Double lng;
    /**
     * 经纬度查找等级
     */
    @TableField("findLevel")
    private Integer findLevel;


}
