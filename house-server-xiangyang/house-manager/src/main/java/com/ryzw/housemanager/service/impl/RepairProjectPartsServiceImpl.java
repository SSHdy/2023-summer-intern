package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.dto.RepairLogDto;
import com.ryzw.housemanager.dto.RepairManageDto;
import com.ryzw.housemanager.entity.*;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.*;
import com.ryzw.housemanager.service.IAuthorityUnitService;
import com.ryzw.housemanager.service.IRepairApplyService;
import com.ryzw.housemanager.service.IRepairProjectPartsService;
import com.ryzw.housemanager.vo.PartsIdAndImgVo;
import com.ryzw.housemanager.vo.RepairManageUpdateVo;
import com.ryzw.housemanager.vo.RepairMangeAddVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.Element;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 项目管理施工部位表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */
@Service
@Slf4j
public class RepairProjectPartsServiceImpl extends ServiceImpl<RepairProjectPartsMapper, RepairProjectParts> implements IRepairProjectPartsService {

    @Autowired
    RepairProjectPartsMapper repairProjectPartsMapper;

    @Autowired
    IRepairApplyService repairApplyService;

    @Autowired
    RepairModuleMapper repairModuleMapper;

    @Autowired
    RepairProjectMapper repairProjectMapper;

    @Autowired
    RepairPmImgMapper repairPmImgMapper;

    @Autowired
    IAuthorityUnitService authorityUnitService;

    @Autowired
    RepairItemsMapper repairItemsMapper;

    @Autowired
    RepairApplyMapper repairApplyMapper;

