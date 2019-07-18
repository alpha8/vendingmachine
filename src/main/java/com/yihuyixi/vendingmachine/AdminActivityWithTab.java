package com.yihuyixi.vendingmachine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.WindowDecorActionBar;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.yihuyixi.vendingmachine.fragment.TabFragment;
import com.yihuyixi.vendingmachine.view.TabView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AdminActivityWithTab extends BaseActivity {
    @BindView(R.id.vp_pager) ViewPager mViewPager;
    @BindView(R.id.tab_device_info) TabView mBtnDeviceInfo;
    @BindView(R.id.tab_net_check)  TabView mBtnNetCheck;
    @BindView(R.id.tab_channel_check)  TabView mBtnChannelCheck;

    private List<String> mTitles = Arrays.asList("设备信息", "网络检测", "货道检测");
    private Unbinder mUnbinder;
    private SparseArray<TabFragment> mFragments = new SparseArray<>();
    private List<TabView> mTabs = new ArrayList<>();
    private static final String BUNDLE_TAB_INDEX = "BUNDLE_TAB_INDEX";
    private int mCurrentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tab);
        mUnbinder = ButterKnife.bind(this);

        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(BUNDLE_TAB_INDEX, 0);
        }

        initViews();
        initViewPagerAdapter();
        initEvents();
    }

    private void initEvents() {
        for (int i = 0; i < mTabs.size(); i++) {
            TabView tabView = mTabs.get(i);
            final int offset = i;
            tabView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(offset, false);
                    setCurrentTab(offset);
                }
            });
        }
    }

    private void initViewPagerAdapter() {
        mViewPager.setOffscreenPageLimit(mTitles.size());
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return TabFragment.newInstance(mTitles.get(i));
            }

            @Override
            public int getCount() {
                return mTitles.size();
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                TabFragment fragment = (TabFragment) super.instantiateItem(container, position);
                mFragments.put(position, fragment);
                return fragment;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                mFragments.remove(position);
                super.destroyItem(container, position, object);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 左->右  0->1, left pos, right pos + 1, positionOffset 0~1
                if (positionOffset > 0) {
                    TabView left = mTabs.get(position);
                    TabView right = mTabs.get(position + 1);
                    left.setProgress(1 - positionOffset);
                    right.setProgress(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initViews() {
        mTabs.add(mBtnDeviceInfo);
        mTabs.add(mBtnNetCheck);
        mTabs.add(mBtnChannelCheck);

        mBtnDeviceInfo.setIconAndText(R.drawable.aim, R.drawable.ail, "设备信息");
        mBtnNetCheck.setIconAndText(R.drawable.aiq, R.drawable.aip, "网络检测");
        mBtnChannelCheck.setIconAndText(R.drawable.anw, R.drawable.anx, "货道检测");
        setCurrentTab(mCurrentIndex);
    }

    private void setCurrentTab(int pos) {
        for (int i = 0; i < mTabs.size(); i++) {
            TabView tabView = mTabs.get(i);
            if (i == pos) {
                tabView.setProgress(1);
            } else {
                tabView.setProgress(0);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_TAB_INDEX, mViewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
