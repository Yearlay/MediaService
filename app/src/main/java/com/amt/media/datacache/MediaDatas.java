package com.amt.media.datacache;

import com.amt.media.bean.MediaBean;
import com.amt.media.util.DBConfig;

import java.util.ArrayList;

public class MediaDatas {
    public static ArrayList<MediaBean> mSdcardAudios = new ArrayList<MediaBean>();
    public static ArrayList<MediaBean> mSdcardVideos = new ArrayList<MediaBean>();
    public static ArrayList<MediaBean> mSdcardImages = new ArrayList<MediaBean>();

    public static ArrayList<MediaBean> mUsb1Audios = new ArrayList<MediaBean>();
    public static ArrayList<MediaBean> mUsb1Videos = new ArrayList<MediaBean>();
    public static ArrayList<MediaBean> mUsb1Images = new ArrayList<MediaBean>();

    public static ArrayList<MediaBean> mUsb2Audios = new ArrayList<MediaBean>();
    public static ArrayList<MediaBean> mUsb2Videos = new ArrayList<MediaBean>();
    public static ArrayList<MediaBean> mUsb2Images = new ArrayList<MediaBean>();

    public static ArrayList<MediaBean> mCollectAudios = new ArrayList<MediaBean>();
    public static ArrayList<MediaBean> mCollectVideos = new ArrayList<MediaBean>();
    public static ArrayList<MediaBean> mCollectImages = new ArrayList<MediaBean>();

    public static ArrayList<MediaBean> getMediaList(String tableName) {
        ArrayList<MediaBean> mediaBeans = null;
        if (DBConfig.DBTable.SDCARD_AUDIO.equals(tableName)) {
            mediaBeans = mSdcardAudios;
        } else if (DBConfig.DBTable.SDCARD_VIDEO.equals(tableName)) {
            mediaBeans = mSdcardVideos;
        } else if (DBConfig.DBTable.SDCARD_IMAGE.equals(tableName)) {
            mediaBeans = mSdcardImages;
        } else if (DBConfig.DBTable.USB1_AUDIO.equals(tableName)) {
            mediaBeans = mUsb1Audios;
        } else if (DBConfig.DBTable.USB1_VIDEO.equals(tableName)) {
            mediaBeans = mUsb1Videos;
        } else if (DBConfig.DBTable.USB1_IMAGE.equals(tableName)) {
            mediaBeans = mUsb1Images;
        } else if (DBConfig.DBTable.USB2_AUDIO.equals(tableName)) {
            mediaBeans = mUsb2Audios;
        } else if (DBConfig.DBTable.USB2_VIDEO.equals(tableName)) {
            mediaBeans = mUsb2Videos;
        } else if (DBConfig.DBTable.USB2_IMAGE.equals(tableName)) {
            mediaBeans = mUsb2Images;
        } else if (DBConfig.DBTable.COLLECT_AUDIO.equals(tableName)) {
            mediaBeans = mCollectAudios;
        } else if (DBConfig.DBTable.COLLECT_VIDEO.equals(tableName)) {
            mediaBeans = mCollectVideos;
        } else if (DBConfig.DBTable.COLLECT_IMAGE.equals(tableName)) {
            mediaBeans = mCollectImages;
        }
        return mediaBeans;
    }

}
