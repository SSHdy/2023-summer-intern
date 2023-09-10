package com.ryzw.housemanager.vo;

import com.ryzw.housemanager.entity.RepairModule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 新增维修模块参数
 * </p>
 *
 * @author zf
 * @since 2019/3/29
 */
@Data
public class RepairModuleVo {

    /**
     * 维修模块对象
     */
    private RepairModule repairModule;

    /**
     * 维修照片路径集合
     */
    private List<Map<String, Object>> addressList;

    /**
     * 维修删除照片id数组
     */
    @ApiModelProperty(value = "维修删除照片id数组")
    private List<Long> deleteRepairImgIdList;


}
