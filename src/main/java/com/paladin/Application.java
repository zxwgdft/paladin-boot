package com.paladin;

import com.paladin.framework.constants.GlobalProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@MapperScan({"com.paladin.common.mapper", "com.paladin.data.mapper", "com.paladin.demo.mapper"})
public class Application {

    public static void main(String[] args) {
        GlobalProperties.project = "demo";
        SpringApplication.run(Application.class, args);
    }


}