    /**
     * 修改施工日志信息（同一天才能修改）
     *
     * @param repairManageUpdateVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRepairManage(RepairManageUpdateVo repairManageUpdateVo) {
        try {
            RepairProject repairProject = repairManageUpdateVo.getRepairProject();
            //获取日志上的日期
            Date repairProjectDate = repairProject.getRepairProjectDate();
            //判断日志日期与当前日期是否为同一天
            boolean sameDate = this.isSameDate(repairProjectDate, new Date());
            if (sameDate) {
                List<RepairItems> repairItemsList = repairManageUpdateVo.getRepairItemsList();
                if (CollectionUtils.isNotEmpty(repairItemsList)) {
                    //先删除数据库中的文件信息
                    repairItemsMapper.delete(new QueryWrapper<RepairItems>().eq("repair_apply_id", repairManageUpdateVo.getRepairApplyId()));
                    for (RepairItems repairItems : repairItemsList) {
                        //新增新的文件信息
                        repairItemsMapper.insert(repairItems);
                    }
                }
                Long repairProjectId = repairProject.getRepairProjectId();
                Double repairProjectProgress = repairProject.getRepairProjectProgress();
                RepairProject oldRepairProject = repairProjectMapper.selectById(repairProjectId);
                Double oldRepairProjectProgress = oldRepairProject.getRepairProjectProgress();
                if (oldRepairProjectProgress <= repairProjectProgress) {
                    RepairApply repairApply = new RepairApply();
                    repairApply.setRepairApplyId(repairManageUpdateVo.getRepairApplyId());
                    repairApply.setRepairProgress(repairProjectProgress);
                    if (repairProjectProgress.equals(100)) {
                        //完成施工管理--到竣工验收16
                        repairApply.setStep(StepNode.CNACCEPTANCE.getValue());
                    }
                    //修改维修总体进度（维修申请中）
                    repairApplyMapper.updateById(repairApply);
                } else {
                    throw new ServiceException("当前进度不能小于上一次进度");
                }
                //修改日志信息
                repairProjectMapper.updateById(repairProject);

                List<Long> deleteRepairPmImgIdList = repairManageUpdateVo.getDeleteRepairPmImgIdList();
                if (CollectionUtils.isNotEmpty(deleteRepairPmImgIdList)) {
                    //批量删除数据库原施工照片
                    repairPmImgMapper.deleteBatchIds(deleteRepairPmImgIdList);
                }
                List<PartsIdAndImgVo> partsIdAndImgVoList = repairManageUpdateVo.getPartsIdAndImgVoList();
                if (CollectionUtils.isNotEmpty(partsIdAndImgVoList)) {
                    for (PartsIdAndImgVo partsIdAndImgVo : partsIdAndImgVoList) {
                        List<RepairPmImg> repairPmImgList = partsIdAndImgVo.getRepairPmImgList();
                        Long repairProjectPartsId = partsIdAndImgVo.getRepairProjectPartsId();
                        for (RepairPmImg repairPmImg : repairPmImgList) {
                            repairPmImg.setRepairProjectPartsId(repairProjectPartsId);
                            //新增新的照片
                            repairPmImgMapper.insert(repairPmImg);
                        }
                    }
                }
                return true;
            } else {
                throw new ServiceException("同一天才能修改！");
            }
        } catch (Exception ex) {
            log.error("updateRepairManageError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 新增施工日志
     *
     * @param repairMangeAddVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addRepairManage(RepairMangeAddVo repairMangeAddVo) {
        try {
            List<RepairItems> repairItemsList = repairMangeAddVo.getRepairItems();
            if (CollectionUtils.isNotEmpty(repairItemsList)) {
                for (RepairItems repairItems : repairItemsList) {
                    //新增增减项文件
                    repairItemsMapper.insert(repairItems);
                }
            }
            List<PartsIdAndImgVo> partsIdAndImgVoList = repairMangeAddVo.getPartsIdAndImgVoList();
            RepairProject repairProject = repairMangeAddVo.getRepairProject();
            repairProject.setRepairProjectDate(new Date());
            //新增项目管理（日志）信息
            repairProjectMapper.insert(repairProject);
            //修改维修总体进度（维修申请中）
            Double repairProjectProgress = repairProject.getRepairProjectProgress();
            RepairApply repairApply = new RepairApply();
            repairApply.setRepairApplyId(repairMangeAddVo.getRepairApplyId());
            repairApply.setRepairProgress(repairProjectProgress);
            repairApplyMapper.updateById(repairApply);
            //获取新增项目管理后的repairProjectId
            Long repairProjectId = repairProject.getRepairProjectId();
            if (CollectionUtils.isNotEmpty(partsIdAndImgVoList)) {
                for (PartsIdAndImgVo partsIdAndImgVo : partsIdAndImgVoList) {
                    //获取施工部位Id
                    Long repairPartsId = partsIdAndImgVo.getRepairPartsId();
                    //获取施工照片对象集合
                    List<RepairPmImg> repairPmImgList = partsIdAndImgVo.getRepairPmImgList();
                    RepairProjectParts repairProjectParts = new RepairProjectParts();
                    repairProjectParts.setRepairPart(repairPartsId.intValue());
                    repairProjectParts.setRepairProjectId(repairProjectId);
                    Long repairModuleId = partsIdAndImgVo.getRepairModuleId();
                    repairProjectParts.setRepairModuleId(repairModuleId);
                    //新增项目管理施工部位
                    repairProjectPartsMapper.insert(repairProjectParts);
                    //获取新增项目管理施工部位后的repairProjectPartsId
                    Long repairProjectPartsId = repairProjectParts.getRepairProjectPartsId();
                    for (RepairPmImg repairPmImg : repairPmImgList) {
                        repairPmImg.setRepairProjectPartsId(repairProjectPartsId);
                        //新增项目管理照片
                        repairPmImgMapper.insert(repairPmImg);
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            log.error("addRepairManageError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询施工日志信息
     *
     * @param repairApplyId
     * @return
     */
    @Override
    public RepairManageDto selectRepairLogDetail(Long repairApplyId) {
        try {
            RepairManageDto repairManageDto = repairProjectPartsMapper.selectRepairLogDetail(repairApplyId);
            List<RepairLogDto> repairLogDtoList = null;
            if (repairManageDto != null) {
                repairLogDtoList = repairManageDto.getRepairLogDtoList();
            }
            if (CollectionUtils.isNotEmpty(repairLogDtoList)) {
                for (RepairLogDto repairLogDto : repairLogDtoList) {
                    Date repairProjectDate = repairLogDto.getRepairProjectDate();
                    if (this.isSameDate(repairProjectDate, new Date())) {
                        //设置日期状态
                        repairManageDto.setDateState(1);
                    }
                }
            }
            return repairManageDto;
        } catch (Exception ex) {
            log.error("selectRepairLogDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询增减项
     *
     * @param repairApplyId
     * @return
     */
    @Override
    public List<RepairItems> selectRepairItem(Long repairApplyId) {
        try {
            return repairItemsMapper.selectList(new QueryWrapper<RepairItems>().eq("repair_apply_id", repairApplyId));
        } catch (Exception ex) {
            log.error("selectRepairItemError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 判断日期是否在同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public boolean isSameDate(Date date1, Date date2) {
        try {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                    .get(Calendar.YEAR);
            boolean isSameMonth = isSameYear
                    && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
            boolean isSameDate = isSameMonth
                    && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                    .get(Calendar.DAY_OF_MONTH);
            return isSameDate;
        }catch (Exception ex){
            log.error("isSameDateError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


}
