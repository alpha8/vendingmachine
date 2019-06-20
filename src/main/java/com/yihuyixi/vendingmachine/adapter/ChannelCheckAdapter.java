package com.yihuyixi.vendingmachine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yihuyixi.vendingmachine.R;
import com.yihuyixi.vendingmachine.bean.ChannelBean;

import java.util.List;

public class ChannelCheckAdapter extends BaseAdapter {
    private Context context=null;
    private List<ChannelBean> data = null;
    private LayoutInflater inflater;
    int currentId = -1;
    //构造方法
    public ChannelCheckAdapter(Context context,LayoutInflater  inflater, List<ChannelBean> data) {
        this.context = context;
        this.inflater = inflater;
        this.data = data;
    }

    public int getCurrentId() {
        return currentId;
    }

    public void setCurrentId(int currentId) {
        this.currentId = currentId;
    }

    private class Holder{
        ImageView img;
        TextView text;

        public ImageView getImg() {
            return img;
        }

        public void setImg(ImageView img) {
            this.img = img;
        }

        public TextView getText() {
            return text;
        }

        public void setText(TextView text) {
            this.text = text;
        }
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Holder holder;
        if(view==null){
            view=inflater.inflate(R.layout.channelview_item,null);
            holder=new Holder();
            holder.img=(ImageView)view.findViewById(R.id.img);
            holder.text=(TextView)view.findViewById(R.id.text);
            view.setTag(holder);
        }else{
            holder=(Holder) view.getTag();
        }
        holder.text.setText(data.get(position).getChannelName());
        if(currentId==position){
            data.get(position).setChecked(true);
        }
        if(data.get(position).isChecked()){
            holder.img.setImageResource(R.mipmap.success);
        }else{
            holder.img.setImageResource(R.mipmap.checkready);
        }
        return view;
    }
}
