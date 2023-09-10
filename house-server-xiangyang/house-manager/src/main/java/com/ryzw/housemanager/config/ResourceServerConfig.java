package com.ryzw.housemanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * <p>
 *
 * </p>
 *
 * @author yz
 * @since 2018/11/26
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
//                .csrf().disable()
                .authorizeRequests()
//                .antMatchers("/yard/**").access("#oauth2.hasScope('openid')")
//                .antMatchers("/build/**").access("#oauth2.hasScope('build')")
                .antMatchers("/favor.ioc","/swagger-ui.html","/webjars/springfox-swagger-ui/**","/swagger-resources/**","/v2/**","/login","/images/**","/downloadTemplate/**","/actuator/**").permitAll()
                .anyRequest().authenticated();
//                .and()
//                .httpBasic();

//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    }
}