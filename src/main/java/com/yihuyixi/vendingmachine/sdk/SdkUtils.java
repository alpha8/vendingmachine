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
import com.yihuyixi.vendingmachine.message.EventMessage;
import com.yihuyixi.vendingmachine.utils.MessageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class SdkUtils {
    private static SdkUtils instance = new SdkUtils();
    private SdkUtils() {
        EventBus.getDefault().register(this);
    }

    public static SdkUtils getInstance() {
        return instance;
    }

    public void initialize(Context context) {
        WMSerialportManager.initWMSerialport(context, 10 * 1000);   //overtime: 10s
        WMSerialportManager.openSerialPort("ttyS3", new WMSerialportCallBack() {
            @Override
            public void onSucceed(WMSSendType type, Object o) {
                Log.i(AppConstants.TAG_YIHU, String.format("openSerialPort onSucceed type=%s, o=%s", type, o));
                EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_SDK_INIT_SUCCESS, "设备已就绪！"));
            }

            @Override
            public void onFailed(WMSSendType type, int i) {
                Log.e(AppConstants.TAG_YIHU, String.format("openSerialPort onFailed type=%s, i=%d", type, i));
                EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_SDK_INIT_FAIL, "设备未就绪！"));
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
                SdkResponse response = new SdkResponse(type, s);
                EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_SDK_SUCCESS, response));
                MessageUtils.getInstance().produce(response);
                EventBus.getDefault().post(new EventMessage(AppConstants.FLAG_SDK_STOCKOUT, response));
            }

            @Override
            public void onFaild(WMSSendType type, int errorType, List<String> errorDataList, String s) {
                Log.e(AppConstants.TAG_YIHU, String.format("addWMDeviceToAppCallBack onFailed: type=%s, errorType=%d, errorDataList=%s, errData=%s", type, errorType, errorDataList, s));
                EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_SDK_FAIL, new SdkResponse(type, s)));
            }
        });
    }

    public void release() {
        try {
            EventBus.getDefault().removeAllStickyEvents();
            EventBus.getDefault().unregister(this);
            WMSerialportManager.closeSerialPort();
        } catch(Throwable e) {
            Log.e(AppConstants.TAG_YIHU, e.getMessage(), e);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.ASYNC)
    public void deliveryFailed(EventMessage message) {
        if (message.getType() == AppConstants.FLAG_SDK_FAIL) {
            SdkResponse response = (SdkResponse) message.getData();
            Log.d(AppConstants.TAG_YIHU, response.getMessage());
        }
    }

    public void checkout(int channelId){
        long orderId = OrderIdGenerator.getNewOrderId();
        Log.i(AppConstants.TAG_YIHU, String.format("准备出货中, 货架号：%d, 订单号：%s", channelId, orderId));
        WMSerialportManager.setShipments(0, channelId, orderId, 15 * 1000);
    }

    public void checkout(int channelId, long orderId){
        Log.i(AppConstants.TAG_YIHU, String.format("准备出货中, 货架号：%d, 订单号：%s", channelId, orderId));
        WMSerialportManager.setShipments(0, channelId, orderId, 15 * 1000);
    }

    public void checkout(int address, int channelId, long orderId){
        Log.i(AppConstants.TAG_YIHU, String.format("准备出货中, 下位机地址：%d, 货架号：%d, 订单号：%s", address, channelId, orderId));
        WMSerialportManager.setShipments(address, channelId, orderId, 15 * 1000);
    }

    public static final class OrderIdGenerator {
        private static long orderId;

        public static synchronized long getNewOrderId() {
            return ++orderId;
        }
    }
}
