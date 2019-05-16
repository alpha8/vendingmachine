package com.yihuyixi.vendingmachine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yihuyixi.vendingmachine.R;

import java.util.ArrayList;
import java.util.List;

public class StaggeredAdapter extends SimpleAdapter {
    private List<Integer> mHeights;

    public StaggeredAdapter(Context context, List<String> datas) {
        super(context, datas);

        this.mHeights = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            mHeights.add((int) (100 + Math.random() * 300));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = mHeights.get(position);
        holder.itemView.setLayoutParams(params);
        holder.tv.setText(mDatas.get(position));

        setItemEvent(holder);
    }
}
