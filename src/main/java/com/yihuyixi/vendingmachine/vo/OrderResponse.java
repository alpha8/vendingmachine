package com.yihuyixi.vendingmachine.vo;

import com.yihuyixi.vendingmachine.bean.OrderInfo;

import java.io.Serializable;
import java.util.List;

public class OrderResponse implements Serializable {
    private int totalRecords;
    private int totalPages;
    private List<OrderInfo> orders;

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<OrderInfo> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderInfo> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "totalRecords=" + totalRecords +
                ", totalPages=" + totalPages +
                ", orders=" + orders +
                '}';
    }
}
