package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 输出给前段的相关维修记录详情
 * </p>
 *
 * @author zf
 * @since 2019/4/9
 */
@Data
public class RepairRecordDetailDto {

    /**
     * 维修模块id
     */

    private Long repairModuleId;

    /**
     * 维修院落
     */
    private String yardName;

    /**
     * 维修楼座
     */
    private String buildName;

    /**
     * 维修楼层
     */
    private Integer floorName;

    /**
     * 维修房间号
     */
    private String houseNumber;

    /**
     * 维修部位名称
     */
    private String repairPartName;

    /**
     * 质保期
     */
    private Integer repairPartQuality;

    /**
     * 维修模块位置
     */
    private String repairModulePosition;

    /**
     *维修申请Id
     */
    private Long repairApplyId;

    /**
     *联系电话
     */
    private String contactPhone;

    /**
     * 负责人
     */
    private String responsiblePerson;

    /**
     * 维修单位集合
     */
    private List<String> repairUnitNameList;

    /**
     * 竣工时间
     */
    private Date completionTime;

    /**
     * 竣工后的质保期
     */
    private Date completionQuality;

    /**
     * 维修地点字符串
     */
    private String positionStr;

}
