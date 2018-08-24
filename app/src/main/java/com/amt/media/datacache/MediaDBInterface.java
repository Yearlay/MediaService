package com.amt.media.datacache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.amt.media.bean.AudioBean;
import com.amt.media.bean.CollectAudioBean;
import com.amt.media.bean.CollectImageBean;
import com.amt.media.bean.CollectVideoBean;
import com.amt.media.bean.ImageBean;
import com.amt.media.bean.MediaBean;
import com.amt.media.bean.StorageBean;
import com.amt.media.bean.VideoBean;
import com.amt.media.util.DBConfig;
import com.amt.media.util.MediaUtil;
import com.amt.media.util.UriConfig;
import com.amt.util.DebugLog;

import java.io.File;
import java.util.ArrayList;

public class MediaDBInterface {
    private static String TAG = "MediaDBInterface";
    private Context mContext = null;

    private MediaDBInterface(Context context) {
        this.mContext = context;
    }

    private static MediaDBInterface mSelf;

    public static MediaDBInterface instance(Context context) {
        if (mSelf == null) {
            mSelf = new MediaDBInterface(context);
        }
        return mSelf;
    }

    public ArrayList<StorageBean> queryStorageBeans() {
        ArrayList<StorageBean> storageBeans = new ArrayList<StorageBean>();
        Uri uriAddress = Uri.parse(UriConfig.URI_STORAGE_ADDR);
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(uriAddress, null,
                    null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    storageBeans.add(new StorageBean(cursor));
                } while (cursor.moveToNext());
            } else {
                DebugLog.e(TAG, cursor == null ? "cursor is null." : "no datas!" +
                        " && uriAddress: " + uriAddress.toString());
            }
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return storageBeans;
    }

    public ArrayList<MediaBean> query(String tableName, String whereClause, String[] whereArgs) {
        ArrayList<MediaBean> mediaBeans = new ArrayList<MediaBean>();
        Uri uriAddress = Uri.parse(UriConfig.getUriAddress(tableName));
        int fileType = DBConfig.getFileType(tableName);
        boolean collectFlag = DBConfig.isCollectTable(tableName);
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(uriAddress, null,
                    whereClause, whereArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MediaBean mediaBean = getMediaBean(fileType, collectFlag, cursor);
                    File file = new File(mediaBean.getFilePath());
                    if (file.exists()) {
                        mediaBeans.add(mediaBean);
                    }
                } while (cursor.moveToNext());
            } else {
                DebugLog.e(TAG, cursor == null ? "cursor is null." : "no datas!" +
                        " && tableName: " + tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return mediaBeans;
    };

    private MediaBean getMediaBean(int fileType, boolean collectFlag, Cursor cursor) {
        MediaBean mediaBean = null;
        switch (fileType) {
            case MediaUtil.FileType.AUDIO:
                mediaBean = collectFlag ? new CollectAudioBean(cursor) : new AudioBean(cursor);
                break;
            case MediaUtil.FileType.VIDEO:
                mediaBean = collectFlag ? new CollectVideoBean(cursor) : new VideoBean(cursor);
                break;
            case MediaUtil.FileType.IMAGE:
                mediaBean = collectFlag ? new CollectImageBean(cursor) : new ImageBean(cursor);
                break;
        }
        return mediaBean;
    }

    public void insert(MediaBean mediaBean) {
        String tableName = DBConfig.getTableName(mediaBean);
        Uri uriAddress = Uri.parse(UriConfig.getUriAddress(tableName));
        ContentValues contentValues = new ContentValues();
        contentValues = mediaBean.getContentValues(contentValues);
        mContext.getContentResolver().insert(uriAddress, contentValues);
    }

    public void delete(MediaBean mediaBean) {
        String tableName = DBConfig.getTableName(mediaBean);
        Uri uriAddress = Uri.parse(UriConfig.getUriAddress(tableName));
        String whereClause = MediaBean.FIELD_FILE_PATH + "=?";
        String[] whereArgs = new String[] {mediaBean.getFilePath()};
        mContext.getContentResolver().delete(uriAddress, whereClause, whereArgs);
    }

    public void update(MediaBean mediaBean) {
        String tableName = DBConfig.getTableName(mediaBean);
        Uri uriAddress = Uri.parse(UriConfig.getUriAddress(tableName));
        ContentValues contentValues = new ContentValues();
        contentValues = mediaBean.getContentValues(contentValues);
        String whereClause = MediaBean.FIELD_ID + "=?";
        String[] whereArgs = new String[] {mediaBean.getId() + ""};
        mContext.getContentResolver().update(uriAddress, contentValues, whereClause, whereArgs);
    }

    public void update(StorageBean storageBean) {
        Uri uriAddress = Uri.parse(UriConfig.getUriAddress(DBConfig.DBTable.STORAGR));
        ContentValues contentValues = new ContentValues();
        contentValues = storageBean.getContentValues();
        String whereClause = StorageBean.FIELD_PORT_ID + "=?";
        String[] whereArgs = new String[] {storageBean.getPortId() + ""};
        mContext.getContentResolver().update(uriAddress,
                storageBean.getContentValues(), whereClause, whereArgs);
    }
}
