package com.ryzw.housemanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * 文件映射
 * </p>
 *
 * @author yz
 * @since 2018-11-20
 */
@Configuration
public class MyWebAppConfigurer implements WebMvcConfigurer {

    @Value("${uploadUrl.url}")
    private String path;

    @Value("${uploadUrl.virtualPath}")
    private String virtualPath;

    /**
     * 映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(virtualPath+"**").addResourceLocations("file:"+path);
    }
}