package com.ryzw.housegateway.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 权限判断服务
 * </p>
 *
 * @author yz
 * @since 2018/11/27
 */
public interface PermissionService {

    /**
     * 判断是否有权限
     * @param request
     * @param authentication
     * @return
     */
    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
