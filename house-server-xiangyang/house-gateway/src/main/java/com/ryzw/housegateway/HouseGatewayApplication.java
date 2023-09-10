package com.ryzw.housegateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableZuulProxy
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableDiscoveryClient
public class HouseGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(HouseGatewayApplication.class, args);
    }
}
