package com.ryzw.housemanager.service.impl;

import com.ryzw.housemanager.dto.MenuChildAllDto;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IAuthorityUnitService;
import com.ryzw.housemanager.vo.BasicVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *调用远程权限获取可以查看的单位
 * </p>
 *
 * @author yz
 * @since 2019/1/31
 */
@Service
public class AuthorityUnitServiceImpl implements IAuthorityUnitService {

    /**
     * 远程获取用户可以查看的单位
     * @return
     */
    @Override
    public List<Long> getUnitIdByToken() {
        return new ArrayList<>();
    }

    /**
     * 按单位编号查询用户编号
     * @param unitId
     * @return
     */
    @Override
    public List<Long> getUserIdByUnit(Long unitId) {
        return new ArrayList<>();
    }

    /**
     * 获取单位使用的角色数目
     * @param unitId
     * @return
     */
    @Override
    public int selectCountByUnit(Long unitId){
        throw new ServiceException("查询单位使用角色数失败！");
    }

    /**
     * 跨服务调用用户列表接口
     * @return
     */
    @Override
    public List<Map<String, Object>> userList(List<Long> userIds) {
        return new ArrayList<>();
    }

    /**
     * 根据TOKEN获取用户信息
     * @return
     */
    @Override
    public Map<String, Object> getUserByToken() {
        return new HashMap<>();
    }

    /**
     * 按step查询维修用户id
     * @return
     */
    @Override
    public List<String> selectRepairByStep(Integer step) {
        throw new ServiceException("按step查询维修用户id失败！");
    }

    /**
     * 按step查询处置用户id
     * @return
     */
    @Override
    public List<String> selectHandleByStep(Integer step) {
        throw new ServiceException("按step查询处置用户id失败！");
    }

    /**
     * 按照用户权限获取有效的菜单列表
     * @return
     */
    @Override
    public List<MenuChildAllDto> getListMenu(BasicVo<Long> basicVo) {
        throw new ServiceException("按照用户权限获取有效的菜单列表失败！");
    }

    @Override
    public List<String> selectConfigureByStep(Integer step) {
        throw new ServiceException("按step查询配置用户id失败！");
    }

}
