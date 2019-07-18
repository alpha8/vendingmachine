package com.yihuyixi.vendingmachine.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yihuyixi.vendingmachine.R;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.utils.Utils;
import com.yihuyixi.vendingmachine.vo.ExtGoods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    protected LayoutInflater mInflater;
    protected List<ExtGoods> mDatas;
    private Context context;
    private int layoutResourceId;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public ListAdapter(Context context, int layoutResourceId, List<ExtGoods> datas) {
        this.context = context;
        if (datas != null) {
            this.mDatas = datas;
        } else {
            this.mDatas = new ArrayList<>();
        }
        this.mInflater = LayoutInflater.from(context);
        this.layoutResourceId = layoutResourceId;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = mInflater.inflate(layoutResourceId, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ExtGoods product = mDatas.get(position);
        holder.name.setText(product.getName());
        holder.sellPoint.setText(product.getSellPoint());
        holder.price.setText(String.format("¥%s", Utils.getFormatPrice(product.getButtomFee())));
        holder.marketPrice.setText(String.format("¥%s", Utils.getFormatPrice(product.getFieldPrice())));
        holder.marketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        String tips = "1人";
        if (product.getType() == 2) {
            tips = product.getLimitCount() + "人拼团";
        } else {
            int persons = (int)Math.floor((product.getFieldPrice() - product.getButtomFee()) / product.getForwardFee());
            if (persons > 1) {
                tips = "1-" + persons + "人";
            }
        }
        holder.tips.setText(tips);
        Glide.with(context)
                .load(Utils.getPictureServerUrl(product.getIcon()))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).dontAnimate()
                .into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                if (resource != null) {
                    holder.avatar.setImageDrawable(resource);
                }
            }
        });
        setItemEvent(holder);
    }

    protected void setItemEvent(@NonNull final ListViewHolder holder) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setDatas(List<ExtGoods> datas) {
        Log.d(AppConstants.TAG_YIHU, "setDatas, size=" + datas.size());
        mDatas.clear();
        mDatas.addAll(datas);
        this.notifyDataSetChanged();
    }

    public List<ExtGoods> getDatas() {
        return mDatas;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.id_item_avatar) ImageView avatar;
        @BindView(R.id.id_item_name) TextView name;
        @BindView(R.id.id_item_sellpoint) TextView sellPoint;
        @BindView(R.id.id_item_price) TextView price;
        @BindView(R.id.id_item_marketPrice) TextView marketPrice;
        @BindView(R.id.id_item_tips) TextView tips;

        public ListViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
