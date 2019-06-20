package com.yihuyixi.vendingmachine.utils;

import com.yihuyixi.vendingmachine.bean.ChannelBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron
 */
public class ChannelUtils {
    public static List<ChannelBean> getAllChannels(){
        List<ChannelBean> channels = new ArrayList<ChannelBean>();
        int position = 0;
        for (int i=0;i<6;i++){
            if(i<4){
                for(int j=0;j<9;j++){
                    ChannelBean c = new ChannelBean();
                    c.setSide("A");
                    c.setLevel("A"+(i+1));
                    if(i==0)
                        c.setChannelName(c.getSide()+(i*10+(9-j)));
                    else
                        c.setChannelName(c.getSide()+((i+1)*10+(9-j)));
                    int cid = i*10+j;
                    c.setChannelId(cid);
                    c.setPosition(position);
                    c.setOrderNo(getOrderNo(position,cid));
                    position++;
                    channels.add(c);
                    if(i==3&&j==8){
                        ChannelBean c1 = new ChannelBean();
                        c1.setSide("A");
                        c1.setLevel("A"+(i+1));
                        c1.setChannelName(c.getSide()+51);
                        c1.setChannelId(39);
                        c1.setPosition(position);
                        c1.setOrderNo(getOrderNo(position,cid));
                        position++;
                        channels.add(c1);
                    }
                }

            }else{
                for(int j=0;j<5;j++){
                    ChannelBean c = new ChannelBean();
                    c.setSide("A");
                    c.setChannelName(c.getSide()+((i+2)*10+(5-j)));
                    int cid = i*10+j;
                    c.setChannelId(cid);
                    c.setPosition(position);
                    c.setOrderNo(getOrderNo(position,cid));
                    position++;
                    channels.add(c);
                }
            }
        }
        int id = 59;
        for(int i=0;i<6;i++){
            switch (i){
                case 0:
                case 1:
                case 3:
                    for(int j=0;j<4;j++){
                        id++;
                        ChannelBean c = new ChannelBean();
                        c.setSide("B");
                        if(i==0)
                            c.setChannelName(c.getSide()+(i*10+(j+1)));
                        else
                            c.setChannelName(c.getSide()+((i+1)*10+(j+1)));
                        c.setChannelId(id);
                        c.setPosition(position);
                        c.setOrderNo(getOrderNo(position,id));
                        position++;
                        channels.add(c);
                    }
                    break;
                case 2:
                case 5:
                    for(int j=0;j<2;j++){
                        id++;
                        ChannelBean c = new ChannelBean();
                        c.setSide("B");
                        c.setChannelName(c.getSide()+((i+1)*10+(j+1)));
                        c.setChannelId(id);
                        c.setPosition(position);
                        c.setOrderNo(getOrderNo(position,id));
                        position++;
                        channels.add(c);
                    }
                    break;
                case 4:
                    for(int j=0;j<3;j++){
                        id++;
                        ChannelBean c = new ChannelBean();
                        c.setSide("B");
                        c.setChannelName(c.getSide()+((i+1)*10+(j+1)));
                        c.setChannelId(id);
                        c.setPosition(position);
                        c.setOrderNo(getOrderNo(position,id));
                        position++;
                        channels.add(c);
                        if(j==2)
                            id++;
                    }
                    break;

            }
        }
        return  channels;
    }

