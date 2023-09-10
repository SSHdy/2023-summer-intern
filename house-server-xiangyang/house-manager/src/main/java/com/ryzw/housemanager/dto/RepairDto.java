package com.ryzw.housemanager.dto;

import com.ryzw.housemanager.vo.PageVo;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *     输出给前端的维修申请信息
 * </p>
 *
 * @author wj
 * @since 2019/3/4 0004
 */
@Data
public class RepairDto extends PageVo {

    /**
     * 维修编号
     */
    private Long repairId;


    /**
     * 楼座编号
     */
    private Long buildId;

    /**
     * 楼座名称
     */
    private String buildName;

    /**
     * 所属单位id
     */
    private Long unitId;

    /**
     * 所属单位名称
     */
    private String unitName;


    /**
     * 申请单位id
     */
    private Long applicationUnitId;

    /**
     * 申请单位名称
     */
    private String applicationUnitName;

    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 房间编号
     */
    private String houseId;

    /**
     * 维修楼层
     */
    private Integer repairLevel;

    /**
     * 项目名称
     */
    private String repairProject;

    /**
     * 维修部位
     */
    private Integer repairPart;

    /**
     * 申请时间
     */
    private Date applicationTime;

    /**
     * 申请状态(0未审核  1未审核通过  2审核通过)
     */
    private Integer applyStatus;

    /**
     * 维修状态(0无效  1挂起  2取消  3完成)
     */
    private Integer repairStatus;





}


