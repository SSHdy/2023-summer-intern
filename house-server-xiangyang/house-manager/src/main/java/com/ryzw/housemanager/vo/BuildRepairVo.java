package com.ryzw.housemanager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <p>
 * 查询楼栋产权信息
 * </p>
 *
 * @author zf
 * @since 2019/7/25
 */
@Data
public class BuildRepairVo {

    /**
     * 楼座Id
     */
    @NotNull(message = "楼座Id不能为空！")
    private Long buildId;

    /**
     * 开始申请时间
     */
    private Date startTime;

    /**
     * 结束申请时间
     */
    private Date endTime;

}
