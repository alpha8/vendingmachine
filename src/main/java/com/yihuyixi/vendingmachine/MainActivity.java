package com.yihuyixi.vendingmachine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mylibrary.serialportlibrary.WMSerialportManager;
import com.example.mylibrary.serialportlibrary.listener.OnDataSentListener;
import com.example.mylibrary.serialportlibrary.listener.WMDeviceToAppCallBack;
import com.example.mylibrary.serialportlibrary.listener.WMSerialportCallBack;
import com.example.mylibrary.serialportlibrary.protocol.WMSSendType;
import com.yihuyixi.vendingmachine.adapter.SimpleAdapter;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Banner mBanner;
    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private SimpleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Activity", "main is created.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setSystemUIVisible(false);
        initSDK();
        initSliderView();
        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                this.mAdapter.addData(1);
                break;
            case R.id.action_delete:
                this.mAdapter.removeData(1);
                break;
            case R.id.action_gridview:
                this.mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                break;
            case R.id.action_listview:
                this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            case R.id.action_hgridview:
                StaggeredGridLayoutManager slm = new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);
                this.mRecyclerView.setLayoutManager(slm);
                break;
            case R.id.action_staggered:
                Intent intent = new Intent(this, StaggeredGridActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerview);
        mDatas = new ArrayList<>();
        for(int i = 'A'; i <= 'z'; i++) {
            mDatas.add("" + (char)i);
        }
        mAdapter = new SimpleAdapter(getApplicationContext(), mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter.setOnItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "click: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, "long click: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSDK() {
        final Context context = getApplicationContext();
        WMSerialportManager.initWMSerialport(getApplicationContext(), 10 * 1000);   //overtime: 10s
        WMSerialportManager.openSerialPort("ttyS3", new WMSerialportCallBack() {
            @Override
            public void onSucceed(WMSSendType wmsSendType, Object o) {
                Log.d("SDK", "open the serial port(ttyS3) success.");
            }

            @Override
            public void onFailed(WMSSendType wmsSendType, int i) {
                Log.d("SDK", "open the serial port(ttyS3) failed.");
            }
        });
        WMSerialportManager.addOnDataSentListener(new OnDataSentListener() {
            @Override
            public void onDataSent(String s) {
                Log.d("SDK", "sent data is " + s);
            }
        });
        WMSerialportManager.addWMDeviceToAppCallBack(new WMDeviceToAppCallBack() {
            @Override
            public void onSuccess(WMSSendType wmsSendType, String s) {
                Log.d("SDK", "DeviceToApp sendType=" + wmsSendType + ", text=" + s);
            }

            @Override
            public void onFaild(WMSSendType wmsSendType, int i, List<String> list, String s) {
                Log.d("SDK", "DeviceToApp failed sendType=" + wmsSendType + ", text=" + s);
            }
        });
    }

    private void initSliderView() {
        mBanner = findViewById(R.id.mbanner);
        int[] imgIds = new int[] { R.mipmap.slider1, R.mipmap.slider2, R.mipmap.slider3, R.mipmap.slider4};
        List<Integer> images = new ArrayList<>();
        for (int i = 0, len = imgIds.length; i < len; i++) {
            images.add(imgIds[i]);
            mBanner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    Glide.with(MainActivity.this).load(path).into(imageView);
                }
            });
            mBanner.setImages(images);
            mBanner.setDelayTime(3000);
            mBanner.start();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mBanner != null) {
            mBanner.startAutoPlay();
        }
    }

    @Override
    protected void onStop() {
        Log.d("Activity", "onStop fired.");
        super.onStop();
        if (mBanner != null) {
            mBanner.stopAutoPlay();
        }
        WMSerialportManager.closeSerialPort();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Activity", "onPause fired.");
    }

    public void checkout(View view){
        int channelId = 0;
        WMSerialportManager.setShipments(0, channelId, 1, 15 * 1000);
        Log.d("Activity", "准备出货中..., 货架号：" + channelId);
    }

    private void setSystemUIVisible(boolean show) {
        if (show) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } else {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }
}
