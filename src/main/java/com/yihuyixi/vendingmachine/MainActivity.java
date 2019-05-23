package com.yihuyixi.vendingmachine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import com.yihuyixi.vendingmachine.activity.BaseActivity;
import com.yihuyixi.vendingmachine.adapter.SimpleAdapter;
import com.yihuyixi.vendingmachine.api.Api;
import com.yihuyixi.vendingmachine.bean.ProductInfo;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.divider.DividerItemDecoration;
import com.yihuyixi.vendingmachine.exception.AppException;
import com.yihuyixi.vendingmachine.exception.NoDataException;
import com.yihuyixi.vendingmachine.sdk.SdkUtils;
import com.yihuyixi.vendingmachine.utils.NetUtils;
import com.yihuyixi.vendingmachine.utils.Utils;
import com.yihuyixi.vendingmachine.utils.VideoUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.yihuyixi.vendingmachine.constants.AppConstants.TAG_YIHU;

public class MainActivity extends BaseActivity {
    private VideoView mVideo;
    private RecyclerView mRecyclerView;
    private SimpleAdapter mAdapter;
    private List<ProductInfo> mProductInfos;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG_YIHU, msg.toString());
            switch(msg.what) {
                case AppConstants.FLAG_GOODS:
                    initRecyclerView();
                    break;
                case AppConstants.FLAG_RELOAD_GOODS:
                    if (mRecyclerView != null) {
                        mAdapter.setDatas(mProductInfos);
                    } else {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG_YIHU, "main is created.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchGoodsData();
        this.initVideoView();
//        this.initOtherViews();
        this.initSdk();
    }
    private void initSdk() {
        SdkUtils.getInstance().initialize(getApplicationContext(), mHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SdkUtils.getInstance().release();
    }

    private void initVideoView() {
        mVideo = findViewById(R.id.video);
        VideoUtils.getInstance(getApplicationContext()).playNextVideo(mVideo);
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new SimpleAdapter(getApplicationContext(), mProductInfos);
        mRecyclerView.setAdapter(mAdapter);
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
                    mProductInfos = Api.getInstance().getGoods(60);
                    message.what = AppConstants.FLAG_GOODS;
                    mHandler.sendMessage(message);
                } catch (NoDataException | AppException e) {
                    Log.e(TAG_YIHU, "fetchGoodsData encounted exception, message=" + e.getMessage());
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG_YIHU, "onResume");
        Utils.hideBottomUIMenu(getWindow());
        VideoUtils.getInstance(getApplicationContext()).playNextVideo(mVideo);
    }

    @Override
    public void notifyNetChanged(NetUtils.NetworkType type) {
        super.notifyNetChanged(type);
        if (type == NetUtils.NetworkType.none) {
            if (mProductInfos == null || mProductInfos.isEmpty()) {
                try {
                    mProductInfos = Api.getInstance().getGoods(60);
                    if (mProductInfos != null && !mProductInfos.isEmpty()) {
                        Message message = mHandler.obtainMessage();
                        message.what = AppConstants.FLAG_RELOAD_GOODS;
                        mHandler.sendMessage(message);
                    }
                } catch (AppException e) {
                    e.printStackTrace();
                    Log.e(TAG_YIHU, "reload goods data encounted exception, message=" + e.getMessage());
                }
            }
        }
    }

    public void doHelp(View view) {
        Log.d(TAG_YIHU, "doHelp");
//        new AlertDialog.Builder(this)
//                .setTitle("客服中心")
//                .setView(getLayoutInflater().inflate(R.layout.popup, null))
//                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .create().show();
    }
}
