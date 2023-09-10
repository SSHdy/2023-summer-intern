package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryzw.housemanager.dto.BuildCoeDto;
import com.ryzw.housemanager.dto.CommonHouseTypeDto;
import com.ryzw.housemanager.dto.HouseTypeReportDto;
import com.ryzw.housemanager.dto.TypeReportDto;
import com.ryzw.housemanager.entity.HouseType;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.HouseTypeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 房屋类型表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2019-04-18
 */
public interface HouseTypeMapper extends BaseMapper<HouseType> {

    /**
     * 房屋类型报表查询
     *
     * @param unitIds
     * @return
     */
    List<HouseTypeReportDto> houseTypeReport(@Param("unitIds") List<Long> unitIds);

    /**
     * 服务用房报表查询
     *
     * @param houseTypeVo
     * @return
     */
    List<TypeReportDto> serviceHouseReport(@Param("type") HouseTypeVo houseTypeVo);

    /**
     * 单位树房间信息
     *
     * @param basicVo
     * @return
     */
    HouseTypeReportDto unitDetail(@Param("Id") BasicVo<Long> basicVo);

    /**
     * 查询某用房类型的面积
     *
     * @param unitIdList
     * @param type
     * @param buildId
     * @return
     */
    List<CommonHouseTypeDto> areaByType(@Param("unitIdList") List<Long> unitIdList, @Param("type") int type, @Param("buildId") Long buildId);

    /**
     * 查询某用房类型的面积
     *
     * @param unitIdList
     * @param type
     * @return
     */
    List<CommonHouseTypeDto> areaByTypeAndYard(@Param("unitIdList") List<Long> unitIdList, @Param("type") int type, @Param("yardId") Long yardId);

    /**
     * 查询各楼栋的系数
     *
     * @return
     */
    List<BuildCoeDto> selectBuildCoe();

    /**
     * 查询单个楼栋的系数
     *
     * @param buildId
     * @return
     */
    BuildCoeDto selectOneBuildCoe(@Param("buildId") Long buildId);
}