    public static List<ChannelBean> getAllChannels2(){
        List<ChannelBean> channels = new ArrayList<ChannelBean>();
        int position = 0;
        for (int i=0;i<7;i++){
            if(i<4){
                for(int j=8;j>=0;j--){
                    ChannelBean c = new ChannelBean();
                    c.setSide("A");
                    c.setLevel("A"+(i+1));
                    int cid = i*10+j;
                    c.setChannelId(cid);
                    setPostion(c,position,cid);
                    position++;
                    channels.add(c);
                }

            }else if(i==4){
                ChannelBean c = new ChannelBean();
                c.setSide("A");
                c.setLevel("A"+(i+1));
                c.setChannelId(39);
                setPostion(c,position,39);
                position++;
                channels.add(c);
            }else if(i==5){
                ChannelBean c = new ChannelBean();
                c.setSide("A");
                c.setLevel("A"+(i+1));
                c.setChannelId(48);
                setPostion(c,position,48);
                position++;
                channels.add(c);
                c = new ChannelBean();
                c.setSide("A");
                c.setLevel("A"+(i+1));
                c.setChannelId(47);
                setPostion(c,position,47);
                position++;
                channels.add(c);
            }
            else{
                for(int j=4;j>=0;j--){
                    ChannelBean c = new ChannelBean();
                    c.setSide("A");
                    c.setLevel("A"+(i+1));
                    int cid = 0;
                    switch (j) {
                        case 0:
                            cid = 50;
                            break;
                        case 1:
                            cid = 52;
                            break;
                        case 2:
                            cid = 53;
                            break;
                        case 3:
                            cid = 55;
                            break;
                        case 4:
                            cid = 58;
                            break;
                    }
                    c.setChannelId(cid);
                    setPostion(c,position,cid);
                    position++;
                    channels.add(c);
                }
            }
        }
        int id = 59;
        for(int i=0;i<6;i++){
            switch (i){
                case 0:
                case 1:
                case 3:
                    for(int j=0;j<4;j++){
                        id++;
                        ChannelBean c = new ChannelBean();
                        c.setSide("B");
                        c.setLevel("B"+(i+1));
                        if(i==0)
                            c.setChannelName(c.getSide()+(i*10+(j+1)));
                        else
                            c.setChannelName(c.getSide()+((i+1)*10+(j+1)));
                        c.setChannelId(id);
                        setPostion(c,position,id);
                        position++;
                        channels.add(c);
                    }
                    break;
                case 2:
                case 5:
                    for(int j=0;j<2;j++){
                        id++;
                        ChannelBean c = new ChannelBean();
                        c.setSide("B");
                        c.setLevel("B"+(i+1));
                        c.setChannelName(c.getSide()+((i+1)*10+(j+1)));
                        c.setChannelId(id);
                        setPostion(c,position,id);
                        position++;
                        channels.add(c);
                    }
                    break;
                case 4:
                    for(int j=0;j<3;j++){
                        id++;
                        ChannelBean c = new ChannelBean();
                        c.setSide("B");
                        c.setLevel("B"+(i+1));
                        c.setChannelName(c.getSide()+((i+1)*10+(j+1)));
                        c.setChannelId(id);
                        setPostion(c,position,id);
                        position++;
                        channels.add(c);
                        if(j==2)
                            id++;
                    }
                    break;
            }
        }
        return  channels;
    }

    public static void main(String[] args){

        String ss1 = "0000000000000101";
        String s1 = String.valueOf(ss1).substring(12,14);
        String s2 = String.valueOf(ss1).substring(14,16);

        System.out.println(getAllChannels2());
        System.out.println(getNo(s2));


    }
    public static int getNo(String s1){
        int no = 0;
        String ss1 = s1.substring(0,1);
        if("0".equals(ss1)){
            String ss2 = s1.substring(1,2);
            return Integer.parseInt(ss2);
        }else{
            return Integer.parseInt(s1);
        }
    }

    public static void setPostion(ChannelBean c,int position,int cid){
        c.setPosition(position);
        c.setOrderNo(getOrderNo(position,cid));
    }
    public static long getOrderNo(int position,int no){
        StringBuilder sb = new StringBuilder("111111111111");
        append(sb,position);
        append(sb,no);
        return Long.parseLong(sb.toString());
    }
    public static void append(StringBuilder sb,int no){
        if(no<10){
            sb.append("0"+no);
        }else{
            sb.append(no);
        }
    }

}