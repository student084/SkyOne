package com.student0.www;

import com.student0.www.bean.PerDatePhotos;
import com.student0.www.bean.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willj on 2017/2/23.
 */

public class SimulateData {
    private   List<PerDatePhotos> perDatePhotosList = new ArrayList<>();
    private List<Photo> photoList1 = new ArrayList<>();
    private List<Photo> photoList2 = new ArrayList<>();
    private List<Photo> photoList3 = new ArrayList<>();
    private List<Photo> photoList4 = new ArrayList<>();
    private List<Photo> photoList5 = new ArrayList<>();
    private List<Photo> photoList6 = new ArrayList<>();
    private List<Photo> photoList7 = new ArrayList<>();
    private String url1 = "http://10.60.0.164:8080/SkyOne/images/4e7ccd06ca4b7e47545a792e3fabcf31.png";
    private String url2 = "http://img.mukewang.com/55249cf30001ae8a06000338.jpg";
    private String url3 = "http://img.mukewang.com/551de0570001134f06000338.jpg";
    private String url4 = "http://img.mukewang.com/5518ecf20001cb4e06000338.jpg";
    private String[] urls = {url1, url2, url3, url4};
    public SimulateData() {

        for (int i = 0; i < 2; i ++ ){
            photoList1.add(new Photo(i+1, urls[i%4]));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-12", photoList1));
        for (int i = 0; i < 3; i ++ ){
            photoList2.add(new Photo(i+1, urls[i%4] ));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-13", photoList2));
        for (int i = 0; i < 7; i ++ ){
            photoList3.add(new Photo(i+1, urls[i%4] ));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-14", photoList3));
        for (int i = 0; i < 5; i ++ ){
            photoList4.add(new Photo(i+1, urls[i%4]));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-15", photoList4));
        for (int i = 0; i < 9; i ++ ){
            photoList5.add(new Photo(i+1, urls[i%4] ));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-16", photoList5));
        for (int i = 0; i < 1; i ++ ){
            photoList6.add(new Photo(i+1, urls[i%4]));
        }
        for (int i = 0; i < 2; i ++ ){
            photoList1.add(new Photo(i+1, urls[i%4]));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-12", photoList1));
        for (int i = 0; i < 3; i ++ ){
            photoList2.add(new Photo(i+1, urls[i%4] ));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-13", photoList2));
        for (int i = 0; i < 7; i ++ ){
            photoList3.add(new Photo(i+1, urls[i%4] ));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-14", photoList3));
        for (int i = 0; i < 5; i ++ ){
            photoList4.add(new Photo(i+1, urls[i%4]));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-15", photoList4));
        for (int i = 0; i < 9; i ++ ){
            photoList5.add(new Photo(i+1, urls[i%4] ));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-16", photoList5));
        for (int i = 0; i < 1; i ++ ){
            photoList6.add(new Photo(i+1, urls[i%4]));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-17", photoList6));
        for (int i = 0; i < 13; i ++ ){
            photoList7.add(new Photo(i+1, urls[i%4]));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-18", photoList7));
    }

    public List<PerDatePhotos> getPerDatePhotosList() {
        return perDatePhotosList;
    }
}
