package com.ryzw.houseauthority.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.houseauthority.dto.UserDetailDto;
import com.ryzw.houseauthority.dto.UserRoleDto;
import com.ryzw.houseauthority.entity.UserEntity;
import com.ryzw.houseauthority.vo.NewPassWordVo;
import com.ryzw.houseauthority.vo.UserRoleVo;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yz
 * @since 2018-12-27
 */
public interface IUserService extends IService<UserEntity> {
    /**
     * 新增用户信息
     *
     * @param userRoleVo
     * @return
     */
    boolean add(UserRoleVo userRoleVo);

    /**
     * 修改用户信息
     *
     * @param userRoleVo
     * @return
     */
    boolean update(UserRoleVo userRoleVo);

    /**
     * 查询
     *
     * @param userRoleVo
     * @return
     */
    UserRoleDto selectUserAndRole(UserRoleVo userRoleVo);

    /**
     * 修改密码
     *
     * @param newPassWordVo
     * @return
     */
    boolean updatePassWord(NewPassWordVo newPassWordVo);

    /**
     * 根据用户编号查询权限可以查看的单位编号
     *
     * @param userid
     * @return
     */
    List<Object> getUnitIdByUser(Long userid);

    /**
     * 删除用户信息
     *
     * @param user
     * @return
     */
    boolean delete(UserEntity user);

    /**
     * 批量删除用户信息
     *
     * @param userIds
     * @return
     */
    boolean deleteIds(List<Long> userIds);

    /**
     * 根据单位编号查询用户编号
     *
     * @return
     */
    List<Long> getUserIdByUnit(Long unitIds);

    /**
     * 根据权限Id查询对应的用户集合
     *
     * @param repairId
     * @return
     */
    Set selectRepairList(Integer repairId);

    /**
     * 删除用户（删除该用户所有权限）
     *
     * @param userId
     * @return
     */
    boolean deleteRepairById(Long userId);

    /**
     * 根据处置权限Id查询对应的用户集合
     *
     * @param step
     * @return
     */
    Set selectHandleByStep(Integer step);

    /**
     * 根据配置权限Id查询对应的用户集合
     *
     * @param step
     * @return
     */
    Set selectConfigByStep(Integer step);

    /**
     * 查询用户详情
     * @param userName
     * @return
     */
    UserDetailDto userDetail(String userName);
}
