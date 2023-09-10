package com.ryzw.houseauthority.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 分页信息
 * </p>
 *
 * @author yz
 * @since 2018-11-21
 */
@Data
public class PageVo {
    /**
     * 当前页码
     */
    @TableField(exist = false)
    @NotNull(message = "当前页面数无效")
    @ApiModelProperty(value = "当前页码")
    private Integer currentPage;

    /**
     * 每页显示的数量
     */
    @NotNull(message = "页面数无效")
    @TableField(exist = false)
    @ApiModelProperty(value = "每页显示的数量")
    private Integer pageSize;
}
