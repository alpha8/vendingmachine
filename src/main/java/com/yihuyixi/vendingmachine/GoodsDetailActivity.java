package com.yihuyixi.vendingmachine;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.yihuyixi.vendingmachine.api.Api;
import com.yihuyixi.vendingmachine.api.Channels;
import com.yihuyixi.vendingmachine.asynctask.OrderPayStateTask;
import com.yihuyixi.vendingmachine.asynctask.PayTimeoutTask;
import com.yihuyixi.vendingmachine.bean.ProductInfo;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.exception.AppException;
import com.yihuyixi.vendingmachine.vo.PayVO;
import com.yihuyixi.vendingmachine.vo.QrcodeVO;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.List;

public class GoodsDetailActivity extends AppCompatActivity {
    private Banner mBanner;
    private TextView mProductName;
    private ImageView mQrcode;
    private ImageView mIvPhone;
    private TextView mTvTips;
    private TextView mSellpoint;
    private TextView mPrice;
    private TextView mSalesCount;
    private ProductInfo mProductInfo;
    private PayVO mPayVO;
    private String outChannel;

    private TextView mTvMsg;
    private Button mBackButton;
    private OrderPayStateTask mPayStateTask;
    private PayTimeoutTask mTimeoutTask;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case AppConstants.FLAG_QRCODE_URL:
                    PayVO payVO = (PayVO) msg.obj;
                    mPayVO = payVO;
                    String qrcodeUrl = String.format("%s/getQrcode?url=%s", AppConstants.WX_API, payVO.getCode_url());
                    Glide.with(GoodsDetailActivity.this).load(qrcodeUrl).into(mQrcode);
                    mQrcode.setVisibility(View.VISIBLE);
                    mIvPhone.setVisibility(View.VISIBLE);
                    mTvTips.setVisibility(View.VISIBLE);

                    mTimeoutTask = new PayTimeoutTask(mHandler,"返回(%s秒)");
                    mTimeoutTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    mPayStateTask = new OrderPayStateTask(getApplicationContext(), mTvMsg);
                    mPayStateTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, payVO.getOut_trade_no(), outChannel);
                    break;
                case AppConstants.FLAG_UPDATE_COUNTDOWN:
                    mBackButton.setText((String) msg.obj);
                    break;
                case AppConstants.FLAG_CLOSE_DETAIL:
                    GoodsDetailActivity.this.finish();
                    break;
                case AppConstants.FLAG_ERROR:
                    String errorMsg = (String) msg.obj;
                    Toast.makeText(GoodsDetailActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_detail);
        ProductInfo productInfo = (ProductInfo) getIntent().getSerializableExtra(AppConstants.INTENT_GOODS);
        Log.d("GoodsDetailActivity", productInfo.toString());
        mProductInfo = productInfo;
        initRecylcerView(productInfo.getIcons());
        initView(productInfo);
    }

    private void initView(ProductInfo productInfo) {
        mProductName = findViewById(R.id.tv_name);
        mProductName.setText(productInfo.getName());

        mSellpoint = findViewById(R.id.tv_sellpoint);
        mSellpoint.setText(productInfo.getSellpoint());

        mPrice = findViewById(R.id.tv_price);
        mPrice.setText(String.format("¥%s", productInfo.getPrice()));

        mSalesCount = findViewById(R.id.tv_count);
        mSalesCount.setText(String.format("(已售:%s件)", productInfo.getSellCount()));

        mQrcode = findViewById(R.id.id_qrcode);
        mTvMsg = findViewById(R.id.tv_msg);
        mIvPhone = findViewById(R.id.iv_phone);
        mTvTips = findViewById(R.id.tv_tips);
        mBackButton = findViewById(R.id.btn_back);
    }

    private void initRecylcerView(List<String> icons) {
        mBanner = findViewById(R.id.id_banner);
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
        getQrcode();
    }

    private void getQrcode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                QrcodeVO qrcode = new QrcodeVO();
                qrcode.setProductId(mProductInfo.getId());
//                qrcode.setTotalFee(mProductInfo.getPrice());
                qrcode.setTotalFee(0.01f);
                qrcode.setDeviceInfo("名进投资大厦店");
                qrcode.setBody(mProductInfo.getName());
                qrcode.setDetail(mProductInfo.getName());
                qrcode.setAttach("茶美自动售卖机");
                outChannel = Channels.getInstance().getRandomChannel();
                qrcode.setChannelId(outChannel.substring(1));
                qrcode.setIcon(mProductInfo.getAvatar());

                Message message = mHandler.obtainMessage();
                try {
                    String json = JSON.toJSONString(qrcode);
                    PayVO payVO = Api.getInstance().getWxPay(json);
                    message.what = AppConstants.FLAG_QRCODE_URL;
                    message.obj = payVO;
                    mHandler.sendMessage(message);
                } catch (AppException e) {
                    Log.e("GoodsDetailActivity", e.getMessage());
                    message.what = AppConstants.FLAG_ERROR;
                    message.obj = e.getMessage();
                    mHandler.sendMessage(message);
                }
            }
        }).start();
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
        mTvMsg.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mBanner != null) {
            mBanner.startAutoPlay();
        }
    }
}
