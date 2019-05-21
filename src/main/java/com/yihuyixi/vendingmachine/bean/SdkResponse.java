package com.yihuyixi.vendingmachine.bean;

import com.example.mylibrary.serialportlibrary.protocol.WMSSendType;

import java.io.Serializable;

/**
 * 在WMDeviceToAppCallBack回调中，28为出货指令回调，其中s.substring(34,36)截取到为出货结果，详细为：
 * 00  //正常工作
 * 01  //无电机，电机通路OPEN(无工作过)
 * 02  //无电机，电机通路OPEN(上次工作过)
 * 03  //位置线断开或者电机卡死不转 ,在缺口位置
 * 04  //位置线常闭,转7s多于1圈,或电机卡死不转，不在缺口位置
 * 05  //转小于2s,转小于1圈。在缺口位置
 * 06  //转大于5s,转多于1圈。在缺口位置
 * 07  //转7s,转多于1圈。不在缺口位置#define MOTOR_S_EER6
 * 08  //履带货道未检测到反馈按压（可能无货）
 * 09  //系统忙，不接受指令，或者收到错误的货道值，再或者上次升降机出货失败
 * 10  //电机转了两圈
 * 11  //失败，没有检测到掉货，超时
 * 12  //失败，转了半圈，没有商品掉下来
 * 13  //红外一直检测到低电平，可能被挡住了，也有可能接触不良
 * 14  //没有检测到电机
 * 15  //其它状态
 *
 * 在WMDeviceToAppCallBack回调中，E0为主动上报错误，其中s.substring(34,36)截取到为错误结果，详细为：
 * 00   升降机取货过程中出错。
 * 01   升降机送货到出货口过程中出错。
 * 02   开启货道超时。
 * 03   升降机复位不成功。
 * 04   3min未打开取货口，
 * 05   升降机到达限位点，复原超时报错
 *
 * 在WMDeviceToAppCallBack回调中，E1为开关门上报，其中s.substring(34,36)截取到为门状态，详细为：
 * 01   开门状态
 * 02   关门状态
 */
public class SdkResponse implements Serializable {
    private String cmdString;
    private WMSSendType type;

    public SdkResponse() {

    }

    public SdkResponse(WMSSendType type, String s) {
        this.type = type;
        this.cmdString = s;
    }

    public String getCmdString() {
        return cmdString;
    }

    public void setCmdString(String cmdString) {
        this.cmdString = cmdString;
    }

    public WMSSendType getType() {
        return type;
    }

    public void setType(WMSSendType type) {
        this.type = type;
    }

    public String getOrderNo() {
        if (cmdString != null && !"".equals(cmdString)) {
            return cmdString.substring(18,34);
        }
        return "";
    }

    public String getCode() {
        if (cmdString != null && !"".equals(cmdString)) {
            return cmdString.substring(12, 14);
        }
        return "";
    }

    public String getStatus() {
        if (cmdString != null && !"".equals(cmdString)) {
            return cmdString.substring(34,36);
        }
        return "";
    }

    public String getMessage() {
        if("28".equals(getCode()) && "00".equals(getStatus())) {
            return "出货成功";
        } else if("28".equals(getCode())) {
            return "出货失败";
        } else if("E1".equals(getCode())) {
            return "开关门自动上报";
        } else if("E0".equals(getCode())) {
            return "下位机出现错误自动上报异常";
        }
        return "未知异常";
    }

    @Override
    public String toString() {
        return "SdkResponse{" +
                "cmdString='" + cmdString + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
