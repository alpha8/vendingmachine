package com.yihuyixi.vendingmachine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yihuyixi.vendingmachine.R;
import com.yihuyixi.vendingmachine.bean.ProductInfo;
import com.yihuyixi.vendingmachine.constants.AppConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.MyViewHolder> {
    protected LayoutInflater mInflater;
    protected List<ProductInfo> mDatas;
    private Context context;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public SimpleAdapter(Context context, List<ProductInfo> datas) {
        this.context = context;
        if (datas != null) {
            this.mDatas = datas;
        } else {
            this.mDatas = new ArrayList<>();
        }
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = mInflater.inflate(R.layout.item_productview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, int position) {
        ProductInfo product = mDatas.get(position);
        holder.name.setText(product.getName());
        holder.sellPoint.setText(product.getSellpoint());
        holder.price.setText(String.format("¥%s", product.getFormatPrice()));
        holder.sellCount.setText(String.format("(已售%d件)", product.getSellCount()));
        Glide.with(context).load(product.getAvatar()).into(holder.avatar);
        if (product.isSaleoff()) {
            holder.salesOff.setVisibility(View.VISIBLE);
        } else {
            holder.salesOff.setVisibility(View.GONE);
            setItemEvent(holder);
        }
    }

    protected void setItemEvent(@NonNull final MyViewHolder holder) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setDatas(List<ProductInfo> datas) {
        Log.d(AppConstants.TAG_YIHU, "setDatas, size=" + datas.size());
        mDatas.clear();
        mDatas.addAll(datas);
        this.notifyDataSetChanged();
    }

    public void addItem(ProductInfo p, int pos) {
        mDatas.add(p);
        this.notifyItemInserted(pos);
    }
    public void updateItem(ProductInfo p) {
        if (p == null || p.getPid() == null) {
            return;
        }
        for(int i=0; i<mDatas.size(); i++) {
            ProductInfo pi = mDatas.get(i);
            if (pi.getId().equals(p.getPid())) {
                Log.d(AppConstants.TAG_YIHU, "found product=" + p.getPid());
                pi.setStatus(p.getStatus());
                this.notifyItemChanged(i, pi);
                break;
            }
        }
    }

    public List<ProductInfo> getDatas() {
        return mDatas;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.id_avatar) ImageView avatar;
        @BindView(R.id.id_name) TextView name;
        @BindView(R.id.id_sellpoint) TextView sellPoint;
        @BindView(R.id.id_price) TextView price;
        @BindView(R.id.id_count) TextView sellCount;
        @BindView(R.id.id_product_saleoff) ImageView salesOff;

        public MyViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
