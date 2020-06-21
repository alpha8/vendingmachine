package com.yihuyixi.vendingmachine;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;

import com.igexin.sdk.PushManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.service.AppIntentService;
import com.yihuyixi.vendingmachine.service.AppPushService;

public class BootApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(restartHandler);
        initCrashReport();
        initPushService();
    }

    private void initPushService() {
        try {
            PushManager.getInstance().initialize(getApplicationContext(), AppPushService.class);
            PushManager.getInstance().registerPushIntentService(getApplicationContext(), AppIntentService.class);
        } catch (Throwable e) {
            Log.e(AppConstants.TAG_YIHU, e.getMessage(), e);
        }
    }

    private void initCrashReport() {
        try {
            // todo: 启用bugly上传crash异常数据并重启应用
            CrashReport.initCrashReport(getApplicationContext(), "f48baf74f7", false);
        } catch (Throwable e) {
            Log.e(AppConstants.TAG_YIHU, e.getMessage(), e);
        }
    }

    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            restartApp();//发生崩溃异常时,重启应用
        }
    };

    private void restartApp() {
        try {
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch(Throwable e) {
            Log.e(AppConstants.TAG_YIHU, e.getMessage(), e);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
