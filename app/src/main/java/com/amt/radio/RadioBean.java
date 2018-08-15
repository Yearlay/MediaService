package com.amt.radio;

import android.content.ContentValues;
import android.database.Cursor;

public class RadioBean {
    public static final String FIELD_USERNAME = "username";
    private String username;
    public static final String FIELD_FREQ = "freq";
    private String freq;
    public static final String FIELD_NAME = "name";
    private String name;

    public class RadioType {
        public static final int FM_TYPE = 1;
        public static final int AM_TYPE = 2;
    }
    private int radioType;

    public int getRadioType() {
        return radioType;
    }

    public void setRadioType(int radioType) {
        this.radioType = radioType;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_USERNAME, username);
        contentValues.put(FIELD_FREQ, freq);
        contentValues.put(FIELD_NAME, name);
        return contentValues;
    }

    public RadioBean(Cursor cursor) {
        username = cursor.getString(cursor.getColumnIndex(FIELD_USERNAME));
        freq = cursor.getString(cursor.getColumnIndex(FIELD_FREQ));
        name = cursor.getString(cursor.getColumnIndex(FIELD_NAME));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
