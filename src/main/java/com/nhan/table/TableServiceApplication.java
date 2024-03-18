package com.nhan.table;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = "com.nhan.table.config")
//@ComponentScan(basePackages = "com.nhan.table.controller")
public class TableServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TableServiceApplication.class, args);
    }

}
