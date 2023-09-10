package com.ryzw.housemanager.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *     统计流程待办事项数量
 * </p>
 *
 * @author wj
 * @since 2019-05-29 0029
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProcessNumDto {

    /**
     *维修数量
     */
    private Long repairNum;

    /**
     * 处置数量
     */
    private Long handleNum;

    /**
     * 处置数量
     */
    private Long configureNum;

}

