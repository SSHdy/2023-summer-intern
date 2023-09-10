package com.ryzw.housemanager.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//MongoDb工具类
public class MongoDbUtil {

    /**
     * Document转Map
     *
     * @param documents
     * @return
     */
    public static List<Map<String, Object>> DocumentToMap(List<Document> documents) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(documents)) {
            for (Document document : documents) {
                Map<String, Object> map = new HashMap<>();
                map.putAll(document);
                mapList.add(map);
            }
        }
        return mapList;
    }

    /**
     * 获取mongo中记录条数
     *
     * @param collectionName
     * @return
     */
    public Integer getCount(MongoTemplate mongoTemplate, String collectionName, Integer propertyType) {
        //创建客户端连接
        MongoCollection<Document> collection = mongoTemplate.getCollection(collectionName);
        DBObject groupFields = new BasicDBObject("_id", null);
        groupFields.put("count", new BasicDBObject("$sum", 1));
        BasicDBObject group = new BasicDBObject("$group", groupFields);
        List<BasicDBObject> aggreList = new ArrayList<BasicDBObject>();
        if (propertyType != null) {
            DBObject matchFields = new BasicDBObject("propertyType", propertyType);
            BasicDBObject match = new BasicDBObject("$match", matchFields);
            aggreList.add(match);
        }
        aggreList.add(group);
        AggregateIterable<Document> output = collection.aggregate(aggreList);
        int total = 0;
        for (Document dbObject : output) {
            total = (int) dbObject.get("count");
        }
        return total;
    }

}
