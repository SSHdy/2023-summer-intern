package com.ryzw.houseauthority.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </p>
 *
 * @author yz
 * @since 2019/1/11
 */
@Component
@ConfigurationProperties(prefix = "security.oauth2.client")
@Data
public class KeycloakProperties {

    private String clientId;

    private String clientSecret;

    private String accessTokenUri;

    private String userAuthorizationUri;

}
