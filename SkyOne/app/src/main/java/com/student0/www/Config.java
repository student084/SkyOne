package com.student0.www;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by willj on 2017/2/23.
 */

public class Config {
    public final static String PHOTOS_DIR = "/sdcard/skyone";
    public final static int THREAD_NUMBER_URL_LOAD= 3;
    public final static int THREAD_NUMBER_LOAD_LOCAL = 1;



    public final static int TEMPS_FRAGMENT_POSITION_IN_VIEWPAGER = 1;

    //Set a cache
    private LruCache<String, Bitmap> localCache;



}
