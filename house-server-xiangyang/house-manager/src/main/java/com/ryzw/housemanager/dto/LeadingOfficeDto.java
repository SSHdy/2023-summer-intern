package com.ryzw.housemanager.dto;

import com.ryzw.housemanager.entity.LeadingOffice;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 领导人员办公室输出信息
 * </p>
 *
 * @author mfl
 * @since 2018/12/28
 */
@Data
public class LeadingOfficeDto {

    /**
     * 单位序号
     */
    private Long unitId;

    /**
     * 使用形式
     */
    private String useForm;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 分配时间
     */
    private Date distributionTime;


    /**
     * 编号
     */
    private Long useunitId;

    /**
     * 人员集合
     */
    private List<LeadingOffice> leadingOfficeList;

    /**
     * 关系集合
     */
    private List<UseRelationshipListDto> useRelationshipList;

}
