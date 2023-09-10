package com.ryzw.houseauthority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.houseauthority.dto.MenuChildDto;
import com.ryzw.houseauthority.dto.UserMenuDto;
import com.ryzw.houseauthority.dto.UserMenuListDto;
import com.ryzw.houseauthority.entity.Menu;
import com.ryzw.houseauthority.entity.RoleMenu;
import com.ryzw.houseauthority.exception.ServiceException;
import com.ryzw.houseauthority.mapper.MenuMapper;
import com.ryzw.houseauthority.mapper.RoleMenuMapper;
import com.ryzw.houseauthority.service.IMenuService;
import com.ryzw.houseauthority.vo.BasicVo;
import com.ryzw.houseauthority.vo.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单模块表 服务实现类
 * </p>
 *
 * @author yz
 * @since 2018-12-27
 */
@Service
@Slf4j
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Autowired
    MenuMapper menuMapper;
    @Autowired
    RoleMenuMapper roleMenuMapper;

//    @Value("${uploadUrl.url}")
//    private String url;

    /**
     * 查询一级菜单和它下面的子级菜单，并且按照一级菜单进行分页，不需要查询条件
     *
     * @param pageVo
     * @return
     */
    @Override
    public Page<MenuChildDto> menuList(PageVo pageVo) {
        Page page = new Page(pageVo.getCurrentPage(), pageVo.getPageSize());

        //按分页查询一级菜单信息
        Page<MenuChildDto> mList = menuMapper.menuListByPage(page);
        //获取所有菜单信息
        List<MenuChildDto> menuList = menuMapper.menuList();

        //为一级菜单设置子菜单准备递归
        for (MenuChildDto m : mList.getRecords()) {
            //获取父菜单下的子菜单
            List<MenuChildDto> childDtoList = getChilds(Integer.valueOf(m.getMenuId()), menuList);
            m.setChildren(childDtoList);
        }
        return mList;
    }

    /**
     * 获取父，子不分页
     *
     * @return
     */
    @Override
    public List<MenuChildDto> menus() {
        List<MenuChildDto> menus = menuMapper.menus();
        //获取所有菜单信息
        List<MenuChildDto> menuList = menuMapper.menuList();
        //为一级菜单设置子菜单准备递归
        for (MenuChildDto m : menus) {
            //获取父菜单下的子菜单
            List<MenuChildDto> childDtoList = getChilds(Integer.valueOf(m.getMenuId()), menuList);
            m.setChildren(childDtoList);
        }
        return menus;
    }

    public List<MenuChildDto> getChilds(Integer id, List<MenuChildDto> menuList) {
        //构建子菜单
        List<MenuChildDto> children = new ArrayList<>();
        //遍历传入的list
        for (MenuChildDto child : menuList) {
            if (id.equals(child.getMenuPid())) {
                children.add(child);
            }
        }
        //递归
        for (MenuChildDto menu : children) {
            menu.setChildren(getChilds(menu.getMenuId(), menuList));
        }
        //所有菜单的父id与传入的根节点id比较，若相等则为该级菜单的子菜单
        //退出循环条件
        if (children.size() == 0) {
            return null;
        }
        return children;
    }


    /**
     * 用户角色菜单查询
     *
     * @param basicVo
     * @return
     */
    @Override
    public List<UserMenuDto> userMenu(BasicVo<Long> basicVo) {
        return menuMapper.userMenu(basicVo);
    }


    /**
     * 修改菜单
     *
     * @param menu
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean update(Menu menu) {

        Menu dbMenu = menuMapper.selectById(menu.getMenuId());
        try {
            if (StringUtils.isNotEmpty(menu.getMenuImgurl()) && !menu.getMenuImgurl().equals(dbMenu.getMenuImgurl())) {
//                Files.deleteIfExists(Paths.get(dbMenu.getMenuImgurl()));
                this.deleteImg(dbMenu.getMenuImgurl());
            }
            menuMapper.updateById(menu);
        } catch (Exception ex) {
            log.error("update menu error", ex);
            throw new ServiceException(ex.getMessage());
        }
        return this.saveOrUpdate(menu);
    }


    /**
     * 判断菜单是否被使用,如果没有使用，则删除
     *
     * @param basicVo
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean deleteMenu(BasicVo<Integer> basicVo) {
        try {
            Integer count = roleMenuMapper.selectCount(new QueryWrapper<RoleMenu>().eq("menu_id", basicVo.getId()));
            if (count > 0) {
                throw new ServiceException("该菜单被角色使用，无法进行删除！");
            }
            Integer childCount = menuMapper.selectCount(new QueryWrapper<Menu>().eq("menu_pid", basicVo.getId()));
            if (childCount > 0) {
                throw new ServiceException("该菜单存在子菜单，请先删除子菜单后再操作！");
            }
            Menu menu = menuMapper.selectById(basicVo.getId());
            if (menu != null) {
                menuMapper.deleteById(basicVo.getId());
                this.deleteImg(menu.getMenuImgurl());
            }
            return true;
        } catch (Exception ex) {
            log.error("error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除公共服务器上的图片
     *
     * @param menuImgs
     */
    public void deleteImg(String... menuImgs) throws Exception {
//     锁机制，控制多人同时操作该文件
        if (ArrayUtils.isNotEmpty(menuImgs)) {
            for (String img : menuImgs) {
                if (StringUtils.isNotEmpty(img)) {
                    synchronized (this) {
                        Files.deleteIfExists(Paths.get(img));
                    }
                }
            }
        }
    }

    /**
     * 用户菜单列表
     *
     * @param basicVo
     * @return
     */
    @Override
    public List<UserMenuListDto> userMenuList(BasicVo<Long> basicVo) {
        return menuMapper.userMenuList(basicVo);
    }
}
