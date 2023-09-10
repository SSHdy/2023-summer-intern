package com.ryzw.houseauthority.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.houseauthority.dto.MenuChildDto;
import com.ryzw.houseauthority.dto.MenuDto;
import com.ryzw.houseauthority.dto.UserMenuDto;
import com.ryzw.houseauthority.dto.UserMenuListDto;
import com.ryzw.houseauthority.entity.Menu;
import com.ryzw.houseauthority.vo.BasicVo;
import com.ryzw.houseauthority.vo.MenuListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单模块表 Mapper 接口
 * </p>
 *
 * @author yz
 * @since 2018-12-27
 */
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 查询一级菜单和它下面的子级菜单，并且按照一级菜单进行分页，不需要查询条件
     * @param page
     * @return
     */
    Page<MenuChildDto> menuListByPage(Page page);

    /**
     * 获取所有菜单
     * @return
     */
    List<MenuChildDto> menuList();

    /**
     * 父菜单不分页
     * @return
     */
    List<MenuChildDto> menus();

    /**
     * 查询
     * @param page
     * @param menuListVo
     * @return
     */
    Page<MenuDto> list(Page<MenuDto> page, @Param("menu") MenuListVo menuListVo);

    /**
     * 用户权限菜单查询
     * @return
     */
    List<UserMenuDto> userMenu(@Param("mu") BasicVo<Long> basicVo);

    /**
     * 用户权限菜单查询
     * @return
     */
    List<UserMenuListDto> userMenuList(@Param("menu") BasicVo<Long> basicVo);


}
