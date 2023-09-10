package com.ryzw.housegateway.service.impl;


import com.ryzw.housegateway.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 判断是否有权限访问此URL
 * </p>
 *
 * @author yz
 * @since 2018/11/27
 */
@Service("permissionService")
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 判断是否有权限
     * @param request
     * @param authentication
     * @return
     */
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String requestUrl = request.getRequestURI();
        log.info("requestUrl:{}", requestUrl);
        List<SimpleGrantedAuthority> grantedAuthorityList = (List<SimpleGrantedAuthority>) authentication.getAuthorities();
        boolean hasPermission = false;
        if (principal != null) {
            if (CollectionUtils.isEmpty(grantedAuthorityList)) {
                return hasPermission;
            }
            hasPermission = grantedAuthorityList.stream().anyMatch(p->antPathMatcher.match(p.getAuthority(),requestUrl));
        }
       return hasPermission;
    }

}
