package com.yihuyixi.vendingmachine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.mylibrary.serialportlibrary.protocol.WMSSendType;
import com.igexin.sdk.PushManager;
import com.yihuyixi.vendingmachine.adapter.GridItem;
import com.yihuyixi.vendingmachine.adapter.GridViewAdapter;
import com.yihuyixi.vendingmachine.adapter.SimpleAdapter;
import com.yihuyixi.vendingmachine.api.Api;
import com.yihuyixi.vendingmachine.asynctask.NotakenCheckTask;
import com.yihuyixi.vendingmachine.bean.ChannelRequest;
import com.yihuyixi.vendingmachine.bean.DeviceInfo;
import com.yihuyixi.vendingmachine.bean.GoodsType;
import com.yihuyixi.vendingmachine.bean.ProductInfo;
import com.yihuyixi.vendingmachine.bean.SdkResponse;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.divider.DividerItemDecoration;
import com.yihuyixi.vendingmachine.exception.AppException;
import com.yihuyixi.vendingmachine.exception.NoDataException;
import com.yihuyixi.vendingmachine.message.EventMessage;
import com.yihuyixi.vendingmachine.sdk.SdkUtils;
import com.yihuyixi.vendingmachine.service.AppIntentService;
import com.yihuyixi.vendingmachine.service.AppPushService;
import com.yihuyixi.vendingmachine.utils.NetUtils;
import com.yihuyixi.vendingmachine.utils.VideoUtils;
import com.yihuyixi.vendingmachine.view.DiyDialog;
import com.yihuyixi.vendingmachine.vo.VendorResponse;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import butterknife.Unbinder;

import static com.yihuyixi.vendingmachine.constants.AppConstants.TAG_YIHU;

public class MainActivity extends BaseActivity {
    @BindView(R.id.video) VideoView mVideo;
    @BindView(R.id.recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.gv_promote) GridView mGridView;
    @BindView(R.id.id_main_banner) Banner mBanner;

    private SimpleAdapter mAdapter;
    private List<ProductInfo> mProductInfos = new ArrayList<>();
    private volatile boolean initialized = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG_YIHU, msg.toString());
            switch(msg.what) {
                case AppConstants.FLAG_GOODS:
                    initRecyclerView();
                    break;
                case AppConstants.FLAG_RELOAD_GOODS:
                    if (mAdapter != null) {
                        mAdapter.setDatas((List<ProductInfo>) msg.obj);
                    } else {
                        mProductInfos = (List<ProductInfo>) msg.obj;
                        initRecyclerView();
                    }
                    break;
                case AppConstants.FLAG_NO_DATA:
                    initialized = false;
                    EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_NETWORK_ERROR));
                    break;
                case AppConstants.FLAG_SDK_FAIL:
//                    Toast.makeText(MainActivity.this, "出货失败，请联系客服处理售后问题！", Toast.LENGTH_LONG).show();
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
        this.initBanner();
    }


    private void initBanner() {
        List<String> icons = new ArrayList<>();
        icons.add("http://www.yihuyixi.com/ps/download/5a352b7de4b04d4e77f2da57");
        icons.add("http://www.yihuyixi.com/ps/download/5a352b80e4b04d4e77f2da59");
        icons.add("http://www.yihuyixi.com/ps/download/5a352b81e4b04d4e77f2da5a");
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(MainActivity.this).load(path).into(imageView);
            }
        });
        mBanner.setImages(icons);
        mBanner.setDelayTime(3000);
        mBanner.start();
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
        VideoUtils.getInstance(getApplicationContext()).playNextVideo(mVideo, mBanner);
    }

    private void initRecyclerView() {
        initialized = true;
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
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firstOffset = -1, lastOffset = -1;
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof GridLayoutManager) {
                        lastOffset = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                        firstOffset = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    }
