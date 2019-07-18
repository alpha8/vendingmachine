package com.yihuyixi.vendingmachine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yihuyixi.vendingmachine.R;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceInfoAdapter extends BaseAdapter {
    private LinkedList<DeviceItem> mList;
    private Context mContext;

    public DeviceInfoAdapter(LinkedList<DeviceItem> data, Context context) {
        this.mContext = context;
        this.mList = data;
    }

    @Override
    public int getCount() {
        return mList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.device_list_item,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DeviceItem item = mList.get(position);
        holder.mText.setText(item.getName());
        holder.mValue.setText(item.getValue());
        return convertView;
    }

    public class ViewHolder {
        @BindView(R.id.id_admin_item_name) TextView mText;
        @BindView(R.id.id_admin_item_value) TextView mValue;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public static class DeviceItem {
        private String name;
        private String value;

        public DeviceItem(){}

        public DeviceItem(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "DeviceItem{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
