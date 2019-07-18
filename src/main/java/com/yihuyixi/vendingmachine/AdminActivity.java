package com.yihuyixi.vendingmachine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.yihuyixi.vendingmachine.adapter.DeviceInfoAdapter;
import com.yihuyixi.vendingmachine.utils.AudioMngHelper;
import com.yihuyixi.vendingmachine.utils.DeviceUtils;
import com.yihuyixi.vendingmachine.utils.NetUtils;
import com.yihuyixi.vendingmachine.utils.Utils;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AdminActivity extends BaseActivity {
    @BindView(R.id.id_admin_listview) ListView mListView;
    Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_ui);
        mUnbinder = ButterKnife.bind(this);
        initListViewAdapter();
    }

    private void initListViewAdapter() {
        Context context = this.getApplicationContext();
        LinkedList<DeviceInfoAdapter.DeviceItem> data = new LinkedList<>();
        data.add(new DeviceInfoAdapter.DeviceItem("生产厂商:", "深圳市一虎一席艺术有限公司"));
        data.add(new DeviceInfoAdapter.DeviceItem("设备型号:", "茶美自动售货机"));
        data.add(new DeviceInfoAdapter.DeviceItem("软件版本:", "V" + DeviceUtils.getVersionName(context)));
        data.add(new DeviceInfoAdapter.DeviceItem("版本编号:", DeviceUtils.getVersionCode(context)));
        NetUtils.NetworkType networkType = NetUtils.getNetworkState(context);
        String netstatus = "已连接有线网络";
        if (networkType == NetUtils.NetworkType.wifi) {
            netstatus = "已连接上Wifi";
        } else if(networkType == NetUtils.NetworkType.mobile) {
            netstatus = "已连接上4G网络";
        }
        data.add(new DeviceInfoAdapter.DeviceItem("网络状态:", DeviceUtils.isNetworkAvailable(context) ? netstatus : "无网络连接"));
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        data.add(new DeviceInfoAdapter.DeviceItem("屏幕尺寸:", String.format("%s * %s   ratio=%s", metrics.widthPixels, metrics.heightPixels, metrics.density)));
        data.add(new DeviceInfoAdapter.DeviceItem("进程号:", String.valueOf(DeviceUtils.getAppProcessId())));
        data.add(new DeviceInfoAdapter.DeviceItem("SD卡:", DeviceUtils.hasSdCard() ? "有" : "无"));
        data.add(new DeviceInfoAdapter.DeviceItem("IMEI:", Utils.getDeviceId(context)));

        //获取系统的Audio管理者
        AudioMngHelper audioMngHelper = new AudioMngHelper(context).setFlag(AudioMngHelper.FLAG_PLAY_SOUND);
        data.add(new DeviceInfoAdapter.DeviceItem("音量:", String.valueOf(audioMngHelper.get100CurrentVolume())));

        DeviceInfoAdapter adapter = new DeviceInfoAdapter(data, context);
        mListView.setAdapter(adapter);
        View footer = LayoutInflater.from(context).inflate(R.layout.admin_toolbar, null);
        mListView.addFooterView(footer);

        Button addVolume = footer.findViewById(R.id.id_add_volume);
        Button reduceVolume = footer.findViewById(R.id.id_reduce_volume);
        Button muteVolume = footer.findViewById(R.id.id_mute_volume);
        Button goHome = footer.findViewById(R.id.id_go_home);
        Button toggleWifi = footer.findViewById(R.id.id_disable_wifi);
        boolean wifiStatus = NetUtils.getWifiState(context);
        toggleWifi.setText(wifiStatus ?  "禁用wifi" : "启用wifi");
        addVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newValue = audioMngHelper.addVoice100();
                data.getLast().setValue(String.valueOf(newValue));
                adapter.notifyDataSetChanged();
            }
        });
        reduceVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newValue = audioMngHelper.subVoice100();
                data.getLast().setValue(String.valueOf(newValue));
                adapter.notifyDataSetChanged();
            }
        });
        muteVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newValue = audioMngHelper.setVoice100(0);
                data.getLast().setValue(String.valueOf(newValue));
                adapter.notifyDataSetChanged();
            }
        });
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);// "android.intent.action.MAIN"
                intent.addCategory(Intent.CATEGORY_HOME); //"android.intent.category.HOME"
                startActivity(intent);
            }
        });
        toggleWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean enabled = NetUtils.toggleWifiState(context);
                toggleWifi.setText(enabled ? "禁用wifi" : "启用wifi");
            }
        });
    }

    @OnClick(R.id.id_admin_channel)
    public void enterChannelCheck(View view) {
        startActivity(new Intent(AdminActivity.this, ChannelActivity.class));
    }

    @OnClick(R.id.id_admin_back)
    public void goBack(View view) {
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
