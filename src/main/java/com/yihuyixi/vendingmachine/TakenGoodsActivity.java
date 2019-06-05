package com.yihuyixi.vendingmachine;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yihuyixi.vendingmachine.adapter.TakenGoodsAdapter;
import com.yihuyixi.vendingmachine.api.Api;
import com.yihuyixi.vendingmachine.api.Channels;
import com.yihuyixi.vendingmachine.bean.OrderInfo;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.divider.DividerItemDecoration;
import com.yihuyixi.vendingmachine.exception.AppException;
import com.yihuyixi.vendingmachine.exception.NoDataException;
import com.yihuyixi.vendingmachine.message.EventMessage;
import com.yihuyixi.vendingmachine.sdk.SdkUtils;
import com.yihuyixi.vendingmachine.vo.VendorResponse;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.yihuyixi.vendingmachine.constants.AppConstants.TAG_YIHU;

public class TakenGoodsActivity extends BaseActivity {
    @BindView(R.id.id_taken_recycler) RecyclerView mRecyclerView;
    private VendorResponse.VendorUser mUserVO;
    private TakenGoodsAdapter mTakenGoodsAdapter;
    private Unbinder mUnbinder;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.d(AppConstants.TAG_YIHU, msg.toString());
            switch (msg.what) {
                case AppConstants.FLAG_GOODS:
                    initRecyclerView((List<OrderInfo>) msg.obj);
                    break;
                case AppConstants.FLAG_NO_DATA:
                    Toast.makeText(TakenGoodsActivity.this, "网络故障，数据加载失败！", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taken_goods);
        ButterKnife.bind(this);

        mUserVO = (VendorResponse.VendorUser) getIntent().getSerializableExtra(AppConstants.INTENT_TAKEN_DEVICE);
        Log.d(AppConstants.TAG_YIHU, mUserVO.toString());
        this.fetchGoodsData();
    }

    private void initRecyclerView(List<OrderInfo> list) {
        mTakenGoodsAdapter = new TakenGoodsAdapter(this, R.layout.taken_goods_item, list);
        mRecyclerView.setAdapter(mTakenGoodsAdapter);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mTakenGoodsAdapter.setOnItemClickListener(new TakenGoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG_YIHU, "onItemClick position=" + position + ", size=" + list.size());
                String outChannel = Channels.getInstance().getRandomChannel();
                mTakenGoodsAdapter.removeItem(list.get(position), position);
                SdkUtils.getInstance().checkout(Integer.parseInt(outChannel.substring(1)));
            }
        });
    }

    public void fetchGoodsData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = mHandler.obtainMessage();
                try {
                    message.what = AppConstants.FLAG_GOODS;
                    message.obj = Api.getInstance().getOrderList("", mUserVO.getUserId());
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
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @OnClick(R.id.id_taken_back)
    public void goBack(View view) {
        this.finish();
    }
}
