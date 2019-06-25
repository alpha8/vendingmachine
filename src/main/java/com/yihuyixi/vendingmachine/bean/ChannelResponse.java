package com.yihuyixi.vendingmachine.bean;

import java.io.Serializable;

public class ChannelResponse implements Serializable {
    private int result;
    private ProductInfo data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public ProductInfo getData() {
        return data;
    }

    public void setData(ProductInfo data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ChannelRequest{" +
                "result=" + result +
                ", data=" + data +
                '}';
    }
}
