package com.yihuyixi.vendingmachine.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.alibaba.fastjson.util.IOUtils;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.exception.AppException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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

    public static String getImei(Context context) throws AppException {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            Log.d(AppConstants.TAG_YIHU, "READ_PHONE_STATE has permission.");
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (isNotBlank(imei)) {
                Log.d(AppConstants.TAG_YIHU, "imei=" + imei);
                return imei;
            }
        }
        throw new AppException("获取IMEI号失败！");
    }

    public static boolean isNotBlank(String str) {
        return str != null && !"".equals(str);
    }

    public static boolean isBlank(String str) {
        return str == null || "".equals(str);
    }

    /**
     * 描述: 安装
     */
    public static boolean installApk(File apkPath,Context context){
        // 先判断手机是否有root权限
        if(hasRootPerssion()){
            Log.d(AppConstants.TAG_YIHU, "has root permission.");
            // 有root权限，利用静默安装实现
            return clientInstall(apkPath.getAbsolutePath(), context);
        }else{
            // 没有root权限，利用意图进行安装
            Log.d(AppConstants.TAG_YIHU, "has no root permission.");
            if(!apkPath.exists())
                return false;
            Log.d(AppConstants.TAG_YIHU, "start to install");
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(apkPath),"application/vnd.android.package-archive");
            context.startActivity(intent);
            return true;
        }
    }

    /**
     * 描述: 卸载
     */
    public static boolean uninstall(String packageName,Context context){
        if(hasRootPerssion()){
            // 有root权限，利用静默卸载实现
            return clientUninstall(packageName);
        }else{
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,packageURI);
            uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(uninstallIntent);
            return true;
        }
    }

    /**
     * 判断手机是否有root权限
     */
    public static boolean hasRootPerssion(){
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(process!=null){
                process.destroy();
            }
        }
        return false;
    }

    /**
     * 静默安装
     */
    public static boolean clientInstall(String apkPath,Context context){
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("chmod 777 "+apkPath);
            PrintWriter.println("export LD_LIBRARY_PATH=/vendor/lib:/system/lib");
            PrintWriter.println("pm install -r "+apkPath);
//          PrintWriter.println("exit");
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(process!=null){
                process.destroy();
            }
        }
        return false;
    }

    /**
     * 静默卸载
     */
    public static boolean clientUninstall(String packageName){
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("LD_LIBRARY_PATH=/vendor/lib:/system/lib ");
            PrintWriter.println("pm uninstall "+packageName);
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(process!=null){
                process.destroy();
            }
        }
        return false;
    }

    /**
     * 启动app
     * com.exmaple.client/.MainActivity
     * com.exmaple.client/com.exmaple.client.MainActivity
     */
    public static boolean startApp(String packageName,String activityName){
        boolean isSuccess = false;
        String cmd = "am start -n " + packageName + "/" + activityName + " \n";
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(process!=null){
                process.destroy();
            }
        }
        return isSuccess;
    }

    private static boolean returnResult(int value){
        // 代表成功
        if (value == 0) {
            return true;
        } else if (value == 1) { // 失败
            return false;
        } else { // 未知情况
            return false;
        }
    }
}
