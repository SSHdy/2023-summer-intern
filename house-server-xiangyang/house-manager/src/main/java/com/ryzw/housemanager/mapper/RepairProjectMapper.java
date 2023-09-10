package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.RepairProjectListDto;
import com.ryzw.housemanager.entity.RepairProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryzw.housemanager.entity.RepairUnit;
import com.ryzw.housemanager.vo.RepairProjectListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 项目管理表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */
public interface RepairProjectMapper extends BaseMapper<RepairProject> {
    /**
     * 分页查询维修项目列表
     * @param page
     * @param repairProjectListVo
     * @return
     */
    Page<RepairProjectListDto> repairProject(Page page,@Param("Pro") RepairProjectListVo repairProjectListVo);


//    /**
//     * 查询维修单位
//     * @param repair_apply_id
//     * @return
//     */
//    List<String> getRepairUnit(@Param("apply")Long repair_apply_id);
}