//                    Log.d(TAG_YIHU, "firstOffset=" + firstOffset + ", lastOffset=" + lastOffset);
                    if (lastOffset > 9) {
                        mGridView.setVisibility(View.GONE);
                    } else {
                        mGridView.setVisibility(View.VISIBLE);
                    }
                    if (firstOffset == 0) {
                        mGridView.setVisibility(View.VISIBLE);
                    }
                }
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
        if (mBanner != null) {
            mBanner.stopAutoPlay();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG_YIHU, "onResume");
        VideoUtils.getInstance(getApplicationContext()).playNextVideo(mVideo, mBanner);
        PushManager.getInstance().initialize(getApplicationContext(), AppPushService.class);
        PushManager.getInstance().registerPushIntentService(getApplicationContext(), AppIntentService.class);
        if (mBanner != null) {
            mBanner.startAutoPlay();
        }
    }

    @Override
    public void notifyNetChanged(NetUtils.NetworkType type) {
        super.notifyNetChanged(type);
        if (type != NetUtils.NetworkType.none) {
            if ("".equals(AppConstants.VENDOR_ID)) {
                EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_UPDATE_DEVICE_INFO));
            } else {
                EventBus.getDefault().post(new EventMessage(AppConstants.FLAG_RELOAD_GOODS));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void reloadProducts(EventMessage event) {
        if (event.getType() != AppConstants.FLAG_RELOAD_GOODS) {
            return;
        }
        Log.d(TAG_YIHU, "reloadProducts event=" + event.toString());
        try {
            Message message = mHandler.obtainMessage();
            message.what = AppConstants.FLAG_RELOAD_GOODS;
            message.obj = Api.getInstance().getGoods(AppConstants.VENDOR_ID);
            mHandler.sendMessage(message);
        } catch (NoDataException | AppException e) {
            Log.e(TAG_YIHU, "reload goods data encounted exception, message=" + e.getMessage(), e);
            Message message = mHandler.obtainMessage();
            message.what = AppConstants.FLAG_NO_DATA;
            mHandler.sendMessage(message);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void updateStock(EventMessage message) {
        if (message.getType() == AppConstants.FLAG_UPDATE_STOCK_INFO) {
            Log.d(TAG_YIHU, "message=" + message);
            // productInfo: {"pid":"p001","status":1}   status => 1: 已售罄  0: 正常购买
            mAdapter.updateItem((ProductInfo) message.getData());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC, sticky = true)
    public void updateDeviceInfo(EventMessage message) {
        if (message.getType() == AppConstants.FLAG_UPDATE_DEVICE_INFO) {
            try {
                DeviceInfo di = Api.getInstance().getDeviceInfo(AppConstants.VENDOR_ID);
                if (di == null) {
                    return;
                }
                AppConstants.CURRENT_DEVICE = di;
                if (!"".equals(AppConstants.VENDOR_ID)) {
                    EventBus.getDefault().post(new EventMessage(AppConstants.FLAG_RELOAD_GOODS));
                }
            } catch (AppException e) {
                Log.e(AppConstants.TAG_YIHU, e.getMessage(), e);
            }
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.ASYNC)
    public void deliverySucceed(EventMessage message) {
        if (message.getType() == AppConstants.FLAG_SDK_SUCCESS && !AppConstants.IS_DEVICE_CHECKING) {
            SdkResponse response = (SdkResponse) message.getData();
            Log.d(AppConstants.TAG_YIHU, "deliverySucceed SDK response=" + response.getMessage());
            if (response.getType() == WMSSendType.SHIPMENTS && response.isSuccess()) {
                // ChannelVO: {"channelNo":"21","level":"B1","count":1, "deviceId":"202227061185125"}
                String orderNo = response.getRealOrderNo();
                ChannelRequest.ChannelVO channelVO = new ChannelRequest.ChannelVO();
                channelVO.setChannelNo(orderNo);
                channelVO.setDeviceId(AppConstants.VENDOR_ID);
                channelVO.setLevel(AppConstants.LAST_SHIPMENT_LEVEL);
                channelVO.setCount(1);
                String json = JSON.toJSONString(channelVO);
                try {
                    ProductInfo pi = Api.getInstance().stockOut(json);
                    Log.d(AppConstants.TAG_YIHU, "stock out response=" + pi);
                    EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_UPDATE_STOCK_INFO, pi));
                } catch (AppException e) {
                    Log.e(TAG_YIHU, e.getMessage(), e);
                }
            } else {
                // ChannelVO: {"channelNo":"21","level":"B1","preId":0, "deviceId":"202227061185125", "pickCode": 690794}   普通商品无pickCode, 拼团和砍价有
                String orderNo = response.getRealOrderNo();
                ChannelRequest.ChannelVO channelVO = new ChannelRequest.ChannelVO();
                channelVO.setChannelNo(orderNo);
                channelVO.setDeviceId(AppConstants.VENDOR_ID);
                channelVO.setLevel(AppConstants.LAST_SHIPMENT_LEVEL);
                channelVO.setPickCode(AppConstants.LAST_PICK_CODE);
                channelVO.setPreId(AppConstants.LAST_PREORDER_ID);
                String json = JSON.toJSONString(channelVO);
                try {
                    ProductInfo pi = Api.getInstance().takenError(json);
                    Log.d(AppConstants.TAG_YIHU, "takenError response=" + pi);
                    EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_UPDATE_STOCK_INFO, pi));
                } catch (AppException e) {
                    Log.e(TAG_YIHU, e.getMessage(), e);
                }
            }
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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void networkDisconnect(EventMessage message) {
        if (message.getType() != AppConstants.FLAG_NETWORK_ERROR) {
            return;
        }
        View dialogView = LayoutInflater.from(this).inflate(R.layout.reconnect_dialog, null);
        dialog = new DiyDialog(this, dialogView);
        dialog.setDialogWidth(50);
        dialog.setDialogHeight(20);
        dialog.show();

        TextView confirm = dialogView.findViewById(R.id.id_reconnect_dialog_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (initialized) {
                    EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_RELOAD_GOODS));
                }
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.id_help)
    public void doHelp(View view) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog, null);
        dialog = new DiyDialog(this, dialogView);
        dialog.setDialogWidth(50);
        dialog.setDialogHeight(10);
        dialog.show();

        if (AppConstants.CURRENT_DEVICE != null) {
            TextView content = dialogView.findViewById(R.id.id_dialog_content);
            content.setText(AppConstants.CURRENT_DEVICE.getServiceNo());
        }
        TextView confirm = dialogView.findViewById(R.id.id_dialog_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private long[] hints = new long[10];
    @OnTouch(R.id.fl_main_footer)
    public boolean enterAdminUI(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            System.arraycopy(hints, 1, hints, 0, hints.length - 1);
            hints[hints.length - 1] = SystemClock.uptimeMillis();
            if (SystemClock.uptimeMillis() - hints[0] <= 3000) {
                Log.d(TAG_YIHU, "enterAdminUI");
                startActivity(new Intent(MainActivity.this, ChannelActivity.class));
            }
        }
        return true;
    }
}
