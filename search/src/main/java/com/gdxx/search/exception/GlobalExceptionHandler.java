package com.gdxx.search.exception;

import com.gdxx.search.common.Response;
import com.gdxx.search.enums.ResEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-12 18:57
 * @description:
 **/
@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Response<String> exceptionHandler(Exception e) {
        e.printStackTrace();
        if (e instanceof GlobalException) {
            GlobalException ex = (GlobalException) e;
            return Response.newFailedResponse(ex.getCode(), ex.getMessage());
        } else {
            return Response.newFailedResponse(ResEnum.FAIL);
        }
    }

}
