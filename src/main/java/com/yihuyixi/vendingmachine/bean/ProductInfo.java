package com.yihuyixi.vendingmachine.bean;

import java.io.Serializable;
import java.util.List;

public class ProductInfo implements Serializable {
    private String id;
    private String name;
    private String avatar;
    private float price;
    private String sellpoint;
    private int sellCount;
    private List<String> icons;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getIcons() {
        return icons;
    }

    public void setIcons(List<String> icons) {
        this.icons = icons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getSellpoint() {
        return sellpoint;
    }

    public void setSellpoint(String sellpoint) {
        this.sellpoint = sellpoint;
    }

    public int getSellCount() {
        return sellCount;
    }

    public void setSellCount(int sellCount) {
        this.sellCount = sellCount;
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", price=" + price +
                ", sellpoint='" + sellpoint + '\'' +
                ", sellCount='" + sellCount + '\'' +
                '}';
    }
}
