package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.DownLoadDto;
import com.ryzw.housemanager.dto.FloorDataDto;
import com.ryzw.housemanager.dto.MessageFilesDto;
import com.ryzw.housemanager.entity.FloorDrawing;
import com.ryzw.housemanager.vo.BuildAndYardVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mfl
 * @since 2018-11-26
 */
public interface IFloorDrawingService extends IService<FloorDrawing> {

    /**
     * 查找jsonPlan数据库里面的所有Url
     *
     * @param floorDrawingIdList
     * @return
     */
    List<DownLoadDto> selectCAD(List<Integer> floorDrawingIdList);


    /**
     * 查询单张CAD原文件名
     *
     * @param url
     * @return
     */
    String selectCADName(String url);

    /**
     * 查询信息档案列表
     *
     * @param buildAndYardVo
     * @return
     */
    Page<MessageFilesDto> messageFilesList(BuildAndYardVo buildAndYardVo);

    /**
     * 查询某一楼层相关数据
     *
     * @param buildAndYardVo
     * @return
     */
    FloorDataDto floorData(BuildAndYardVo buildAndYardVo);
}
