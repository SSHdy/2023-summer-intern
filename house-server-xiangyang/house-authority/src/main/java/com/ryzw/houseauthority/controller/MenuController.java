package com.ryzw.houseauthority.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.houseauthority.dto.MenuChildAllDto;
import com.ryzw.houseauthority.dto.MenuChildDto;
import com.ryzw.houseauthority.dto.UserMenuDto;
import com.ryzw.houseauthority.dto.UserMenuListDto;
import com.ryzw.houseauthority.entity.Menu;
import com.ryzw.houseauthority.entity.UserEntity;
import com.ryzw.houseauthority.exception.ServiceException;
import com.ryzw.houseauthority.service.IMenuService;
import com.ryzw.houseauthority.service.IUserService;
import com.ryzw.houseauthority.vo.BasicVo;
import com.ryzw.houseauthority.vo.MenuListVo;
import com.ryzw.houseauthority.vo.MenuVo;
import com.ryzw.houseauthority.vo.PageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 菜单模块表 前端控制器
 * </p>
 *
 * @author yz
 * @since 2018-12-27
 */
@RestController
@RequestMapping("/menu")
@Api(value = "菜单管理", description = "菜单管理")
public class MenuController {
    @Autowired
    IMenuService menuService;

    @Autowired
    DozerBeanMapper dozerBeanMapper;

    @Autowired
    IUserService userService;

    /**
     * 查询一级菜单和它下面的子级菜单，并且按照一级菜单进行分页，不需要查询条件
     *
     * @param pageVo
     * @return
     */
    @RequestMapping(value = "menuList", method = RequestMethod.POST)
    @ApiOperation(value = "查询一级菜单和二级菜单", notes = "查询一级菜单和二级菜单")
    public Page<MenuChildDto> menuList(@RequestBody @Valid PageVo pageVo) {
        return menuService.menuList(pageVo);
    }

    /**
     * 查询菜单所有
     *
     * @return
     */
    @RequestMapping(value = "menus", method = RequestMethod.POST)
    @ApiOperation(value = "查询菜单", notes = "查询菜单")
    public List<MenuChildDto> menus() {
        return menuService.menus();
    }

    /**
     * 按照用户权限获取有效的菜单列表
     *
     * @return
     */
    @ApiOperation(value = "获得菜单列表", notes = "列表信息", httpMethod = "POST")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public List<MenuChildAllDto> getListMenu(Principal user) {
        //获取登录用户的  userId
        String userName = user.getName();
        QueryWrapper<UserEntity> qw = new QueryWrapper<>();
        qw = qw.eq("username", userName)
                .eq("is_freeze", 0)
                .select("user_id", "username");
        Map<String, Object> map = userService.getMap(qw);
        Long userId = Long.valueOf(String.valueOf(map.get("user_id")));
        BasicVo<Long> basicVo = new BasicVo<>();
        basicVo.setId(userId);
//        加载全部功能菜单
        List<UserMenuListDto> rootlist = menuService.userMenuList(basicVo);

        List<MenuChildAllDto> getList = new LinkedList<>();
        getMenus(rootlist, null, getList);

        return getList;
    }

    /**
     * 递归获取菜单
     *
     * @param allMenus
     * @param id
     * @param list
     */
    private void getMenus(List<UserMenuListDto> allMenus, Integer id, List<MenuChildAllDto> list) {
        int parentId = Optional.ofNullable(id).orElse(0);
        allMenus.stream().filter(p -> p.getMenuPid() == parentId).forEach(p -> {
            MenuChildAllDto menuDto = dozerBeanMapper.map(p, MenuChildAllDto.class);
            list.add(menuDto);
            getMenus(allMenus, p.getMenuId(), menuDto.getChildren());
        });
    }

