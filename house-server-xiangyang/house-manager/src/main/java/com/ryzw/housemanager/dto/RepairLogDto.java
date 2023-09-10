package com.ryzw.housemanager.dto;

import com.ryzw.housemanager.vo.PartsIdAndImgVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 施工日志信息
 * </p>
 *
 * @author zf
 * @since 2019/4/4
 */
@Data
public class RepairLogDto {

    /**
     * 维修项目管理编号
     */
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

    /**
     * 施工照片与施工部位Id集合
     */
    private List<PartsIdAndImgVo> partsIdAndImgVoList;

    /**
     * 删除项目管理照片Id集合
     */
    @ApiModelProperty(value = "删除项目管理照片Id集合")
    private List<Long> deleteRepairPmImgIdList;


}
