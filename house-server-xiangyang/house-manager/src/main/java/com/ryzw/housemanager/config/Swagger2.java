package com.ryzw.housemanager.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.ryzw.housemanager.entity.KeycloakProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Swagger配置
 * </p>
 *
 * @author yz
 * @since 2018-11-20
 */
@EnableSwagger2
@Configuration
@EnableSwaggerBootstrapUI
public class Swagger2 {

    @Autowired
    private KeycloakProperties keycloakProperties;


    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .securitySchemes(Arrays.asList(securityScheme()))
                .securityContexts(Arrays.asList(securityContext()))
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ryzw.housemanager.controller"))
                .paths(PathSelectors.any())
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("十堰办公用户系统 Rest API")
                .description("十堰办公用户系统后台API接口文档")
                .version("1.0")
                .build();
    }


    @Bean
    List<GrantType> grantTypes() {
        List<GrantType> grantTypes = new ArrayList<>();
        TokenRequestEndpoint tokenRequestEndpoint = new TokenRequestEndpoint(
                keycloakProperties.getUserAuthorizationUri(),
                keycloakProperties.getClientId(), keycloakProperties.getClientSecret());
        TokenEndpoint tokenEndpoint = new TokenEndpoint(keycloakProperties.getAccessTokenUri(), "access_token");
        grantTypes.add(new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint));
        return grantTypes;
    }

    private SecurityScheme securityScheme() {
        GrantType grantType = new AuthorizationCodeGrantBuilder().tokenEndpoint(new TokenEndpoint(keycloakProperties.getAccessTokenUri(), "access_token"))
                .tokenRequestEndpoint(new TokenRequestEndpoint( keycloakProperties.getUserAuthorizationUri(),keycloakProperties.getClientId(), keycloakProperties.getClientSecret())).build();

        SecurityScheme oauth = new OAuthBuilder().name("spring_oauth").grantTypes(Arrays.asList(grantType)).scopes(Arrays.asList(scopes())).build();
        return oauth;
    }



    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = {new AuthorizationScope("openid", "Grants openid access")};
        return scopes;
    }


    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(new SecurityReference("spring_oauth", scopes())))
                .forPaths(PathSelectors.any())
                .build();
    }


    @Bean
    public SecurityConfiguration securityInfo() {
        return SecurityConfigurationBuilder.builder().clientId(keycloakProperties.getClientId())
                .clientSecret( keycloakProperties.getClientSecret())
                .useBasicAuthenticationWithAccessCodeGrant(false).build();

    }
}
