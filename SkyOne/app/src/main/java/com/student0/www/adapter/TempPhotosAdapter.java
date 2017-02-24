package com.student0.www.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.student0.www.Config;
import com.student0.www.holder.TempPhotoHolder;
import com.student0.www.skyone.R;
import com.student0.www.util.ImageLoader;
import com.student0.www.util.SkyOneLocalImage;

import java.util.List;

/**
 * Created by willj on 2017/2/25.
 */

public class TempPhotosAdapter extends BaseAdapter{


    private List<String> photoNameList;
    private LayoutInflater layoutInflater;
    private String dirPath;

    public TempPhotosAdapter(Context context, List<String> datas, String dir){
        dirPath = dir;
        photoNameList = datas;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return photoNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TempPhotoHolder photoHolder = null;
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_temp_photo, parent, false);
            photoHolder = new TempPhotoHolder();
            photoHolder.imageView = (ImageView)convertView.findViewById(R.id.id_item_temp_photo);
            convertView.setTag(photoHolder);
        }else{
            photoHolder = (TempPhotoHolder)convertView.getTag();
        }
        String s = dirPath + "/" + photoNameList.get(position);
        System.out.println(dirPath + "/" + photoNameList.get(position));
        SkyOneLocalImage.getInstance(3, SkyOneLocalImage.Type.LIFO).loadImage(dirPath + "/" + photoNameList.get(position), photoHolder.imageView);
        return convertView;
    }
}
