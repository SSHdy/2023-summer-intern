package com.ryzw.housegateway.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 操作日志实体类
 * </p>
 *
 * @author yz
 * @since 2019/5/15
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "operationlog")
public class OperationLog implements Serializable {

    /**
     * 操作用户名
     */
    @Indexed
    private String userName;

    /**
     * 操作的接口URL
     */
    private String url;

    /**
     * 操作时间
     */
    @Indexed
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
