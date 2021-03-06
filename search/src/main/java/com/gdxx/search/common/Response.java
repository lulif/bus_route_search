package com.gdxx.search.common;


import com.gdxx.search.enums.ResEnum;
import lombok.Data;

import java.util.Objects;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-12 19:00
 * @description:
 **/
@Data
public class Response<T> {

    private boolean success;
    private int retCode;
    private String retMsg;
    private T retObj;

    public static <T> Response<T> newSuccessResponse(T data) {
        return newResponse(true, data, ResEnum.SUCCESS.getCode(), ResEnum.SUCCESS.getDescCN());
    }
    public static <T> Response<T> newFailedResponse(ResEnum resEnum) {
        return newResponse(false, null, resEnum.getCode(), resEnum.getDescCN());
    }

    public static <T> Response<T> newFailedResponse(Integer code, String message) {
        return newResponse(false, null, code, message);
    }

    public static <T> Response<T> newResponse(boolean success, T data, Integer code, String message) {
        Response<T> response = new Response<T>();
        response.setRetCode(code);
        response.setRetMsg(message);
        response.setSuccess(success);
        if (data != null && data instanceof String
                && Objects.equals(data, "")) {
            response.setRetObj(null);
        } else {
            response.setRetObj(data);
        }
        return response;
    }
}
