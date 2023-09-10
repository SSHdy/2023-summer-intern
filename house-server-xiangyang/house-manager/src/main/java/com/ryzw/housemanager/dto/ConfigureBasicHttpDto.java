package com.ryzw.housemanager.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *     配置返回给内控的基础信息
 * </p>
 *
 * @author wj
 * @since 2019-06-27 0027
 */
@Data
@Accessors(chain = true)
public class ConfigureBasicHttpDto {

    /**
     * 申请单位名称
     */
    private String unitName;

    /**
     * 单位负责人
     */
    private String unitCharger;

    /**
     * 编制人数
     */
    private Integer unitComplement;

    /**
     * 实有人数
     */
    private Integer actualNumber;

    /**
     * 现有面积(总面积)
     */
    private Float totalArea;

    /**
     * 人均面积
     */
    private Float perArea;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 单位性质
     */
    private String unitProperty;

    /**
     * 单位地址
     */
    private String unitAddress;

    /**
     * 申请原因
     */
    private String applyReason;

    /**
     * 配置申请编号
     */
    private Long configureId;


}
