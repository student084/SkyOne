package com.student0.www.skyone;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.LruCache;
import android.widget.Toast;

import com.student0.www.Config;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by willj on 2017/2/25.
 */

public class MyCache {
    private LruCache<String, Bitmap> cache;
    private static MyCache mInstance;
    private MyCache(int per_for){
        int maxMemory = (int)Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory/per_for;
        cache = new LruCache<String, Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //获取图片的大小
                //每一行的字节数*行高度，所以单位为byte
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    public static MyCache getInstance() {
        if (mInstance == null){
            synchronized (MyCache.class){
                //同步二次处理判断十分必要
                if (mInstance == null){
                    mInstance = new MyCache(6);
                }
            }
        }
        return mInstance;
    }

    public LruCache<String, Bitmap> getCache(){
        return cache;
    }

    private static List<String> photosName;

    private static List<String> initPhotosName(){
        List<String> names= new ArrayList<>();
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return null;
        }
        names = Arrays.asList(new File(Config.PHOTOS_DIR).list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.endsWith(".jpg")
                        || name.endsWith(".jpeg")
                        ||name.endsWith(".png"))
                    return true;
                return false;
            }
        }));
        List<String> result = new ArrayList<>(names);
        return result;
    }

    public static void setListCache(){
        photosName = initPhotosName();
    }

    public static void addPhotoNameToList(String photoName){
        photosName.add(photoName);
    }

    public static void deletePhotoName(String photoName){
        photosName.remove(photoName);
    }

    public static List<String> getPhotosName(){
        return photosName;
    }
}
