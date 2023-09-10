package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 查询业务用房列表
 * </p>
 *
 * @author zf
 * @since 2019/8/30
 */
@Data
public class BusinessHouseDto {

    /**
     * 院落Id
     */
    private Long yardId;

    /**
     * 地址坐落
     */
    private String yardPosition;

    /**
     * 行政区划
     */
    private String regionName;

    /**
     * 楼栋Id
     */
    private Long buildId;

    /**
     * 楼栋名称
     */
    private String buildName;

    /**
     * 权属登记情况
     */
    private String warrantRemark;

    /**
     * 用地面积
     */
    private Float coveredArea;


    /**
     * 总建筑面积
     */
    private Float buildArea;

    /**
     * 建设年代
     */
    private Date buildDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 房间编号集合
     */
    private List<String> houseIdList;

    /**
     * 主要使用单位
     */
    private List<String> unitNameList;


}
