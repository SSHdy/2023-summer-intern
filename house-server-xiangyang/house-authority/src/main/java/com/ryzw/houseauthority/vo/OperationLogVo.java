package com.ryzw.houseauthority.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <p>
 *  操作日志查询输入类
 * </p>
 *
 * @author yz
 * @since 2019/5/15
 */
@Data
public class OperationLogVo {

    /**
     * 操作人
     */
    private String userName;

    /**
     * 操作开始时间
     */
    private Date beginTime;


    /**
     * 操作结束时间
     */
    private Date endTime;


    /**
     * 当前页码
     */
    @NotNull(message = "当前页面数无效")
    private Integer currentPage;

    /**
     * 每页显示的数量
     */
    @NotNull(message = "页面数无效")
    private Integer pageSize;
}
