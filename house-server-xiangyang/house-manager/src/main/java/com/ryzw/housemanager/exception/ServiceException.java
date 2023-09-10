package com.ryzw.housemanager.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


/**
 * <p>
 *  业务异常信息
 * </p>
 *
 * @author yz
 * @since 2018-11-21
 */
@Getter
public class ServiceException extends  RuntimeException {
       public HttpStatus errorCode;
       public String errorMsg;

       public ServiceException(ExceptionType exceptionType){
              this.errorCode = exceptionType.getStatus();
              this.errorMsg = exceptionType.getExceptionText();
       }

       public ServiceException(HttpStatus errorCode, String errorMsg){
              super(errorMsg);
              this.errorCode = errorCode;
              this.errorMsg = errorMsg;
       }

       public ServiceException(String errorMsg){
              super(errorMsg);
              this.errorCode = HttpStatus.INTERNAL_SERVER_ERROR;
              this.errorMsg = errorMsg;
       }
}
