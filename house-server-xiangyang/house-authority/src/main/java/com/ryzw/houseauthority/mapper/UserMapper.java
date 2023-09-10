package com.ryzw.houseauthority.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryzw.houseauthority.dto.UserDetailDto;
import com.ryzw.houseauthority.entity.UserEntity;
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
public interface UserMapper extends BaseMapper<UserEntity> {

    /**
     * 根据用户编号查询权限可以查看的单位编号
     * @param userid
     * @return
     */
    List<Object> getUnitIdByUser(@Param("userid") Long userid);


    /**
     * 根据单位编号查询用户编号
     * @return
     */
    List<Long> getUserIdByUnit(@Param("unitId") Long unitIds);

    /**
     * 查询用户详情
     * @param userName
     * @return
     */
    List<UserDetailDto> userDetail(@Param("userName") String userName);
}
