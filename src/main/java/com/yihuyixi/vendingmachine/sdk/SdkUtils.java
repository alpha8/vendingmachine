package com.yihuyixi.vendingmachine.sdk;

import android.content.Context;
import android.util.Log;

import com.example.mylibrary.serialportlibrary.WMSerialportManager;
import com.example.mylibrary.serialportlibrary.listener.OnDataSentListener;
import com.example.mylibrary.serialportlibrary.listener.WMDeviceToAppCallBack;
import com.example.mylibrary.serialportlibrary.listener.WMSerialportCallBack;
import com.example.mylibrary.serialportlibrary.protocol.WMSSendType;

import java.util.List;

public class SdkUtils {
    private static final String TAG_SDK = "SDK";
    private SdkUtils() {
    }

    public static SdkUtils getInstance() {
        return new SdkUtils();
    }

    public void initialize(Context context) {
        WMSerialportManager.initWMSerialport(context, 10 * 1000);   //overtime: 10s
        WMSerialportManager.openSerialPort("ttyS3", new WMSerialportCallBack() {
            @Override
            public void onSucceed(WMSSendType wmsSendType, Object o) {
                Log.d(TAG_SDK, "open the serial port(ttyS3) success.");
            }

            @Override
            public void onFailed(WMSSendType wmsSendType, int i) {
                Log.d(TAG_SDK, "open the serial port(ttyS3) failed.");
            }
        });
        WMSerialportManager.addOnDataSentListener(new OnDataSentListener() {
            @Override
            public void onDataSent(String s) {
                Log.d(TAG_SDK, "sent data is " + s);
            }
        });
        WMSerialportManager.addWMDeviceToAppCallBack(new WMDeviceToAppCallBack() {
            @Override
            public void onSuccess(WMSSendType wmsSendType, String s) {
                Log.d(TAG_SDK, "DeviceToApp sendType=" + wmsSendType + ", text=" + s);
            }

            @Override
            public void onFaild(WMSSendType wmsSendType, int i, List<String> list, String s) {
                Log.d(TAG_SDK, "DeviceToApp failed sendType=" + wmsSendType + ", text=" + s);
            }
        });
    }

    public void release() {
        WMSerialportManager.closeSerialPort();
    }

    public void checkout(int channelId, int orderId){
        WMSerialportManager.setShipments(0, channelId, orderId, 15 * 1000);
        Log.d("SDK", "准备出货中..., 货架号：" + channelId);
    }
}
