package com.yihuyixi.vendingmachine.vo;

import java.io.Serializable;

public class ResponseEntity implements Serializable {
    private PayVO data;
    private String message;
    private int result;

    @Override
    public String toString() {
        return "ResponseEntity{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
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
