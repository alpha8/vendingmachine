package com.yihuyixi.vendingmachine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.vo.VendorResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TakenSuccessActivity extends BaseActivity {
    private Unbinder mUnbinder;
    private VendorResponse.VendorUser mVendorUser;
    @BindView(R.id.id_takensuccess_msg) TextView mTakenTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taken_success);
        mUnbinder = ButterKnife.bind(this);

        mVendorUser = (VendorResponse.VendorUser) getIntent().getSerializableExtra(AppConstants.INTENT_TAKEN_DEVICE);
        if (mVendorUser != null && mVendorUser.getVendorMsg() != null) {
            Log.d(AppConstants.TAG_YIHU, mVendorUser.toString());
            mTakenTips.setText(mVendorUser.getVendorMsg());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @OnClick(R.id.id_success_back)
    public void backToHome(View view) {
        Intent intent = new Intent(TakenSuccessActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
    @OnClick(R.id.id_success_continue)
    public void continueTakenGoods(View view) {
        Intent intent = new Intent(TakenSuccessActivity.this, TakenGoodsActivity.class);
        intent.putExtra(AppConstants.INTENT_TAKEN_DEVICE, mVendorUser);
        startActivity(intent);
        this.finish();
    }
}
