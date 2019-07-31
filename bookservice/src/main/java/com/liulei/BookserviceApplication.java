package com.liulei;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

@MapperScan(basePackages = "com.liulei.*.dao")
@SpringBootApplication
@EnableCaching
public class BookserviceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BookserviceApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BookserviceApplication.class);
    }

}
