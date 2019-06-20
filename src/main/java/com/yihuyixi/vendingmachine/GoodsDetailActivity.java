package com.yihuyixi.vendingmachine;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.mylibrary.serialportlibrary.protocol.WMSSendType;
import com.yihuyixi.vendingmachine.api.Api;
import com.yihuyixi.vendingmachine.api.Channels;
import com.yihuyixi.vendingmachine.asynctask.OrderPayStateTask;
import com.yihuyixi.vendingmachine.asynctask.PayTimeoutTask;
import com.yihuyixi.vendingmachine.bean.ChannelResponse;
import com.yihuyixi.vendingmachine.bean.ProductInfo;
import com.yihuyixi.vendingmachine.bean.SdkResponse;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.exception.AppException;
import com.yihuyixi.vendingmachine.message.EventMessage;
import com.yihuyixi.vendingmachine.sdk.SdkUtils;
import com.yihuyixi.vendingmachine.vo.QrcodeVO;
import com.yihuyixi.vendingmachine.vo.ResponseEntity;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GoodsDetailActivity extends BaseActivity {
    @BindView(R.id.id_banner) Banner mBanner;
    @BindView(R.id.tv_name) TextView mProductName;
    @BindView(R.id.id_qrcode) ImageView mQrcode;
    @BindView(R.id.tv_sellpoint) TextView mSellpoint;
    @BindView(R.id.tv_price) TextView mPrice;
    @BindView(R.id.tv_count) TextView mSalesCount;
    @BindView(R.id.tv_msg) TextView mTvMsg;
    @BindView(R.id.btn_back) Button mBackButton;
    @BindView(R.id.fl_pay) FrameLayout mPayLayout;
    @BindView(R.id.fl_paySuccess) FrameLayout mPaySuccessLayout;

    private ProductInfo mProductInfo;

    private OrderPayStateTask mPayStateTask;
    private PayTimeoutTask mTimeoutTask;
    private Unbinder mUnbinder;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case AppConstants.FLAG_PAY_SUCCESS:
                    mPayLayout.setVisibility(View.GONE);
                    mPaySuccessLayout.setVisibility(View.VISIBLE);
                    mTvMsg.setText((String)msg.obj);
                    break;
                case AppConstants.FLAG_PAY_FAIL:
                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case AppConstants.FLAG_UPDATE_COUNTDOWN:
                    mBackButton.setText((String) msg.obj);
                    break;
                case AppConstants.FLAG_CLOSE_DETAIL:
                    GoodsDetailActivity.this.finish();
                    break;
                case AppConstants.FLAG_ERROR:
                    Toast.makeText(GoodsDetailActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        ProductInfo productInfo = (ProductInfo) getIntent().getSerializableExtra(AppConstants.INTENT_GOODS);
        Log.d(AppConstants.TAG_YIHU, productInfo.toString());
        mProductInfo = productInfo;
        initBanner(productInfo.getIcons());
        initView(productInfo);
    }

    private void initView(ProductInfo productInfo) {
        mProductName.setText(productInfo.getName());
        mSellpoint.setText(productInfo.getSellpoint());
        mPrice.setText(String.format("¥%s", productInfo.getFormatPrice()));
        mSalesCount.setText(String.format("(已售:%s件)", productInfo.getSellCount()));
    }

    private void initBanner(List<String> icons) {
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(GoodsDetailActivity.this).load(path).into(imageView);
            }
        });
        mBanner.setImages(icons);
        mBanner.setDelayTime(3000);
        mBanner.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().post(new EventMessage(AppConstants.FLAG_QRCODE_URL));
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void getQrcode(EventMessage event) {
        if (event.getType() != AppConstants.FLAG_QRCODE_URL) {
            return;
        }
        Log.d(AppConstants.TAG_YIHU, "getQrcode event=" + event.toString());
        QrcodeVO qrcode = new QrcodeVO();
        qrcode.setName(mProductInfo.getName());
        qrcode.setIcon(mProductInfo.getPictureId());
        qrcode.setProductId(mProductInfo.getId());
//        qrcode.setPrice(mProductInfo.getPrice());
        qrcode.setPrice(0.01f);
        qrcode.setVendingId(AppConstants.VENDOR_ID);

        try {
            String json = JSON.toJSONString(qrcode);
            ResponseEntity responseEntity = Api.getInstance().getWxPay(json);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String qrcodeUrl = String.format("%s/getQrcode?url=%s", AppConstants.WX_API, responseEntity.getUrl());
                    Glide.with(GoodsDetailActivity.this).load(qrcodeUrl).into(mQrcode);
                    mPayLayout.setVisibility(View.VISIBLE);

                    mTimeoutTask = new PayTimeoutTask(mHandler,"返回（%s秒）");
                    mTimeoutTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    mPayStateTask = new OrderPayStateTask(mHandler);
                    String preOrderId = responseEntity.getData() != null ? responseEntity.getData().getId() : "";
                    mPayStateTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, preOrderId);
                }
            });
        } catch (AppException e) {
            Log.e(AppConstants.TAG_YIHU, e.getMessage(), e);
        }
    }

    public void goBack(View view) {
        this.mPayStateTask.cancelJob();
        this.mTimeoutTask.cancelJob();
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBanner != null) {
            mBanner.stopAutoPlay();
        }
        mPaySuccessLayout.setVisibility(View.GONE);
        this.mPayStateTask.cancelJob();
        this.mTimeoutTask.cancelJob();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mBanner != null) {
            mBanner.startAutoPlay();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.ASYNC)
    public void deliverySucceed(EventMessage message) {
        if (message.getType() == AppConstants.FLAG_SDK_SUCCESS) {
            SdkResponse response = (SdkResponse) message.getData();
            if (response.getType() == WMSSendType.SHIPMENTS) {
                String orderNo = response.getRealOrderNo();
                ChannelResponse.ChannelVO channelVO = new ChannelResponse.ChannelVO();
                channelVO.setChannelNo(orderNo);
                channelVO.setDeviceId(AppConstants.VENDOR_ID);
                channelVO.setCount(1);
                String json = JSON.toJSONString(channelVO);
                try {
                    ProductInfo pi = Api.getInstance().stockOut(json);
                    Log.d(AppConstants.TAG_YIHU, "stock out response=" + pi);
                    EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_UPDATE_STOCK_INFO, pi));
                } catch (AppException e) {
                    e.printStackTrace();
                }
            } else {
                //todo: 异常出货，上报服务端处理
            }
        }
    }
}
