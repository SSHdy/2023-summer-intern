package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 * 树形结构中院落信息
 * </p>
 *
 * @author yz
 * @since 2018/12/12
 */
@Data
public class YardResultVo {
    /**
     * 院落ID
     */
    private Long yardId;

    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 包含的楼座数目
     */
    private Integer buildCount;

    /**
     * 单位编号
     */
    private Long unitId;
}
