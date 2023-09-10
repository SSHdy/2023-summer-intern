package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.RepairDetailDto;
import com.ryzw.housemanager.dto.RepairRemindListDto;
import com.ryzw.housemanager.entity.RepairDic;
import com.ryzw.housemanager.entity.RepairParts;
import com.ryzw.housemanager.entity.RepairProjectParts;
import com.ryzw.housemanager.entity.RepairRemind;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.RepairDicMapper;
import com.ryzw.housemanager.mapper.RepairPartsMapper;
import com.ryzw.housemanager.mapper.RepairProjectPartsMapper;
import com.ryzw.housemanager.mapper.RepairRemindMapper;
import com.ryzw.housemanager.service.IRepairPartsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.RepairRemindVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * 建筑物保养部位表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */
@Slf4j
@Service
public class RepairPartsServiceImpl extends ServiceImpl<RepairPartsMapper, RepairParts> implements IRepairPartsService {
    @Autowired
    private RepairPartsMapper repairPartsMapper;
    @Autowired
    private RepairRemindMapper repairRemindMapper;
    @Autowired
    private RepairProjectPartsMapper repairProjectPartsMapper;
    @Autowired
    private RepairDicMapper repairDicMapper;

    /**
     * 删除维修部位信息
     *
     * @param basicVo
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delete(BasicVo<Integer> basicVo) {
        try {
            if (basicVo.getId() == null) {
                throw new ServiceException("请选择要删除的维修部位");
            }
            RepairRemindVo repairRemindVo = new RepairRemindVo();
            RepairDic repairDic = repairDicMapper.selectOne(new QueryWrapper<RepairDic>().eq("repair_dic_key", "repair_dic_days"));
            //获取提醒天数
            String repairRemindDay = repairDic.getRepairDicValue();
            repairRemindVo.setRepairRemindDay(Integer.valueOf(repairRemindDay));
            repairRemindVo.setRepairPart(basicVo.getId());
            Integer countb = repairRemindMapper.selectRemindCount(repairRemindVo);
            //计算维修申请使用的维修部位数量
            Integer applyPartsCount = repairPartsMapper.selectApplyParts(basicVo.getId());
            if (applyPartsCount > 0) {
                throw new ServiceException("该维修部位有维修项目申请使用,无法进行删除！");
            }
            if (countb > 0) {
                throw new ServiceException("该维修部位被维修提醒使用,无法进行删除！");
            }
            Integer countc = repairProjectPartsMapper.selectCount(new QueryWrapper<RepairProjectParts>().eq("repair_part", basicVo.getId()));
            if (countc > 0) {
                throw new ServiceException("该维修部位被维修项目管理使用,无法进行删除！");
            }

//            RepairParts repairParts = repairPartsMapper.selectById(basicVo.getId());
            RepairParts parts = new RepairParts();
            UpdateWrapper<RepairParts> wrapper = new UpdateWrapper<>();
            wrapper.eq("repair_parts_id",basicVo.getId());
            parts.setIsDelete(1);
            repairPartsMapper.update(parts,wrapper);
            return true;

//            return repairPartsMapper.deleteById(basicVo.getId()) > 0;
        } catch (Exception ex) {
            log.error("deleteError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }
}
