package com.amt.media.bean;

import android.content.ContentValues;

/**
 * Created by archermind on 2018/8/9.
 */

public class CollectAudioBean extends AudioBean {
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
}
