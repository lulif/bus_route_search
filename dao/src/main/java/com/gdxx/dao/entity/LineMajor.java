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
public class LineMajor implements Serializable {

    private static final long serialVersionUID = 1189450854202732811L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 路线名称
     */
    private String name;

    /**
     * 票价
     */
    private String price;

    /**
     * 所属公交公司
     */
    private String company;

    /**
     * 公交网站数据最后更新时间
     */
    @TableField("lastUpdateTime")
    private String lastUpdateTime;

    /**
     * 班次(单位 分钟)
     */
    private String frequency;

    /**
     * 全程耗时
     */
    @TableField("wholeTime")
    private String wholeTime;

    /**
     * 运行时间具体描述
     */
    @TableField("runTimeDesc")
    private String runTimeDesc;
    /**
     * 班次信息具体描述
     */
    @TableField("frequencyDesc")
    private String frequencyDesc;

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


}
