package com.amt.media.bean;

import android.content.ContentValues;
import android.database.Cursor;

import com.amt.media.util.MediaUtil;

/**
 * Created by archermind on 2018/8/9.
 */

public class CollectVideoBean extends VideoBean {
    public static final String FIELD_USERNAME = "username";
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ContentValues getContentValues(ContentValues contentValues) {
        contentValues = super.getContentValues(contentValues);
        contentValues.put(FIELD_USERNAME, username);
        return contentValues;
    }

    public CollectVideoBean(Cursor cursor) {
        super(cursor);
        username = cursor.getString(cursor.getColumnIndex(FIELD_USERNAME));
    }

    public CollectVideoBean(VideoBean videoBean) {
        setPortId(videoBean.getPortId());
        setFileType(videoBean.getFileType());
        setFilePath(videoBean.getFilePath());
        setFileName(videoBean.getFileName());
        setFileNamePY(videoBean.getFileNamePY());
        setFileSize(videoBean.getFileSize());
        setLastDate(videoBean.getLastDate());
        setOnlyreadFlag(videoBean.getOnlyreadFlag());
        setId3Flag(videoBean.getId3Flag());
        setUnsupportFlag(videoBean.getUnsupportFlag());
        setCollectFlag(videoBean.getCollectFlag());

        setDuration(videoBean.getDuration());
        setPlayTime(videoBean.getPlayTime());
        setThumbnailPath(videoBean.getThumbnailPath());
        setUsername(MediaUtil.getUserName());
    }
}
