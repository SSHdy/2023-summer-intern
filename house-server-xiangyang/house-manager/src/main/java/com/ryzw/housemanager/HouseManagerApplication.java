package com.ryzw.housemanager;

import io.micrometer.core.instrument.MeterRegistry;
import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("com.ryzw.housemanager.mapper")
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
@SpringBootApplication(exclude = SecurityAutoConfiguration.class,scanBasePackages = {"com.ryzw.housemanager","com.ryzw.houseprocess"})
public class HouseManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HouseManagerApplication.class, args);
    }


    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(
            @Value("${spring.application.name}") String applicationName) {
        return (registry) -> registry.config().commonTags("application", applicationName);
    }
}
