package com.yihuyixi.vendingmachine.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.yihuyixi.vendingmachine.activity.BaseActivity;
import com.yihuyixi.vendingmachine.utils.NetUtils;

public class NetBroadcastReceiver extends BroadcastReceiver {
    public NetworkListener mNetworkListener = BaseActivity.mNetworkListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            mNetworkListener.onNetChanged(NetUtils.getNetworkState(context));
        }
    }

    public interface NetworkListener {
        void onNetChanged(NetUtils.NetworkType type);
    }
}
