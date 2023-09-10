package com.ryzw.housemanager.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 修改出租租金提醒,到期提醒状态的条件
 * </p>
 *
 * @author zf
 * @since 2019/5/10
 */
@Data
public class UpdateIsReadDto {

    /**
     * 类型 1出租租金提醒  2出租到期提醒  3合同租金提醒  4合同到期提醒
     */
    @NotNull(message = "类型不能为空")
    private Integer type;

    /**
     * 出租编号集合
     */
    private List<Long> rentIdList;

    /**
     * 租用合同编号集合
     */
    private List<Long> contractIdList;

}
