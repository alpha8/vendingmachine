package com.yihuyixi.vendingmachine.vo;

import java.io.Serializable;

public class VendorOrderResponse implements Serializable {
    private String message;
    private int result;

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

    @Override
    public String toString() {
        return "VendorOrderResponse{" +
                "message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
