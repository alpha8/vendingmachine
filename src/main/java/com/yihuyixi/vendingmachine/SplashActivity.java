package com.yihuyixi.vendingmachine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.yihuyixi.vendingmachine.api.Api;
import com.yihuyixi.vendingmachine.bean.DeviceInfo;
import com.yihuyixi.vendingmachine.constants.AppConstants;
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
                    // TODO: 模拟机测试时启用
//                    AppConstants.VENDOR_ID = "0c:9a:42:1a:43:0c";
                    AppConstants.VENDOR_ID = Utils.getImei(getApplicationContext());
                    DeviceInfo di = Api.getInstance().getDeviceInfo(AppConstants.VENDOR_ID);
                    if (di != null) {
                        AppConstants.CURRENT_DEVICE = di;
                    }
                } catch (Throwable e) {
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
