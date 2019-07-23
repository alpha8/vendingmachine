package com.yihuyixi.vendingmachine.service;

import android.content.Context;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.message.EventMessage;
import com.yihuyixi.vendingmachine.utils.DeviceUtils;
import com.yihuyixi.vendingmachine.utils.Utils;

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
        try {
            String content = gtNotificationMessage.getContent();
            Log.d(AppConstants.TAG_YIHU, "onNotificationMessageArrived ->  title = " + gtNotificationMessage.getTitle() + ", content=" + content);
            if (Utils.isBlank(content)) {
                return;
            }
            String[] msg = content.split("/");
            if (msg.length <= 2) {
                return;
            }
            String type = msg[0];
            String deviceId = msg[1];
            String ts = msg[2];
            switch (type) {
                case "UPDATE_DEVICE":
                    try {
                        AppConstants.VENDOR_ID = Utils.getImei(getApplicationContext());
                        if (AppConstants.VENDOR_ID.equals(deviceId)) {
                            EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_UPDATE_DEVICE_INFO));
                        }
                    } catch (Exception e) {
                        Log.e(AppConstants.TAG_YIHU, e.getMessage(), e);
                    }
                    break;
                case "APP_UPGRADE":
                    String currentVersion = DeviceUtils.getVersionCode(getApplicationContext());
                    Log.d(AppConstants.TAG_YIHU, "current versionCode=" + currentVersion);
                    if (AppConstants.VENDOR_ID.equals(deviceId) && !ts.equals(currentVersion)) {
                        EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_UPGRADE_APP, ts));
                    } else if ("".equals(deviceId) && !ts.equals(currentVersion)) {
                        EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_UPGRADE_APP, ts));
                    }
                    break;
                default:
                    if (AppConstants.VENDOR_ID.equals(deviceId)) {
                        EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_RELOAD_GOODS));
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e(AppConstants.TAG_YIHU, e.getMessage(), e);
        }
    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {

    }
}
