package com.yihuyixi.vendingmachine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yihuyixi.vendingmachine.R;
import com.yihuyixi.vendingmachine.bean.OrderInfo;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TakenGoodsAdapter extends RecyclerView.Adapter<TakenGoodsAdapter.TakenViewHolder> {
    protected LayoutInflater mInflater;
    protected List<OrderInfo> mDatas;
    private Context mContext;
    private int mResourceId;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public TakenGoodsAdapter(Context context, int mResourceId, List<OrderInfo> datas) {
        this.mContext = context;
        if (datas != null) {
            this.mDatas = datas;
        } else {
            this.mDatas = new ArrayList<>();
        }
        this.mInflater = LayoutInflater.from(context);
        this.mResourceId = mResourceId;
    }

    @NonNull
    @Override
    public TakenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = mInflater.inflate(mResourceId, parent, false);
        return new TakenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull TakenViewHolder holder, int position) {
        OrderInfo orderInfo = mDatas.get(position);
        holder.name.setText(orderInfo.getTitle());
        if (orderInfo.getExpress() != null) {
            holder.code.setText(String.format("用户名：%s", orderInfo.getExpress().getReceiver()));
        }
        holder.createAt.setText(String.format("下单时间: %s", Utils.getFormatDate(orderInfo.getCreateAt())));
        if (orderInfo.getProducts() != null && !orderInfo.getProducts().isEmpty()) {
            Glide.with(mContext)
                    .load(Utils.getPictureServerUrl(orderInfo.getProducts().get(0).getIcon()))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).dontAnimate()
                    .into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    if (resource != null) {
                        holder.icon.setImageDrawable(resource);
                    }
                }
            });
        }
        setItemEvent(holder);
    }

    protected void setItemEvent(@NonNull final TakenViewHolder holder) {
        if (mOnItemClickListener != null) {
            holder.btnDeposit.setOnClickListener(new View.OnClickListener() {
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

    public void setDatas(List<OrderInfo> datas) {
        Log.d(AppConstants.TAG_YIHU, "setDatas, size=" + datas.size());
        mDatas.clear();
        mDatas.addAll(datas);
        this.notifyDataSetChanged();
    }

    public void removeItem(OrderInfo p, int pos) {
        mDatas.remove(pos);
        this.notifyItemRemoved(pos);
    }

    public List<OrderInfo> getDatas() {
        return mDatas;
    }

    class TakenViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.id_takengoods_icon) ImageView icon;
        @BindView(R.id.id_takengoods_name) TextView name;
        @BindView(R.id.id_takengoods_code) TextView code;
        @BindView(R.id.id_takengoods_time) TextView createAt;
        @BindView(R.id.id_takengoods_btn)  TextView btnDeposit;

        public TakenViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
