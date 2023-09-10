package com.ryzw.houseauthority.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.houseauthority.dto.MenuChildDto;
import com.ryzw.houseauthority.dto.UserMenuDto;
import com.ryzw.houseauthority.dto.UserMenuListDto;
import com.ryzw.houseauthority.entity.Menu;
import com.ryzw.houseauthority.vo.BasicVo;
import com.ryzw.houseauthority.vo.PageVo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单模块表 服务类
 * </p>
 *
 * @author yz
 * @since 2018-12-27
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 查询一级菜单和它下面的子级菜单，并且按照一级菜单进行分页，不需要查询条件
     * @param pageVo
     * @return
     */
    Page<MenuChildDto> menuList(PageVo pageVo);

    /**
     * 查询菜单
     * @return
     */
    List<MenuChildDto> menus();

    /**
     * 用户角色菜单查询
     * @param basicVo
     * @return
     */
    List<UserMenuDto> userMenu(BasicVo<Long> basicVo);

    /**
     * 修改菜单
     * @param menu
     * @return
     */
    boolean update(Menu menu);

    /**
     * 用于判断菜单是否被使用,如果没有使用，则删除
     *
     * @param basicVo
     * @return
     */
    boolean deleteMenu(BasicVo<Integer> basicVo);

    /**
     * 用户权限菜单查询
     * @return
     */
    List<UserMenuListDto> userMenuList(BasicVo<Long> basicVo);

}
