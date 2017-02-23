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
    private List<Photo> photoList = new ArrayList<>();

    public SimulateData() {

        for (int i = 0; i < 10; i ++ ){
            photoList.add(new Photo(i+1, "url" + i));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-12", photoList));
        for (int i = 0; i < 10; i ++ ){
            photoList.add(new Photo(i+1, "url" + i));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-13", photoList));
        for (int i = 0; i < 10; i ++ ){
            photoList.add(new Photo(i+1, "url" + i));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-14", photoList));
        for (int i = 0; i < 10; i ++ ){
            photoList.add(new Photo(i+1, "url" + i));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-15", photoList));
        for (int i = 0; i < 10; i ++ ){
            photoList.add(new Photo(i+1, "url" + i));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-16", photoList));
        for (int i = 0; i < 10; i ++ ){
            photoList.add(new Photo(i+1, "url" + i));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-17", photoList));
    }

    public List<PerDatePhotos> getPerDatePhotosList() {
        return perDatePhotosList;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }
}
