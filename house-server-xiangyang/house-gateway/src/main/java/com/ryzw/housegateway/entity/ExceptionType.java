package com.ryzw.housegateway.entity;

import org.springframework.http.HttpStatus;


public enum ExceptionType {

    NoGoods(HttpStatus.INTERNAL_SERVER_ERROR,"没有货物");


    private HttpStatus status;
    private String exceptionText;

    ExceptionType(HttpStatus status, String exceptionText){
        this.status = status;
        this.exceptionText = exceptionText;
    }

    public HttpStatus getStatus(){
        return status;
    }


    public String getExceptionText(){
        return exceptionText;
    }
}
