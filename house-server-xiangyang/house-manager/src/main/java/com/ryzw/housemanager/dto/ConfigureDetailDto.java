package com.ryzw.housemanager.dto;

import com.ryzw.housemanager.entity.Configure;
import com.ryzw.housemanager.entity.ConfigureEnclosure;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 配置详情数据
 * </p>
 *
 * @author zf
 * @since 2019/7/8
 */
@Data
public class ConfigureDetailDto {

    /**
     * 配置对象
     */
    private Configure configure;

    /**
     * 配置附件集合
     */
    private List<ConfigureEnclosure> configureEnclosureList;
}
