package com.student0.www.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.student0.www.bean.Photo;
import com.student0.www.holder.PhotoHolder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by willj on 2017/2/24.
 */

public class ImageLoader {
    //Make sure there only one Cache
    private static ImageLoader mInstance;

    //Set a thread to run the listen and run the task in the threadPool
    private Thread mPoolThread;
    /**
     * set a pool for loading task
     * */
    private ExecutorService mThreadPool;

    private Handler mPoolTreadHandler;
    //Set a cache
    private LruCache<String, Bitmap> localCache;
    /**
     * 任务列表维护任务
     * 供线程池取出任务
     * LinkedList可以从尾部取也可从头部取
     * LinkedList采用的是链表，在内存中不一定要要求连续，区别于普通的list
     * 普通List要顺序或者指定获取
     */
    private LinkedList<Runnable> mTaskQueue;
    /**
     * 线程池信号量
     * */
    private Semaphore mSemaphoreThreadPool;

    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
    /**
     * 分析图片UI线程中的Handler
     * 当传入的path成功获取到图像时，通过这个Handler发送成功消息，为图片设置回调，回调显示bitmap
     * 使用ImageLoader.getInstance.loadImage传入一个path就可返回bitmap,mUIHandler就是这个的核心
     * */
    private Handler mUIHandler;


