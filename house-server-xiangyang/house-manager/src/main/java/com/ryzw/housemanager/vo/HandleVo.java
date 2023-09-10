package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 查询处置申请列表条件
 * </p>
 *
 * @author zf
 * @since 2019/5/24
 */
@Data
public class HandleVo extends PageVo {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 处置楼栋
     */
    private Long buildId;

    /**
     * 处置院落(土地)集合
     */
    private List<Long> yardIdList;

    /**
     * 申请开始时间
     */
    private Date startTime;

    /**
     * 申请结束时间
     */
    private Date endTime;

    /**
     * 申请单位编号
     */
    private Long unitId;

    /**
     * 处置申请id集合
     */
    private List<Long> handleIdList;

    /**
     * 房间编号集合
     */
    private List<String> houseIdList;

}
