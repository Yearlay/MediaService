package com.amt.bean;

import android.content.ContentValues;

/**
 * Created by archermind on 2018/8/9.
 */

public class VideoBean extends MediaBean {
    public static final String FIELD_DURATION = "duration";
    private int duration;
    public static final String FIELD_THUMBNAIL_PATH = "thumbnail_path";
    private String thumbnailPath;
    public static final String FIELD_PLAY_TIME = "playtime";
    private int playTime;

    public ContentValues getContentValues(ContentValues contentValues) {
        contentValues = super.getContentValues(contentValues);
        contentValues.put(FIELD_DURATION, duration);
        contentValues.put(FIELD_THUMBNAIL_PATH, thumbnailPath);
        contentValues.put(FIELD_PLAY_TIME, playTime);
        return contentValues;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }
}
