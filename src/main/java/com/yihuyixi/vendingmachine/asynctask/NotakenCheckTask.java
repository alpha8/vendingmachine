package com.yihuyixi.vendingmachine.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.yihuyixi.vendingmachine.api.Api;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.exception.AppException;
import com.yihuyixi.vendingmachine.message.EventMessage;
import com.yihuyixi.vendingmachine.vo.VendorResponse;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

public class NotakenCheckTask extends AsyncTask<String, Integer, VendorResponse.VendorUser> {
    private int SECONDS = AppConstants.PAY_TIMEOUT_SECONDS;
    private volatile boolean isStop = false;

    public NotakenCheckTask() {
    }

    @Override
    protected void onPreExecute() {
        SECONDS = AppConstants.PAY_TIMEOUT_SECONDS;
        isStop = false;
    }

    @Override
    protected VendorResponse.VendorUser doInBackground(String... params) {
        Log.d(AppConstants.TAG_YIHU, "NotakenCheckTask doInBackground params: " + Arrays.toString(params));
        if (params.length > 1) {
            while(!isStop && --SECONDS >= 0){
                try {
                    VendorResponse.VendorUser userVO = Api.getInstance().checkUserLogin(params[0], params[1]);
                    if (userVO == null) {
                        Thread.sleep(2000);
                    } else {
                        return userVO;
                    }
                } catch (AppException | InterruptedException e) {
                    Log.d(AppConstants.TAG_YIHU, "NotakenCheckTask doInBackground failed, caused by " + e.getMessage());
                }
            }
        }
        return null;
    }

    public void cancelJob() {
        this.isStop = true;
    }

    @Override
    protected void onPostExecute(VendorResponse.VendorUser jobResult) {
        if (isStop || jobResult == null) {
            EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_TAKEN_FAIL));
            return;
        }
        EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_TAKEN_SUCCESS, jobResult));
    }
}
