package com.ryzw.houseauthority.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.ryzw.houseauthority.entity.OperationLog;
import com.ryzw.houseauthority.service.IOperationLogService;
import com.ryzw.houseauthority.vo.OperationLogVo;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <p>
 * 操作日志服务类
 * </p>
 *
 * @author yz
 * @since 2019/5/15
 */
@Service
public class OperationLogServiceImpl implements IOperationLogService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 分页查询系统日志
     *
     * @param operationLogVo
     * @return
     */
    @Override
    public Page<OperationLog> pageQuery(OperationLogVo operationLogVo) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        String regex = StringUtils.isNotEmpty(operationLogVo.getUserName()) ? "^.*" + operationLogVo.getUserName() + ".*$" : "^.*.*$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        query.addCriteria(Criteria.where("userName").regex(pattern));

        if (operationLogVo.getBeginTime() != null) {
            criteria = Criteria.where("operationTime").gte(operationLogVo.getBeginTime());
        }
        if (operationLogVo.getEndTime() != null) {
            criteria.andOperator(Criteria.where("operationTime").lte(operationLogVo.getEndTime()));
        }
        query.addCriteria(Criteria.where("method").in("GET", "POST"));

        query.addCriteria(criteria);
        query.with(new Sort(Sort.Direction.DESC, "operationTime"))
                .skip((operationLogVo.getCurrentPage() - 1) * operationLogVo.getPageSize())
                .limit(operationLogVo.getPageSize());
        List<OperationLog> operationLogList = mongoTemplate.find(query, OperationLog.class);
        int count = (int) mongoTemplate.count(query, OperationLog.class);
        Page<OperationLog> page = new Page<>();
        page.setRecords(operationLogList);
        page.setTotal(count);
        page.setSize(count % operationLogVo.getPageSize() == 0 ? count / operationLogVo.getPageSize() : count / operationLogVo.getPageSize() + 1);
        return page;
    }
}
