package com.yihuyixi.vendingmachine.asynctask;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yihuyixi.vendingmachine.api.Api;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.exception.AppException;
import com.yihuyixi.vendingmachine.sdk.SdkUtils;
import com.yihuyixi.vendingmachine.vo.PayVO;

import java.util.Arrays;

public class OrderPayStateTask extends AsyncTask<String, Integer, OrderPayStateTask.JobResult> {
    private int SECONDS = AppConstants.PAY_TIMEOUT_SECONDS;
    private volatile boolean isStop = false;
    private Handler mHandler;

    public OrderPayStateTask(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    protected void onPreExecute() {
        SECONDS = AppConstants.PAY_TIMEOUT_SECONDS;
        isStop = false;
        AppConstants.LAST_SHIPMENT_LEVEL = "";
        AppConstants.LAST_PREORDER_ID = "";
    }

    @Override
    protected OrderPayStateTask.JobResult doInBackground(String... params) {
        Log.d(AppConstants.TAG_YIHU, "doInBackground params: " + Arrays.toString(params));
        if (params.length >= 1) {
            while(!isStop && --SECONDS >= 0){
                try {
                    String preOrderId = params[0];
                    PayVO payVO = Api.getInstance().checkPayState(preOrderId);
                    if (payVO != null && payVO.getChannelNo() != null) {
                        Log.d(AppConstants.TAG_YIHU, "payVO=" + payVO.toString());
                        AppConstants.LAST_SHIPMENT_LEVEL = payVO.getLevel();
                        String cluster = payVO.getLevel().substring(0, 1);
                        AppConstants.LAST_PREORDER_ID = preOrderId;
                        return new JobResult(true, String.format("支付成功！请从%s柜拿取货物。", cluster), payVO.getChannelNo(), payVO.getPlc());
                    }
                    Thread.sleep(1000);
                } catch (AppException | InterruptedException e) {
                    Log.d(AppConstants.TAG_YIHU, "doInBackground failed, caused by " + e.getMessage());
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
        Message message = mHandler.obtainMessage();
        if (jobResult.isSuccess && jobResult.channelNo != null) {
            SdkUtils.getInstance().checkout(jobResult.address, jobResult.channelNo, jobResult.channelNo);
            message.what = AppConstants.FLAG_PAY_SUCCESS;
        } else {
            message.what = AppConstants.FLAG_PAY_FAIL;
        }
        message.obj = jobResult.getMessage();
        mHandler.sendMessage(message);
    }

    class JobResult {
        private boolean isSuccess;
        private String message;
        private Integer channelNo;
        private int address;     //下位机地址

        public JobResult() {

        }

        public JobResult(boolean isSuccess, String message) {
            this.isSuccess = isSuccess;
            this.message = message;
        }

        public JobResult(boolean isSuccess, String message, String channelNo) {
            this.isSuccess = isSuccess;
            this.message = message;
            if (channelNo != null && !channelNo.equals("")) {
                this.channelNo = Integer.parseInt(channelNo);
            }
        }

        public JobResult(boolean isSuccess, String message, String channelNo, int address) {
            this.isSuccess = isSuccess;
            this.message = message;
            if (channelNo != null && !channelNo.equals("")) {
                this.channelNo = Integer.parseInt(channelNo);
            }
            this.address = address;
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
