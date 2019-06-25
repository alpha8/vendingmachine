package com.yihuyixi.vendingmachine.vo;

import java.io.Serializable;

public class PayVO implements Serializable {
    private String id;
    private String trade_state;
    private String trade_no;
    private String channelNo;
    private String level;
    private int plc;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getPlc() {
        return plc;
    }

    public void setPlc(int plc) {
        this.plc = plc;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getTrade_state() {
        return trade_state;
    }

    public void setTrade_state(String trade_state) {
        this.trade_state = trade_state;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PayVO{" +
                "id='" + id + '\'' +
                ", trade_state='" + trade_state + '\'' +
                ", trade_no='" + trade_no + '\'' +
                ", plc='" + plc + '\'' +
                ", level='" + level + '\'' +
                ", channelNo='" + channelNo + '\'' +
                '}';
    }
}
