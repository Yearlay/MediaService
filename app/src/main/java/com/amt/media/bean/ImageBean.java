package com.amt.media.bean;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.BitmapFactory;

/**
 * Created by archermind on 2018/8/9.
 */

public class ImageBean extends MediaBean {
    public static final String FIELD_IMAGE_WIDTH = "width";
    private int width;
    public static final String FIELD_IMAGE_HEIGHT = "height";
    private int height;
    public static final String FIELD_THUMBNAIL_PATH = "thumbnail_path";
    private String thumbnailPath;

    public ContentValues getContentValues(ContentValues contentValues) {
        contentValues = super.getContentValues(contentValues);
        contentValues.put(FIELD_IMAGE_WIDTH, width);
        contentValues.put(FIELD_IMAGE_HEIGHT, height);
        contentValues.put(FIELD_THUMBNAIL_PATH, thumbnailPath);
        return contentValues;
    }

    public ImageBean(Cursor cursor) {
        super(cursor);
        width = cursor.getInt(cursor.getColumnIndex(FIELD_IMAGE_WIDTH));
        height = cursor.getInt(cursor.getColumnIndex(FIELD_IMAGE_HEIGHT));
        thumbnailPath = cursor.getString(cursor.getColumnIndex(FIELD_THUMBNAIL_PATH));
    }

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

    public void parseID3() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(getFilePath(), options);
        height = options.outHeight;
        width = options.outWidth;
        setThumbnailPath(null);
        setId3Flag(1);
    }
}
