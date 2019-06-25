package com.yihuyixi.vendingmachine.service;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.yihuyixi.vendingmachine.api.Api;
import com.yihuyixi.vendingmachine.bean.DeviceInfo;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.exception.AppException;
import com.yihuyixi.vendingmachine.message.EventMessage;

import org.greenrobot.eventbus.EventBus;

public class AppIntentService extends GTIntentService {
    public AppIntentService() {
    }

    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.d(AppConstants.TAG_YIHU, "onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {

    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {
        String msg = gtNotificationMessage.getContent();
        Log.d(AppConstants.TAG_YIHU, "onNotificationMessageArrived ->  title = " + gtNotificationMessage.getTitle() + ", content=" + msg);
        if ("UPDATE_DEVICE".equalsIgnoreCase(msg)) {
            EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_UPDATE_DEVICE_INFO));
        } else {
            EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_RELOAD_GOODS));
        }
    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {

    }
}
