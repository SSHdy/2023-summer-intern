package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.dto.DownLoadDto;
import com.ryzw.housemanager.dto.FloorDataDto;
import com.ryzw.housemanager.dto.FloorDrawingDto;
import com.ryzw.housemanager.dto.MessageFilesDto;
import com.ryzw.housemanager.entity.FloorDrawing;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.FloorDrawingMapper;
import com.ryzw.housemanager.service.IFloorDrawingService;
import com.ryzw.housemanager.vo.BuildAndYardVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2018-11-26
 */
@Service
@Slf4j
public class FloorDrawingServiceImpl extends ServiceImpl<FloorDrawingMapper, FloorDrawing> implements IFloorDrawingService {
    @Autowired
    FloorDrawingMapper floorDrawingMapper;

    /**
     * 查找jsonPlan数据库里面的所有Url
     *
     * @param floorDrawingIdList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<DownLoadDto> selectCAD(List<Integer> floorDrawingIdList) {
        try {
            //通过buildId和floorName找到对应的url对象
            List<FloorDrawingDto> floorDrawingDtoList = floorDrawingMapper.findFloorDrawingList(floorDrawingIdList);
            int size = floorDrawingDtoList.size();
            //找到对应的url，循环添加到数组
            List<DownLoadDto> floordrawingUrlList = new ArrayList<>();
            for (FloorDrawingDto floorDrawingDto : floorDrawingDtoList) {
                String floordrawingUrl = floorDrawingDto.getFloordrawingUrl();
                if (size > 1) {
                    DownLoadDto downLoadDto = new DownLoadDto();
                    downLoadDto.setPath("file:///" + floordrawingUrl);
                    floordrawingUrlList.add(downLoadDto);
                } else {
                    String str = selectCADName(floordrawingUrl);
                    DownLoadDto downLoadDto = new DownLoadDto();
                    downLoadDto.setDownLoadName(str);
                    downLoadDto.setPath(floordrawingUrl);
                    floordrawingUrlList.add(downLoadDto);
                }
            }
            return floordrawingUrlList;
        } catch (Exception ex) {
            log.error("selectCADError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 查询单张CAD原文件名
     *
     * @param url
     * @return
     */
    @Override
    public String selectCADName(String url) {
        try {
            FloorDrawing floorDrawing = floorDrawingMapper.selectOne(new QueryWrapper<FloorDrawing>().eq("floorDrawing_url", url));
            String floordrawingFileName = floorDrawing.getFloordrawingFileName();
            return floordrawingFileName;
        } catch (Exception ex) {
            log.error("selectCADNameError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询信息档案列表
     *
     * @param buildAndYardVo
     * @return
     */
    @Override
    public Page<MessageFilesDto> messageFilesList(BuildAndYardVo buildAndYardVo) {
        try {
            Page<MessageFilesDto> page = new Page<>(buildAndYardVo.getCurrentPage(), buildAndYardVo.getPageSize());
            return floorDrawingMapper.messageFilesList(page, buildAndYardVo);
        } catch (Exception ex) {
            log.error("messageFilesListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询某一楼层相关数据
     *
     * @param buildAndYardVo
     * @return
     */
    @Override
    public FloorDataDto floorData(BuildAndYardVo buildAndYardVo) {
        try {
            //查询某一楼层房间数量与面积
            FloorDataDto floorDataDto = floorDrawingMapper.selectHouseNum(buildAndYardVo);
            //查询领导人数
            Integer leaderNum = floorDrawingMapper.selectLeaderHouse(buildAndYardVo, 0);
            //查询使用人数
            Integer useNum = floorDrawingMapper.selectLeaderHouse(buildAndYardVo, 1);
            //查询办公用房数量及人数
            Map<String, Object> map = floorDrawingMapper.selectOfficeNum(buildAndYardVo);
            floorDataDto.setOfficeNum(Long.valueOf(String.valueOf(map.get("officeNum"))));
            if (map.get("officeArea") == null) {
                floorDataDto.setOfficeArea(0D);
            } else {
                floorDataDto.setOfficeArea(Double.valueOf(String.valueOf(map.get("officeArea"))));
            }
            floorDataDto.setLeaderNum(leaderNum);
            floorDataDto.setUseNum(useNum);
            return floorDataDto;
        } catch (Exception ex) {
            log.error("messageFilesListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }
}
