package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryzw.housemanager.dto.CategoryDto;
import com.ryzw.housemanager.dto.CategoryTreeDto;
import com.ryzw.housemanager.entity.Category;
import com.ryzw.housemanager.vo.CategoryTreeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资产类别表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2019-01-07
 */
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 查询对应父id下的孩子数量
     * @param ids
     * @return
     */
    List<CategoryDto> getNum(@Param("list") List<Long> ids);

    /**
     * 按类型和代码查询
     * @return
     */
    List<CategoryTreeDto> treeCategory(CategoryTreeVo categoryTreeVo);
}
