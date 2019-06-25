package com.yihuyixi.vendingmachine;

import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.yihuyixi.vendingmachine.adapter.ChannelCheckAdapter;
import com.yihuyixi.vendingmachine.bean.ChannelBean;
import com.yihuyixi.vendingmachine.bean.SdkResponse;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.message.EventMessage;
import com.yihuyixi.vendingmachine.sdk.SdkUtils;
import com.yihuyixi.vendingmachine.utils.ChannelUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChannelActivity extends BaseActivity {
    private static final String CHANNEL_TAG = "channel";
    private GridView gridView;
    private Handler mHandler ;
    private List<Map<String, Object>> dataList;
    private List<ChannelBean> cs;
    private ChannelCheckAdapter adapter;
    LayoutInflater inflater;
    int i = 0;
    volatile boolean flag = false;
    volatile boolean isAutoRun = false;
    boolean isBack = false;
    Unbinder mBinder;
    @BindView(R.id.id_channel_num) EditText mChannelNo;
    @BindView(R.id.id_channel_address) EditText mAddress;
    @BindView(R.id.id_channel_btn) Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("channel","onCreate");
        setContentView(R.layout.activity_channel);
        gridView = findViewById(R.id.gridview);
        mBinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        cs = ChannelUtils.getAllChannels2();
        inflater= LayoutInflater.from(this);
        adapter = new ChannelCheckAdapter(this,inflater,cs);
        gridView.setAdapter(adapter);
    }
    public ChannelBean getChannel(List<ChannelBean> cs,String strPosition){
        int position = ChannelUtils.getNo(strPosition);
        int nextPosion = position+1;
        ChannelBean c = cs.get(nextPosion);
        return c;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("channel","onResume");
    }
    public void checkStop(View view){
        flag = true;
    }
    public void checkAuto(View view){
        Log.d("Button", "checkAuto" );
        try{
            isAutoRun = true;
            AppConstants.IS_DEVICE_CHECKING = true;
            Toast.makeText(this,"第"+i+"个串口",Toast.LENGTH_SHORT);
//            WMSerialportManager.setShipments(0, 8, 0, 15 * 1000);
            SdkUtils.getInstance().checkout(8, 0);
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT);
        }
    }
    public void changeResult(){
        Log.d("channelReady", "changeResult" );
        adapter.setCurrentId(i);
        i++;
        adapter.notifyDataSetChanged();
    }
    void initData() {
        cs = ChannelUtils.getAllChannels();
                dataList = new ArrayList<Map<String, Object>>();
        for (ChannelBean c:cs) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("text",c.getChannelName());
            map.put("img",R.mipmap.checkready);
            dataList.add(map);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void deliverySucceed(EventMessage message) {
        if (message.getType() == AppConstants.FLAG_SDK_SUCCESS) {
            if (!isAutoRun) {
                mButton.setEnabled(true);
                return;
            }
            if(flag) {
                return;
            }
            Log.d(CHANNEL_TAG,"enter into next position");
            SdkResponse response = (SdkResponse) message.getData();
            String orderNo = response.getOrderNo();
            String strPosition = orderNo.substring(14,16);
            int curposition = ChannelUtils.getNo(strPosition);
            adapter.setCurrentId(curposition);
            adapter.notifyDataSetChanged();
            if(curposition+1==cs.size())
                return;
            int nextPosion = curposition+1;
            ChannelBean c = cs.get(nextPosion);
            Log.d(CHANNEL_TAG,"curposition："+curposition+" nextPostion:"+nextPosion+" nextChannelId: "+c.getChannelId());
            SdkUtils.getInstance().checkout(c.getChannelId(), c.getPosition());
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void deliveryFailed(EventMessage message) {
        if (message.getType() == AppConstants.FLAG_SDK_FAIL) {
            mButton.setEnabled(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SdkUtils.getInstance().release();
        if (mBinder != null) {
            mBinder.unbind();
        }
        isAutoRun = false;
        AppConstants.IS_DEVICE_CHECKING = false;
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.id_channel_btn)
    public void handleDelivery(View view) {
        String channelNo = mChannelNo.getText().toString();
        String address = mAddress.getText().toString();
        if (address == null || "".equals(address)) {
            address = "0";
        }
        if (channelNo != null && !"".equals(channelNo)) {
            mButton.setEnabled(false);
            AppConstants.IS_DEVICE_CHECKING = true;
            SdkUtils.getInstance().checkout(Integer.parseInt(address), Integer.parseInt(channelNo.toString()), SdkUtils.OrderIdGenerator.getNewOrderId());
        }
    }


    @OnClick(R.id.id_channel_back)
    public void goBack(View view) {
        this.finish();
    }
}
