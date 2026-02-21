package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class TypingAdapter extends BaseAdapter {

    private Context context;
    private List<ListData> list_data;

    public TypingAdapter(Context context, List<ListData> list) {
        this.context = context;
        this.list_data = list;
    }

    // アダプターが管理するアイテムの数や，指定された位置のアイテムを返す．
    @Override
    public int getCount() {
        return list_data.size();
    }

    @Override
    public ListData getItem(int position) {
        return list_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //指定された位置のアイテムのビューを返す
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        ListData list = getItem(position);
        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);
        text1.setText(list.getTextData_str());
        text2.setText(list.getTextData_len());

        return convertView;
    }
}

