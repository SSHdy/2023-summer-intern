package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.RepairManageDto;
import com.ryzw.housemanager.entity.RepairItems;
import com.ryzw.housemanager.entity.RepairProjectParts;
import com.ryzw.housemanager.vo.RepairManageUpdateVo;
import com.ryzw.housemanager.vo.RepairMangeAddVo;

import java.util.List;

/**
 * <p>
 * 项目管理施工部位表 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */
public interface IRepairProjectPartsService extends IService<RepairProjectParts> {


    /**
     * 修改施工日志信息
     *
     * @param repairManageUpdateVo
     * @return
     */
    boolean updateRepairManage(RepairManageUpdateVo repairManageUpdateVo);

    /**
     * 新增施工日志
     *
     * @param repairMangeAddVo
     * @return
     */
    boolean addRepairManage(RepairMangeAddVo repairMangeAddVo);

    /**
     * 查询施工日志信息
     *
     * @param repairApplyId
     * @return
     */
    RepairManageDto selectRepairLogDetail(Long repairApplyId);

    /**
     * 查询增减项
     *
     * @param repairApplyId
     * @return
     */
    List<RepairItems> selectRepairItem(Long repairApplyId);
}
