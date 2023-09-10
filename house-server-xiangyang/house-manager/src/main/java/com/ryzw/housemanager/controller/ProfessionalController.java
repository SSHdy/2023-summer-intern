package com.ryzw.housemanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.ProfessionalDto;
import com.ryzw.housemanager.entity.Professional;
import com.ryzw.housemanager.entity.UseRelationship;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IProfessionalService;
import com.ryzw.housemanager.service.IUseRelationshipService;
import com.ryzw.housemanager.vo.ProfessionalVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 人员职称表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2018-12-06
 */

@Api(value = "ProfessionalController", description = "人员职称关系管理")
@Slf4j
@RestController
@RequestMapping("/professional")
public class ProfessionalController {
    @Autowired
    private IProfessionalService professionalService;

    @Autowired
    private IUseRelationshipService useRelationshipService;


    /**
     * 人员职称关系管理列表
     *
     * @return
     */
    @RequestMapping(value = "/all", method = RequestMethod.POST)
    @ApiOperation(value = "人员职称关系管理列表", notes = "查询人员职称关系管理列表")
    public List<Professional> getList(@RequestBody ProfessionalDto professionalDto) {
        try {
            QueryWrapper<Professional> wrapper = new QueryWrapper<>();
            wrapper.select("professional_id", "professional_name", "specified_area");
            wrapper.orderByAsc("professional_id");
            if (professionalDto.getProfessionalLevel() != null && professionalDto.getUnitGrade() == 0) {
                wrapper.eq("professional_level", professionalDto.getProfessionalLevel());

            } else if(professionalDto.getUnitGrade() != null && professionalDto.getUnitGrade() ==1){
                wrapper.eq("unit_grade", 3);
            }

            return professionalService.list(wrapper);
        } catch (Exception ex) {
            log.error("getListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 人员职称关系管理列表
     *
     * @param professionalVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "人员职称关系管理列表", notes = "可分页查询")
    public IPage<Professional> getList(@RequestBody @Valid ProfessionalVo professionalVo) {
        try {
            QueryWrapper<Professional> wrapper = new QueryWrapper<>();
            if (StringUtils.isNotBlank(professionalVo.getProfessionalName())) {
                wrapper.like("professional_name", professionalVo.getProfessionalName());
            }
            wrapper.orderByAsc("professional_id");
            IPage<Professional> page = new Page<>();
            page.setCurrent(professionalVo.getCurrentPage());
            page.setSize(professionalVo.getPageSize());
            return professionalService.page(page, wrapper);
        } catch (Exception ex) {
            log.error("getListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 删除单个人员职称关系信息
     *
     * @param professional
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除单个人员职称关系信息", notes = "删除单个人员职称关系信息")
    public boolean delete(@RequestBody Professional professional) {
        try {
            if (professional.getProfessionalId() != null) {
                int useRelationShipCount = useRelationshipService.count(new QueryWrapper<UseRelationship>().eq("professional_id", professional.getProfessionalId()));
                if (useRelationShipCount > 0) {
                    throw new ServiceException("该职称与房间存在关联关系不能删除！");
                }
            } else {
                throw new ServiceException("职称ID不能为空！");
            }
            return professionalService.removeById(professional.getProfessionalId());
        } catch (Exception ex) {
            log.error("deleteError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 批量删除人员职称关系信息
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除人员职称关系信息", notes = "批量删除人员职称关系信息")
    public boolean deleteBatchPro(@RequestBody List<Long> ids) {
        try {
            return professionalService.removeByIds(ids);
        } catch (Exception ex) {
            log.error("deleteBatchProError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 新增或修改人员职称关系
     *
     * @param professional
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "新增或修改人员职称关系", notes = "新增或修改人员职称关系")
    public boolean addPro(@RequestBody Professional professional) {
        try {
            return professionalService.saveOrUpdate(professional);
        } catch (Exception ex) {
            log.error("addProError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 所有职称id和名称列表
     *
     * @return
     */
    @ApiOperation(value = "所有职称id和名称列表", notes = "所有职称id和名称列表")
    @RequestMapping(value = "allProList", method = RequestMethod.POST)
    public List<Professional> proInfo() {
        try {
            QueryWrapper<Professional> yardQueryWrapper = new QueryWrapper<>();
            yardQueryWrapper.select("professional_id", "professional_name");
            return professionalService.list(yardQueryWrapper);
        } catch (Exception ex) {
            log.error("proInfoError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

}
