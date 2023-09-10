package com.ryzw.housemanager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 退租条件
 * </p>
 *
 * @author zf
 * @since 2019/9/2
 */
@Data
public class RentWithdrawalVo {

    /**
     * 租赁编号
     */
    @NotNull(message = "租赁编号不能为空")
    private Long rentId;

    /**
     * 房间编号集合
     */
    private List<String> houseIdList;

}
