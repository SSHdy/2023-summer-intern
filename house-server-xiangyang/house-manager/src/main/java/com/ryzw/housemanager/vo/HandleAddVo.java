package com.ryzw.housemanager.vo;

import com.ryzw.housemanager.entity.HandleModule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 新增处置申请参数
 * </p>
 *
 * @author zf
 * @since 2019/5/24
 */
@Data
public class HandleAddVo {


    /**
     * 处置申请人
     */
    @ApiModelProperty(required = true,value = "处置申请人")
    @NotNull(message = "处置申请人不能为空")
    private String handleApplicant;

    /**
     * 处置申请人编号
     */
    @ApiModelProperty(required = true,value = "处置申请人编号")
    @NotNull(message = "处置申请人id不能为空")
    private Long handleApplicantId;

    /**
     * 申请原因
     */
    private String applyReason;

    /**
     * 处置是否到房间 0否 1是
     */
    private Integer isHouse;

    /**
     * 处置模块对象集合
     */
    private List<HandleModule> handleModuleList;


}
