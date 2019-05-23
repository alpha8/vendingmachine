package com.yihuyixi.vendingmachine.vo;

public class QrcodeVO {
    private String vendingId;
    private String channelId;
    private String productId;
    private String name;
    private String icon;
    private float price;

    @Override
    public String toString() {
        return "QrcodeVO{" +
                "vendingId='" + vendingId + '\'' +
                ", channelId='" + channelId + '\'' +
                ", productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", price=" + price +
                '}';
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getVendingId() {
        return vendingId;
    }

    public void setVendingId(String vendingId) {
        this.vendingId = vendingId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
