package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.CategoryDto;
import com.ryzw.housemanager.dto.CategoryTreeDto;
import com.ryzw.housemanager.entity.Category;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.impl.CategoryServiceImpl;
import com.ryzw.housemanager.vo.CategoryTreeVo;
import com.ryzw.housemanager.vo.CategoryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

/**
 * <p>
 * 资产类别表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-01-04
 */

@Slf4j
@Api(value = "资产信息管理", description = "资产信息管理")
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryService;

    /**
     * 新增或修改资产类别信息
     *
     * @param category
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "新增或修改资产类别信息", notes = "新增或修改资产类别信息")
    public boolean saveOrUpdate(@RequestBody @Valid Category category) {
        try {
            return categoryService.saveOrUpdate(category);
        } catch (Exception ex){
            log.error("error",ex);
            throw new  ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除资产信息
     *
     * @param category
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除资产信息", notes = "删除资产信息")
    public boolean deleteUnit(@RequestBody Category category) {
        try {
            QueryWrapper<Category> wrapper = new QueryWrapper<>();
            if (categoryService.count(wrapper.eq("category_pid", category.getCategoryId())) > 0) {
                throw new ServiceException("存在子节点，无法删除");
            }
            return categoryService.removeById(category.getCategoryId());
        } catch (Exception ex){
            log.error("error",ex);
            throw new  ServiceException(ex.getMessage());
        }
    }

    /**
     * 批量删除资产信息
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除资产信息", notes = "批量删除资产信息")
    public boolean deleteCategorys(@RequestBody List<Long> ids) {
        try {
            return categoryService.removeByIds(ids);
        } catch (Exception ex){
            log.error("error",ex);
            throw new  ServiceException(ex.getMessage());
        }
    }

    /**
     * 资产信息列表
     *
     * @return
     */
    @RequestMapping(value = "categoryList", method = RequestMethod.POST)
    @ApiOperation(value = "资产信息列表", notes = "资产信息列表")
    public IPage<Category> categoryList(@RequestBody @Valid CategoryVo categoryVo) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(categoryVo.getCategoryName())) {
            wrapper.like("category_name", categoryVo.getCategoryName());
        }
        if (StringUtils.isNotBlank(categoryVo.getCategoryCode())) {
            wrapper.like("category_code", categoryVo.getCategoryCode());
        }
        wrapper.orderByDesc("category_id");
        IPage<Category> page = new Page<>();
        page.setCurrent(categoryVo.getCurrentPage());
        page.setSize(categoryVo.getPageSize());
        return categoryService.page(page, wrapper);
    }

    /**
     * 用于判断资产编码是否重复
     *
     * @param category
     * @return
     */
    @RequestMapping(value = "unique", method = RequestMethod.POST)
    @ApiOperation(value = "用于判断资产编码是否重复", notes = "用于判断资产编码是否重复")
    public boolean unique(@RequestBody Category category) {
        return categoryService.unique(category);
    }

    /**
     * 查询对应父id下的孩子数量
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "getNum", method = RequestMethod.POST)
    @ApiOperation(value = "查询对应父id下的孩子数量", notes = "查询对应父id下的孩子数量")
    public List<CategoryDto> getNum(@RequestBody List<Long> ids) {
        return categoryService.getNum(ids);

    }

    @RequestMapping(value = "/tree/category", method = RequestMethod.POST)
    @ApiOperation(value = "查询指定类型下的类目", notes = "查询指定类型下的类目")
    public  List<Map<String, Object>> getCategoryTree(@RequestBody @Valid CategoryTreeVo categoryTreeVo) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<CategoryTreeDto> treeDtos = categoryService.treeCategory(categoryTreeVo);

        treeDtos.forEach(tree -> {
            Map<String, Object> treeMap = new HashMap<>();
            treeMap.put("id", tree.getCategoryCode());
            treeMap.put("text", tree.getCategoryName());
            treeMap.put("unit", Strings.isNotBlank(tree.getCategoryUnit()) ? Arrays.asList(tree.getCategoryUnit().split(",")) : Strings.EMPTY);
            treeMap.put("expand", tree.getNum() > 0);
            list.add(treeMap);
        });
        return list;
    }
}
