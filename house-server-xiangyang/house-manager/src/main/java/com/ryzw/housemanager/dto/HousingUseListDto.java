package com.ryzw.housemanager.dto;

import com.ryzw.housemanager.entity.LeadingOffice;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *房屋使用列表输出
 * </p>
 *
 * @author mfl
 * @since 2018/12/28
 */
@Data
public class HousingUseListDto {

    /**
     * 编号
     */
    private Long useunitId;

    /**
     * 单位序号
     */
    private Long unitId;

    /**
     * 关系集合
     */
    private List<UseRelationshipListDto> useRelationshipList;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 分配时间
     */
    private Date distributionTime;

    /**
     * 使用形式
     */
    private String useForm;

    /**
     * 院落楼座关系id
     */
    private Long housinguseId;

    /**
     * 人员集合
     */
    private List<LeadingOffice> leadingOfficeList;

}
