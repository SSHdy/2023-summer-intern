package com.ryzw.houseauthority.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ryzw.houseauthority.vo.PageVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lyx
 * @since 2019-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OauthClientDetails extends PageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 认证表编号
     */
    @TableId(value = "oauth_detail_id", type = IdType.AUTO)
    private Long oauthDetailId;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号",example = "app")
    private String clientId;

    private String resourceIds;

    @ApiModelProperty(value = "密码",example = "***")
    private String clientSecret;

    @ApiModelProperty(value = "范围",example = "app")
    private String scope;

    @ApiModelProperty(value = "认证方式",example = "password")
    private String authorizedGrantTypes;

    private String webServerRedirectUri;

    private String authorities;

    private Integer accessTokenValidity;

    private Integer refreshTokenValidity;

    private String additionalInformation;

    private String autoapprove;

    /**
     * 公司名称
     */
    private String companyName;

}
