package com.ryzw.housemanager.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *  <p>
 *  前端传输过来的房屋信息
 * </p>
 *
 * @author mfl
 * @since 2018/12/8
 */

/**
 * @Data ：注解在类上；提供类所有属性的 getting 和 setting 方法，此外还提供了equals、canEqual、hashCode、toString 方法
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HouseVo extends PageVo {

    /**
     * 房间编号
     */
    @ApiModelProperty(value = "房间编号",example = "1")
    private String houseId;

    /**
     * 房屋类型编号
     */
    @ApiModelProperty(value = "房屋类型编号",example = "1")
    private Integer housetypeId;

    /**
     * 院落楼座关系id
     */
    @ApiModelProperty(value = "编号",example = "1")
    private Long housinguseId;

    /**
     * 楼栋id
     */
    @ApiModelProperty(value = "楼栋id",example = "1")
    private Long buildId;

    /**
     * 房间号
     */
    @ApiModelProperty(value = "房间号",example = "1")
    private String houseNumber;
    /**
     * 楼层
     */
    @ApiModelProperty(value = "楼层",example = "1")
    private Integer floorName;

    /**
     * 使用面积
     */
    @ApiModelProperty(value = "使用面积",example = "70")
    private Float houseUseArea;

    /**
     * 建筑面积
     */
    @ApiModelProperty(value = "建筑面积",example = "150")
    private Float houseBuildArea;

    /**
     * 院落
     */
    @ApiModelProperty(value = "院落",example = "1")
    private Long yardId;

    /**
     * 是否备案(1备案  0取消)
     */
    @ApiModelProperty(value = "是否备案",example = "1")
    private Integer isRecord;


    /**
     * 是否使用
     */
    @ApiModelProperty(value = "是否使用",example = "***")
    private Integer isUse;

    /**
     * 是否闲置
     */
    @ApiModelProperty(value = "是否闲置",example = "***")
    private Integer isIdle;

    /**
     * 使用类型
     */
    @ApiModelProperty(value = "使用类型",example = "***")
    private String useType;

    /**
     * 删除自用房间单位ids
     */
    @ApiModelProperty(value = "删除自用房间单位ids",example = "[]")
    private List<Long> deleteUseUnitIds;

    /**
     * 领导人员办公信息
     */
    @ApiModelProperty(value = "领导人员办公信息",example = "{}")
    private LeadingOfficeVo leadingOfficeVo;

    /**
     * 接收修改院落楼座单位关系list
     */
    @ApiModelProperty(value = "院落楼座单位关系list",example = "[]")
    private List<HousingUseVo> housingUseVoList;

    /**
     *使用单位关系list
     */
    private List<HouseUseUnitVo> housingUseUnitList;
}
