package com.yihuyixi.vendingmachine.vo;

import java.io.Serializable;

public class PromoteGoodsVO implements Serializable {
    private int id;
    private String artworkId;
    private int type;
    private int status;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PromoteGoodsVO{" +
                "id=" + id +
                ", artworkId='" + artworkId + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", name='" + name + '\'' +
                '}';
    }
}
