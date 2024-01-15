package com.zk.demo.yzf.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by Administrator on 2018/10/17.
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BestpayResult<T> {
    private String code;
    private String msg;

    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BestpayResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BestpayResult(String code, String msg){
        this.code = code;
        this.msg = msg;
        this.data = null;
    }



    public BestpayResult() {
    }

    @Override
    public String toString() {
        return "BestpayResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
