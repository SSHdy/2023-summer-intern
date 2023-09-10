package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 * 返回给内控的数据
 * </p>
 *
 * @author zf
 * @since 2019-09-25
 */
@Data
public class DbStepListDto {

    /**
     * 步骤编号
     */
    private Long step;

    /**
     * 名称
     */
    private String name;

    /**
     * 步骤标识
     */
    private String stepTags;


}
