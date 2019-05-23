package com.yihuyixi.vendingmachine.vo;

import java.util.List;

public class Artwork {
    private String id;
    private String name;
    private float price;
    private List<PictureInfo> pictures;
    private String sellPoint;
    private Stock stock;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        if (stock != null) {
            return stock.getSalesCount();
        }
        return 0;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Artwork{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", sellPoint='" + sellPoint + '\'' +
                ", stock=" + stock +
                '}';
    }
}
