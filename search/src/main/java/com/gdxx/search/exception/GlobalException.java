package com.gdxx.search.exception;

import com.gdxx.search.enums.ResEnum;
import lombok.Data;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-12 20:03
 * @description:
 **/
@Data
public class GlobalException extends RuntimeException {

    private int code;

    public GlobalException(ResEnum resEnum) {
        super(resEnum.getDescCN());
        this.setCode(resEnum.getCode());
    }


    public GlobalException(int code, String message) {
        super(message);
        this.setCode(code);
    }

}
