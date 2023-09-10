package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.Yard;
import com.ryzw.housemanager.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 院落信息表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2018-11-21
 */
public interface YardMapper extends BaseMapper<Yard> {

    /**
     * 院落信息列表
     *
     * @param page
     * @param yard
     * @return
     */
    Page<Yard> findInfoById(Page page, @Param("Ya") YardVo yard);


    /**
     * 查询单位绑定所有院落
     *
     * @param unitIds
     * @return
     */
    List<YardResultVo> getAllByUnitID(@Param("unitIds") List<Long> unitIds, @Param("searchYardId") Long searchYardId);

    /**
     * 查询人工分房的院落信息
     *
     * @param unitIds
     * @param searchYardId
     * @return
     */
    List<YardResultVo> getYardHandle(@Param("unitIds") List<Long> unitIds, @Param("searchYardId") Long searchYardId);


    /**
     * 查询单位绑定所有院落
     *
     * @param unitIds
     * @return
     */
    List<YardResultVo> getYardByUnitID(@Param("unitIds") List<Long> unitIds);


    /**
     * 查询所有院落
     *
     * @return
     */
    List<YardResultVo> getAll(@Param("searchId") Long searchId,@Param("unitIds") List<Long> unitIds);


    /**
     * 查询单位下面的院落信息
     *
     * @param unitTreeVo
     * @return
     */
    List<UnitYardDto> unitYard(@Param("Uy") UnitTreeVo unitTreeVo);

    /**
     * 查询院落关联的楼座
     *
     * @param basicVo
     * @return
     */
    YardBuildDto yardBuilds(@Param("Ba") BasicVo<Long> basicVo);

    /**
     * 查询用户权限下的院落位置信息
     *
     * @param unitIds
     * @return
     */
    List<YardPositionDto> yardPosition(@Param("unitIds") List<Long> unitIds);

    /**
     * 根据院落编号查询物业关系
     *
     * @param yardId
     * @return
     */
    YardEstateDto selectEstate(@Param("yardId") Long yardId);

    /**
     * 查询院落详情
     *
     * @param basicVo
     * @return
     */
    YardDetailDto yardDetail(@Param("id") BasicVo<Long> basicVo);

    /**
     * 判断院落/土地下是否有出租出借
     *
     * @param yardId
     * @return
     */
    Integer isHaveRent(@Param("yardId") Long yardId);

    /**
     * 市（州、盟）办公用房信息报表查询第一块
     * @return
     */
    List<YardOneDto> yardOne(@Param("unitIds") List<Long> unitIds);

    /**
     * 市（州、盟）房屋类型信息报表查询第二块
     * @return
     */
    List<YardTypeTwoDto> yardTypeTwo(@Param("unitIds") List<Long> unitIds);

    /**
     * 市（州、盟）办公用房信息报表查询第三块
     * @return
     */
    List<YardThreeDto> yardThree();


    /**
     * 查询院落楼座建成时间
     * @return
     */
    List<YardBuildDateDto> selectYardBuildDate(@Param("unitIds") List<Long> unitIds);

    /**
     * 查询单位列表信息
     * @return
     */
    List<YardUnitDto> unitList(@Param("unitIds") List<Long> unitIds);

    /**
     * 查询单位列表信息
     * @return
     */
    List<YardUnitDto> unitRankList(@Param("unitIds") List<Long> unitIds);

}
