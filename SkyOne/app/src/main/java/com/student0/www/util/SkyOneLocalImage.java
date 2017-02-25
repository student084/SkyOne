package com.student0.www.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by willj on 2017/2/25.
 */

public class SkyOneLocalImage {


    private static SkyOneLocalImage mInstance;

    /**
     * 图片缓存的核心对象
     * **/

    private LruCache<String, Bitmap> mLruCache;

    /***
     * 有个后台线程，后台线程负责获取任务，并将任务加入线程池，
     * 避免为每一个path生成获取图片的线程而耗费资源
     *线程池建立，用来加载执行任务
     * */
    private ExecutorService mThreadPool;
    private static final int DEFAULT_THREAD_COUNT = 1; //默认线程数
    /**
     * 线程池队列的调度方式
     * */
    private Type mType = Type.LIFO;

    /**
     * 任务列表维护任务
     * 供线程池取出任务
     * LinkedList可以从尾部取也可从头部取
     * LinkedList采用的是链表，在内存中不一定要要求连续，区别于普通的list
     * 普通List要顺序或者指定获取
     */
    private LinkedList<Runnable> mTaskQueue;

    /**
     * 后台的轮询线程
     * 负责获取任务，并将任务加入线程池
     * */
    private Thread mPoolTread;
    /**
     *信号量引入,加锁，防止未初始化的变量进入并被其他线程调用，引起空指针异常
     */

    /**
     * 线程池信号量
     * */
    private Semaphore mSemaphoreThreadPool;

    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
    /**
     * Handler在多线程框架中主要用来发送消息
     * Handler给MessageQueues发送消息
     * */
    private Handler mPoolTreadHandler;
    /**
     * 分析图片UI线程中的Handler
     * 当传入的path成功获取到图像时，通过这个Handler发送成功消息，为图片设置回调，回调显示bitmap
     * 使用SkyOneLocalImage.getInstance.loadImage传入一个path就可返回bitmap,mUIHandler就是这个的核心
     * */
    private Handler mUIHandler;

    //调度类型枚举
    public enum Type{
        FIFO,LIFO;
    }


    /**
     *由于只有建立一个缓存区，SkyOneLocalImage采用单例设计
     *不允许外部建立(new)SkyOneLocalImage实例，所以构造方法为private
     * @param threadCount 指定后台允许的线程数
     * @param type 指定调度类型
     * */
    private SkyOneLocalImage(int threadCount, Type type){
        init(threadCount, type);
    }

    /**
     * 初始化
     * */
    private void init(int threadCount, Type type) {
        /**
         * 后台轮询线程
         * Handler + Looper + Message实现
         * */
        mPoolTread = new Thread(){
            @Override
            public void run() {
                /**
                 * 准备Looper,建立MessageQueues等
                 * */
                Looper.prepare();
                /**
                 * 这里的mPoolTreadHandler的初始化与下文中mPoolTreadHandler理论上是并行的
                 *为了防止下文中出现空指针异常，所以此处需要信号量
                 * */
                mPoolTreadHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        //去线程池取出一个任务进行执行
                        mThreadPool.execute(getTask());
                        try {
                            mSemaphoreThreadPool.acquire();
                        } catch (InterruptedException e) {
                        }
                    }
                };
                //释放一个信号量
                mSemaphorePoolThreadHandler.release();
                Looper.loop();
            }
        };
        /**
         *启动后台轮询线程
         * */
        mPoolTread.start();
        /**
         * 获取应用的最大可用内存
         * */
        int maxMemory = (int)Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory/6;

        mLruCache = new LruCache<String, Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //获取图片的大小
                //每一行的字节数*行高度，所以单位为byte
                return value.getRowBytes() * value.getHeight();
            }
        };
        //创建线程池
        mThreadPool = Executors.newFixedThreadPool(threadCount);

        mTaskQueue = new LinkedList<Runnable>();
        mType = type;
