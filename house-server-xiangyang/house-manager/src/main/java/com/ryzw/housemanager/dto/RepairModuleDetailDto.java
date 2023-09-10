package com.ryzw.housemanager.dto;

import com.ryzw.housemanager.entity.RepairImg;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 返回给前段的维修模块详情
 * </p>
 *
 * @author zf
 * @since 2019/4/2
 */
@Data
public class RepairModuleDetailDto {

    /**
     * 维修模块Id
     */
    private Long repairModuleId;

    /**
     * 维修院落
     */
    private String yardName;

    /**
     * 院落Id
     */
    private Long yardId;

    /**
     * 维修楼座
     */
    private String buildName;

    /**
     * 楼座Id
     */
    private Long buildId;

    /**
     * 维修楼层
     */
    private Integer floorName;

    /**
     * 房间Id
     */
    private String houseId;

    /**
     * 维修房间号
     */
    private String houseNumber;

    /**
     * 维修部位
     */
    private String repairPartName;

    /**
     * 维修部位id
     */
    private Long repairPartId;

    /**
     * 质保期
     */
    private Integer repairPartQuality;

    /**
     * 维修目标（内容）
     */
    private String repairModuleTarget;

    /**
     * 维修模块位置
     */
    private String repairModulePosition;

    /**
     * 维修面积
     */
    private Double repairModuleArea;

    /**
     * 维修地点字符串
     */
    private String positionStr;

    /**
     * 维修照片集合
     */
    private List<RepairImg> repairImgList;


}
