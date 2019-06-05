package com.yihuyixi.vendingmachine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yihuyixi.vendingmachine.R;
import com.yihuyixi.vendingmachine.bean.GoodsType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridViewAdapter extends ArrayAdapter<GridItem> {
    private Context mContext;
    private int layoutResourceId;
    private List<GridItem> mGridData;
    private LayoutInflater mInflater;
    public interface OnItemClickListener{
        void onItemClick(ViewHolder holder, int position);
    }
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public GridViewAdapter(Context context, int resource, List<GridItem> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.layoutResourceId = resource;
        this.mGridData = objects;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setGridData(List<GridItem> mGridData) {
        this.mGridData.clear();
        this.mGridData.addAll(mGridData);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        GridItem item = mGridData.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder(convertView);
            holder.sectionType = item.getSectionType();
            Glide.with(mContext).load(item.getIcon()).centerCrop().into(holder.sectionIcon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setItemEvent(holder, position);
        return convertView;
    }

    protected void setItemEvent(@NonNull final ViewHolder holder, final int pos) {
        if (mOnItemClickListener != null) {
            holder.sectionIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder, pos);
                }
            });
        }
    }

    public class ViewHolder {
        @BindView(R.id.iv_sectionIcon) ImageView sectionIcon;
        GoodsType sectionType;

        public ViewHolder(@NonNull View view) {
            ButterKnife.bind(this, view);
        }

        public void setSectionType(GoodsType sectionType) {
            this.sectionType = sectionType;
        }
        public GoodsType getSectionType() {
            return sectionType;
        }
    }
}
