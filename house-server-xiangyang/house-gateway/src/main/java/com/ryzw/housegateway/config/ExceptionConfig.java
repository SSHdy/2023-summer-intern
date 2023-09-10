package com.ryzw.housegateway.config;

import com.ryzw.housegateway.dto.ResponseMessage;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <p>
 * 全局异常
 * </p>
 *
 * @author yz
 * @since 2018-11-20
 */
@ControllerAdvice
public class ExceptionConfig {
    @ExceptionHandler(value = IllegalStateException.class)
    @ResponseBody
    @ResponseStatus
    public ResponseMessage handlerBindException(IllegalStateException ex) {
        if (ex.getCause().getClass() == FileUploadBase.SizeLimitExceededException.class) {
            return ResponseMessage.fail(HttpStatus.INTERNAL_SERVER_ERROR, "上传文件过大,文件不能超过200M!");
        }
        return ResponseMessage.fail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}