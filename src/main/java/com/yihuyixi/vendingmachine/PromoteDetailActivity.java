package com.yihuyixi.vendingmachine;

import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yihuyixi.vendingmachine.api.Api;
import com.yihuyixi.vendingmachine.asynctask.PayTimeoutTask;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.exception.AppException;
import com.yihuyixi.vendingmachine.message.EventMessage;
import com.yihuyixi.vendingmachine.utils.Utils;
import com.yihuyixi.vendingmachine.vo.Artwork;
import com.yihuyixi.vendingmachine.vo.ExtGoods;
import com.yihuyixi.vendingmachine.vo.PictureInfo;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PromoteDetailActivity extends BaseActivity{
    @BindView(R.id.id_detail_banner) Banner mBanner;
    @BindView(R.id.id_detail_name) TextView mProductName;
    @BindView(R.id.id_detail_sellpoint) TextView mSellpoint;
    @BindView(R.id.id_detail_price) TextView mPrice;
    @BindView(R.id.id_detail_marketPrice) TextView mMarketPrice;
    @BindView(R.id.id_detail_limitcount) TextView mLimitCount;
    @BindView(R.id.id_detail_qrcode) ImageView mQrcode;
    @BindView(R.id.id_detail_back) Button mBackButton;

    private PayTimeoutTask mTimeoutTask;
    private ExtGoods mExtGoods;
    private Unbinder mUnbinder;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case AppConstants.FLAG_UPDATE_COUNTDOWN:
                    mBackButton.setText((String) msg.obj);
                    break;
                case AppConstants.FLAG_CLOSE_DETAIL:
                    PromoteDetailActivity.this.finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        ExtGoods goods = (ExtGoods) getIntent().getSerializableExtra(AppConstants.INTENT_PROMOTE_GOODS);
        Log.d(AppConstants.TAG_YIHU, goods.toString());
        mExtGoods = goods;
        initView(goods);
    }

    private void renderBanner(List<String> icons) {
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(PromoteDetailActivity.this).load(path).into(imageView);
            }
        });
        mBanner.setImages(icons);
        mBanner.setDelayTime(3000);
        mBanner.start();
    }

    private void initView(ExtGoods goods) {
        mProductName.setText(goods.getName());
        mSellpoint.setText(goods.getSellPoint());
        //1: bargain   2: tuan
        if (goods.getType() == 1) {
            mPrice.setText(String.format("底价:¥%s", Utils.getFormatPrice(goods.getButtomFee())));
            int persons = (int)Math.floor((goods.getFieldPrice() - goods.getButtomFee()) / goods.getForwardFee());
            if (persons > 1) {
                mLimitCount.setText(String.format("1-%s人", persons));
            } else {
                mLimitCount.setText("1人");
            }
        } else {
            mPrice.setText(String.format("拼团价:¥%s", Utils.getFormatPrice(goods.getButtomFee())));
            mLimitCount.setText(String.format("%s人拼团", goods.getLimitCount()));
        }
        mMarketPrice.setText(Utils.getFormatPrice(goods.getFieldPrice()));
        mMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().post(new EventMessage(AppConstants.FLAG_DETAIL_BANNER));
        EventBus.getDefault().post(new EventMessage(AppConstants.FLAG_DETAIL_QRCODE_URL));
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void getQrcode(EventMessage event) {
        if (event.getType() != AppConstants.FLAG_DETAIL_QRCODE_URL) {
            return;
        }
        Log.d(AppConstants.TAG_YIHU, "getQrcode event=" + event.toString());
        int type = mExtGoods.getType(); //1: bargain(mapping cms type is 9)   2: tuan(cms type: 8)
        String qrcodeUrl = String.format("%s/qrcode/artwork?aid=%s&type=%s&size=450&vendorId=%s",
                AppConstants.CMS_API,
                mExtGoods.getId(),
                type == 1 ? 9 : 8,
                AppConstants.VENDOR_ID);
        Log.d(AppConstants.TAG_YIHU, "detail qrcode=" + qrcodeUrl);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(PromoteDetailActivity.this).load(qrcodeUrl).into(mQrcode);
                mTimeoutTask = new PayTimeoutTask(mHandler,"返回（%s秒）");
                mTimeoutTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void initBanner(EventMessage event) {
        if (event.getType() != AppConstants.FLAG_DETAIL_BANNER) {
            return;
        }
        Log.d(AppConstants.TAG_YIHU, "initBanner event=" + event.toString());
        try {
            Artwork artwork = Api.getInstance().getGoodsById(mExtGoods.getArtworkId());
            Log.d(AppConstants.TAG_YIHU, "artwork=" + artwork);
            List<String> icons = new ArrayList<>();
            if (artwork == null || artwork.getPictures() == null) {
                icons.add(Utils.getPictureServerUrl(mExtGoods.getIcon()));
            } else {
                List<PictureInfo> pics = artwork.getPictures();
                for (PictureInfo pid : pics) {
                    icons.add(Utils.getPictureServerUrl(pid.getId()));
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    renderBanner(icons);
                }
            });
        } catch (AppException e) {
            Log.d(AppConstants.TAG_YIHU, e.getMessage(), e);
        }
    }

    @OnClick(R.id.id_detail_back)
    public void goBack(View view) {
        this.mTimeoutTask.cancelJob();
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBanner != null) {
            mBanner.stopAutoPlay();
        }
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
        EventBus.getDefault().unregister(this);
    }
}
