package com.ryzw.housemanager.vo;

import com.ryzw.housemanager.dto.BusinessHouseDto;
import com.ryzw.housemanager.dto.ConstructHouseDto;
import com.ryzw.housemanager.dto.YardUnitProfessionalDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 *  省平台上传数据
 * </p>
 *
 * @author yz
 * @since 2019/9/10
 */
@Data
public class UploadDataVo {

    /**
     * 前端传输的行政区划相关信息
     */
    @ApiModelProperty(value = "行政区划相关信息")
    @NotNull(message = "行政区划相关信息不能为空")
    private DistInformationVo distInformation;


    /**
     * 技术业务用房数据
     */
    @ApiModelProperty(value = "技术业务用房数据")
    @NotNull(message = "技术业务用房数据不能为空")
    private List<BusinessHouseDto> businessHouseDtos;


    /**
     * 市(州、盟)办公用房
     */
    @ApiModelProperty(value = "市(州、盟)办公用房数据")
    @NotNull(message = "市(州、盟)办公用房不能为空")
    private List<YardUnitProfessionalDto> yardUnitProfessionalDtos;

    /**
     * 办公用房建设项目
     */
    @ApiModelProperty(value = "办公用房建设项目数据")
    @NotNull(message = "办公用房建设项目不能为空")
    private List<ConstructHouseDto> constructHouseDtos;
}
