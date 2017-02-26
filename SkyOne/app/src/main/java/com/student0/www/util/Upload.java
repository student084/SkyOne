package com.student0.www.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import com.student0.www.Config;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by willj on 2017/2/26.
 */

public class Upload {

    private static Upload mInstance;

    private ExecutorService threadPool = Executors.newFixedThreadPool(1);

    private Thread loopThread;
    private Handler loopThreadHandle;

    private LinkedList<Runnable> taskList = new LinkedList<>();

    private Semaphore loopThreadHandleSemaphore = new Semaphore(0);

    private Upload(){
        initData();
    }

    private void initData() {

        //start the loopThread
        loopThread = new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                loopThreadHandle = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        threadPool.execute(getTask());
                    }
                };
                loopThreadHandleSemaphore.release();
                Looper.loop();
            }
        };

        loopThread.start();
    }

    private Runnable getTask() {
        return taskList.removeFirst();
    }

    public static Upload getInstance(){
        if (mInstance == null){
            synchronized (Upload.class){
                if (mInstance == null){
                    mInstance = new Upload();
                }
            }
        }
        return mInstance;
    }


    public void addTak(final String Uri){
        taskList.add(new Runnable() {
            @Override
            public void run() {
                //upLoad a new Image
                postPic(Uri);
            }
        });
        if (loopThreadHandle == null)
        try {
            loopThreadHandleSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loopThreadHandle.sendEmptyMessage(0x112);
    }
    public static final MediaType MEDIA_TYPE_PNG =
            MediaType.parse("image/png");

    private final OkHttpClient client = new OkHttpClient();
    private Boolean postPic(String uri) {
        File file = new File(uri);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("batch", "1")
                .addFormDataPart("image", uri,
                        RequestBody.create(MEDIA_TYPE_PNG, file))
                .build();
        System.out.println(requestBody.toString());
        Request request = new Request.Builder()
                .url(Config.UPLOAD_PHOTO_URL)
                .post(requestBody)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null) {
            return true;
        };
        //System.out.println(response.body().toString());

        return false;
    }
}
