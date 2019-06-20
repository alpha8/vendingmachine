package com.yihuyixi.vendingmachine.bean;

import java.io.Serializable;

public class ChannelResponse implements Serializable {
    private int result;
    private ChannelVO data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public ChannelVO getData() {
        return data;
    }

    public void setData(ChannelVO data) {
        this.data = data;
    }

    public static class ChannelVO implements Serializable{
        private ProductInfo product;
        private String channelNo;
        private int count;
        private String deviceId;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public ProductInfo getProduct() {
            return product;
        }

        public void setProduct(ProductInfo product) {
            this.product = product;
        }

        public String getChannelNo() {
            return channelNo;
        }

        public void setChannelNo(String channelNo) {
            this.channelNo = channelNo;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "ChannelVO{" +
                    "product=" + product +
                    ", channelNo=" + channelNo +
                    ", count=" + count +
                    ", deviceId=" + deviceId +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ChannelResponse{" +
                "result=" + result +
                ", data=" + data +
                '}';
    }
}
