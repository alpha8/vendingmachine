package com.yihuyixi.vendingmachine.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yihuyixi.vendingmachine.api.Api;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.exception.AppException;
import com.yihuyixi.vendingmachine.sdk.SdkUtils;

import java.util.Arrays;

public class OrderPayStateTask extends AsyncTask<String, Integer, OrderPayStateTask.JobResult> {
    private static final String TAG = "OrderPayStateTask";
    private int SECONDS = AppConstants.PAY_TIMEOUT_SECONDS;
    private Context mContext;
    private boolean isStop = false;
    private TextView mTvMsg;
    private int mChannelId;

    public OrderPayStateTask(Context context, TextView tvMsg) {
        this.mContext = context;
        this.mTvMsg = tvMsg;
    }

    @Override
    protected void onPreExecute() {
        SECONDS = AppConstants.PAY_TIMEOUT_SECONDS;
        isStop = false;
        mTvMsg.setVisibility(View.GONE);
    }

    @Override
    protected OrderPayStateTask.JobResult doInBackground(String... params) {
        Log.d(TAG, "doInBackground params: " + Arrays.toString(params));
        if (params.length > 1) {
            while(!isStop && --SECONDS >= 0){
                try {
                    boolean isSuccess = Api.getInstance().checkPayState(params[0]);
                    String cluster = params[1].substring(0, 1);
                    this.mChannelId = Integer.parseInt(params[1].substring(1));
                    if (isSuccess) {
                        return new JobResult(true, String.format("支付成功！请从%s柜拿取货物，祝您购物愉快！", cluster));
                    }
                    Thread.sleep(1000);
                } catch (AppException | InterruptedException e) {
                    Log.d(TAG, "doInBackground failed, caused by " + e.getMessage());
                }
            }
            return new JobResult(false, "支付超时，请稍候再试！");
        }
        return null;
    }

    public void cancelJob() {
        this.isStop = true;
    }

    @Override
    protected void onPostExecute(OrderPayStateTask.JobResult jobResult) {
        if (isStop) {
            return;
        }
        if (jobResult.isSuccess) {
            mTvMsg.setText(jobResult.getMessage());
            mTvMsg.setVisibility(View.VISIBLE);
            SdkUtils.getInstance().checkout(mChannelId);
        } else {
            Toast.makeText(mContext, jobResult.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    class JobResult {
        private boolean isSuccess;
        private String message;

        public JobResult() {

        }

        public JobResult(boolean isSuccess, String message) {
            this.isSuccess = isSuccess;
            this.message = message;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
