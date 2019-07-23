package com.yihuyixi.vendingmachine.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.yihuyixi.vendingmachine.BaseActivity;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.utils.NetUtils;

public class NetBroadcastReceiver extends BroadcastReceiver {
    public NetworkListener mNetworkListener = BaseActivity.mNetworkListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mNetworkListener == null) {
            return;
        }
        try {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                mNetworkListener.onNetChanged(NetUtils.getNetworkState(context));
            }
        }catch(Exception e) {
            Log.e(AppConstants.TAG_YIHU, e.getMessage(), e);
        }
    }

    public interface NetworkListener {
        void onNetChanged(NetUtils.NetworkType type);
    }
}
