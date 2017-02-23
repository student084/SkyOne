package com.student0.www.bean;

import java.util.List;

/**
 * Created by willj on 2017/2/23.
 *
 * Description:
 * the data for each item_date_and_photos
 * it contents :
 * $a data(String)
 * $a batch(int)
 * $a list<Photo>
 */

public class PerDatePhotos {
    private String data;
    private int batch;
    private List<Photo> photos;

    public PerDatePhotos(int batch, String data, List<Photo> photos) {
        this.batch = batch;
        this.data = data;
        this.photos = photos;
    }

    public int getBatch() {
        return batch;
    }

    public String getData() {
        return data;
    }

    public List<Photo> getPhotos() {
        return photos;
    }
}
