package com.ryzw.houseauthority.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryzw.houseauthority.dto.RoleDetailDto;
import com.ryzw.houseauthority.entity.Role;
import com.ryzw.houseauthority.vo.BasicVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yz
 * @since 2018-12-27
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 按用户编号查询角色
     * @param userId
     * @return
     */
    List<Role> getRoleByUserId(@Param("userId") Long userId);

    /**
     * 角色信息详情查询
     * @param roleId
     * @return
     */
    RoleDetailDto roleDetail(@Param("roleId") Long roleId);

    /**
     * 角色信息更新查询
     * @param roleId
     * @return
     */
    RoleDetailDto roleUpdate(@Param("roleId") Long roleId);
}
