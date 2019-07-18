package com.yihuyixi.vendingmachine;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;

import com.tencent.bugly.crashreport.CrashReport;

public class BootApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // todo: 启用bugly上传crash异常数据并重启应用
        CrashReport.initCrashReport(getApplicationContext(), "f48baf74f7", false);
        Thread.setDefaultUncaughtExceptionHandler(restartHandler);
    }

    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            restartApp();//发生崩溃异常时,重启应用
        }
    };

    private void restartApp() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
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