    /**
     * 根据查询条件搜索菜单
     *
     * @param menuListVo
     * @return
     */
    @RequestMapping(value = "menuTitleList", method = RequestMethod.POST)
    @ApiOperation(value = "根据查询条件搜索菜单", notes = "根据查询条件搜索菜单")
    public IPage<Menu> menuTitleList(@RequestBody MenuListVo menuListVo) {
        Page<Menu> page = new Page<>(menuListVo.getCurrentPage(), menuListVo.getPageSize());
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (Strings.isNotBlank(menuListVo.getMenuTitle())) {
            queryWrapper.like("menu_title", menuListVo.getMenuTitle());
        }
        queryWrapper.orderByDesc("menu_id");
        return menuService.page(page, queryWrapper);
    }

    /**
     * 用于单独操作冻结或者启用
     *
     * @param menuVo
     * @return
     */
    @RequestMapping(value = "isFreeze", method = RequestMethod.POST)
    @ApiOperation(value = "用于单独操作冻结或者启用", notes = "用于单独操作冻结或者启用")
    public boolean isFreeze(@RequestBody MenuVo menuVo) {
        Menu menu = dozerBeanMapper.map(menuVo, Menu.class);
        return menuService.updateById(menu);
    }

    /**
     * 用于判断菜单名是否重复
     *
     * @param menuVo
     * @return
     */
    @RequestMapping(value = "unique", method = RequestMethod.POST)
    @ApiOperation(value = "用于判断菜单名是否重复", notes = "用于判断菜单名是否重复")
    public boolean unique(@RequestBody MenuVo menuVo) {
        Integer menuId = menuVo.getMenuId();
        QueryWrapper<Menu> qw = new QueryWrapper<>();
        qw = qw.eq("menu_title", menuVo.getMenuTitle());
        int count = (menuId == null) ? menuService.count(qw) : menuService.count(qw.ne("menu_id", menuId));
        if (count > 0) {
            throw new ServiceException("菜单名重复");
        }
        return true;
    }

    /**
     * 菜单详情
     *
     * @param menu
     * @return
     */
    @RequestMapping(value = "selectMenu", method = RequestMethod.POST)
    @ApiOperation(value = "菜单详情", notes = "菜单详情")
    public Menu selectMenu(@RequestBody Menu menu) {
        return menuService.getOne(new QueryWrapper<Menu>().eq("menu_id", menu.getMenuId()));
    }


    /**
     * 查询用户权限菜单
     *
     * @param basicVo
     * @return
     */
    @ApiOperation(value = "获得菜单列表", notes = "列表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/userMenuList", method = RequestMethod.POST)
    public List<UserMenuDto> userMenu(@RequestBody BasicVo<Long> basicVo) {
//        加载全部有效菜单
        List<UserMenuDto> rootlist = menuService.userMenu(basicVo);

        List<UserMenuDto> getList = new LinkedList<>();
        getUserMenus(rootlist, null, getList);

        return getList;
    }

    /**
     * 递归获取菜单
     *
     * @param allMenus
     * @param id
     * @param list
     */
    private void getUserMenus(List<UserMenuDto> allMenus, Integer id, List<UserMenuDto> list) {
        int parentId = Optional.ofNullable(id).orElse(0);
        allMenus.stream().filter(p -> p.getMenuPid() == parentId).forEach(p -> {
            UserMenuDto userMenuDto = dozerBeanMapper.map(p, UserMenuDto.class);
            list.add(userMenuDto);
            getUserMenus(allMenus, p.getMenuId(), userMenuDto.getChildren());
        });
    }


    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ApiOperation(value = "新增菜单", notes = "新增菜单")
    public boolean add(@RequestBody Menu menu) {
        return menuService.save(menu);
    }


    /**
     * 修改菜单
     *
     * @param menu
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ApiOperation(value = "修改菜单", notes = "修改菜单")
    public boolean update(@RequestBody Menu menu) {
        return menuService.update(menu);
    }

    /**
     * 删除菜单
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除菜单", notes = "删除菜单")
    public boolean deleteMenu(@RequestBody BasicVo<Integer> basicVo) {
        boolean result = menuService.deleteMenu(basicVo);
        return result;
    }

}
