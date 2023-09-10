package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.RepairProjectListDto;
import com.ryzw.housemanager.entity.RepairProject;
import com.ryzw.housemanager.mapper.RepairProjectMapper;
import com.ryzw.housemanager.service.IRepairProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.vo.RepairProjectListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 项目管理表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */
@Service
public class RepairProjectServiceImpl extends ServiceImpl<RepairProjectMapper, RepairProject> implements IRepairProjectService {
    @Autowired
    private RepairProjectMapper repairProjectMapper;


    /**
     * 分页查询维修项目列表
     * @param repairProjectListVo
     * @return
     */
    @Override
    public Page<RepairProjectListDto> repairProject(RepairProjectListVo repairProjectListVo) {
        Page<RepairProjectListDto> page = new Page<>(repairProjectListVo.getCurrentPage(),repairProjectListVo.getPageSize());
        return repairProjectMapper.repairProject(page,repairProjectListVo);
    }
}
