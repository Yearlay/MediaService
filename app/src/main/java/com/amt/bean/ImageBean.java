package com.amt.bean;

/**
 * Created by archermind on 2018/8/9.
 */

public class ImageBean extends MediaBean {
    private int width;
    private int height;
    private String thumbnailPath;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
}
