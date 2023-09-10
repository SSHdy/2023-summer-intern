package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *人员职称信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/9/5
 */
@Data
public class YardFourDto {
    /**
     * 职称编号
     */
    private Integer professionalId;

    /**
     * 职称名字
     */
    private String professionalName;

    /**
     * 职称人员数量
     */
    private Integer personNum;
}
