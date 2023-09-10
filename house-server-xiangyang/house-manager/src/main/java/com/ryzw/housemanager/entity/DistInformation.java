package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 行政区划信息表
 * </p>
 *
 * @author wj
 * @since 2019-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DistInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 行政区划id
     */
    @TableId(value = "dist_information_id", type = IdType.AUTO)
    private Integer distInformationId;

    /**
     * 行政区划名称
     */
    private String distName;

    /**
     * 行政区划级别
     */
    private String distLevel;

    /**
     * 行政区划代码
     */
    private String distCode;

    /**
     * 填表人单位
     */
    private String fillerUnit;


}
