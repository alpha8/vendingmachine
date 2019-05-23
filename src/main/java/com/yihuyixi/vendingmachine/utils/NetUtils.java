package com.yihuyixi.vendingmachine.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {
    public static NetworkType getNetworkState(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            NetworkType type = NetworkType.none;
            switch(networkInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    type = NetworkType.wifi;
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    type = NetworkType.mobile;
                    break;
            }
            return type;
        }
        return NetworkType.none;
    }

    public enum NetworkType {
        none, mobile, wifi
    }
}
