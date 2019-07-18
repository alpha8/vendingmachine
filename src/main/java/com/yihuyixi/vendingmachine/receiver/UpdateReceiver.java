package com.yihuyixi.vendingmachine.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yihuyixi.vendingmachine.SplashActivity;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.message.EventMessage;

import org.greenrobot.eventbus.EventBus;

public class UpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_INSTALL_SUCCESS));
            Intent event = new Intent(context, SplashActivity.class);
            event.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(event);
            Log.d(AppConstants.TAG_YIHU, "Upgrade System is successful.");
        } else if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            Log.d(AppConstants.TAG_YIHU, "安装了:" + intent.getDataString() + "包名的程序");
        } else if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            Log.d(AppConstants.TAG_YIHU, "卸载了:" + intent.getDataString() + "包名的程序");
        }
    }
}
