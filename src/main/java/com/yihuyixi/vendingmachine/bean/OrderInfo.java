package com.yihuyixi.vendingmachine.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrderInfo implements Serializable {
    private String orderNo;
    private int userId;
    private String openid;
    private int status;
    private String title;
    private int type;
    private String remarks;
    private Date createAt;
    private Express express;
    private List<ProductItem> products;
    private VendorInfo vendor;

    public static class Express {
        private String expressAddress;
        private String receiver;

        public String getExpressAddress() {
            return expressAddress;
        }

        public void setExpressAddress(String expressAddress) {
            this.expressAddress = expressAddress;
        }

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        @Override
        public String toString() {
            return "Express{" +
                    "expressAddress='" + expressAddress + '\'' +
                    ", receiver='" + receiver + '\'' +
                    '}';
        }
    }

    public static class ProductItem {
        private String id;
        private String name;
        private String icon;
        private float price;
        private int count;

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

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "ProductItem{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", icon='" + icon + '\'' +
                    ", price=" + price +
                    ", count=" + count +
                    '}';
        }
    }

    public static class VendorInfo {
        private String vendorId;
        private int pickCode;
        private int status;

        public String getVendorId() {
            return vendorId;
        }

        public void setVendorId(String vendorId) {
            this.vendorId = vendorId;
        }

        public int getPickCode() {
            return pickCode;
        }

        public void setPickCode(int pickCode) {
            this.pickCode = pickCode;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "VendorInfo{" +
                    "vendorId='" + vendorId + '\'' +
                    ", pickCode=" + pickCode +
                    ", status=" + status +
                    '}';
        }
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Express getExpress() {
        return express;
    }

    public void setExpress(Express express) {
        this.express = express;
    }

    public List<ProductItem> getProducts() {
        return products;
    }

    public void setProducts(List<ProductItem> products) {
        this.products = products;
    }

    public VendorInfo getVendor() {
        return vendor;
    }

    public void setVendor(VendorInfo vendor) {
        this.vendor = vendor;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "orderNo='" + orderNo + '\'' +
                ", userId=" + userId +
                ", openid='" + openid + '\'' +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", remarks='" + remarks + '\'' +
                ", createAt=" + createAt +
                ", express=" + express +
                ", products=" + products +
                ", vendor=" + vendor +
                '}';
    }
}
