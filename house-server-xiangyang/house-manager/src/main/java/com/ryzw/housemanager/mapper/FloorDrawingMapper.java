package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.FloorDataDto;
import com.ryzw.housemanager.dto.FloorDrawingDto;
import com.ryzw.housemanager.dto.MessageFilesDto;
import com.ryzw.housemanager.entity.FloorDrawing;
import com.ryzw.housemanager.vo.BuildAndYardVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2018-11-26
 */
public interface FloorDrawingMapper extends BaseMapper<FloorDrawing> {


    /**
     * 查找jsonPlan数据库里面的所有Url
     *
     * @param floorDrawingIdList
     * @return
     */
    List<FloorDrawingDto> findFloorDrawingList(@Param("floorDrawingIdList") List<Integer> floorDrawingIdList);

    /**
     * 查询信息档案列表
     *
     * @param page
     * @param buildAndYardVo
     * @return
     */
    Page<MessageFilesDto> messageFilesList(Page<MessageFilesDto> page, @Param("buildAndYardVo") BuildAndYardVo buildAndYardVo);

    /**
     * 查询某一楼层房间数量与面积
     *
     * @param buildAndYardVo
     * @return
     */
    FloorDataDto selectHouseNum(@Param("buildAndYardVo") BuildAndYardVo buildAndYardVo);

    /**
     * 查询人数
     *
     * @param buildAndYardVo
     * @return
     */
    Integer selectLeaderHouse(@Param("buildAndYardVo") BuildAndYardVo buildAndYardVo, @Param("isLeader") Integer isLeader);

    /**
     * 查询办公用房数量及人数
     *
     * @param buildAndYardVo
     * @return
     */
    Map<String, Object> selectOfficeNum(@Param("buildAndYardVo") BuildAndYardVo buildAndYardVo);
}