//
        mSemaphoreThreadPool = new Semaphore(threadCount);
    }

    /**
     * 外部通过调用getInstance来获取单个SkyOneLocalImage实例
     * **/
    public static SkyOneLocalImage getInstance(int threadCount, Type type){
        /**
         * 当实力为空时，建立实例，当实例不为空时直接获取，不需进入同步排队直接返回
         * 没有可用实例（初次建立实例）时，
         * 且避免此时多个线程到达getInstance造成建立单个实例的死锁出现，
         * 对SkyOneLocalImage资源进行同步处理（先后排序单个进行，一次执行一个，其他等待）解决死锁
         * 当资源轮到下一个请求run时，再次判断，有则获取，无则建立
         * **/
        if (mInstance == null){
            synchronized (SkyOneLocalImage.class){
                //同步二次处理判断十分必要
                if (mInstance == null){
                    mInstance = new SkyOneLocalImage(threadCount, type);
                }
            }
        }
        return mInstance;
    }

    //turn bitmap 90 degree

    private Bitmap turn90d(Bitmap pic){
        Bitmap bm;
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        bm = Bitmap.createBitmap(pic, 0, 0 ,pic.getWidth(), pic.getHeight(), matrix,true);
        return bm;
    }
    /**
     * 核心方法
     * 根据path为imageView设置图片
     * */

    public void loadImage(final String path, final ImageView imageView){
        /**
         * 回收内存
         * */
//        if (imageView != null && imageView.getDrawable() != null){
//            Bitmap oldBitmap = (Bitmap) imageView.getDrawingCache();
//            imageView.setImageDrawable(null);
//            if (oldBitmap != null){
//                oldBitmap.recycle();
//                oldBitmap = null;
//            }
//        }

        //给控件对象设置Tag,以后可以通过未确定的imageView的getTag找会确定的对象

        //通过Tag的判断可以防止回收机制的错误加载
        imageView.setTag(path);
        if(mUIHandler == null){
            mUIHandler = new Handler(){
                /**
                 * 这里注意handleMessage中的不能直接使用path, imageView变量
                 * 因为此时handleMessage中是靠线程自己获得消息异步处理
                 * handleMessage相对loadImage来说是异步
                 * */
                @Override
                public void handleMessage(Message msg) {
                    //获取得到的图片，为imageView回调设置图片
                    //获取消息，实行强转
                    ImageBeanHolder holder = (ImageBeanHolder)msg.obj;
                    Bitmap bm = holder.bitmap;
                    ImageView imageView = holder.imageView;
                    String path = holder.path;

                    //将path与getTag存储路径进行比较，排除复用的负面干扰
                    if(imageView.getTag().toString().equals(path)){
                        bm = turn90d(bm);
                        imageView.setImageBitmap(bm);
                    }

                }
            };
        }
        //根据path在缓存中获取bitmap
        Bitmap bm = getBitmapFromLruCache(path);
        //判断图片是否获取成功，即是否再缓存中
        if (bm != null){
            //当图片不为空则将消息发送给mUIHandler
            //然后mUIHandler就会获取消息，回掉处理消息
            refreshBitmap(bm, path, imageView);
        }else {
            /**
             * 不在缓存，则新建一个任务
             * */
            addTask(new Runnable() {
                @Override
                public void run() {
                    //加载图片
                    //图片的压缩
                    //1、获得图片布局ImageView的大小
                    SkyOneLocalImage.ImageSize imageSize = getImageViewSize(imageView);
                    //2、获取图片并根据ImageView的要求压缩图片
                    Bitmap bm = decodeSampledBitmapFromPath(path , imageSize.width, imageSize.height);
                    //3、把图片加入到缓存
                    addBitmapToLruCache(path, bm);

                    refreshBitmap(bm, path, imageView);
                    mSemaphoreThreadPool.release();
                }
            });
        }
    }

    private void refreshBitmap(Bitmap bm, String path, ImageView imageView) {
        Message message = Message.obtain();
        //传送图片的holder信息给回调函数
        ImageBeanHolder imageBeanHolder = new ImageBeanHolder();
        imageBeanHolder.bitmap = bm;
        imageBeanHolder.path = path;
        imageBeanHolder.imageView = imageView;
        message.obj = imageBeanHolder;
        mUIHandler.sendMessage(message);
    }

    /**
     * 将图片加入缓存
     *LruCache
     * */
    private void addBitmapToLruCache(String path, Bitmap bm) {
        if(getBitmapFromLruCache(path) == null){
            if (bm != null){
                mLruCache.put(path, bm);
            }
        }
    }

    /**
     * 根据图片需要显示的宽和高对图片进行压缩
     * */

    protected Bitmap decodeSampledBitmapFromPath(String path, int width, int height){

        //获取图片的大小，并且不把图片加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);
        //设置压缩比例，压缩图片
        options.inSampleSize = calculateInSampleSize(options, width, height);
        //使用获得的InSampleSize再次解析图片

        options.inJustDecodeBounds = false;//将图片加载到内存
        Bitmap bitmap = null;
        try{
            bitmap = BitmapFactory.decodeFile(path, options);
        }catch (OutOfMemoryError e){
            //
        }
        return bitmap;
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
     * 根据ImageView获得适当的压缩的宽和高对图片进行压缩，防止内存溢出
     * */
    private SkyOneLocalImage.ImageSize getImageViewSize(ImageView imageView) {
        SkyOneLocalImage.ImageSize imageSize = new SkyOneLocalImage.ImageSize();

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
    private synchronized void addTask(Runnable runnable) {
        mTaskQueue.add(runnable);
        try {
            if(mPoolTreadHandler == null)
                //请求信号量资源，请求不通过则暂时阻塞
                mSemaphorePoolThreadHandler.acquire();
        } catch (InterruptedException e) {
        }
        //通知mPoolTreadHandler回调处理任务，处理mTaskQueue中的任务
        mPoolTreadHandler.sendEmptyMessage(0x010);
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
    //根据path在缓存中获取bitmap
    private Bitmap getBitmapFromLruCache(String path) {
        return mLruCache.get(path);
    }

    private class ImageBeanHolder{
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }
}
