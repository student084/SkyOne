package com.student0.www.util;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.widget.ImageView;

/**
 * Created by willj on 2017/2/24.
 */

public class ImageLoader {
    //Make sure there only one Cache
    private static ImageLoader mInstance;

    private Handler mPoolTreadHandler;
    //Set a cache
    private LruCache<String, Bitmap> localCache;
    /**
     *由于只有建立一个缓存区，ImageLoader采用单例设计
     *不允许外部建立(new)ImageLoader实例，所以构造方法为private
     * @param threadCount 指定后台允许的线程数
     * @param type 指定调度类型
     * */
    private ImageLoader(int threadCount, Type type) {
        //Create a cache
        int maxMemory = (int)Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory/6;
        //init cache
        localCache = new LruCache<String, Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //获取图片的大小
                //每一行的字节数*行高度，所以单位为byte
                return value.getRowBytes() * value.getHeight();
            }
        };

        //Prepare a Looper, and the MessageQueue will be auto-create when Looper prepare
        Looper.loop();

    }

    public static ImageLoader getInstance(int threadCount, Type type){
        /**
         * 当实力为空时，建立实例，当实例不为空时直接获取，不需进入同步排队直接返回
         * 没有可用实例（初次建立实例）时，
         * 且避免此时多个线程到达getInstance造成建立单个实例的死锁出现，
         * 对ImageLoader资源进行同步处理（先后排序单个进行，一次执行一个，其他等待）解决死锁
         * 当资源轮到下一个请求run时，再次判断，有则获取，无则建立
         * **/
        if (mInstance == null){
            synchronized (ImageLoader.class){
                //同步二次处理判断十分必要
                if (mInstance == null){
                    mInstance = new ImageLoader(threadCount, type);
                }
            }
        }
        return mInstance;
    }

    /**
     * Set ImageView's Bitmap by URL
     *
     * */
    public void loadImages(final String url,final ImageView imageView){

    }

}
