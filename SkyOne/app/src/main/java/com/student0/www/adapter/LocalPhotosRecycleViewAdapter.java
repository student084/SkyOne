package com.student0.www.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.student0.www.Config;
import com.student0.www.holder.TempPhotoHolder;
import com.student0.www.skyone.AppList;
import com.student0.www.skyone.MyCache;
import com.student0.www.skyone.R;
import com.student0.www.util.SkyOneLocalImage;
import com.student0.www.util.Upload;

import java.util.List;

/**
 * Created by willj on 2017/2/25.
 */

public class LocalPhotosRecycleViewAdapter extends BaseAdapter{


    private List<String> photoNameList;
    private LayoutInflater layoutInflater;
    private String dirPath;

    public LocalPhotosRecycleViewAdapter(Context context){
        dirPath = Config.PHOTOS_DIR;
        photoNameList = MyCache.getPhotosName();
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


        final String s = dirPath + "/" + photoNameList.get(position);
        photoHolder.Uri = s;

        /**
         * A tempPhoto will add it's uri to uploadPhotoList and waiting for post
         * */
        final TempPhotoHolder finalPhotoHolder = photoHolder;
        photoHolder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Add  path
                AppList.select_photos_wait_upload.add(finalPhotoHolder.Uri);
                finalPhotoHolder.imageView.setColorFilter(Color.parseColor("#77000000"));
                Upload.getInstance().addTak(finalPhotoHolder.Uri);
//                MainActivity.refreshTempFragment();
                return false;
            }
        });
        //System.out.println(dirPath + "/" + photoNameList.get(position));
        //
        if (AppList.select_photos_wait_upload.contains(finalPhotoHolder.Uri)){
            finalPhotoHolder.imageView.setColorFilter(Color.parseColor("#77000000"));
        }else {
            finalPhotoHolder.imageView.setColorFilter(Color.parseColor("#00000000"));
        }
        SkyOneLocalImage.getInstance(Config.THREAD_NUMBER_LOAD_LOCAL, SkyOneLocalImage.Type.LIFO).loadImage(dirPath + "/" + photoNameList.get(position), photoHolder.imageView);

        return convertView;
    }


}
