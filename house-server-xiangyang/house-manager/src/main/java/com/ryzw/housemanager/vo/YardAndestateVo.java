package com.ryzw.housemanager.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 新增院落物业参数
 * </p>
 *
 * @author zf
 * @since 2018/05/13
 */
@Data
public class YardAndestateVo {

    /**
     *院落物业编号
     */
    private Long yardEstateId;

    /**
     * 开始使用日期
     */
    private Date startTime;

    /**
     * 结束使用日期
     */
    private Date endTime;

    /**
     *合同金额
     */
    private BigDecimal contractAmount;

    /**
     * 物业编号
     */
    private Long estateId;

    /**
     * 物业服务内容编号
     */
    private Long serviceContentId;

    /**
     * 物业名称
     */
    private String estateName;

    /**
     * 经费来源
     */
    private String moneyFrom;

    /**
     * 服务内容集合
     */
    private List<Long> serviceContentList;
}
