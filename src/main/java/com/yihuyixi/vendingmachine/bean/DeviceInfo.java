package com.yihuyixi.vendingmachine.bean;

import java.io.Serializable;

public class DeviceInfo implements Serializable {
    private String type;
    private String address;
    private String deviceId;
    private String deviceStatus;
    private String managerPwd;
    private String serviceNo;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getManagerPwd() {
        return managerPwd;
    }

    public void setManagerPwd(String managerPwd) {
        this.managerPwd = managerPwd;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "type='" + type + '\'' +
                ", address='" + address + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", deviceStatus='" + deviceStatus + '\'' +
                ", managerPwd='" + managerPwd + '\'' +
                ", serviceNo='" + serviceNo + '\'' +
                '}';
    }
}
