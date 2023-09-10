package com.ryzw.housemanager.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *房屋类型面积输出
 * </p>
 *
 * @author mfl
 * @since 2019/5/6
 */
@Data
public class TypeAreaDto {
    /**
     * 房屋类型名称
     */
    private String housetypeName;

    /**
     * 房屋类型面积
     */
    private Float typeArea;
}
