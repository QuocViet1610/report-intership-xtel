package com.example.project1.model.dto;

import com.example.project1.local.Translator;
import com.example.project1.utils.Constants;
import com.example.project1.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> {
    private String datetime;
    private String code;
    private String message;
    private T data;
    private String errorCode;

    public static <T> ResponseResult<T> ofSuccess(T data) {
        ResponseResult<T> ResponseResult = new ResponseResult<>();
        ResponseResult.setCode("200");
        ResponseResult.setMessage(Translator.toMessage("common.success"));
        Date date = new Date();
        ResponseResult.setDatetime(date.toString());
        ResponseResult.setData(data);
        return ResponseResult;
    }

    public static <T> ResponseResult<T> ofSuccess() {
        ResponseResult<T> ResponseResult = new ResponseResult<>();
        ResponseResult.setErrorCode(Constants.ERROR_CODE.SUCCESS);
        ResponseResult.setMessage(Translator.toMessage("common.success"));
        ResponseResult.setDatetime(DateUtils.timeServerNow());
        return ResponseResult;
    }



    public static <T> ResponseResult<T> errorServer(String message) {
        ResponseResult<T> ResponseResult = new ResponseResult<>();
        ResponseResult.setMessage(message);
        ResponseResult.setCode("500");
        Date date = new Date();
        ResponseResult.setDatetime(date.toString());
        return ResponseResult;
    }

    public static <T> ResponseResult<T> ofFail(String message) {
        ResponseResult<T> ResponseResult = new ResponseResult<>();
        ResponseResult.setMessage(message);
        ResponseResult.setCode("400");
        Date date = new Date();
        ResponseResult.setDatetime(date.toString());
        return ResponseResult;
    }

    public static <T> ResponseResult<T> ofFail(String message, String errorCode) {
        ResponseResult<T> ResponseResult = new ResponseResult<>();
        ResponseResult.setMessage(message);
        ResponseResult.setErrorCode(errorCode);
        ResponseResult.setDatetime(DateUtils.timeServerNow());
        return ResponseResult;
    }


    public static <T> ResponseResult<T> unAuthorization(String message) {
        ResponseResult<T> ResponseResult = new ResponseResult<>();
        ResponseResult.setMessage(message);
        ResponseResult.setCode("401");
        Date date = new Date();
        ResponseResult.setDatetime(date.toString());
        return ResponseResult;
    }
}
