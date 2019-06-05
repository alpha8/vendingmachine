package com.yihuyixi.vendingmachine.adapter;

import com.yihuyixi.vendingmachine.bean.GoodsType;

public class GridItem {
    private String icon;
    private GoodsType sectionType;

    public GridItem(){
    }

    public GridItem(String icon, GoodsType sectionType) {
        this.icon = icon;
        this.sectionType = sectionType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public GoodsType getSectionType() {
        return sectionType;
    }

    @Override
    public String toString() {
        return "GridItem{" +
                "icon=" + icon +
                ", sectionType=" + sectionType +
                '}';
    }

    public void setSectionType(GoodsType sectionType) {
        this.sectionType = sectionType;
    }
}
