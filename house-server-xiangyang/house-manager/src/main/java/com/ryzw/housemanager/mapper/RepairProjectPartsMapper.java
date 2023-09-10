package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryzw.housemanager.dto.RepairManageDto;
import com.ryzw.housemanager.entity.RepairProjectParts;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 项目管理施工部位表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */
public interface RepairProjectPartsMapper extends BaseMapper<RepairProjectParts> {

    /**
     * 查询施工日志信息
     *
     * @param repairApplyId
     * @return
     */
    RepairManageDto selectRepairLogDetail(@Param("repairApplyId") Long repairApplyId);
}
