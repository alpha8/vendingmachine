package com.yihuyixi.vendingmachine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.receiver.NetBroadcastReceiver;
import com.yihuyixi.vendingmachine.utils.NetUtils;
import com.yihuyixi.vendingmachine.utils.Utils;

public class BaseActivity extends AppCompatActivity implements NetBroadcastReceiver.NetworkListener {
    public static NetBroadcastReceiver.NetworkListener mNetworkListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNetworkListener = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.hideBottomUIMenu(getWindow());
    }

    @Override
    public void onNetChanged(NetUtils.NetworkType type) {
        if (type == NetUtils.NetworkType.none) {
            Toast.makeText(getApplicationContext(), "当前网络已断开", Toast.LENGTH_LONG).show();
        }
        this.notifyNetChanged(type);
    }

    public void notifyNetChanged(NetUtils.NetworkType type) {
        Log.d(AppConstants.TAG_YIHU, "network status is changed. type=" + type.name());
    }
}
