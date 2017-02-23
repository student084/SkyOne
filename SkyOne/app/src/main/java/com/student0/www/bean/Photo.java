package com.student0.www.bean;

/**
 * Created by willj on 2017/2/23.
 */

/**
 * 加载相片时用到的相册信息
 *
 * */

public class Photo {
    private String resourceURL;
    private int photoId;

    /**
     * @param photoId 唯一标签
     * @param resourceURL 图片对应的URL地址
     * */
    public Photo(int photoId, String resourceURL) {
        this.photoId = photoId;
        this.resourceURL = resourceURL;
    }

    public int getPhotoId() {
        return photoId;
    }

    public String getResourceURL() {
        return resourceURL;
    }
}
