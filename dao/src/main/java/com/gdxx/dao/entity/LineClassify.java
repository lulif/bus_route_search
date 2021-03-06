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
public class LineClassify implements Serializable {

    private static final long serialVersionUID = -3296482241458342086L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 线路主要信息(line_major) 的id
     */
    @TableField("majorId")
    private Long majorId;

    /**
     * 路线区间
     */
    private String section;

    /**
     * 首班车时间
     */
    @TableField("firstBusTime")
    private String firstBusTime;

    /**
     * 末班车时间
     */
    @TableField("lastBusTime")
    private String lastBusTime;

    /**
     * 站数
     */
    @TableField("stationNum")
    private Integer stationNum;

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
