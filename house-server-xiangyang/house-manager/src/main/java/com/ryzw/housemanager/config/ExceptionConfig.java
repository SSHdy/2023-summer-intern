package com.ryzw.housemanager.config;

import com.ryzw.housemanager.dto.ResponseMessage;
import com.ryzw.housemanager.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadBase;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
@Slf4j
public class ExceptionConfig {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus
    public ResponseMessage handlerBindException(MethodArgumentNotValidException ex){
        BindingResult  bindingResult = ex.getBindingResult();
        if(bindingResult.hasFieldErrors()){
           return  ResponseMessage.fail(HttpStatus.INTERNAL_SERVER_ERROR, bindingResult.getFieldError().getDefaultMessage());
        }
        return  ResponseMessage.fail(HttpStatus.INTERNAL_SERVER_ERROR, "服务端验证信息有误");
    }

    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    @ResponseStatus
    public ResponseMessage handlerBindException(ServiceException ex){
        return ResponseMessage.fail(ex.getErrorCode(), ex.getErrorMsg());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus
    public ResponseMessage handlerBindException(Exception ex){
        return ResponseMessage.fail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}