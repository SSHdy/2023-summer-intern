package com.ryzw.housemanager.controller;

import com.ryzw.housemanager.upload.FileOperateUtils;
import com.ryzw.housemanager.upload.UploadConstant;
import com.ryzw.housemanager.upload.Url;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * <p>
 * 图片上传管理
 * </p>
 */
@RestController
@Slf4j
@Api(value = "上传图片管理", description = "上传图片管理")
@RequestMapping(value = "/uploads")
public class CommunalContorller {

    @Value("${uploadUrl.url}")
    private String path;


    @Value("${uploadUrl.virtualPath}")
    private String virtualPath;

    /**
     * 上传图片
     *
     * @param request
     * @param uploadType
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/file", method = RequestMethod.POST)
    @ApiOperation(value = "上传图片", notes = "上传图片")
    public Object upload(HttpServletRequest request,
                         @RequestParam("uploadType") String uploadType) throws Exception {

        // 图标图片路径
        String imagePath = "";
        switch (uploadType) {
            case "build":
                imagePath = path
                        + UploadConstant.IMAGES_BUILD;
                break;
            case "floor":
                imagePath = path
                        + UploadConstant.IMAGES_FLOOR;
                break;
            case "assets":
                imagePath = path
                        + UploadConstant.IMAGES_ASSETS;
                break;
            case "repair":
                imagePath = path
                        + UploadConstant.IMAGES_REPAIR;
                break;
            case "repairData":
                imagePath = path
                        + UploadConstant.IMAGES_REPAIR_DATA;
                break;
            case "menu":
                imagePath = path
                        + UploadConstant.MENU_IMG;
                break;
            case "rentImg":
                imagePath = path
                        + UploadConstant.rentImg;
                break;
            case "rentCertificate":
                imagePath = path
                        + UploadConstant.rentCertificate;
                break;
            case "propertyRights":
                imagePath = path
                        + UploadConstant.propertyRights;
                break;
            case "constructFile":
                imagePath = path
                        + UploadConstant.constructFile;
                break;
            case "partyWill":
                imagePath = path
                        + UploadConstant.partyWill;
                break;
            case "repairPlan":
                imagePath = path
                        + UploadConstant.repairPlan;
                break;
            case "budgetPlan":
                imagePath = path
                        + UploadConstant.budgetPlan;
                break;
            case "feasibilityStudy":
                imagePath = path
                        + UploadConstant.feasibilityStudy;
                break;
            case "governmentFile":
                imagePath = path
                        + UploadConstant.governmentFile;
                break;
            case "detailDesign":
                imagePath = path
                        + UploadConstant.detailDesign;
                break;
            case "projectBudget":
                imagePath = path
                        + UploadConstant.projectBudget;
                break;
            case "projectReview":
                imagePath = path
                        + UploadConstant.projectReview;
                break;
            case "financeApproval":
                imagePath = path
                        + UploadConstant.financeApproval;
                break;
            case "conversionPurposes":
                imagePath = path
                        + UploadConstant.conversionPurposes;
                break;
            case "auction":
                imagePath = path
                        + UploadConstant.auction;
                break;
            case "dismantle":
                imagePath = path
                        + UploadConstant.dismantle;
                break;
            case "configure":
                imagePath = path
                        + UploadConstant.configure;
                break;
            case "improvingData":
                imagePath = path
                        + UploadConstant.improvingData;
                break;
            case "contract":
                imagePath = path
                        + UploadConstant.contract;
                break;
            case "reportUnit":
                imagePath = path
                        + UploadConstant.reportUnit;
                break;
            case "leaderFile":
                imagePath = path
                        + UploadConstant.leaderFile;
                break;
            default:
                imagePath = path;
                break;
        }

        List<Url> urlList = FileOperateUtils.upload(request, imagePath);
        Map<String, Object> map = new HashMap<String, Object>();

        List<Map<String, Object>> uploadList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(urlList)) {
            for (Url url : urlList) {
                String newImagePath = imagePath + url.getRenameFile();
                String newVirtualPath = newImagePath.replaceAll(path, virtualPath);
                Map<String, Object> imgMap = new HashMap<>(3);
                imgMap.put("id", UUID.randomUUID().toString());
                imgMap.put("imgPath", newImagePath);
                imgMap.put("imgUrl", newVirtualPath);
                imgMap.put("originalName", url);
                uploadList.add(imgMap);
            }
            map.put("imgList", uploadList);
            map.put("msg", "成功了");
        } else {
            map.put("msg", "没有传入图片");
        }
        return map;
    }

    /**
     * 上传图片--省平台
     *
     * @param request
     * @param uploadType
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/unitFile", method = RequestMethod.POST)
    @ApiOperation(value = "上传省平台文件", notes = "上传省平台文件")
    public Object unitUpload(HttpServletRequest request,
                             @RequestParam("uploadType") String uploadType) throws Exception {

        // 图标图片路径
        String imagePath = "";
        switch (uploadType) {
            case "build":
                imagePath = path
                        + UploadConstant.IMAGES_BUILD;
                break;
            case "floor":
                imagePath = path
                        + UploadConstant.IMAGES_FLOOR;
                break;
            case "assets":
                imagePath = path
                        + UploadConstant.IMAGES_ASSETS;
                break;
            case "repair":
                imagePath = path
                        + UploadConstant.IMAGES_REPAIR;
                break;
            case "repairData":
                imagePath = path
                        + UploadConstant.IMAGES_REPAIR_DATA;
                break;
            case "menu":
                imagePath = path
                        + UploadConstant.MENU_IMG;
                break;
            case "rentImg":
                imagePath = path
                        + UploadConstant.rentImg;
                break;
            case "rentCertificate":
                imagePath = path
                        + UploadConstant.rentCertificate;
                break;
            case "propertyRights":
                imagePath = path
                        + UploadConstant.propertyRights;
                break;
            case "constructFile":
                imagePath = path
                        + UploadConstant.constructFile;
                break;
            case "partyWill":
                imagePath = path
                        + UploadConstant.partyWill;
                break;
            case "repairPlan":
                imagePath = path
                        + UploadConstant.repairPlan;
                break;
            case "budgetPlan":
                imagePath = path
                        + UploadConstant.budgetPlan;
                break;
            case "feasibilityStudy":
                imagePath = path
                        + UploadConstant.feasibilityStudy;
                break;
            case "governmentFile":
                imagePath = path
                        + UploadConstant.governmentFile;
                break;
            case "detailDesign":
                imagePath = path
                        + UploadConstant.detailDesign;
                break;
            case "projectBudget":
                imagePath = path
                        + UploadConstant.projectBudget;
                break;
            case "projectReview":
                imagePath = path
                        + UploadConstant.projectReview;
                break;
            case "financeApproval":
                imagePath = path
                        + UploadConstant.financeApproval;
                break;
            case "conversionPurposes":
                imagePath = path
                        + UploadConstant.conversionPurposes;
                break;
            case "auction":
                imagePath = path
                        + UploadConstant.auction;
                break;
            case "dismantle":
                imagePath = path
                        + UploadConstant.dismantle;
                break;
            case "configure":
                imagePath = path
                        + UploadConstant.configure;
                break;
            case "improvingData":
                imagePath = path
                        + UploadConstant.improvingData;
                break;
            case "contract":
                imagePath = path
                        + UploadConstant.contract;
                break;
            case "reportUnit":
                imagePath = path
                        + UploadConstant.reportUnit;
                break;
            default:
                imagePath = path;
                break;
        }

        List<Url> urlList = FileOperateUtils.unitUpload(request, imagePath);
        Map<String, Object> map = new HashMap<String, Object>();

        List<Map<String, Object>> uploadList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(urlList)) {
            for (Url url : urlList) {
                String newImagePath = imagePath + url.getRenameFile();
                String newVirtualPath = newImagePath.replaceAll(path, virtualPath);
                Map<String, Object> imgMap = new HashMap<>(3);
                imgMap.put("id", UUID.randomUUID().toString());
                imgMap.put("imgPath", newImagePath);
                imgMap.put("imgUrl", newVirtualPath);
                imgMap.put("originalName", url);
                uploadList.add(imgMap);
            }
            map.put("imgList", uploadList);
            map.put("msg", "成功了");
        } else {
            map.put("msg", "没有传入图片");
        }
        return map;
    }


    /**
     * 删除服务器图片方法
     *
     * @param physicsImgList
     * @throws Exception
     */
    @RequestMapping(value = "deleteServerImg", method = RequestMethod.POST)
    @ApiOperation(value = "删除服务器图片方法", notes = "删除服务器图片方法")
    public void deleteServerImg(@RequestBody List<String> physicsImgList) throws Exception {
        if (CollectionUtils.isNotEmpty(physicsImgList)) {
            for (String imgUrl : physicsImgList) {
                // 图片的文件路径
                String imagePath = imgUrl;
                if (StringUtils.isNotEmpty(imagePath)) {
                    synchronized (this) {
                        Files.deleteIfExists(Paths.get(imagePath));
                    }
                }
            }
        }
    }
}
