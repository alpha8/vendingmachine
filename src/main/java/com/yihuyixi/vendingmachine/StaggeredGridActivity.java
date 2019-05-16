package com.yihuyixi.vendingmachine;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yihuyixi.vendingmachine.adapter.SimpleAdapter;
import com.yihuyixi.vendingmachine.adapter.StaggeredAdapter;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class StaggeredGridActivity extends AppCompatActivity {
    private Banner mBanner;
    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private StaggeredAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Activity", "main is created.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSliderView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerview);
        mDatas = new ArrayList<>();
        for(int i = 'A'; i <= 'z'; i++) {
            mDatas.add("" + (char)i);
        }
        mAdapter = new StaggeredAdapter(getApplicationContext(), mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter.setOnItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onItemLongClick(View view, int position) {
                mAdapter.removeData(position);
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
                    Glide.with(StaggeredGridActivity.this).load(path).into(imageView);
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
        super.onStop();
        if (mBanner != null) {
            mBanner.stopAutoPlay();
        }
        Log.d("StaggeredActivity", "onStop fired.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("StaggeredActivity", "onPause fired.");
    }
}
