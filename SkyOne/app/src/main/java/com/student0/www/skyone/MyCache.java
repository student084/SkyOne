package com.student0.www.skyone;

import android.graphics.Bitmap;
import android.util.LruCache;

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

    private static MyCache getInstance() {
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
}
