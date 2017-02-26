package com.student0.www.fragment;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.student0.www.Config;
import com.student0.www.skyone.MainActivity;
import com.student0.www.skyone.MyCache;
import com.student0.www.skyone.R;
import com.student0.www.util.MyMD5Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by willj on 2017/2/23.
 */

public class CameraFragment extends Fragment implements SurfaceHolder.Callback{

    private static int REQUIRE_CAMERA = 1;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ImageButton imageButton;

    private Camera camera;


    //storage the pic to /skyone file
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Date curDate = new Date(System.currentTimeMillis());
            String photoName = MyMD5Util.md5(curDate.toString());
            photoName =  photoName+ ".png";

            String dir = Config.PHOTOS_DIR + "/"+ photoName;
            File tempFile = new File(dir);
            try {

                FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
                fileOutputStream.write(data);
                MyCache.addPhotoNameToList(photoName);
                fileOutputStream.close();
                camera.startPreview();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        camera = getCamera();
        View view = inflater.inflate(R.layout.camera, container, false);

        imageButton = (ImageButton) view.findViewById(R.id.id_take_photo);
        surfaceView = (SurfaceView) view.findViewById(R.id.id_camera);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.autoFocus(null);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });


        return view;
    }

    private void takePhoto() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();

        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewFormat(ImageFormat.JPEG);
        parameters.setPictureSize(displayMetrics.widthPixels ,displayMetrics.heightPixels);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.autoFocus(new Camera.AutoFocusCallback(){
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success){
                   MainActivity.refreshTempFragment();
                    camera.takePicture(null, null,pictureCallback );
                }
            }
        });
    }

    /**
     * Get camera instance
     * */
    private Camera getCamera(){
        Camera camera;
        try {
            camera = Camera.open();
        }catch (Exception e){
            camera = null;
            e.printStackTrace();
        }
        return camera;
    }

    /**
     * bind the data between camera and surface
     * */

    private void setStartPreview(Camera camera, SurfaceHolder holder){
        try {
            camera.setPreviewDisplay(holder);

            //turn around 90 d
            camera.setDisplayOrientation(90);
            camera.startPreview();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Release the resource
     * */
    public void releaseCamera(){
        if (camera != null){
            //release callback
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(camera, surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        camera.stopPreview();
        setStartPreview(camera, surfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }



}
