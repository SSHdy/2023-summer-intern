package com.ryzw.housemanager.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.entity.ConfigureEnclosure;
import com.ryzw.housemanager.entity.ConfigureRoom;
import com.ryzw.housemanager.entity.Yard;
import lombok.Data;
import org.bson.Document;

import java.util.List;

/**
 * <p>
 * 返回领导查看配置流程数据
 * </p>
 *
 * @author zf
 * @since 2019/6/11
 */
@Data
public class ConLeaderDto {

    /**
     * mongo中数据
     */
    private List<Document> documentList;

    /**
     * 区域名称集合
     */
    private List<String> regionList;

    /**
     * 电梯台数
     */
    private Integer elevatorsNumber;

    /**
     * 重新分配单位
     */
    private String newUnitName;

    /**
     * 房间信息集合
     */
    private List<ConfigureRoom> configureRoomList;

    /**
     * 分配方案(1调剂 2置换 3租用 4建设)
     */
    private Integer allocationPlan;

    /**
     * 选择方案(1智能分配 2人工分配)
     */
    private Integer selectionPlan;

    /**
     * 方案编号
     */
    private Long planId;

    /**
     * 分配方案数据
     */
    private Page<ConPlanDataDto> pageReuslt;

    /**
     * 配置附件集合
     */
    private List<ConfigureEnclosure> enclosureList;

    /**
     * 院落信息
     */
    private Yard yard;

    /**
     * 楼座信息集合
     */
    private List<BuildDetailDto> buildDetailDtoList;

}
