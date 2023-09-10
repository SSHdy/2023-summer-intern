package com.ryzw.housemanager.service;

import com.ryzw.housemanager.dto.CategoryDto;
import com.ryzw.housemanager.dto.CategoryTreeDto;
import com.ryzw.housemanager.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.CategoryTreeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资产类别表 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-01-07
 */
public interface ICategoryService extends IService<Category> {

    /**
     * 用于判断资产编码是否重复
     * @param category
     * @return
     */
    boolean unique(Category category);

    /**
     * 查询对应父id下的孩子数量
     * @param ids
     * @return
     */
    List<CategoryDto> getNum(List<Long> ids);

    /**
     * 按类型和代码查询
     * @return
     */
    List<CategoryTreeDto> treeCategory(CategoryTreeVo categoryTreeVo);
}
