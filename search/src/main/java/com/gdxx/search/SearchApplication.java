package com.gdxx.search;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-13 15:59
 * @description:
 **/
@SpringBootApplication
@MapperScan("com.gdxx.dao.mapper")
public class SearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }
}
