package com.student0.www.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.student0.www.Config;
import com.student0.www.skyone.AppList;
import com.student0.www.skyone.MyCache;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by willj on 2017/2/25.
 */

public class UpLoadPhotos {

    private static UpLoadPhotos mInstance;

    private Thread uploadThread;
    private Semaphore semaphoreUploadThread = new Semaphore(1);
    private Handler uploadHandler;


    private UpLoadPhotos(){
        uploadThread = new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                uploadHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        while (AppList.select_photos_wait_upload.size() > 0){
                            try {
                                semaphoreUploadThread.acquire();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if(upload(AppList.select_photos_wait_upload.get(0))){
                            //Remove the photo from upload list
                            MyCache.deletePhotoName(AppList.select_photos_wait_upload.get(0));
                            //remove the file of this photo
                            deleteTempPhoto(new File(AppList.select_photos_wait_upload.get(0)));
                            //remove the photo from the list which is used to create the tempPhotos
                            AppList.select_photos_wait_upload.remove(0);
                                semaphoreUploadThread.release();

                    }
                }
                    }
                };
                Looper.loop();
//                if (AppList.select_photos_wait_upload.size() > 0){
//                    if(upload(AppList.select_photos_wait_upload.get(0))){
//                        AppList.select_photos_wait_upload.remove(0);
//                        MyCache.deletePhotoName(AppList.select_photos_wait_upload.get(0));
//                        deleteTempPhoto(new File(Config.PHOTOS_DIR + "/" + AppList.select_photos_wait_upload.get(0)));
//                    }
//                }
            }
        };

        uploadThread.start();
    }
    /**
     * Upload pic
     * if success return true, else return false
     * */
    private boolean upload(String s) {
        return true;
    }

    public static UpLoadPhotos getInstance(){

        if (mInstance == null){
            synchronized (UpLoadPhotos.class){
                //同步二次处理判断十分必要
                if (mInstance == null){
                    mInstance = new UpLoadPhotos();
                }
            }
        }
        return mInstance;
    }

    public void sendMessageToUpload(){
       uploadHandler.sendEmptyMessage(0x10086);
    }

    //delete file

    public void deleteTempPhoto(File photo){
        if (photo.exists() && photo.isFile()){
            photo.delete();
        }
        if (photo.exists() & photo.isFile()){
            System.out.println(photo.toString());
        }
    }

}
