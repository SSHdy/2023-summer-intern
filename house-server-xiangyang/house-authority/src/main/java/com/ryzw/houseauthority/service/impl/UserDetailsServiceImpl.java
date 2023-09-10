package com.ryzw.houseauthority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryzw.houseauthority.entity.Role;
import com.ryzw.houseauthority.entity.UserEntity;
import com.ryzw.houseauthority.service.IRoleService;
import com.ryzw.houseauthority.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author yz
 * @since 2018/11/26
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

//    @Autowired
//    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        UserEntity user = userService.getOne(queryWrapper);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
        if(user.getIsFreeze().equals(1)){
            throw new UsernameNotFoundException("用户被冻结!");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        boolean enabled = true; // 可用性 :true:可用 false:不可用
        boolean accountNonExpired = true; // 过期性 :true:没过期 false:过期
        boolean credentialsNonExpired = true; // 有效性 :true: 凭证有效 false:凭证无效
        boolean accountNonLocked = true; // 锁定性 :true:未锁定 false:已锁定

        GrantedAuthority authority = new SimpleGrantedAuthority("/**");
        grantedAuthorities.add(authority);

        List<Role> roleList = roleService.getRoleByUserId(user.getUserId());
        if (roleList != null) {
            for (Role role : roleList) {
                //角色必须是ROLE_开头，可以在数据库中设置
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + role.getRoleValue());
                grantedAuthorities.add(grantedAuthority);
            }
        }
        return new User(user.getUsername(), user.getPassword(),
                enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthorities);
    }
}
