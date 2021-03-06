package com.gdxx.crawler;


import org.mybatis.spring.annotation.MapperScan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: grab
 * @author: lulif
 * @create: 2021-02-08 23:26
 * @description:
 **/
@SpringBootApplication
@MapperScan("com.gdxx.dao.mapper")
public class CrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrawlerApplication.class, args);
    }

}
