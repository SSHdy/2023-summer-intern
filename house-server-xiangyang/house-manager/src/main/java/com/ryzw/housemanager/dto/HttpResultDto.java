package com.ryzw.housemanager.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Http返回结果
 * </p>
 *
 * @author yz
 * @since 2019/2/22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HttpResultDto<T> {
    /**
     * 错误代码
     */
    private int code;
    /**
     * 消息
     */
    private String msg;
    /**
     * 时间
     */
    private String time;
    /**
     * 数据
     */
    private T data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class DataDetail{
        /**
         * 同步成功的序列
         */
        private List<String> success;

        /**
         * 同步失败的序列
         */
        private List<String> failed;

        /**
         * 同步失败的序列
         */
        private List<String> ignored;

    }
}
