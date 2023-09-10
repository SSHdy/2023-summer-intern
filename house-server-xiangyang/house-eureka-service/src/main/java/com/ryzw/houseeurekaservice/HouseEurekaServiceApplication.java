package com.ryzw.houseeurekaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class HouseEurekaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HouseEurekaServiceApplication.class, args);
    }
}
