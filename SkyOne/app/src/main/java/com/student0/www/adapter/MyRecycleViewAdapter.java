package com.student0.www.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.student0.www.bean.PerDatePhotos;
import com.student0.www.bean.Photo;
import com.student0.www.holder.DateHolder;
import com.student0.www.holder.PhotoHolder;
import com.student0.www.skyone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willj on 2017/2/23.
 */

public class MyRecycleViewAdapter extends RecyclerView.Adapter {

    private LayoutInflater layoutInflater;

    private List<PerDatePhotos> list = new ArrayList<>();
    private List<DataForm> dataFormList = new ArrayList<>();

    public final static int TYPE_DATE = 1;
    public final static int TYPE_PHOTO = 2;

    //DataForm's Position in dataFormList
    private static int position;
    public MyRecycleViewAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    //Each time List<PerDatePhotos> be added ,all of the added part will
    //      be formed as a List<DataForm>
    public void AddList(List<PerDatePhotos> datas){

        for (int i = 0; i < datas.size(); i ++){

            dataFormList.add(new DataForm(datas.get(i).getDate(), position ++, TYPE_DATE));

            List<Photo> photos = datas.get(i).getPhotos();
            for (int j = 0; j < photos.size(); j ++){

            dataFormList.add(new DataForm(photos.get(j).getResourceURL(),
                    position ++, TYPE_PHOTO));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return dataFormList.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_DATE:
                return new DateHolder(layoutInflater.inflate(R.layout.item_date, parent, false));
            default:
                return new PhotoHolder(layoutInflater.inflate(R.layout.item_photo, parent, false));
        }
    }

    //数据匹配
    //根据日期判断，该处会自动调用DateAndPhotoHolder
    //并联系R.layout.item_date_and_photos
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType){
            case TYPE_DATE:
                ((DateHolder)(holder)).bindHolder(dataFormList.get(position));
                break;
            default:
                ((PhotoHolder)(holder)).bindHolder(dataFormList.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataFormList.size();
    }
}
