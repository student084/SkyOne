package com.student0.www.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.student0.www.adapter.DatePhotosAdapter;
import com.student0.www.adapter.PhotosAdapter;
import com.student0.www.bean.PerDatePhotos;
import com.student0.www.skyone.MainActivity;
import com.student0.www.skyone.R;

/**
 * Created by willj on 2017/2/23.
 */

public class DateAndPhotoHolder extends RecyclerView.ViewHolder{

    private PhotosAdapter photosAdapter;
    private TextView tv_date;
    private GridView gv_photos;

    public DateAndPhotoHolder(View itemView) {
        super(itemView);
        tv_date = (TextView)itemView.findViewById(R.id.id_dateAndPhoto_date);
        gv_photos = (GridView)itemView.findViewById(R.id.id_dateAndPhoto_gridView);
    }

    public void bindHolder(PerDatePhotos perDatePhotos, Context context){
        tv_date.setText(perDatePhotos.getData());
        photosAdapter = new PhotosAdapter(context,perDatePhotos.getPhotos());
        gv_photos.setAdapter(photosAdapter);
    }
}
