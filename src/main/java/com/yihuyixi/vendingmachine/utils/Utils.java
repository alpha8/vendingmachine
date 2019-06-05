package com.yihuyixi.vendingmachine.utils;

import android.os.Build;
import android.view.View;
import android.view.Window;

import com.yihuyixi.vendingmachine.constants.AppConstants;

import java.text.DecimalFormat;
import java.util.Date;

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
}
