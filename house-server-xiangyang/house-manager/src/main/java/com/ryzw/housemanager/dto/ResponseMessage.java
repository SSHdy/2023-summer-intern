package com.ryzw.housemanager.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 *
 *消息提醒
 * </p >
 *
 * @author mfl
 * @since 2018/12/8
 */
@Data
@Builder(toBuilder = true)
public  class ResponseMessage<T> {
    private int code;
    private T result;

    /**
     * 返回成功的结果
     * @param entity
     * @return
     */
    public static  <T> ResponseMessage suceess(T entity) {
        return builder()
                .result(entity)
                .build();
    }

    /**
     * 返回错误的结果
     * @param error
     * @param entity
     * @return
     */
    public static <T> ResponseMessage fail(HttpStatus error,T entity){
        return builder()
                .code(error.value())
                .result(entity)
                .build();
    }
}
