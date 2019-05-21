package com.yihuyixi.vendingmachine;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.VideoView;

import com.yihuyixi.vendingmachine.adapter.SimpleAdapter;
import com.yihuyixi.vendingmachine.api.Api;
import com.yihuyixi.vendingmachine.bean.ProductInfo;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.divider.DividerItemDecoration;
import com.yihuyixi.vendingmachine.exception.AppException;
import com.yihuyixi.vendingmachine.exception.NoDataException;
import com.yihuyixi.vendingmachine.sdk.SdkUtils;
import com.yihuyixi.vendingmachine.utils.VideoUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private VideoView mVideo;
    private RecyclerView mRecyclerView;
    private SimpleAdapter mAdapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("MainActivity", msg.toString());
            switch(msg.what) {
                case AppConstants.FLAG_GOODS:
                    List<ProductInfo> products = (List<ProductInfo>) msg.obj;
                    initRecyclerView(products);
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
        Log.d("MainActivity", "main is created.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSystemUIVisible(false);
        fetchGoodsData();
        this.initVideoView();
        this.initOtherViews();
        this.initSdk();
    }

    private PopupWindow popup;
    private void initOtherViews() {
        View root = this.getLayoutInflater().inflate(R.layout.popup, null);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        popup = new PopupWindow(root, dm.widthPixels, dm.heightPixels);
        final Button cancel = root.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
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
//        VideoUtils.getInstance(getApplicationContext()).playNextVideo(mVideo);
    }

    private void initRecyclerView(final List<ProductInfo> goods) {
        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new SimpleAdapter(getApplicationContext(), goods);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        mAdapter.setOnItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, GoodsDetailActivity.class);
                intent.putExtra(AppConstants.INTENT_GOODS, goods.get(position));
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
                    List<ProductInfo> products = Api.getInstance().getGoods(50);
                    message.what = AppConstants.FLAG_GOODS;
                    message.obj = products;
                    mHandler.sendMessage(message);
                } catch (NoDataException | AppException e) {
                    Log.e("MainActivity", "fetchGoodsData encounted exception, message=" + e.getMessage());
                    message.what = AppConstants.FLAG_NO_DATA;
                    mHandler.sendMessage(message);
                }
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity", "onStop");
        mVideo.pause();
        mVideo.stopPlayback();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume");
        VideoUtils.getInstance(getApplicationContext()).playNextVideo(mVideo);
    }

    private void setSystemUIVisible(boolean show) {
        if (show) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } else {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }

    public void doHelp(View view) {
        Log.d("MainActivity", "doHelp");
        popup.showAtLocation(view, Gravity.CENTER,20, 20);
    }
}
