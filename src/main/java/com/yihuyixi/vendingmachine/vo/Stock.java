package com.yihuyixi.vendingmachine.vo;

public class Stock {
    private int salesCount;

    public int getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(int salesCount) {
        this.salesCount = salesCount;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "salesCount=" + salesCount +
                '}';
    }
}
