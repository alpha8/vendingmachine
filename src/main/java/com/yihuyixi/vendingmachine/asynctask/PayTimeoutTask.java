package com.yihuyixi.vendingmachine.asynctask;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yihuyixi.vendingmachine.constants.AppConstants;

public class PayTimeoutTask extends AsyncTask<Void, Void, Void> {
    private int SECONDS = AppConstants.PAY_TIMEOUT_SECONDS;
    private volatile boolean isStop = false;
    private Handler mHandler;
    private String mPattern;

    public PayTimeoutTask(Handler handler, String pattern) {
        this.mHandler = handler;
        this.mPattern = pattern;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d(AppConstants.TAG_YIHU, "doInBackground, isStop=" + isStop);
        while(!isStop && --SECONDS >= 0){
            try {
                Message message = mHandler.obtainMessage();
                message.what = AppConstants.FLAG_UPDATE_COUNTDOWN;
                message.obj = String.format(mPattern, SECONDS);
                mHandler.sendMessage(message);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.d(AppConstants.TAG_YIHU, "doInBackground failed, caused by " + e.getMessage());
            }
        }
        return null;
    }

    public void renewTimes() {
        if (!this.isStop) {
            SECONDS = AppConstants.PAY_TIMEOUT_SECONDS;
        }
    }

    public void cancelJob() {
        this.isStop = true;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Message message = mHandler.obtainMessage();
        message.what = AppConstants.FLAG_CLOSE_DETAIL;
        mHandler.sendMessage(message);
    }
}
