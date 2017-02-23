package com.student0.www.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.student0.www.bean.Photo;
import com.student0.www.holder.PhotoHolder;
import com.student0.www.skyone.R;

import java.util.List;

/**
 * Created by willj on 2017/2/23.
 */

public class PhotosAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<Photo> photos;

    public PhotosAdapter(Context context, List<Photo> photos) {
        this.layoutInflater = LayoutInflater.from(context);
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoHolder photoHolder = null;
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_gridview_photo, parent, false);
            photoHolder = new PhotoHolder();
            photoHolder.imageView = (ImageView)convertView.findViewById(R.id.id_gridview_item_image);
            convertView.setTag(photoHolder);
        }else{
            photoHolder = (PhotoHolder)convertView.getTag();
        }
        /****/
        /***
         *
         * 此处加载photoHolder应有的图片
         * R.layout.item_gridview_photo
         * */
        /****/
        photoHolder.imageView.setImageResource(R.mipmap.ic_launcher);
        return convertView;
    }
}
