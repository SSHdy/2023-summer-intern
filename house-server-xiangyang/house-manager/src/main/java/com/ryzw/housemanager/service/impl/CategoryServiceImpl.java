package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.dto.CategoryDto;
import com.ryzw.housemanager.dto.CategoryTreeDto;
import com.ryzw.housemanager.entity.Category;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.CategoryMapper;
import com.ryzw.housemanager.service.ICategoryService;
import com.ryzw.housemanager.vo.CategoryTreeVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资产类别表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-01-07
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryServiceImpl categoryService;

    /**
     * 用于判断资产编码是否重复
     *
     * @param category
     * @return
     */
    @Override
    public boolean unique(Category category) {
        try {
            Long categoryId = category.getCategoryId();
//        新增资产信息的情况，没有资产id
            if (categoryId == null) {
                int count = categoryService.count(new QueryWrapper<Category>().eq("category_code", category.getCategoryCode()));
                if (count > 0) {
                    throw new SecurityException("资产编码重复");
                }
            } else {
//            资产信息存在的情况下，修改资产信息
                Category category1 = categoryMapper.selectOne(new QueryWrapper<Category>().eq("category_code", category.getCategoryCode()));
                if (category1 != null) {
                    Long categoryId1 = category1.getCategoryId();
                    if (!categoryId.equals(categoryId1)) {
                        throw new ServiceException("资产编码重复");
                    }
                }
            }
            return true;
        }catch (Exception ex){
            log.error("uniqueError",ex);
            throw new  ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询对应父id下的孩子数量
     *
     * @param ids
     * @return
     */
    @Override
    public List<CategoryDto> getNum(List<Long> ids) {
        return categoryMapper.getNum(ids);
    }


    /**
     * 树形结构查询类目
     * @param categoryTreeVo
     * @return
     */
    @Override
    public List<CategoryTreeDto> treeCategory(CategoryTreeVo categoryTreeVo) {
        return categoryMapper.treeCategory(categoryTreeVo);
    }
}
