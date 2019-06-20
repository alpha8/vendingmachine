package com.yihuyixi.vendingmachine.bean;

public class ChannelBean {
    private int channelId; //货道id
    private String side;// AB柜子
    private String level;
    private String channelName;//货道贴示
    private boolean checked = false;
    private int position;
    private long orderNo;

    public ChannelBean() {
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public ChannelBean(int channelId, String side, String channelName) {
        this.channelId = channelId;
        this.side = side;
        this.channelName = channelName;
    }


    public long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(long orderNo) {
        this.orderNo = orderNo;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return "{" +
                "channelId=" + channelId +
                ", level='" + level + '\'' +
                ", orderNo='" + orderNo + '\'' +
                '}';
    }
}
