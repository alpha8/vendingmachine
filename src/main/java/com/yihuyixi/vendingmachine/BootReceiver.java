package com.yihuyixi.vendingmachine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent event = new Intent(context, MainActivity.class);
        event.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(event);
        Log.d("BootApp", "Boot System is running.");
    }
}
