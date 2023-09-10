package com.ryzw.housemanager.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mongodb.BasicDBObject;
import com.ryzw.housemanager.entity.Purchasing;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 *     操作资产，前端需要传输的参数
 * </p>
 *
 * @author wj
 * @since 2019/1/11 0011
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetsDbVo {

    /**
     * 采购项目编号
     */
    @ApiModelProperty(required = true,value = "采购项目编号")
    @NotNull(message = "purchasingId不能为空")
    private String purchasingId;

    /**
     * 采购项目名称
     */
    @ApiModelProperty(required = true,value = "采购项目名称",example = "申请10台笔记本电脑")
    @NotNull(message = "采购项目名称不能为空")
    private String purchasingPname;

    /**
     * 操作人
     */
    @ApiModelProperty(required = true,value = "操作人",example = "张三")
    @NotNull(message = "操作人不能为空")
    private String operator;

    /**
     * 资产
     */
    private List<BasicDBObject> assetsList;

    /**
     * 采购基本信息
     */
    private PurchasingInfoVo purchasingInfoVo;

}
