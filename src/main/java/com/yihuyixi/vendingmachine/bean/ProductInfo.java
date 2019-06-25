package com.yihuyixi.vendingmachine.bean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

public class ProductInfo implements Serializable {
    private String pid;
    private String id;
    private String name;
    private String avatar;
    private float price;
    private String sellpoint;
    private int sellCount;
    private List<String> icons;
    private String pictureId;
    private int stock;
    private int status;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public boolean isSaleoff() {
        return status == 1;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

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

    public String getFormatPrice() {
        DecimalFormat df = new DecimalFormat("####.##");
        return df.format(this.getPrice());
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
                "id='" + id + '\'' +
                "pid='" + pid + '\'' +
                "name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", pictureId='" + pictureId + '\'' +
                ", price=" + price +
                ", sellpoint='" + sellpoint + '\'' +
                ", sellCount='" + sellCount + '\'' +
                ", status='" + status + '\'' +
                ", stock='" + stock + '\'' +
                '}';
    }
}
