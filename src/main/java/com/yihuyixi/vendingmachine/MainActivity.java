package com.yihuyixi.vendingmachine;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.yihuyixi.vendingmachine.adapter.SimpleAdapter;
import com.yihuyixi.vendingmachine.api.Api;
import com.yihuyixi.vendingmachine.bean.ProductInfo;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.divider.DividerItemDecoration;
import com.yihuyixi.vendingmachine.exception.NoDataException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Activity", "main is created.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initSliderView();
        fetchGoodsData();
//        setSystemUIVisible(false);
        this.initVideoView();
    }

    private void initVideoView() {
        mVideo = findViewById(R.id.video);
        mVideo.setMediaController(new MediaController(getApplicationContext()));
//        mVideo.setVideoURI(Uri.parse("http://1252423336.vod2.myqcloud.com/950efb46vodtransgzp1252423336/85f5d37d4564972818869478170/v.f20.mp4"));
        mVideo.requestFocus();
        mVideo.start();
        mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });
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
                } catch (NoDataException | IOException e) {
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
        mVideo.pause();
        Log.d("MainActivity", "onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideo.resume();
        Log.d("MainActivity", "onResume");
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
