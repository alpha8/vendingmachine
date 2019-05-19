package com.yihuyixi.vendingmachine.vo;

import java.io.Serializable;

public class PayVO implements Serializable {
    private String code_url;
    private String out_trade_no;

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
                '}';
    }
}
