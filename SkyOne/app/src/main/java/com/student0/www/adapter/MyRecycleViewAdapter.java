package com.student0.www.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.student0.www.bean.PerDatePhotos;
import com.student0.www.holder.DateAndPhotoHolder;
import com.student0.www.skyone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willj on 2017/2/23.
 */

public class MyRecycleViewAdapter extends RecyclerView.Adapter {

    private LayoutInflater layoutInflater;
    private List<PerDatePhotos> list = new ArrayList<>();
    private Context context;
    public MyRecycleViewAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void AddList(List<PerDatePhotos> datas){
        for (int i = 0; i < datas.size(); i ++){
            list.add(datas.get(i));
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DateAndPhotoHolder(layoutInflater.inflate(R.layout.item_date_and_photos, parent, false));
    }

    //数据匹配
    //根据日期判断，该处会自动调用DateAndPhotoHolder
    //并联系R.layout.item_date_and_photos
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DateAndPhotoHolder)(holder)).bindHolder(list.get(position),context);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
