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

public class DatePhotosAdapter extends RecyclerView.Adapter {

    private LayoutInflater layoutInflater;
    private List<PerDatePhotos> list = new ArrayList<>();
    private Context context;
    public DatePhotosAdapter(Context context, List<PerDatePhotos> list) {
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DateAndPhotoHolder(layoutInflater.inflate(R.layout.item_date_and_photos, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DateAndPhotoHolder)(holder)).bindHolder(list.get(position),context);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
