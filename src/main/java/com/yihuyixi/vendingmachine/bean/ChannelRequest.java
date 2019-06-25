package com.yihuyixi.vendingmachine.bean;

import java.io.Serializable;

public class ChannelRequest implements Serializable {
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
        private String level;
        private String pickCode;
        private String preId;

        public String getPickCode() {
            return pickCode;
        }

        public void setPickCode(String pickCode) {
            this.pickCode = pickCode;
        }

        public String getPreId() {
            return preId;
        }

        public void setPreId(String preId) {
            this.preId = preId;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

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
                    ", pickCode=" + pickCode +
                    ", preId=" + preId +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ChannelRequest{" +
                "result=" + result +
                ", data=" + data +
                '}';
    }
}
