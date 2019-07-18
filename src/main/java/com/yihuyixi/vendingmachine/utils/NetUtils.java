package com.yihuyixi.vendingmachine.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.yihuyixi.vendingmachine.constants.AppConstants;

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

    public static boolean toggleWifiState(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager == null) {
                return false;
            }
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
                return false;
            } else {
                wifiManager.setWifiEnabled(true);
                return true;
            }
        }catch(Exception e) {
            Log.e(AppConstants.TAG_YIHU, e.getMessage(), e);
            return false;
        }
    }

    public static boolean getWifiState(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager == null) {
                return false;
            }
            return wifiManager.isWifiEnabled();
        }catch(Exception e) {
            Log.e(AppConstants.TAG_YIHU, e.getMessage(), e);
            return false;
        }
    }

    public static enum NetworkType {
        none, mobile, wifi
    }
}
