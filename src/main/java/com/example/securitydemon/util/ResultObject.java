package com.example.securitydemon.util;

import lombok.Data;

@Data
public class ResultObject <T>{

    private T data;

    private int httpCode;

    private String message;

    public static <T> ResultObject<T> success(T data){
        ResultObject<T> success = new ResultObject<>();
        success.setData(data);
        success.setHttpCode(200);
        return success;
    }

    public static  ResultObject<String> fail(String message , int code){
        ResultObject<String> fail = new ResultObject<>();
        fail.setMessage(message);
        fail.setHttpCode(code);
        return fail;
    }

}
