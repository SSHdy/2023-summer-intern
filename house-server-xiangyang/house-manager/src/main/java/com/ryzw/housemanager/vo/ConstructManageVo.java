package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 施工管理前端需要传入的参数
 * </p>
 *
 * @author mfl
 * @since 2019/4/10
 */
@Data
public class ConstructManageVo {

    /**
     * 维修申请id
     */
    @NotNull(message = "申请编号不能为空")
    private Long repairApplyId;

    /**
     * 施工管理扩展字段
     */
    @ApiModelProperty(value = "施工管理扩展字段")
    private Map<String, Object> constructObject;
}
