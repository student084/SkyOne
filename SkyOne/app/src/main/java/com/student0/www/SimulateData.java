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

    public SimulateData() {

        for (int i = 0; i < 2; i ++ ){
            photoList1.add(new Photo(i+1, "url" + i));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-12", photoList1));
        for (int i = 0; i < 3; i ++ ){
            photoList2.add(new Photo(i+1, "url" + i));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-13", photoList2));
        for (int i = 0; i < 7; i ++ ){
            photoList3.add(new Photo(i+1, "url" + i));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-14", photoList3));
        for (int i = 0; i < 5; i ++ ){
            photoList4.add(new Photo(i+1, "url" + i));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-15", photoList4));
        for (int i = 0; i < 9; i ++ ){
            photoList5.add(new Photo(i+1, "url" + i));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-16", photoList5));
        for (int i = 0; i < 1; i ++ ){
            photoList6.add(new Photo(i+1, "url" + i));
        }
        perDatePhotosList.add(new PerDatePhotos(1, "2017-02-17", photoList6));
    }

    public List<PerDatePhotos> getPerDatePhotosList() {
        return perDatePhotosList;
    }
}
