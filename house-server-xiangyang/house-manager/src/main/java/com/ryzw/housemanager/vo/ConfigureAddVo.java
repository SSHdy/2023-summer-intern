package com.ryzw.housemanager.vo;

import com.ryzw.housemanager.entity.ConfigureEnclosure;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 新增配置申请参数
 * </p>
 *
 * @author zf
 * @since 2019/6/4
 */
@Data
public class ConfigureAddVo {

    /**
     * 配置申请Id
     */
    private Long configureId;

    /**
     * 配置申请人
     */
    @NotNull(message = "配置申请人不能为空")
    private String configureApplicant;

    /**
     * 配置申请人编号
     */
    @NotNull(message = "配置申请人ID不能为空")
    private Long configureApplicantId;

    /**
     * 联系电话
     */
    @NotNull(message = "联系电话不能为空")
    private String contactPhone;

    /**
     * 联系人
     */
    @NotNull(message = "联系人不能为空")
    private String contacts;

    /**
     * 单位性质
     */
    private String unitProperty;

    /**
     * 单位地址
     */
    private String unitAddress;

    /**
     * 申请原因
     */
    @NotNull(message = "申请原因不能为空")
    private String applyReason;

    /**
     * 配置附件集合
     */
    private List<ConfigureEnclosure> enclosureList;

    /**
     * 要删除的配置附件编号集合
     */
    private List<Long> deleteEnclosure;


}
