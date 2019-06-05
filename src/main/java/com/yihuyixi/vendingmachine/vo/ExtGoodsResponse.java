package com.yihuyixi.vendingmachine.vo;

import java.util.List;

public class ExtGoodsResponse {
    private int pageNum;
    private int total;
    private int pages;
    private List<ExtGoods> list;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<ExtGoods> getList() {
        return list;
    }

    public void setList(List<ExtGoods> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ExtGoodsResponse{" +
                "pageNum=" + pageNum +
                ", total=" + total +
                ", pages=" + pages +
                ", list=" + list +
                '}';
    }


}
