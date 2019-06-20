package com.yihuyixi.vendingmachine.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.yihuyixi.vendingmachine.constants.AppConstants;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Utils {
    public static String getTimeStamp() {
        return String.valueOf(new Date().getTime());
    }

    public static void hideBottomUIMenu(Window window) {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            window.getDecorView().setSystemUiVisibility(View.GONE);
        } else {
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(uiOptions);
        }
    }

    public static void fullScreen(Window window) {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            window.getDecorView().setSystemUiVisibility(View.GONE);
        } else {
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(uiOptions);
        }
    }

    public static String getFormatPrice(float price) {
        DecimalFormat df = new DecimalFormat("####.##");
        return df.format(price);
    }

    public static String getPictureServerUrl(String pid) {
        return String.format("%s/%s?w=750&h=500&v=v2", AppConstants.PS_API, pid);
    }

    public static String getFormatDate(Date creatAt) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(creatAt);
    }

    public static String getDeviceId(Context context) {
        //IMEI（imei）
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            Log.d(AppConstants.TAG_YIHU, "READ_PHONE_STATE has permission.");
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (isNotBlank(imei)) {
                Log.d(AppConstants.TAG_YIHU, "imei=" + imei);
                return imei;
            }

            //序列号（sn）
            String sn = tm.getSimSerialNumber();
            if (isNotBlank(sn)) {
                Log.d(AppConstants.TAG_YIHU, "sn=" + sn);
                return sn;
            }
        }

        //wifi mac地址
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String wifiMac = info.getMacAddress();
        if (isNotBlank(wifiMac)) {
            Log.d(AppConstants.TAG_YIHU, "wifiMac=" + wifiMac);
            return wifiMac;
        }
        return UUID.randomUUID().toString();
    }

    private static boolean isNotBlank(String str) {
        return str != null && !"".equals(str);
    }
}
