package com.yihuyixi.vendingmachine.vo;

import java.util.List;

public class ResponseVO {
    private int result;
    private DeviceVO data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public DeviceVO getData() {
        return data;
    }

    public void setData(DeviceVO data) {
        this.data = data;
    }

    public static class DeviceVO {
        private String deviceId;
        private String name;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private List<Artwork> products;

        public List<Artwork> getProducts() {
            return products;
        }

        public void setProducts(List<Artwork> products) {
            this.products = products;
        }
    }
}
