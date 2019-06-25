package com.yihuyixi.vendingmachine.vo;

import java.io.Serializable;

public class VendorResponse implements Serializable {
    private int result;
    private String msg;
    private VendorUser data;

    public static class VendorUser implements Serializable {
        private String vendorId;
        private String requestId;
        private String openid;
        private int userId;
        private String userName;
        private String userIcon;
        private String vendorMsg;

        public String getVendorMsg() {
            return vendorMsg;
        }

        public void setVendorMsg(String vendorMsg) {
            this.vendorMsg = vendorMsg;
        }

        public String getVendorId() {
            return vendorId;
        }

        public void setVendorId(String vendorId) {
            this.vendorId = vendorId;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserIcon() {
            return userIcon;
        }

        public void setUserIcon(String userIcon) {
            this.userIcon = userIcon;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        @Override
        public String toString() {
            return "VendorUser{" +
                    "vendorId='" + vendorId + '\'' +
                    "requestId='" + requestId + '\'' +
                    ", openid='" + openid + '\'' +
                    ", userId=" + userId +
                    ", userName='" + userName + '\'' +
                    ", userIcon='" + userIcon + '\'' +
                    '}';
        }
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public VendorUser getData() {
        return data;
    }

    public void setData(VendorUser data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "VendorResponse{" +
                "result='" + result + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
