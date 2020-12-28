package com.assess;

import com.alibaba.fastjson.JSONArray;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@MapperScan(value = "com.assess.mapper")
@SpringBootApplication
@EnableScheduling
@EnableSwagger2
public class DigitizationAssessApplication extends SpringBootServletInitializer {


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DigitizationAssessApplication.class);
    }


    public static void main(String[] args) {
        SpringApplication.run(DigitizationAssessApplication.class, args);
    }






}
