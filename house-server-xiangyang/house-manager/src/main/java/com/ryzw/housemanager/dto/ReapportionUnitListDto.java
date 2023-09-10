package com.ryzw.housemanager.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *重新分配单位下拉框列表输出
 * </p>
 *
 * @author mfl
 * @since 2019/6/24
 */
@Data
public class ReapportionUnitListDto {
    /**
     * 单位序号
     */
    private Long unitId;

    /**
     * 类型编号
     */
    private String unitName;

    /**
     * 自用单位id
     */
    private Long useunitId;
}
