package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mongodb.BasicDBObject;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.ryzw.housemanager.dto.DownLoadDto;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IPropertyRightService;
import com.ryzw.housemanager.vo.DownLoadRightVo;
import com.ryzw.housemanager.vo.PropertyStorageVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicUpdate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 产权产籍列表 服务实现类
 * </p>
 *
 * @author zf
 * @since 2018-03-12
 */
@Service
@Slf4j
public class PropertyRightServiceImpl implements IPropertyRightService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${uploadUrl.url}")
    private String url;

    /**
     * 添加产权信息
     *
     * @param basicDBObject
     * @return
     */
    @Override
    public BasicDBObject add(BasicDBObject basicDBObject) {
        try {
            BasicDBObject dbObject = mongoTemplate.insert(basicDBObject, "propertyRight");
            return dbObject;
        } catch (Exception ex) {
            log.error("addError", ex);
            throw new ServiceException(ex.getMessage());
        }

    }

    /**
     * 修改产权证信息
     *
     * @param basicDBObject
     * @param physicalAddressList
     * @return
     */
    @Override
    public boolean update(BasicDBObject basicDBObject, List<String> physicalAddressList) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(basicDBObject.get("_id")));
            Document dbObject = mongoTemplate.findById(basicDBObject.get("_id"), Document.class, "propertyRight");
            if (null == dbObject) {
                throw new ServiceException("编号不正确！");
            }
            Object object = dbObject.get("addressList");
            Update update = new BasicUpdate(new Document("$set", basicDBObject));
            if (object != null) {
                List<Map<String, Object>> addressList = (List<Map<String, Object>>) object;
                List<Map<String, Object>> mapList = addressList.stream().filter(p -> CollectionUtils.isEmpty(physicalAddressList) || !physicalAddressList.contains(p.get("enclosurePhysicsUrl"))).collect(Collectors.toList());
                ((List<Map<String, Object>>) basicDBObject.get("addressList")).addAll(mapList);
            }
            UpdateResult updateResult = mongoTemplate.updateMulti(query, update, "propertyRight");
            return updateResult.getModifiedCount() > 0;
        } catch (Exception ex) {
            log.error("updateError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 通过mongodb分页查询产权信息列表
     *
     * @param propertyStorageVo
     * @return
     */
    @Override
    public Page<Document> propertyRightPageList(PropertyStorageVo propertyStorageVo) {
        try {
            Query query = new Query();
            query.with(new Sort(Sort.Direction.DESC, "certificationTime"));
            int skip = (propertyStorageVo.getCurrentPage() - 1) * propertyStorageVo.getPageSize();
            //判断产权编号
            if (StringUtils.isNotBlank(propertyStorageVo.getPropertyRightId())) {
                Pattern pattern = Pattern.compile("^.*" + propertyStorageVo.getPropertyRightId() + ".*$", Pattern.CASE_INSENSITIVE);
                Criteria criteria = Criteria.where("propertyRightId").regex(pattern);
                query.addCriteria(criteria);
            }
            //判断院落Id
            if (StringUtils.isNotBlank(propertyStorageVo.getYardId())) {
                Pattern pattern = Pattern.compile("^.*" + propertyStorageVo.getYardId() + ".*$", Pattern.CASE_INSENSITIVE);
                Criteria criteria = Criteria.where("yardId").regex(pattern);
                query.addCriteria(criteria);
            }
            //判断楼栋Id
            if (StringUtils.isNotBlank(propertyStorageVo.getBuildId())) {
                Pattern pattern = Pattern.compile("^.*" + propertyStorageVo.getBuildId() + ".*$", Pattern.CASE_INSENSITIVE);
                Criteria criteria = Criteria.where("buildId").regex(pattern);
                query.addCriteria(criteria);
            }
            //判断产权类型
            if (propertyStorageVo.getPropertyType() != null) {
                Criteria criteria = Criteria.where("propertyType").is(propertyStorageVo.getPropertyType());
                query.addCriteria(criteria);
            }
            //mongoTemplate.count计算总数
            long total = mongoTemplate.count(query, "propertyRight");

            // 从那条记录开始
            query.skip(skip);
            // 取多少条记录
            query.limit(propertyStorageVo.getPageSize());
            List<Document> items = mongoTemplate.find(query, Document.class, "propertyRight");
            List<Document> newItems = items.stream().map(p ->
            {
                p.replace("_id", p.getObjectId("_id").toString());
                return p;
            }).collect(Collectors.toList());
            Page<Document> page = new Page<>(propertyStorageVo.getCurrentPage(), propertyStorageVo.getPageSize(), total);
            page.setRecords(newItems);
            return page;
        } catch (Exception ex) {
            log.error("propertyRightPageListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 产权下载
     *
     * @param downLoadRightVo
     */
    @Override
    public List<DownLoadDto> downLoad(DownLoadRightVo downLoadRightVo) {
        try {
            List<String> addressList = downLoadRightVo.getAddressList();
            List<DownLoadDto> addressListDto = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(addressList)) {
                for (String address : addressList) {
                    if (addressList.size() > 1) {
                        DownLoadDto downLoadDto = new DownLoadDto();
                        downLoadDto.setPath("file:///" + address);
                        addressListDto.add(downLoadDto);
                    } else {
                        DownLoadDto downLoadDto = new DownLoadDto();
                        downLoadDto.setDownLoadName(address.substring(45));
                        downLoadDto.setPath(address);
                        addressListDto.add(downLoadDto);
                    }
                }
            }
            return addressListDto;
        } catch (Exception ex) {
            log.error("downLoadError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 批量删除产权信息
     *
     * @param ids
     * @return
     */
    @Override
    public boolean batchDelete(List<String> ids) {
        try {
            DeleteResult deleteResult = null;
            if (CollectionUtils.isNotEmpty(ids)) {
                for (String id : ids) {
                    Document document = mongoTemplate.findById(id, Document.class, "propertyRight");
                    List<Map<String, Object>> addressList = (List<Map<String, Object>>) document.get("addressList");
                    if (CollectionUtils.isNotEmpty(addressList)) {
                        for (Map<String, Object> objectMap : addressList) {
                            String physicsUrl = (String) objectMap.get("enclosurePhysicsUrl");
                            //删除服务器图片
                            this.deletePropertyImg(physicsUrl);
                        }
                    }
                }
                Query query = new Query();
                Criteria criteria = Criteria.where("_id").in(ids);
                query.addCriteria(criteria);
                //批量删除产权信息
                deleteResult = mongoTemplate.remove(query, "propertyRight");
            }
            return deleteResult.getDeletedCount() > 0;
        } catch (Exception ex) {
            log.error("batchDeleteError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除公共服务器上的产权照片
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deletePropertyImg(String physicalAddress) throws Exception {
        try {
            // 文件路径
            String dataPath = url + physicalAddress;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(dataPath)) {
                synchronized (this) {
                    Files.deleteIfExists(Paths.get(dataPath));
                }
            }
        } catch (Exception ex) {
            log.error("deletePropertyImgError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


}
