package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.Build;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.BuildListVo;
import com.ryzw.housemanager.vo.BuildVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 楼座基本信息表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2018-11-21
 */
public interface BuildMapper extends BaseMapper<Build> {

    /**
     * 楼栋列表
     *
     * @param buildListVo
     * @return
     */
    Page<BuildDto> list(Page page, @Param("Build") BuildListVo buildListVo);

    /**
     * 楼栋详细信息
     *
     * @param buildId
     * @return
     */
    BuildDetailDto detail(@Param("buildId") Long buildId);

    /**
     * 查询重大设备
     *
     * @param buildVo
     * @return
     */
    List<EquipmentBuildDto> selectBuildAndEquipment(@Param("build") BuildVo buildVo);


    /**
     * 查询某个楼栋下承租方名称列表
     *
     * @param basicVo
     * @return
     */
    List<String> renterNameList(@Param("build") BasicVo<Long> basicVo);

    /**
     * 查询某个楼栋下的所有单位名称
     *
     * @param basicVo
     * @return
     */
    List<String> unitNameList(@Param("build") BasicVo<Long> basicVo);


    /**
     * 查询某栋楼入驻人员数量
     *
     * @param basicVo
     * @return
     */
    Integer buildPersonNum(@Param("build") BasicVo<Long> basicVo);

    /**
     * 查询某栋楼入驻人员数量
     *
     * @param basicVo
     * @return
     */
    Integer buildOfficeNum(@Param("build") BasicVo<Long> basicVo);


    /**
     * 查询某栋楼出租房的面积
     *
     * @param basicVo
     * @return
     */
    Float rentArea(@Param("build") BasicVo<Long> basicVo);

    /**
     * 查询某栋楼闲置用房的面积
     *
     * @param basicVo
     * @return
     */
    Float spareArea(@Param("build") BasicVo<Long> basicVo);

    /**
     * 查询某栋楼闲置用房的面积
     *
     * @param basicVo
     * @return
     */
    Float selfUseArea(@Param("build") BasicVo<Long> basicVo);

    /**
     * 查询一个用户单位集合下所有的楼栋
     *
     * @param unitIds
     * @return
     */
    List<BuildUnitDto> buildUnit(@Param("yardId") Long yardId, @Param("unitIds") List<Long> unitIds);

    /**
     * 查询用户权限下关联合同的所有楼栋
     *
     * @param unitIds
     * @return
     */
    List<BuildUnitDto> buildContract(@Param("yardId") Long yardId, @Param("unitIds") List<Long> unitIds);

    /**
     * 查询用户权限下的所有关于处置的楼栋
     *
     * @param unitIds
     * @return
     */
    List<BuildUnitDto> buildUnitHandle(@Param("yardId") Long yardId, @Param("unitIds") List<Long> unitIds);

    /**
     * 查询一个用户单位集合下所有的楼层
     *
     * @param unitIds
     * @return
     */
    List<BuildUnitDto> floorList(@Param("buildId") Long buildId, @Param("unitIds") List<Long> unitIds);

    /**
     * 查询一个用户单位集合下所有处置的楼层
     *
     * @param unitIds
     * @return
     */
    List<BuildUnitDto> floorListHandle(@Param("buildId") Long buildId, @Param("unitIds") List<Long> unitIds);

    /**
     * 查询一个用户单位集合下所有房间
     *
     * @param buildId
     * @param id
     * @param unitIds
     * @return
     */
    List<BuildUnitDto> houseList(@Param("floorName") Long id, @Param("buildId") Long buildId, @Param("unitIds") List<Long> unitIds);

    /**
     * 查询一个用户单位集合下处置的房间
     *
     * @param buildId
     * @param id
     * @param unitIds
     * @return
     */
    List<BuildUnitDto> houseListHandle(@Param("floorName") Long id, @Param("buildId") Long buildId, @Param("unitIds") List<Long> unitIds);

    /**
     * 查询用户权限下的楼栋位置信息查询
     *
     * @param unitIds
     * @return
     */
    List<BuildPositionDto> buildPosition(@Param("yardId") Long yardId, @Param("unitIds") List<Long> unitIds);


    /**
     * 根据楼座查询楼层信息
     *
     * @param buildId
     * @return
     */
    BuildFloorDto getBuildFloor(@Param("buildId") Long buildId);

    /**
     * 产权楼栋树
     *
     * @return
     */
    List<HandlePositionDto> rightBuildTree();

}
