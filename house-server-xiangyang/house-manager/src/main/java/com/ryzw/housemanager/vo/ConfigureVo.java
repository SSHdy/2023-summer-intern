package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 配置申请列表查询条件
 * </p>
 *
 * @author zf
 * @since 2019/6/4
 */
@Data
public class ConfigureVo extends PageVo{

    /**
     * 用户id
     */
    @ApiModelProperty(required = true,value = "用户ID",example = "13")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 申请单位id
     */
    private Long unitId;

    /**
     * 开始申请时间
     */
    private Date startTime;

    /**
     * 结束申请时间
     */
    private Date endTime;

    /**
     * 配置编号
     */
    private String configureNo;

    /**
     * 配置申请id集合
     */
    private List<Long> configureIdList;

}
