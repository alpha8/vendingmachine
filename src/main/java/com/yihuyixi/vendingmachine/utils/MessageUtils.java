package com.yihuyixi.vendingmachine.utils;

import com.yihuyixi.vendingmachine.bean.SdkResponse;

import java.util.concurrent.ArrayBlockingQueue;

public class MessageUtils {
    private static MessageUtils instance = new MessageUtils();
    private static ArrayBlockingQueue<SdkResponse> queue = new ArrayBlockingQueue<>(2000);

    private MessageUtils(){}
    public static MessageUtils getInstance(){
        return instance;
    }

    public void produce(SdkResponse message){
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public SdkResponse consume() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean exist(SdkResponse message) {
        if (message != null && queue.contains(message)) {
            return queue.remove(message);
        }
        return false;
    }
}
