package com.yihuyixi.vendingmachine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.yihuyixi.vendingmachine.adapter.GridItem;
import com.yihuyixi.vendingmachine.adapter.GridViewAdapter;
import com.yihuyixi.vendingmachine.adapter.SimpleAdapter;
import com.yihuyixi.vendingmachine.api.Api;
import com.yihuyixi.vendingmachine.asynctask.NotakenCheckTask;
import com.yihuyixi.vendingmachine.bean.GoodsType;
import com.yihuyixi.vendingmachine.bean.ProductInfo;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.divider.DividerItemDecoration;
import com.yihuyixi.vendingmachine.exception.AppException;
import com.yihuyixi.vendingmachine.exception.NoDataException;
import com.yihuyixi.vendingmachine.message.EventMessage;
import com.yihuyixi.vendingmachine.sdk.SdkUtils;
import com.yihuyixi.vendingmachine.utils.NetUtils;
import com.yihuyixi.vendingmachine.utils.VideoUtils;
import com.yihuyixi.vendingmachine.view.DiyDialog;
import com.yihuyixi.vendingmachine.vo.VendorResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.yihuyixi.vendingmachine.constants.AppConstants.TAG_YIHU;

public class MainActivity extends BaseActivity {
    @BindView(R.id.video) VideoView mVideo;
    @BindView(R.id.recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.gv_promote) GridView mGridView;
    private SimpleAdapter mAdapter;
    private List<ProductInfo> mProductInfos = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG_YIHU, msg.toString());
            switch(msg.what) {
                case AppConstants.FLAG_GOODS:
                    initRecyclerView();
                    break;
                case AppConstants.FLAG_RELOAD_GOODS:
                    mAdapter.setDatas((List<ProductInfo>) msg.obj);
                    if (mRecyclerView == null) {
                        mProductInfos = mAdapter.getDatas();
                        initRecyclerView();
                    }
                    break;
                case AppConstants.FLAG_NO_DATA:
                    Toast.makeText(MainActivity.this, "网络故障，数据加载失败！", Toast.LENGTH_LONG).show();
                    break;
                case AppConstants.FLAG_SDK_FAIL:
                    Toast.makeText(MainActivity.this, "出货失败，请联系客服处理售后问题！", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private Unbinder mUnbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG_YIHU, "main is created.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        this.initVideoView();
        this.initSdk();
        this.initPromoteView();
        fetchGoodsData();
    }

    private void initPromoteView() {
        List<GridItem> list = new ArrayList<>();
        list.add(new GridItem("http://www.yihuyixi.com/ps/download/5cf0e849e4b05f18f0d23e62", GoodsType.TUAN));
        list.add(new GridItem("http://www.yihuyixi.com/ps/download/5cf0e849e4b05f18f0d23e63", GoodsType.BARGAIN));
        GridViewAdapter mGridAdapter = new GridViewAdapter(this, R.layout.grid_item, list);
        mGridView.setAdapter(mGridAdapter);
        mGridAdapter.setOnItemClickListener(new GridViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GridViewAdapter.ViewHolder holder, int position) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra(AppConstants.INTENT_SECTION, holder.getSectionType());
                startActivity(intent);
            }
        });
    }

    private void initSdk() {
        SdkUtils.getInstance().initialize(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SdkUtils.getInstance().release();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
    }

    private void initVideoView() {
        VideoUtils.getInstance(getApplicationContext()).playNextVideo(mVideo);
    }

    private void initRecyclerView() {
        mAdapter = new SimpleAdapter(getApplicationContext(), mProductInfos);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        mAdapter.setOnItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG_YIHU, "onItemClick position=" + position + ", size=" + mProductInfos.size());
                Intent intent = new Intent(MainActivity.this, GoodsDetailActivity.class);
                intent.putExtra(AppConstants.INTENT_GOODS, mProductInfos.get(position));
                startActivity(intent);
            }
        });
    }

    private void fetchGoodsData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = mHandler.obtainMessage();
                try {
                    mProductInfos.addAll(Api.getInstance().getGoods(AppConstants.VENDOR_ID));
                    message.what = AppConstants.FLAG_GOODS;
                    mHandler.sendMessage(message);
                } catch (NoDataException | AppException e) {
                    Log.e(TAG_YIHU, "fetchGoodsData encounted exception, message=" + e.getMessage(), e);
                    message.what = AppConstants.FLAG_NO_DATA;
                    mHandler.sendMessage(message);
                }
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG_YIHU, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG_YIHU, "onStop");
        mVideo.pause();
        mVideo.stopPlayback();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG_YIHU, "onResume");
        VideoUtils.getInstance(getApplicationContext()).playNextVideo(mVideo);
    }

    @Override
    public void notifyNetChanged(NetUtils.NetworkType type) {
        super.notifyNetChanged(type);
        if (type != NetUtils.NetworkType.none) {
            EventBus.getDefault().post(new EventMessage(AppConstants.FLAG_RELOAD_GOODS));
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void reloadProducts(EventMessage event) {
        if (event.getType() != AppConstants.FLAG_RELOAD_GOODS) {
            return;
        }
        Log.d(TAG_YIHU, "reloadProducts event=" + event.toString());
        try {
            if (mProductInfos.isEmpty()) {
                Message message = mHandler.obtainMessage();
                message.what = AppConstants.FLAG_RELOAD_GOODS;
                message.obj = Api.getInstance().getGoods(AppConstants.VENDOR_ID);
                mHandler.sendMessage(message);
            }
        } catch (AppException e) {
            Log.e(TAG_YIHU, "reload goods data encounted exception, message=" + e.getMessage(), e);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void updateStock(EventMessage message) {
        if (message.getType() == AppConstants.FLAG_UPDATE_STOCK_INFO) {
            mAdapter.updateItem((ProductInfo) message.getData());
        }
    }

    private DiyDialog dialog;
    private NotakenCheckTask mNotakenCheckTask;
    @OnClick(R.id.id_deposit)
    public void doDeposit() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_exchange_code, null);
        dialog = new DiyDialog(this, dialogView);
        dialog.setDialogWidth(50);
        dialog.setDialogHeight(32);
        dialog.show();

        String vendorId = AppConstants.VENDOR_ID;
        String requestId = UUID.randomUUID().toString();
        String qrcodeUrl = Api.getInstance().getTakenQrcode(vendorId, requestId);
        ImageView qrcode = dialogView.findViewById(R.id.iv_exchange_qrcode);
        Glide.with(this).load(qrcodeUrl).into(qrcode);

        TextView confirm = dialogView.findViewById(R.id.id_code_back);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mNotakenCheckTask != null) {
                    mNotakenCheckTask.cancelJob();
                }
            }
        });
        mNotakenCheckTask = new NotakenCheckTask();
        mNotakenCheckTask.execute(vendorId, requestId);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void scanTimeout(EventMessage event) {
        if (event.getType() == AppConstants.FLAG_TAKEN_FAIL) {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void loginSuccess(EventMessage event) {
        if (event.getType() == AppConstants.FLAG_TAKEN_SUCCESS) {
            VendorResponse.VendorUser user = (VendorResponse.VendorUser) event.getData();
            Intent intent = new Intent(MainActivity.this, TakenGoodsActivity.class);
            intent.putExtra(AppConstants.INTENT_TAKEN_DEVICE, user);
            startActivity(intent);
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    @OnClick(R.id.id_help)
    public void doHelp(View view) {
//        String deviceId = Utils.getDeviceId(this);
//        Log.d(TAG_YIHU, "deviceId=" + deviceId);

        startActivity(new Intent(MainActivity.this, ChannelActivity.class));
//        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog, null);
//        dialog = new DiyDialog(this, dialogView);
//        dialog.setDialogWidth(50);
//        dialog.setDialogHeight(28);
//        dialog.show();
//
//        TextView confirm = dialogView.findViewById(R.id.id_dialog_confirm);
//        confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
    }
}
