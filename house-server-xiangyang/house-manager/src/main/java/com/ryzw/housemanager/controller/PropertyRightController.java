package com.ryzw.housemanager.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mongodb.BasicDBObject;
import com.mongodb.client.result.DeleteResult;
import com.ryzw.housemanager.dto.DownLoadDto;
import com.ryzw.housemanager.entity.RepairApply;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IPropertyRightService;
import com.ryzw.housemanager.service.IRepairApplyService;
import com.ryzw.housemanager.upload.FileOperateUtils;
import com.ryzw.housemanager.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产权产籍表 前端控制器
 * </p>
 *
 * @author zf
 * @since 2019-03-11
 */
@Slf4j
@Api(value = "产权产籍列表", description = "产权产籍列表")
@RestController
@RequestMapping("/propertyRight")
public class PropertyRightController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IPropertyRightService iPropertyRightService;

    @Autowired
    private IRepairApplyService repairApplyService;

    @Value("${uploadUrl.url}")
    private String url;

    /**
     * 添加产权信息
     *
     * @param basicDBObject
     * @return
     */
    @RequestMapping(value = "addPropertyRight", method = RequestMethod.POST)
    @ApiOperation(value = "添加产权信息", notes = "添加产权信息")
    public BasicDBObject add(@RequestBody BasicDBObject basicDBObject) {
        try {
            return iPropertyRightService.add(basicDBObject);
        } catch (Exception ex) {
            log.error("error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 修改产权证信息
     *
     * @param updatePropertyVo
     * @return
     */
    @RequestMapping(value = "updatePropertyRight", method = RequestMethod.POST)
    @ApiOperation(value = "修改产权证信息", notes = "修改产权证信息")
    public boolean update(@RequestBody UpdatePropertyVo updatePropertyVo) {
        try {
            List<String> physicalAddressList = updatePropertyVo.getPhysicalAddressList();
            if (CollectionUtils.isNotEmpty(physicalAddressList)) {
                for (String physicalAddress : physicalAddressList) {
                    //删除服务器上的产权
                    this.deletePropertyImg(physicalAddress);
                }
            }
            return iPropertyRightService.update(updatePropertyVo.getBasicDBObject(), updatePropertyVo.getPhysicalAddressList());
        } catch (Exception ex) {
            log.error("error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除产权证信息
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "deletePropertyRight", method = RequestMethod.POST)
    @ApiOperation(value = "删除产权证信息", notes = "删除产权证信息")
    public boolean delete(@RequestBody @Valid BasicVo<String> basicVo) {
        try {
            Query query = new Query();
            Criteria criteria = Criteria.where("_id").is(basicVo.getId());
            query.addCriteria(criteria);
            Document document = mongoTemplate.findById(basicVo.getId(), Document.class, "propertyRight");
            List<Map<String, Object>> addressList = (List<Map<String, Object>>) document.get("addressList");
            if (CollectionUtils.isNotEmpty(addressList)) {
                for (Map<String, Object> objectMap : addressList) {
                    String physicsUrl = (String) objectMap.get("enclosurePhysicsUrl");
                    //删除服务器图片
                    this.deletePropertyImg(physicsUrl);
                }
            }
            DeleteResult deleteResult = mongoTemplate.remove(query, "propertyRight");
            return deleteResult.getDeletedCount() > 0;
        } catch (Exception ex) {
            log.error("error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 批量删除产权信息
     *
     * @param basicIdsVo
     * @return
     */
    @RequestMapping(value = "batchDeletePropertyRight", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除产权证信息", notes = "批量删除产权证信息")
    public boolean batchDelete(@RequestBody @Valid BasicIdsVo<String> basicIdsVo) {

        return iPropertyRightService.batchDelete(basicIdsVo.getIds());
    }


    /**
     * 通过mongodb分页查询产权信息列表
     *
     * @param propertyStorageVo
     * @return
     */
    @RequestMapping(value = "propertyRightList", method = RequestMethod.POST)
    @ApiOperation(value = "通过mongodb分页查询产权信息列表", notes = "通过mongodb分页查询产权信息列表")
    public Page<Document> propertyRightPageList(@RequestBody @Valid PropertyStorageVo propertyStorageVo) {

        return iPropertyRightService.propertyRightPageList(propertyStorageVo);
    }

    /**
     * 通过mongodb查询产权信息详情
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation(value = "通过mongodb查询产权信息详情", notes = "通过mongodb查询产权信息详情")
    public Document detail(@RequestBody @Valid BasicVo<String> basicVo) {
        Document document = mongoTemplate.findById(basicVo.getId(), Document.class, "propertyRight");
        document.replace("_id", document.getObjectId("_id").toString());
        return document;
    }

    /**
     * 下载产权照片
     *
     * @param downLoadRightVo
     * @param response
     * @return
     */
    @RequestMapping(value = "/download", method = RequestMethod.POST)
    @ApiOperation(value = "下载产权照片", notes = "下载产权照片")
    public Object download(@RequestBody DownLoadRightVo downLoadRightVo, HttpServletResponse response) {
        List<String> addressList = downLoadRightVo.getAddressList();
        if (CollectionUtils.isEmpty(addressList)) {
            throw new ServiceException("请选择有效下载文件");
        }
        List<DownLoadDto> addressListDto = iPropertyRightService.downLoad(downLoadRightVo);
        String fileName = "";
        if (addressListDto.size() > 1) {
            //产权证下载名称
            if (downLoadRightVo.getPropertyRightId() != null && downLoadRightVo.getDownLoadType() != null) {
                String propertyRightId = downLoadRightVo.getPropertyRightId();
                Integer downLoadType = downLoadRightVo.getDownLoadType();
                //查询mongo中数据
                Document document = mongoTemplate.findById(propertyRightId, Document.class, "propertyRight");
                if (downLoadType.equals(1)) {
                    String cardNumber = (String) document.get("housePropertyCardNumber");
                    fileName = "房产证-" + cardNumber + ".zip";
                }
                if (downLoadType.equals(2)) {
                    String cardNumber = (String) document.get("LandCardNumber");
                    fileName = "土地证-" + cardNumber + ".zip";
                }
                if (downLoadType.equals(3)) {
                    String cardNumber = (String) document.get("warrantNum");
                    fileName = "不动产-" + cardNumber + ".zip";
                }
            }
            if (downLoadRightVo.getRepairApplyId() != null && downLoadRightVo.getDownLoadType() != null) {
                Integer downLoadType = downLoadRightVo.getDownLoadType();
                Long repairApplyId = downLoadRightVo.getRepairApplyId();
                RepairApply repairApply = repairApplyService.getById(repairApplyId);
                //获取维修申请的申请编号
                String repairApplyNo = repairApply.getRepairApplyNo();
                if (downLoadType.equals(20)) {
                    fileName = "维修照片" + repairApplyNo + ".zip";
                }
            }
        } else {
            fileName = addressListDto.get(0).getDownLoadName();
        }
        FileOperateUtils.fileToZip(response, addressListDto, fileName);
        return addressListDto;
    }

    /**
     * 删除公共服务器上的产权照片
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deletePropertyImg(String physicalAddress) throws Exception {
        // 文件路径
        String dataPath = physicalAddress;
        if (StringUtils.isNotEmpty(dataPath)) {
            synchronized (this) {
                Files.deleteIfExists(Paths.get(dataPath));
            }
        }
    }

}
