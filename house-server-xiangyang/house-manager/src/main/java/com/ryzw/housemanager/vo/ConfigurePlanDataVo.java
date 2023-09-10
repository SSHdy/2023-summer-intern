package com.ryzw.housemanager.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ryzw.housemanager.entity.ConfigurePlanChangeData;
import com.ryzw.housemanager.entity.ConfigurePlanData;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 *     前端传输的配置方案数据参数
 * </p>
 *
 * @author wj
 * @since 2019-06-18 0018
 */
@Data
public class ConfigurePlanDataVo {

    /**
     * 配置申请编号
     */
    @NotNull(message = "配置申请id不能为空")
    private Long configureId;

    /**
     * 方案ID
     */
    @NotNull(message = "方案ID不能为空")
    private Long planId;

    /**
     *配置方案数据列表
     */
    private List<ConfigurePlanData> configurePlanDataList;

    /**
     * 发生改变的配置房间数据
     */
    private List<ConfigurePlanChangeData> configurePlanChangeDataList;

    /**
     * 撤销操作
     */
    private List<Long> deleteCPDIds;

}
