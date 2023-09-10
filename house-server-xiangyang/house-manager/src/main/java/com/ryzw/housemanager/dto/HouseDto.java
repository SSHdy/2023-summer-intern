package com.ryzw.housemanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryzw.housemanager.entity.Rent;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *查询房屋详情信息
 * </p>
 *
 * @author mfl
 * @since 2018/12/12
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class HouseDto {

    /**
     * 房间编号
     */
    private String houseId;

    /**
     * 房间号
     */
    private String houseNumber;

    /**
     * 使用面积
     */
    private Float houseUseArea;

    /**
     * 建筑面积
     */
    private Float houseBuildArea;

    /**
     * 房屋类型编号
     */
    private Integer housetypeId;

    /**
     * 是否备案(1备案  0取消)
     */
    private Integer isRecord;


    /**
     * 集合信息
     */
    private LeadingOfficeDto leadingOfficeVo;

    /**
     * 集合信息
     */
    private List<HousingUseListDto> housingUseVoList;

    /**
     * 非办公用房集合信息
     */
    private List<HouseUseUnitDto> housingUseUnitList;


    /**
     * 房屋租赁
     */
    private Rent rent;

    /**
     * 单位信息集合
     */
    private List<UnitDto> units;

    /**
     * 关联的维修提醒
     */
    private Long repairRemindId;

    /**
     * 关联的维修模块
     */
    private Long repairModuleId;

    /**
     * 关联的配置方案数据
     */
    private Long configurePlanDataId;

    /**
     * 是否使用
     */
    private Integer isUse;

    /**
     * 是否闲置(0使用  1闲置)
     */
    private Integer isIdle;

    /**
     * 使用类型
     */
    private String useType;

    /**
     * 房屋楼栋院落单位id
     */
    private Long housinguseId;

    /**
     * 区分办公和非办公用房
     */
    private Integer houseTypeFlag;

}
