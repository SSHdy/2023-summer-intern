package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 项目管理表
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RepairProject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 维修项目管理编号
     */
    @TableId(value = "repair_project_id", type = IdType.AUTO)
    private Long repairProjectId;

    /**
     * 维修项目管理用户
     */
    private Long repairProjectUserid;

    /**
     * 填写日期
     */
    private Date repairProjectDate;

    /**
     * 天气
     */
    private String repairProjectWeather;

    /**
     * 施工人数
     */
    private Integer repairProjectNum;

    /**
     * 填写人名称
     */
    private String repairProjectUsername;

    /**
     * 存在问题
     */
    private String repairProjectProblems;

    /**
     * 质量技术安全
     */
    private String repairProjectSecurity;

    /**
     * 文明施工情况
     */
    private String repairProjectConstruction;

    /**
     * 维修施工进度
     */
    private Double repairProjectProgress;


}
