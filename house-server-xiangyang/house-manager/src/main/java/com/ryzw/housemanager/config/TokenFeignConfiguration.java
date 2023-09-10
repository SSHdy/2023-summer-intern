package com.ryzw.housemanager.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <p>
 * 微服务间Token验证
 * </p>
 *
 * @author yz
 * @since 2019/2/22
 */
public class TokenFeignConfiguration {

    @Bean
    public TokenRequestInterceptor tokenRequestInterceptor(){
        return new TokenRequestInterceptor();
    }


    public class TokenRequestInterceptor implements RequestInterceptor {

        private String AUTHORIZATION_HEADER = "Authorization";

        @Override
        public void apply(RequestTemplate requestTemplate) {
            String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(AUTHORIZATION_HEADER);
            if (StringUtils.isNotBlank(token)) {
                requestTemplate.header(AUTHORIZATION_HEADER, token);
            }
        }
    }
}
