package com.yihuyixi.vendingmachine.vo;

import java.io.Serializable;

public class PayVO implements Serializable {
    private String code_url;
    private String out_trade_no;
    private String trade_no;
    private String trade_state;

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getTrade_state() {
        return trade_state;
    }

    public void setTrade_state(String trade_state) {
        this.trade_state = trade_state;
    }

    public String getCode_url() {
        return code_url;
    }

    public void setCode_url(String code_url) {
        this.code_url = code_url;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    @Override
    public String toString() {
        return "PayVO{" +
                "code_url='" + code_url + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", trade_no='" + trade_no + '\'' +
                ", trade_state='" + trade_state + '\'' +
                '}';
    }
}
