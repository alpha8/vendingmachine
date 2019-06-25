package com.yihuyixi.vendingmachine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.yihuyixi.vendingmachine.api.Api;
import com.yihuyixi.vendingmachine.bean.DeviceInfo;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.exception.AppException;
import com.yihuyixi.vendingmachine.exception.NoDataException;
import com.yihuyixi.vendingmachine.utils.Utils;

import static com.yihuyixi.vendingmachine.constants.AppConstants.TAG_YIHU;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        Utils.fullScreen(getWindow());
        setContentView(R.layout.activity_splash);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // TODO: 模拟机测试时启用 "867223025692567";
                    AppConstants.VENDOR_ID = Utils.getDeviceId(getApplicationContext());
                    DeviceInfo di = Api.getInstance().getDeviceInfo(AppConstants.VENDOR_ID);
                    if (di != null) {
                        AppConstants.CURRENT_DEVICE = di;
                    }
                } catch (AppException e) {
                    Log.e(TAG_YIHU, e.getMessage(), e);
                }
            }
        }).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, AppConstants.SPLASH_DELAY_TIME);
    }
}
