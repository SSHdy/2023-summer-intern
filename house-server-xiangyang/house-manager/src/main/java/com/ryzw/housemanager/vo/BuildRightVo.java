package com.ryzw.housemanager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 查询楼栋产权信息
 * </p>
 *
 * @author zf
 * @since 2019/7/25
 */
@Data
public class BuildRightVo {

    /**
     * 楼栋Id
     */
    @NotNull(message = "楼栋Id不能为空")
    private Integer buildId;

    /**
     * 产权类型 1房产证  3不动产证
     */
    @NotNull(message = "产权类型")
    private Integer type;
}
