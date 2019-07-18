package com.yihuyixi.vendingmachine.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yihuyixi.vendingmachine.R;

public class TabView extends FrameLayout {
    private ImageView mIcon;
    private ImageView mIconSelect;
    private TextView mTitle;

    private static final int COLOR_DEFAULT = Color.parseColor("#ff000000");
    private static final int COLOR_SELECT = Color.parseColor("#FF45C01A");

    public TabView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.tab_view, this);

        mIcon = findViewById(R.id.id_tab_icon);
        mIconSelect = findViewById(R.id.id_tab_icon_select);
        mTitle = findViewById(R.id.id_tab_title);

        setProgress(0);
    }

    public void setProgress(float progress) {
        mIcon.setAlpha(1 - progress);
        mIconSelect.setAlpha(progress);
        mTitle.setTextColor(evaluate(progress, COLOR_DEFAULT, COLOR_SELECT));
    }

    public void setIconAndText(int icon, int iconSelect, String text) {
        mIcon.setImageResource(icon);
        mIconSelect.setImageResource(iconSelect);
        mTitle.setText(text);
    }

    public int evaluate(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >>  8) & 0xff;
        int startB =  startInt        & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >>  8) & 0xff;
        int endB =  endInt        & 0xff;

        return (int)((startA + (int)(fraction * (endA - startA))) << 24) |
                (int)((startR) + (int)(fraction * (endR - startR))) << 16 |
                (int)((startG) + (int)(fraction * (endG - startG))) << 8 |
                (int)((startB) + (int)(fraction * (endB - startB)));
    }
}
