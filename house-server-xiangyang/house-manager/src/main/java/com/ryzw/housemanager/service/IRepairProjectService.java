package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.RepairProjectListDto;
import com.ryzw.housemanager.entity.RepairProject;
import com.ryzw.housemanager.vo.RepairProjectListVo;

/**
 * <p>
 * 项目管理表 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */
public interface IRepairProjectService extends IService<RepairProject> {

    /**
     * 分页查询维修项目列表
     * @param repairProjectListVo
     * @return
     */
    Page<RepairProjectListDto> repairProject(RepairProjectListVo repairProjectListVo);
}
