package com.amt.media.bean;

import android.content.ContentValues;
import android.database.Cursor;

import com.amt.media.util.MediaUtil;

/**
 * Created by archermind on 2018/8/9.
 */

public class CollectImageBean extends ImageBean {
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

    public CollectImageBean(Cursor cursor) {
        super(cursor);
        username = cursor.getString(cursor.getColumnIndex(FIELD_USERNAME));
    }

    public CollectImageBean(ImageBean imageBean) {
        setPortId(imageBean.getPortId());
        setFileType(imageBean.getFileType());
        setFilePath(imageBean.getFilePath());
        setFileName(imageBean.getFileName());
        setFileNamePY(imageBean.getFileNamePY());
        setFileSize(imageBean.getFileSize());
        setLastDate(imageBean.getLastDate());
        setOnlyreadFlag(imageBean.getOnlyreadFlag());
        setId3Flag(imageBean.getId3Flag());
        setUnsupportFlag(imageBean.getUnsupportFlag());
        setCollectFlag(imageBean.getCollectFlag());

        setWidth(imageBean.getWidth());
        setHeight(imageBean.getHeight());
        setThumbnailPath(imageBean.getThumbnailPath());
        setUsername(MediaUtil.getUserName());
    }
}
