package com.yihuyixi.vendingmachine.vo;

import com.yihuyixi.vendingmachine.bean.DeviceInfo;

public class DeviceInfoResponse {
    private int result;
    private DeviceInfo data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public DeviceInfo getData() {
        return data;
    }

    public void setData(DeviceInfo data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DeviceInfoResponse{" +
                "result=" + result +
                ", data=" + data +
                '}';
    }
}
