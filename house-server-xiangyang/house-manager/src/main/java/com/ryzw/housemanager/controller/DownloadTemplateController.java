package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryzw.housemanager.entity.DownloadTemplate;
import com.ryzw.housemanager.service.IDownloadTemplateService;
import com.ryzw.housemanager.vo.BasicVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 下载模板表 前端控制器
 * </p>
 *
 * @author yz
 * @since 2019-09-04
 */
@RestController
@RequestMapping("/downloadTemplate")
@Api(value = "下载模板管理", description = "下载模板管理")
public class DownloadTemplateController {

    @Autowired
    private IDownloadTemplateService downloadTemplateService;

    /**
     * 下载模板列表
     *
     * @return
     */
    @RequestMapping(value = "/downloadTemplateList", method = RequestMethod.POST)
    @ApiOperation(value = "下载模板列表", notes = "下载模板列表")
    public List<DownloadTemplate> downloadTemplateList() {
        QueryWrapper<DownloadTemplate> queryWrapper = new QueryWrapper<>();
        List<DownloadTemplate> downloadTemplateList = downloadTemplateService.list(queryWrapper);
        return downloadTemplateList;
    }

}