    /**
     * 线程池队列的调度方式
     * */
    private Type mType = Type.LIFO;

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
        //创建线程池
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        //run a listen thread to deal with the threadPool's message
        //in background
        mPoolThread = new Thread(){
            @Override
            public void run() {
                //prepare loop, at the same time the MessageQueue auto-create
                Looper.prepare();
                mPoolTreadHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        //wait until the mThreadPool inited
                        try {
                            mSemaphoreThreadPool.acquire();
                        } catch (InterruptedException e) {
                        }
                        //take a task from pool
                        mThreadPool.execute(getTask());
                    }
                };
                //release a semaphore
                mSemaphorePoolThreadHandler.release();
                Looper.loop();
            }
        };
        mPoolThread.start();// run the loop thread forever


        mTaskQueue = new LinkedList<Runnable>();
        mType = type;
        //A sign : ThreadPool create finished
        mSemaphoreThreadPool = new Semaphore(threadCount);

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
     * 从任务队列mTaskQueue中获取一个任务
     * */
    private Runnable getTask(){
        if (mType == Type.FIFO){
            return mTaskQueue.removeFirst();
        }else if(mType == Type.LIFO){
            return mTaskQueue.removeLast();
        }
        return null;
    }
    /**
     * Set ImageView's Bitmap by URL
     *
     * */
    public void loadImages(final String url,final ImageView imageView){
        //set a tag for imageView avoid pic find error land
        imageView.setTag(url);

        //UIThread create

        if(mUIHandler == null){
            mUIHandler = new Handler(){
                /**
                 * 这里注意handleMessage中的不能直接使用path, imageView变量
                 * 因为此时handleMessage中是靠线程自己获得消息异步处理
                 * handleMessage相对loadImage来说是异步
                 * */
                @Override
                public void handleMessage(Message msg) {
                    PhotoHolder holder = (PhotoHolder)msg.obj;
                    Bitmap bitmap = holder.bitmap;
                    ImageView imageView = holder.imageView;
                    String url = holder.url;
                    //将path与getTag存储路径进行比较，排除复用的负面干扰
                    //图片刷新
                    if(imageView.getTag().toString().equals(url)){
                        imageView.setImageBitmap(bitmap);
                    }
                }
            };
        }
        //Search bitmap from localCache at first
        Bitmap bm = getBitmapFromLruCache(url);

        if ( bm != null){
            //if finding a bitmap,send a message to UIThread,
            //  so the UIThread can refresh photo
            refreshBitmap(bm , url, imageView);
        }else{
            // if photos not exit an cache
            addTask(new Runnable() {
                @Override
                public void run() {
                    //加载图片
                    //图片的压缩
                    //1、获得图片布局ImageView的大小
                    ImageSize imageSize = getImageViewSize(imageView);
                    //2、获取图片并根据ImageView的要求压缩图片
                    //Bitmap bm = decodeSampledBitmapFromPath(url , imageSize.width, imageSize.height);
                    Bitmap bm = getBitmapFromURL(url, imageSize.width, imageSize.height);
                    //3、把图片加入到缓存
                    addBitmapToLruCache(url, bm);

                    refreshBitmap(bm, url, imageView);
                    mSemaphoreThreadPool.release();
                }
            });
        }
    }

    //get photos and zip
    public Bitmap getBitmapFromURL(String urlString, int width, int height){
        InputStream is = null;
        URL url = null;
        try {
            url = new URL(urlString);
            try {
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                is = new BufferedInputStream(connection.getInputStream());
//                Bitmap internetBitmap = BitmapFactory.decodeStream(is);
//                //获取图片的大小，并且不把图片加载到内存中
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                BitmapFactory.decodeStream(is);
//                //设置压缩比例，压缩图片
//                options.inSampleSize = calculateInSampleSize(options, width, height);
//                //使用获得的InSampleSize再次解析图片
//
//                options.inJustDecodeBounds = false;//将图片加载到内存
                Bitmap bitmap = null;
                try{
                    bitmap = BitmapFactory.decodeStream(is);;
                }catch (OutOfMemoryError e){
                    //
                }
                connection.disconnect();
                return  bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }  finally {
                if (is != null){
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据需求的宽和高以及图片实际的宽和高计算SampleSize
     * */

    private int calculateInSampleSize(BitmapFactory.Options options, int reWidth, int reHeight) {

        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;

        if(width > reWidth || height >reHeight){
            int widthRadio = Math.round(width*1.0f/reWidth);
            int heightRadio = Math.round(height*1.0f/reHeight);

            //inSampleSize = Math.max(widthRadio, heightRadio);
            //为使图片不失真，取小值，压缩程度较低
            inSampleSize = Math.min(widthRadio, heightRadio);
        }

        return inSampleSize;
    }


    /**
     * 将图片加入缓存
     *LruCache
     * */
    private void addBitmapToLruCache(String path, Bitmap bm) {
        if(getBitmapFromLruCache(path) == null){
            if (bm != null){
                localCache.put(path, bm);
            }
        }
    }

    /**
     * 根据ImageView获得适当的压缩的宽和高对图片进行压缩，防止内存溢出
     * */
    private ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();

        //获取屏幕的宽度
        /**
         * 判断并获取实际的
         * 判断并获取布局中的
         * 判断并获取最大的
         * 上述都没设置则压缩为最大屏幕的
         * */
        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();

        ViewGroup.LayoutParams lp = imageView.getLayoutParams();

        //压缩宽度
        int width = imageView.getWidth();
        if (width <= 0){
            width = lp.width; //获得imageView再layout中声明的宽度
        }
        if (width <= 0){
            width = imageView.getMaxWidth();//检查最大值
        }
        if(width <= 0){
            width = displayMetrics.widthPixels;
        }
        //压缩高度
        int height = imageView.getHeight();
        if (height <= 0){
            height = lp.height; //获得imageView再layout中声明的宽度
        }
        if (height <= 0){
            height = imageView.getMaxHeight();//检查最大值
        }
        if(height <= 0){
            height = displayMetrics.heightPixels;
        }


        imageSize.height = height;
        imageSize.width = width;
        return imageSize;
    }

    private class ImageSize{
        int width;
        int height;
    }
    /**
     * 缓存中无法获取图片时
     * 建立新的任务，将新的任务放入任务队列mTaskQueue中
     * 然后通知后台线程
     * 资源请求，同步addTask中的线程，防止多请求的死锁
     * */
    private synchronized void addTask(Runnable task) {
        mTaskQueue.add(task);
        try {
            if(mPoolTreadHandler == null)
                //请求信号量资源，请求不通过则暂时阻塞
                mSemaphorePoolThreadHandler.acquire();
        } catch (InterruptedException e) {
        }
        //通知mPoolTreadHandler回调处理任务，处理mTaskQueue中的任务
        mPoolTreadHandler.sendEmptyMessage(0x010);
    }

    //send message to UIThread, auto-callback refresh view in UIHandler-HandleMessage
    private void refreshBitmap(final Bitmap bm, String url, final ImageView imageView) {
        Message message = Message.obtain();

        PhotoHolder photoHolder = new PhotoHolder(imageView);
        photoHolder.imageView = imageView;
        photoHolder.bitmap = bm;
        photoHolder.url = url;

        message.obj = photoHolder;
        mUIHandler.sendMessage(message);

    }


    //return a bitmap from cache if it's exist in cache
    //find by url
    private Bitmap getBitmapFromLruCache(String url) {
        return localCache.get(url);
    }

}
