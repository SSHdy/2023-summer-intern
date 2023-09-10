package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mongodb.BasicDBObject;
import com.ryzw.housemanager.dto.DownLoadDto;
import com.ryzw.housemanager.vo.DownLoadRightVo;
import com.ryzw.housemanager.vo.PropertyStorageVo;
import org.bson.Document;

import java.util.List;

/**
 * <p>
 * 产权产籍列表 服务类
 * </p>
 *
 * @author zf
 * @since 2018-03-12
 */
public interface IPropertyRightService {

    /**
     * 添加产权信息
     *
     * @param basicDBObject
     * @return
     */
    BasicDBObject add(BasicDBObject basicDBObject);

    /**
     * 修改产权证信息
     *
     * @param basicDBObject
     * @param physicalAddressList
     * @return
     */
    boolean update(BasicDBObject basicDBObject, List<String> physicalAddressList);


    /**
     * 通过mongodb分页查询产权信息列表
     *
     * @param propertyStorageVo
     * @return
     */
    Page<Document> propertyRightPageList(PropertyStorageVo propertyStorageVo);

    /**
     * 下载产权
     *
     * @param downLoadRightVo
     */
    List<DownLoadDto> downLoad(DownLoadRightVo downLoadRightVo);

    /**
     * 批量删除产权信息
     *
     * @param ids
     * @return
     */
    boolean batchDelete(List<String> ids);
}
