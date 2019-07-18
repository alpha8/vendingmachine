package com.yihuyixi.vendingmachine.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.DisplayMetrics;
import android.util.Log;

import com.yihuyixi.vendingmachine.constants.AppConstants;

public class DeviceUtils {
    public static DisplayMetrics devicePixel(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public static boolean hasSdCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(AppConstants.TAG_YIHU, e.getMessage(), e);
        }
        return "";
    }

    public static String getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return String.valueOf(pi.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(AppConstants.TAG_YIHU, e.getMessage(), e);
        }
        return "";
    }

    public static int getBuildLevel() {
        return Build.VERSION.SDK_INT;
    }

    public static String getBuildVersion() {
        return Build.VERSION.RELEASE;
    }

    public static int getAppProcessId() {
        return Process.myPid();
    }

    public static String getMetaData(Context context, String key) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return ai.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(AppConstants.TAG_YIHU, e.getMessage(), e);
        }
        return "";
    }
}
