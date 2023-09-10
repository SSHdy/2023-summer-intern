package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 上报附件表
 * </p>
 *
 * @author yz
 * @since 2019-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ReportEnclosure implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 上报附件编号
     */
    @TableId(value = "report_enclosure_id", type = IdType.AUTO)
    private Long reportEnclosureId;

    /**
     * 上报单位编号
     */
    private Long reportId;

    /**
     * 年份
     */
    private Integer reportYear;

    /**
     * 上报文件虚拟地址
     */
    private String reportEnclosureVirtualUrl;

    /**
     * 上报文件物理地址
     */
    private String reportEnclosurePhysicsUrl;

    /**
     * 上报文件名称
     */
    private String reportEnclosureName;

    /**
     * 上报日期
     */
    private Date reportDate;


}
