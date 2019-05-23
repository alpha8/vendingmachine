package com.yihuyixi.vendingmachine.vo;

import java.io.Serializable;

public class ResponseEntity implements Serializable {
    private PayVO data;
    private String message;
    private int result;
    private String url;

    @Override
    public String toString() {
        return "ResponseEntity{" +
                "data=" + data +
                "url=" + url +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PayVO getData() {
        return data;
    }

    public void setData(PayVO data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
