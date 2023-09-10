package com.ryzw.housemanager.mapper;

import com.ryzw.housemanager.entity.RentRelationship;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2019-03-21
 */
public interface RentRelationshipMapper extends BaseMapper<RentRelationship> {

    /**
     * 查询正在租赁的使用房间
     * @param houseIds
     * @return
     */
    int selectRentCount(@Param("houseIds") List<String> houseIds);

}
