package com.yihuyixi.vendingmachine.vo;

import java.util.List;

public class Artwork {
    private String pid;
    private String name;
    private float price;
    private float oldPrice;
    private List<PictureInfo> pictures;
    private String sellPoint;
    private int salesCount;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(float oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<PictureInfo> getPictures() {
        return pictures;
    }

    public void setPictures(List<PictureInfo> pictures) {
        this.pictures = pictures;
    }

    public String getSellPoint() {
        return sellPoint;
    }

    public void setSellPoint(String sellPoint) {
        this.sellPoint = sellPoint;
    }

    public int getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(int salesCount) {
        this.salesCount = salesCount;
    }

    @Override
    public String toString() {
        return "Artwork{" +
                "pid='" + pid + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", oldPrice=" + oldPrice +
                ", sellPoint='" + sellPoint + '\'' +
                ", salesCount='" + salesCount + '\'' +
                ", pictures=" + pictures +
                '}';
    }
}
