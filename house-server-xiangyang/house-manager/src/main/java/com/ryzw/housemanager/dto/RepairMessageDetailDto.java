package com.ryzw.housemanager.dto;

import com.ryzw.housemanager.entity.RepairImg;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 返回给前段的维修信息详情
 * </p>
 *
 * @author zf
 * @since 2019/4/8
 */
@Data
public class RepairMessageDetailDto {

    /**
     * 维修申请Id
     */
    private Long repairApplyId;

    /**
     * 维修模块id
     */
    private Long repairImgId;

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
     * 维修模块位置
     */
    private String repairModulePosition;

    /**
     * 质保期
     */
    private Integer repairPartQuality;

    /**
     * 竣工后质保期
     */
    private String CompletionQuality;

    /**
     * 维修目标（内容）
     */
    private String repairModuleTarget;

    /**
     * 维修面积
     */
    private Double repairModuleArea;

    /**
     * 质保期
     */
    private Date repairQuality;

    /**
     * 维修地点字符串
     */
    private String positionStr;

    /**
     * 维修照片集合
     */
    private List<RepairImg> repairImgList;


}
