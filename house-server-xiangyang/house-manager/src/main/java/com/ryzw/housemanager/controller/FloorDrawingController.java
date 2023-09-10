package com.ryzw.housemanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.DownLoadDto;
import com.ryzw.housemanager.dto.FloorDataDto;
import com.ryzw.housemanager.dto.MessageFilesDto;
import com.ryzw.housemanager.entity.Build;
import com.ryzw.housemanager.entity.FloorDrawing;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IBuildService;
import com.ryzw.housemanager.service.IFloorDrawingService;
import com.ryzw.housemanager.upload.FileOperateUtils;
import com.ryzw.housemanager.vo.BuildAndYardVo;
import com.ryzw.housemanager.vo.DownLoadVo;
import com.ryzw.housemanager.vo.FloorDrawingVo;
import com.ryzw.housemanager.vo.SelectCadVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lyx
 * @since 2018-12-13
 */
@RestController
@Slf4j
@Api(value = "图片信息管理", description = "图片信息管理")
@RequestMapping("/floorDrawing")
public class FloorDrawingController {

    @Autowired
    IFloorDrawingService floorDrawingService;

    @Autowired
    IBuildService buildService;

    @Autowired
    DozerBeanMapper dozerBeanMapper;

    @Value("${uploadUrl.url}")
    private String url;

    /**
     * 添加cad
     *
     * @param floorDrawingVo
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加CAD", notes = "添加CAD")
    public FloorDrawing add(@RequestBody @Valid FloorDrawingVo floorDrawingVo) {
        try {
            FloorDrawing floorDrawing = dozerBeanMapper.map(floorDrawingVo, FloorDrawing.class);
            floorDrawingService.save(floorDrawing);
            return floorDrawing;
        } catch (Exception ex) {
            log.error("error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 批量删除CAD
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除CAD", notes = "删除CAD")
    public boolean delete(@RequestBody @Valid List<Long> ids) {
        try {
            Collection<FloorDrawing> floorDrawings = floorDrawingService.listByIds(ids);
            if (CollectionUtils.isNotEmpty(floorDrawings)) {
                for (FloorDrawing floorDrawing : floorDrawings) {
                    //删除服务器上cad
                    this.deleteCad(floorDrawing.getFloordrawingUrl());
                }
            }
            //删除数据库cad
            return floorDrawingService.removeByIds(ids);
        } catch (Exception e) {
            log.error("error", e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 查询CAD
     *
     * @param selectCadVo
     * @return
     */
    @RequestMapping(value = "/select", method = RequestMethod.POST)
    @ApiOperation(value = "查询CAD", notes = "查询CAD")
    public List<FloorDrawing> getUnit(@RequestBody @Valid SelectCadVo selectCadVo) {
        QueryWrapper<FloorDrawing> wrapper = new QueryWrapper<>();
        if (selectCadVo.getBuildId() != null) {
            wrapper.like("build_id", selectCadVo.getBuildId());
        }
        if (selectCadVo.getFloor() != null) {
            wrapper.like("floor", selectCadVo.getFloor());
        }
        wrapper.orderByDesc("floor");
        return floorDrawingService.list(wrapper);
    }

    /**
     * 查找floorDrawing数据库里面的所有Url并下载CAD打包
     *
     * @param downLoadVo
     * @return
     */
    @RequestMapping(value = "/downLoad", method = RequestMethod.POST)
    @ApiOperation(value = "下载CAD", notes = "下载CAD")
    public Object downLoad(@RequestBody DownLoadVo downLoadVo, HttpServletResponse response) {
        //接收buildName和floorName
        Long buildId = downLoadVo.getBuildId();
        Build build = buildService.getById(buildId);
        String buildName = build.getBuildName();
        Integer floorName = downLoadVo.getFloorName();
        //接收buildid，并查找返回list
        List<Integer> floorDrawingIdList = downLoadVo.getFloorDrawingIdList();
        if (CollectionUtils.isEmpty(floorDrawingIdList)) {
            throw new ServiceException("请选择有效下载文件");
        }
        List<DownLoadDto> floorPlanUrlList = floorDrawingService.selectCAD(floorDrawingIdList);
        //调用下载文件并打包的接口
        String fileName = "";
        if (floorPlanUrlList.size() > 1) {
            if (floorName == null) {
                fileName = buildName + ".zip";
            } else {
                fileName = buildName.concat("-").concat(String.valueOf(floorName)).concat(".zip");
            }
        } else {
            fileName = floorPlanUrlList.get(0).getDownLoadName();
        }
        FileOperateUtils.fileToZip(response, floorPlanUrlList, fileName);
        return floorPlanUrlList;
    }

    /**
     * 查询信息档案列表
     *
     * @param buildAndYardVo
     * @return
     */
    @RequestMapping(value = "/messageFilesList", method = RequestMethod.POST)
    @ApiOperation(value = "查询信息档案列表", notes = "查询信息档案列表")
    public Page<MessageFilesDto> messageFilesList(@RequestBody @Valid BuildAndYardVo buildAndYardVo) {

        return floorDrawingService.messageFilesList(buildAndYardVo);
    }

    /**
     * 查询某一楼层相关数据
     *
     * @param buildAndYardVo
     * @return
     */
    @RequestMapping(value = "/floorData", method = RequestMethod.POST)
    @ApiOperation(value = "查询某一楼层相关数据", notes = "查询某一楼层相关数据")
    public FloorDataDto floorData(@RequestBody BuildAndYardVo buildAndYardVo) {

        return floorDrawingService.floorData(buildAndYardVo);
    }


    /**
     * 删除公共服务器上的cad
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteCad(String physicalAddress) throws Exception {
        // 文件路径
        String dataPath = physicalAddress;
        if (StringUtils.isNotEmpty(dataPath)) {
            synchronized (this) {
                Files.deleteIfExists(Paths.get(dataPath));
            }
        }
    }
}

