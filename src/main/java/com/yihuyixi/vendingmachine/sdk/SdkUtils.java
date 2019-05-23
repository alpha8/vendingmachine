package com.yihuyixi.vendingmachine.sdk;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.mylibrary.serialportlibrary.WMSerialportManager;
import com.example.mylibrary.serialportlibrary.listener.OnDataSentListener;
import com.example.mylibrary.serialportlibrary.listener.WMDeviceToAppCallBack;
import com.example.mylibrary.serialportlibrary.listener.WMSerialportCallBack;
import com.example.mylibrary.serialportlibrary.protocol.WMSSendType;
import com.yihuyixi.vendingmachine.bean.SdkResponse;
import com.yihuyixi.vendingmachine.constants.AppConstants;

import java.util.List;

public class SdkUtils {
    private static SdkUtils instance = new SdkUtils();
    private SdkUtils() {
    }

    public static SdkUtils getInstance() {
        return instance;
    }

    public void initialize(Context context, final Handler handler) {
        WMSerialportManager.initWMSerialport(context, 10 * 1000);   //overtime: 10s
        WMSerialportManager.openSerialPort("ttyS3", new WMSerialportCallBack() {
            @Override
            public void onSucceed(WMSSendType type, Object o) {
                Log.i(AppConstants.TAG_YIHU, String.format("openSerialPort onSucceed type=%s, o=%s", type, o));
            }

            @Override
            public void onFailed(WMSSendType type, int i) {
                Log.e(AppConstants.TAG_YIHU, String.format("openSerialPort onFailed type=%s, i=%d", type, i));
            }
        });
        WMSerialportManager.addOnDataSentListener(new OnDataSentListener() {
            @Override
            public void onDataSent(String s) {
                Log.i(AppConstants.TAG_YIHU, "addOnDataSentListener onDataSent:" + s);
            }
        });
        WMSerialportManager.addWMDeviceToAppCallBack(new WMDeviceToAppCallBack() {
            @Override
            public void onSuccess(WMSSendType type, String s) {
                Log.i(AppConstants.TAG_YIHU, String.format("addWMDeviceToAppCallBack onSuccess: type=%s, 回调指令为=%s", type, s));
                Message message = handler.obtainMessage();
                message.what = AppConstants.FLAG_SDK_SUCCESS;
                SdkResponse response = new SdkResponse(type, s);
                message.obj = response;
                handler.sendMessage(message);
            }

            @Override
            public void onFaild(WMSSendType type, int errorType, List<String> errorDataList, String s) {
                Log.e(AppConstants.TAG_YIHU, String.format("addWMDeviceToAppCallBack onFailed: type=%s, errorType=%d, errorDataList=%s, errData=%s", type, errorType, errorDataList, s));
                Message message = handler.obtainMessage();
                message.what = AppConstants.FLAG_SDK_FAIL;
                SdkResponse response = new SdkResponse(type, s);
                message.obj = response;
                handler.sendMessage(message);
            }
        });
    }

    public void release() {
        WMSerialportManager.closeSerialPort();
    }

    public void checkout(int channelId){
        long orderId = OrderIdGenerator.getNewOrderId();
        Log.i(AppConstants.TAG_YIHU, String.format("准备出货中, 货架号：%d, 订单号：%s", channelId, orderId));
        WMSerialportManager.setShipments(0, channelId, orderId, 15 * 1000);
    }

    private static final class OrderIdGenerator {
        private static long orderId;

        public static synchronized long getNewOrderId() {
            return ++orderId;
        }
    }
}
