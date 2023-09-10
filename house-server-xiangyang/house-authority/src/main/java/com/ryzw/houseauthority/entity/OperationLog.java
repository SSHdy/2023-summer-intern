package com.ryzw.houseauthority.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *  操作日志实体类
 * </p>
 *
 * @author yz
 * @since 2019/5/15
 */
@Data
@Document("operationlog")
public class OperationLog implements Serializable {

    /**
     * 编号
     */
    private String id;

    /**
     * 操作用户名
     */
    private String userName;

    /**
     * 操作的接口URL
     */
    private String url;

    /**
     * 操作时间
     */
    private Date operationTime;

    /**
     * 访问方式
     */
    private String method;

    /**
     * 消息体(POST访问参数)
     */
    private String body;
}
