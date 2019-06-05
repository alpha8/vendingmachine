package com.yihuyixi.vendingmachine.vo;

import java.io.Serializable;
import java.util.Date;

public class ExtGoods implements Serializable {
    private int id;
    private float fieldPrice;
    private float specialPrice;
    private float forwardFee;
    private float buttomFee;
    private int limitCount;
    private String name;
    private String artworkId;
    private int type;
    private String icon;
    private int stock;
    private String sellPoint;
    private String difficulty;
    private String secret;
    private long leftStartTimes;
    private long leftEndTimes;
    private Date startDate;
    private Date endDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getFieldPrice() {
        return fieldPrice;
    }

    public void setFieldPrice(float fieldPrice) {
        this.fieldPrice = fieldPrice;
    }

    public float getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(float specialPrice) {
        this.specialPrice = specialPrice;
    }

    public float getForwardFee() {
        return forwardFee;
    }

    public void setForwardFee(float forwardFee) {
        this.forwardFee = forwardFee;
    }

    public float getButtomFee() {
        return buttomFee;
    }

    public void setButtomFee(float buttomFee) {
        this.buttomFee = buttomFee;
    }

    public int getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(int limitCount) {
        this.limitCount = limitCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtworkId() {
        return artworkId;
    }

    public void setArtworkId(String artworkId) {
        this.artworkId = artworkId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getSellPoint() {
        return sellPoint;
    }

    public void setSellPoint(String sellPoint) {
        this.sellPoint = sellPoint;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public long getLeftStartTimes() {
        return leftStartTimes;
    }

    public void setLeftStartTimes(long leftStartTimes) {
        this.leftStartTimes = leftStartTimes;
    }

    public long getLeftEndTimes() {
        return leftEndTimes;
    }

    public void setLeftEndTimes(long leftEndTimes) {
        this.leftEndTimes = leftEndTimes;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "ExtGoods{" +
                "id=" + id +
                ", fieldPrice=" + fieldPrice +
                ", specialPrice=" + specialPrice +
                ", forwardFee=" + forwardFee +
                ", buttomFee=" + buttomFee +
                ", limitCount=" + limitCount +
                ", name='" + name + '\'' +
                ", artworkId='" + artworkId + '\'' +
                ", type=" + type +
                ", icon='" + icon + '\'' +
                ", stock=" + stock +
                ", sellPoint='" + sellPoint + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", leftStartTimes=" + leftStartTimes +
                ", leftEndTimes=" + leftEndTimes +
                '}';
    }
}