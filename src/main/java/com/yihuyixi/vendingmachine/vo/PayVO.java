package com.yihuyixi.vendingmachine.vo;

import java.io.Serializable;

public class PayVO implements Serializable {
    private String id;
    private String trade_state;
    private String trade_no;

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
                '}';
    }
}
