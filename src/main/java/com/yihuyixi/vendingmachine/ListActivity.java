package com.yihuyixi.vendingmachine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yihuyixi.vendingmachine.adapter.ListAdapter;
import com.yihuyixi.vendingmachine.api.Api;
import com.yihuyixi.vendingmachine.bean.GoodsType;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.divider.DividerItemDecoration;
import com.yihuyixi.vendingmachine.exception.AppException;
import com.yihuyixi.vendingmachine.exception.NoDataException;
import com.yihuyixi.vendingmachine.vo.ExtGoods;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.yihuyixi.vendingmachine.constants.AppConstants.TAG_YIHU;

public class ListActivity extends BaseActivity {
    @BindView(R.id.id_list_banner) Banner mBanner;
    @BindView(R.id.id_list_recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.id_list_back)  Button mBackButton;
    private Unbinder mUnbinder;
    private GoodsType mSectionType;
    private ListAdapter mListAdapter;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.d(AppConstants.TAG_YIHU, msg.toString());
            switch (msg.what) {
                case AppConstants.FLAG_GOODS:
                    initRecyclerView((List<ExtGoods>) msg.obj);
                    break;
                case AppConstants.FLAG_NO_DATA:
                    Toast.makeText(ListActivity.this, "网络故障，数据加载失败！", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mUnbinder = ButterKnife.bind(this);
        GoodsType sectionType = (GoodsType) getIntent().getSerializableExtra(AppConstants.INTENT_SECTION);
        Log.d(AppConstants.TAG_YIHU, "ListActivity type=" + sectionType);
        mSectionType = sectionType;
        initBanner();
        fetchGoodsData();
    }

    private void initRecyclerView(List<ExtGoods> list) {
        mListAdapter = new ListAdapter(getApplicationContext(), R.layout.list_item, list);
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        mListAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG_YIHU, "onItemClick position=" + position + ", size=" + list.size());
                Intent intent = new Intent(ListActivity.this, PromoteDetailActivity.class);
                intent.putExtra(AppConstants.INTENT_PROMOTE_GOODS, list.get(position));
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
                    message.what = AppConstants.FLAG_GOODS;
                    message.obj = Api.getInstance().getPromoteGoods(mSectionType, 1, 30);
                    mHandler.sendMessage(message);
                } catch (NoDataException | AppException e) {
                    Log.e(TAG_YIHU, "fetchGoodsData encounted exception, message=" + e.getMessage(), e);
                    message.what = AppConstants.FLAG_NO_DATA;
                    mHandler.sendMessage(message);
                }
            }
        }).start();
    }

    private void initBanner() {
        List<String> icons = new ArrayList<>();
        if (mSectionType == GoodsType.TUAN) {
            icons.add("http://www.yihuyixi.com/ps/download/5cf0e849e4b05f18f0d23e62");
        } else {
            icons.add("http://www.yihuyixi.com/ps/download/5cf0e849e4b05f18f0d23e63");
        }
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(ListActivity.this).load(path).into(imageView);
            }
        });
        mBanner.setImages(icons);
        mBanner.setDelayTime(3000);
        mBanner.start();
    }

    @OnClick(R.id.id_list_back)
    public void goBack(View view) {
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBanner != null) {
            mBanner.stopAutoPlay();
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
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
