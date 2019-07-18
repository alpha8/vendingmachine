package com.yihuyixi.vendingmachine.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yihuyixi.vendingmachine.R;

import java.nio.file.attribute.PosixFileAttributes;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KeyboardAdapter extends BaseAdapter {
    private Context mContext;

    public KeyboardAdapter(Context context) {
        this.mContext = context;
    }
    private String[] texts = {"1","2","3","4","5","6","7","8","9","重输","0","回退" };
    private String[] types = {"num","num","num","num","num","num","num","num","num","clear","num","back"};

    public interface OnItemClickListener{
        void onItemClick(String type, String num);
    }
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    @Override
    public int getCount() {
        return texts.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        KeyHolder holder;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.keyboard_item,null);
            holder = new KeyHolder(view);
            holder.nums.setText(texts[position]);
            switch (types[position]) {
                case "cancel":
                    holder.nums.setBackgroundColor(Color.parseColor("#d9d5cf"));
                    break;
                case "ok":
                    holder.nums.setBackgroundColor(Color.parseColor("#faaa18"));
                    holder.nums.setTextColor(Color.WHITE);
                    break;
            }
            view.setTag(holder);
        } else {
            holder = (KeyHolder) view.getTag();
        }
        setItemEvent(holder, position);
        return view;
    }

    protected void setItemEvent(@NonNull final KeyHolder holder, final int pos) {
        if (mOnItemClickListener != null) {
            holder.nums.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String type = types[pos];
                    String num = texts[pos];
                    mOnItemClickListener.onItemClick(type, num);
                }
            });
        }
    }

    public class KeyHolder {
        @BindView(R.id.id_keyboard_num) TextView nums;

        public KeyHolder(@NonNull View view) {
            ButterKnife.bind(this, view);
        }
    }
}
