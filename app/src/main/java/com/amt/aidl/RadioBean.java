package com.amt.aidl;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class RadioBean implements Parcelable {
    public static final String FIELD_USERNAME = "username";
    private String username;
    public static final String FIELD_FREQ = "freq";
    private String freq;
    public static final String FIELD_NAME = "name";
    private String name;

    public class RadioType {
        public static final int FM_TYPE = 1;
        public static final int AM_TYPE = 2;
        public static final int MCU_TYPE = 3;
    }
    private int radioType;

    public int getRadioType() {
        return radioType;
    }

    public void setRadioType(int radioType) {
        this.radioType = radioType;
    }

    public RadioBean(String username, String freq, String name, int radioType) {
        this.username = username;
        this.freq = freq;
        this.name = name;
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

    protected RadioBean(Parcel in) {
        username = in.readString();
        freq = in.readString();
        name = in.readString();
        radioType = in.readInt();
    }

    public static final Creator<RadioBean> CREATOR = new Creator<RadioBean>() {
        @Override
        public RadioBean createFromParcel(Parcel in) {
            return new RadioBean(in);
        }

        @Override
        public RadioBean[] newArray(int size) {
            return new RadioBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(freq);
        parcel.writeString(name);
        parcel.writeInt(radioType);
    }
}
