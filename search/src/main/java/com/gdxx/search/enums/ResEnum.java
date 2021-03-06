package com.gdxx.search.enums;

import lombok.Getter;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-12 19:04
 * @description:
 **/
@Getter
public enum ResEnum {

    SUCCESS(200, "success", "成功"),
    FAIL(1004, "fail", "系统内部错误"),
    PARAM_ERROR(1002, "param error", "参数错误"),
    LOCATION_FAIL(1001, "location fail", "定位失败"),
    NOT_FIND_STATION(1003, "not find station", "未匹配到相关站点");

    private Integer code;
    private String desc;
    private String descCN;

    ResEnum(Integer code, String desc, String descCN) {
        this.code = code;
        this.desc = desc;
        this.descCN = descCN;
    }

}
