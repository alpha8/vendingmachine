package com.yihuyixi.vendingmachine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mylibrary.serialportlibrary.protocol.WMSSendType;
import com.yihuyixi.vendingmachine.adapter.TakenGoodsAdapter;
import com.yihuyixi.vendingmachine.api.Api;
import com.yihuyixi.vendingmachine.bean.OrderInfo;
import com.yihuyixi.vendingmachine.bean.SdkResponse;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.exception.AppException;
import com.yihuyixi.vendingmachine.exception.NoDataException;
import com.yihuyixi.vendingmachine.message.EventMessage;
import com.yihuyixi.vendingmachine.sdk.SdkUtils;
import com.yihuyixi.vendingmachine.vo.PayVO;
import com.yihuyixi.vendingmachine.vo.PromoteGoodsVO;
import com.yihuyixi.vendingmachine.vo.VendorResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.yihuyixi.vendingmachine.constants.AppConstants.TAG_YIHU;

public class TakenGoodsActivity extends BaseActivity {
    @BindView(R.id.id_taken_recycler) RecyclerView mRecyclerView;
    @BindView(R.id.rl_takengoods_nomore) RelativeLayout mNoResource;
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
        EventBus.getDefault().register(this);

        mUserVO = (VendorResponse.VendorUser) getIntent().getSerializableExtra(AppConstants.INTENT_TAKEN_DEVICE);
        Log.d(AppConstants.TAG_YIHU, mUserVO.toString());
        this.fetchGoodsData();
        AppConstants.LAST_PICK_CODE = "";
    }

    private void initRecyclerView(List<OrderInfo> list) {
        if (list == null || list.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            mNoResource.setVisibility(View.VISIBLE);
            return;
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoResource.setVisibility(View.GONE);
        }
        mTakenGoodsAdapter = new TakenGoodsAdapter(this, R.layout.taken_goods_item, list);
        mRecyclerView.setAdapter(mTakenGoodsAdapter);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mTakenGoodsAdapter.setOnItemClickListener(new TakenGoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG_YIHU, "onItemClick position=" + position);
                OrderInfo orderInfo = list.get(position);
                if (orderInfo.getVendor() != null) {
                    AppConstants.LAST_PICK_CODE = String.valueOf(orderInfo.getVendor().getPickCode());
                }
                mTakenGoodsAdapter.removeItem(orderInfo, position);
                EventBus.getDefault().postSticky(new EventMessage(AppConstants.FLAG_TAKEN_ORDERS, orderInfo));
            }
        });
    }

    private String mOrderNo = "";
    @Subscribe(sticky = true, threadMode = ThreadMode.ASYNC)
    public void doTakenAction(EventMessage event) {
        if (event.getType() == AppConstants.FLAG_TAKEN_ORDERS) {
            AppConstants.LAST_SHIPMENT_LEVEL = "";
            try {
                OrderInfo orderInfo = (OrderInfo) event.getData();
                if (orderInfo.getType() == 100 || orderInfo.getProducts() == null) {
                    return;
                }
                // 拼团和砍价订单, 通过拼团和砍价的id查询详情，得到待出货的产品ID
                PromoteGoodsVO goodsVO = Api.getInstance().getPromoteGoodsDetail(orderInfo.getProducts().get(0).getId());
                PayVO payVO = Api.getInstance().getOrderDetail(goodsVO.getArtworkId());
                Log.d(TAG_YIHU, "doTakenAction payVO=" + payVO);
                if (payVO != null && payVO.getChannelNo() != null) {
                    AppConstants.LAST_SHIPMENT_LEVEL = payVO.getLevel();
                    mOrderNo = orderInfo.getOrderNo();
                    int channelNo  = Integer.parseInt(payVO.getChannelNo());
                    if (mUserVO != null) {
                        String deliveryMsg = String.format("支付成功！请从%s柜拿取货物。", payVO.getLevel().substring(0, 1));
                        mUserVO.setVendorMsg(deliveryMsg);
                    }
                    SdkUtils.getInstance().checkout(payVO.getPlc(), channelNo, channelNo);
                } else {
                    Toast.makeText(TakenGoodsActivity.this, "商品已售罄！请稍候再来！", Toast.LENGTH_SHORT).show();
                }
            } catch (AppException e) {
                Log.e(AppConstants.TAG_YIHU, e.getMessage(), e);
                Toast.makeText(TakenGoodsActivity.this, "网络异常，请稍后再试！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.ASYNC)
    public void deliverySucceed(EventMessage message) {
        if (message.getType() == AppConstants.FLAG_SDK_SUCCESS && !AppConstants.IS_DEVICE_CHECKING) {
            SdkResponse response = (SdkResponse) message.getData();
            Log.d(AppConstants.TAG_YIHU, "TakenGoodsActivity deliverySucceed SDK response=" + response.getMessage());
            if (response.getType() == WMSSendType.SHIPMENTS && response.isSuccess()) {
                String json = String.format("{\"orderNo\": \"%s\"}", mOrderNo);
                try {
                    boolean isSuccess = Api.getInstance().takenSuccess(json);
                    if (isSuccess) {
                        Intent intent = new Intent(TakenGoodsActivity.this, TakenSuccessActivity.class);
                        intent.putExtra(AppConstants.INTENT_TAKEN_DEVICE, mUserVO);
                        startActivity(intent);
                        this.finish();
                    } else {
                        Toast.makeText(TakenGoodsActivity.this, "取货失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                    }
                } catch (AppException e) {
                    Log.e(TAG_YIHU, e.getMessage(), e);
                }
            }
        }
    }

    public void fetchGoodsData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = mHandler.obtainMessage();
                try {
                    message.what = AppConstants.FLAG_GOODS;
                    message.obj = Api.getInstance().getOrderList(mUserVO.getVendorId(), mUserVO.getUserId());
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
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.id_taken_back)
    public void goBack(View view) {
        this.finish();
    }
}
