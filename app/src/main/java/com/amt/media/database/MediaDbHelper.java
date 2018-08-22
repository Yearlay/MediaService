package com.amt.media.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.amt.media.bean.AudioBean;
import com.amt.media.bean.CollectAudioBean;
import com.amt.media.bean.CollectImageBean;
import com.amt.media.bean.CollectVideoBean;
import com.amt.media.bean.StorageBean;
import com.amt.media.bean.VideoBean;
import com.amt.media.bean.ImageBean;
import com.amt.media.bean.MediaBean;
import com.amt.media.datacache.AllMediaList;
import com.amt.media.datacache.StorageManager;
import com.amt.media.util.DBConfig;
import com.amt.media.util.MediaUtil;
import com.amt.media.util.MediaUtil.FileType;
import com.amt.media.util.UriConfig;
import com.amt.mediaservice.MediaApplication;
import com.amt.util.DebugLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by archermind on 2018/8/9.
 */

public class MediaDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "MediaDbHelper";
    private Context mContext = null;

    public Context getContext() {
        return mContext;
    }

    private MediaDbHelper(Context context) {
        super(context, DBConfig.DATABASE_NAME, null, DBConfig.DATABASE_VERSION);
        mContext = context;
    }

    private static MediaDbHelper sMediaDbHelper = null;
    public static MediaDbHelper instance() {
        if (sMediaDbHelper == null) {
            sMediaDbHelper = new MediaDbHelper(MediaApplication.getInstance());
        }
        return sMediaDbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        initDataBase(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        clearDataBase(sqLiteDatabase);
        initDataBase(sqLiteDatabase);
    }

    private void initDataBase(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(getCreateTableString(DBConfig.DBTable.SDCARD_AUDIO, FileType.AUDIO, false));
        sqLiteDatabase.execSQL(getCreateTableString(DBConfig.DBTable.SDCARD_VIDEO, FileType.VIDEO, false));
        sqLiteDatabase.execSQL(getCreateTableString(DBConfig.DBTable.SDCARD_IMAGE, FileType.IMAGE, false));
        sqLiteDatabase.execSQL(getCreateTableString(DBConfig.DBTable.USB1_AUDIO, FileType.AUDIO, false));
        sqLiteDatabase.execSQL(getCreateTableString(DBConfig.DBTable.USB1_VIDEO, FileType.VIDEO, false));
        sqLiteDatabase.execSQL(getCreateTableString(DBConfig.DBTable.USB1_IMAGE, FileType.IMAGE, false));
        sqLiteDatabase.execSQL(getCreateTableString(DBConfig.DBTable.USB2_AUDIO, FileType.AUDIO, false));
        sqLiteDatabase.execSQL(getCreateTableString(DBConfig.DBTable.USB2_VIDEO, FileType.VIDEO, false));
        sqLiteDatabase.execSQL(getCreateTableString(DBConfig.DBTable.USB2_IMAGE, FileType.IMAGE, false));
        sqLiteDatabase.execSQL(getCreateTableString(DBConfig.DBTable.COLLECT_AUDIO, FileType.AUDIO, true));
        sqLiteDatabase.execSQL(getCreateTableString(DBConfig.DBTable.COLLECT_VIDEO, FileType.VIDEO, true));
        sqLiteDatabase.execSQL(getCreateTableString(DBConfig.DBTable.COLLECT_IMAGE, FileType.IMAGE, true));
    }

    public void clearDataBase(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBConfig.DBTable.SDCARD_AUDIO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBConfig.DBTable.SDCARD_VIDEO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBConfig.DBTable.SDCARD_IMAGE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBConfig.DBTable.USB1_AUDIO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBConfig.DBTable.USB1_VIDEO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBConfig.DBTable.USB1_IMAGE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBConfig.DBTable.USB2_AUDIO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBConfig.DBTable.USB2_VIDEO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBConfig.DBTable.USB2_IMAGE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBConfig.DBTable.COLLECT_AUDIO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBConfig.DBTable.COLLECT_VIDEO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBConfig.DBTable.COLLECT_IMAGE);
    }

    private String getCreateTableString(String table, int fileType, boolean isCollect) {
        String sqlStr = "create table " + table + "(";
        sqlStr += MediaBean.FIELD_ID +                " integer primary key autoincrement,";
        sqlStr += MediaBean.FIELD_PORT_ID +           " integer,";
        sqlStr += MediaBean.FIELD_FILE_TYPE +         " integer,";
        sqlStr += MediaBean.FIELD_FILE_PATH +         " text,";
        sqlStr += MediaBean.FIELD_FILE_NAME +         " text,";
        sqlStr += MediaBean.FIELD_FILE_NAME_PY +      " text,";
        sqlStr += MediaBean.FIELD_FILE_SIZE +         " long DEFAULT 0,";
        sqlStr += MediaBean.FIELD_FILE_LASTDATE +     " text,";
        sqlStr += MediaBean.FIELD_ONLYREAD_FLAG +     " integer DEFAULT 0,";
        sqlStr += MediaBean.FIELD_ID3_FLAG +          " integer DEFAULT 0,";
        sqlStr += MediaBean.FIELD_UNSUPPORT_FLAG +    " integer DEFAULT 1,";
        sqlStr += MediaBean.FIELD_COLLECT_FLAG +      " integer DEFAULT 0,";

        if (fileType == FileType.AUDIO) {
            sqlStr += AudioBean.FIELD_AUDIO_TITLE +       " text,";
            sqlStr += AudioBean.FIELD_AUDIO_TITLE_PY +    " text,";
            sqlStr += AudioBean.FIELD_AUDIO_ARTIST +      " text,";
            sqlStr += AudioBean.FIELD_AUDIO_ARTIST_PY +   " text,";
            sqlStr += AudioBean.FIELD_AUDIO_ALBUM +       " text,";
            sqlStr += AudioBean.FIELD_AUDIO_ALBUM_PY +    " text,";
            sqlStr += AudioBean.FIELD_AUDIO_COMPOSER +    " text,";
            sqlStr += AudioBean.FIELD_AUDIO_GENRE +       " text,";
            sqlStr += AudioBean.FIELD_DURATION +          " integer,";
            sqlStr += AudioBean.FIELD_PLAY_TIME +         " integer,";
            sqlStr += AudioBean.FIELD_THUMBNAIL_PATH +    " text";
        } else if (fileType == FileType.VIDEO) {
            sqlStr += VideoBean.FIELD_DURATION +          " integer,";
            sqlStr += VideoBean.FIELD_PLAY_TIME +         " integer,";
            sqlStr += VideoBean.FIELD_THUMBNAIL_PATH +    " text";
        } else if (fileType == FileType.IMAGE) {
            sqlStr += ImageBean.FIELD_IMAGE_WIDTH +       " integer,";
            sqlStr += ImageBean.FIELD_IMAGE_HEIGHT +      " integer,";
            sqlStr += ImageBean.FIELD_THUMBNAIL_PATH +    " text";
        }
        if (isCollect) {
            sqlStr += "," + CollectAudioBean.FIELD_USERNAME + " text";
        }
        sqlStr += ")";
        return sqlStr;
    }

    /**
     * 注意：批量处理的话，必须批量前调用setStartFlag(true)；批量后调用setStartFlag(false)
     * @param mediaBean
     */
    public void insert(MediaBean mediaBean) {
        addToNeedToInsertList(new TransactionTask(mediaBean, TransactionTask.INSERT_TASK));
    }

    /**
     * 注意：批量处理的话，必须批量前调用setStartFlag(true)；批量后调用setStartFlag(false)
     * @param mediaBean
     */
    public void delete(MediaBean mediaBean) {
        addToNeedToInsertList(new TransactionTask(mediaBean, TransactionTask.DELETE_TASK));
    }

    /**
     * 注意：批量处理的话，必须批量前调用setStartFlag(true)；批量后调用setStartFlag(false)
     * @param mediaBean
     */
    public void update(MediaBean mediaBean) {
        addToNeedToInsertList(new TransactionTask(mediaBean, TransactionTask.UPDATE_TASK));
    }

    public static class TransactionTask {
        public static final int INSERT_TASK = 1;
        public static final int UPDATE_TASK = 2;
        public static final int DELETE_TASK = 3;
        public MediaBean mMediaBean;
        public int mTaskType;
        public TransactionTask(MediaBean mediaBean, int taskType) {
            mMediaBean = mediaBean;
            mTaskType = taskType;
        }
    }

    private List<TransactionTask> mNeedToInsertList = Collections.synchronizedList(new ArrayList<TransactionTask>());
    private boolean mStartFlag = false;

    public void setStartFlag(boolean mStartFlag) {
        this.mStartFlag = mStartFlag;
        if (!mStartFlag) {
            doTaskList();
        }
    }

    public void addToNeedToInsertList(TransactionTask task) {
        if (mNeedToInsertList.size() < 500) {
            mNeedToInsertList.add(task);
        }
        if (mNeedToInsertList.size() >= 500 || !mStartFlag) {
            doTaskList();
        }
    }

    private void doTaskList() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // TODO: 多线程操作List的安全性问题。
            while (mNeedToInsertList.size() > 0) {
                TransactionTask task = mNeedToInsertList.remove(0);
                switch (task.mTaskType) {
                    case TransactionTask.INSERT_TASK:
                        insertEx(task.mMediaBean);
                        break;
                    case TransactionTask.DELETE_TASK:
                        deleteEx(task.mMediaBean);
                        break;
                    case TransactionTask.UPDATE_TASK:
                        updateEx(task.mMediaBean);
                        break;
                    default:
                        break;
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tryCatchEndTransaction(db);
        }
    }

    // 添加SQLiteFullException的判断来规避“因为磁盘被写满而爆出的崩溃问题”。
    private void tryCatchEndTransaction(SQLiteDatabase db) {
        try {
            db.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertEx(MediaBean mediaBean) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String tableName = DBConfig.getTableName(mediaBean);
        ContentValues contentValues = new ContentValues();
        contentValues = mediaBean.getContentValues(contentValues);
        if (tableName != null) {
            long ret = sqLiteDatabase.insert(tableName, null, contentValues);
            if (ret > 0) {
                notifyChange(tableName);
            }
        } else {
            DebugLog.e(TAG, "Error insertEx tableName is null.");
        }
    }

    private void deleteEx(MediaBean mediaBean) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String tableName = DBConfig.getTableName(mediaBean);
        String whereClause = MediaBean.FIELD_FILE_PATH + "=?";
        String[] whereArgs = new String[] {mediaBean.getFilePath()};
        if (tableName != null) {
            long ret = sqLiteDatabase.delete(tableName, whereClause, whereArgs);
            if (ret > 0) {
                notifyChange(tableName);
            }
        }  else {
            DebugLog.e(TAG, "Error deleteEx tableName is null.");
        }
    }

    private void updateEx(MediaBean mediaBean) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String tableName = DBConfig.getTableName(mediaBean);
        ContentValues contentValues = new ContentValues();
        contentValues = mediaBean.getContentValues(contentValues);
        String whereClause = MediaBean.FIELD_ID + "=?";
        String[] whereArgs = new String[] {mediaBean.getId() + ""};
        if (tableName != null) {
            long ret = sqLiteDatabase.update(tableName, contentValues, whereClause, whereArgs);
            if (ret > 0) {
                notifyChange(tableName);
            }
        } else {
            DebugLog.e(TAG, "Error updateEx tableName is null.");
        }
    }

    /**
     * 通知内容观察者tableName表数据有变化。
     * @param tableName
     */
    public void notifyChange(String tableName) {
        AllMediaList.instance().notifyChange(tableName);
        mContext.getContentResolver().notifyChange(Uri.parse(UriConfig.getUriAddress(tableName)), null);
    }

    /**
     * 删除某个磁盘的所有媒体数据。
     * @param portId
     */
    public void clearStorageData(int portId) {
        clearData(portId, FileType.AUDIO);
        clearData(portId, FileType.VIDEO);
        clearData(portId, FileType.IMAGE);
    }

    private void clearData(int portId, int fileType) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String tableName = DBConfig.getTableName(portId, fileType);
        String sqlStr = "DELETE FROM " + tableName;
        sqLiteDatabase.execSQL(sqlStr);
    }

    /**
     *
     * @param tableName 表名
     * @param selection 查询条件
     * @param selectionArgs 查询列
     * @param allFlag 是否查询全部数据（true: 不考虑磁盘是否挂载）
     * @return 媒体列表。
     */
    public ArrayList<MediaBean> query(String tableName, String selection, String[] selectionArgs, boolean allFlag) {
        ArrayList<MediaBean> mediaBeanList = new ArrayList<MediaBean>();
        if (DBConfig.getFileType(tableName) == FileType.AUDIO) {
            mediaBeanList.addAll(queryAudio(tableName, selection, selectionArgs, allFlag));
        } else if (DBConfig.getFileType(tableName) == FileType.VIDEO) {
            mediaBeanList.addAll(queryVideo(tableName, selection, selectionArgs, allFlag));
        } else if (DBConfig.getFileType(tableName) == FileType.IMAGE) {
            mediaBeanList.addAll(queryImage(tableName, selection, selectionArgs, allFlag));
        }
        if (mediaBeanList.size() == 0) {
            DebugLog.e(TAG, "query tableName: + " + tableName + " && allFlag: " + allFlag +
                    " && no datas selection: " + selection + " selectionArgs: " + selectionArgs);
        } else {
            // TODO 进行排序。
            DebugLog.d(TAG, "query tableName: + " + tableName + " && allFlag: " + allFlag +
                    " && media size： " + mediaBeanList.size());
        }
        return mediaBeanList;
    }

    private ArrayList<AudioBean> queryAudio(String tableName, String selection, String[] selectionArgs, boolean allFlag) {
        ArrayList<AudioBean> audioBeans = new ArrayList<AudioBean>();
        if (DBConfig.isCollectTable(tableName)) {
            audioBeans.addAll(queryCollectAudio(tableName, selection, selectionArgs, allFlag));
        } else {
            int portId = DBConfig.getPortId(tableName);
            StorageBean storageBean = StorageManager.instance().getStorageBean(portId);
            if (storageBean.isMounted()) {
                Cursor cursor = null;
                try {
                    cursor = getReadableDatabase().query(tableName, null, selection, selectionArgs, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            audioBeans.add(new AudioBean(cursor));
                        } while (cursor.moveToNext());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            }
        }
        return audioBeans;
    }

    private ArrayList<CollectAudioBean> queryCollectAudio(String tableName, String selection, String[] selectionArgs, boolean allFlag) {
        ArrayList<CollectAudioBean> collectAudioBeans = new ArrayList<CollectAudioBean>();
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(tableName, null, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CollectAudioBean collectAudioBean = new CollectAudioBean(cursor);
                    StorageBean storageBean = StorageManager.instance().getStorageBean(collectAudioBean.getPortId());
                    if (storageBean.isMounted() || allFlag) {
                        collectAudioBeans.add(collectAudioBean);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return collectAudioBeans;
    }

    private ArrayList<VideoBean> queryVideo(String tableName, String selection, String[] selectionArgs, boolean allFlag) {
        ArrayList<VideoBean> videoBeans = new ArrayList<VideoBean>();
        if (DBConfig.isCollectTable(tableName)) {
            videoBeans.addAll(queryCollectVideo(tableName, selection, selectionArgs, allFlag));
        } else {
            int portId = DBConfig.getPortId(tableName);
            StorageBean storageBean = StorageManager.instance().getStorageBean(portId);
            if (storageBean.isMounted()) {
                Cursor cursor = null;
                try {
                    cursor = getReadableDatabase().query(tableName, null, selection, selectionArgs, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            videoBeans.add(new VideoBean(cursor));
                        } while (cursor.moveToNext());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            }
        }
        return videoBeans;
    }

    private ArrayList<CollectVideoBean> queryCollectVideo(String tableName, String selection, String[] selectionArgs, boolean allFlag) {
        ArrayList<CollectVideoBean> collectVideoBeans = new ArrayList<CollectVideoBean>();
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(tableName, null, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CollectVideoBean collectVideoBean = new CollectVideoBean(cursor);
                    StorageBean storageBean = StorageManager.instance().getStorageBean(collectVideoBean.getPortId());
                    if (storageBean.isMounted() || allFlag) {
                        collectVideoBeans.add(collectVideoBean);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return collectVideoBeans;
    }

    private ArrayList<ImageBean> queryImage(String tableName, String selection, String[] selectionArgs, boolean allFlag) {
        ArrayList<ImageBean> imageBeans = new ArrayList<ImageBean>();
        if (DBConfig.isCollectTable(tableName)) {
            imageBeans.addAll(queryCollectImage(tableName, selection, selectionArgs, allFlag));
        } else {
            int portId = DBConfig.getPortId(tableName);
            StorageBean storageBean = StorageManager.instance().getStorageBean(portId);
            if (storageBean.isMounted()) {
                Cursor cursor = null;
                try {
                    cursor = getReadableDatabase().query(tableName, null, selection, selectionArgs, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            imageBeans.add(new ImageBean(cursor));
                        } while (cursor.moveToNext());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            }
        }
        return imageBeans;
    }

    private ArrayList<CollectImageBean> queryCollectImage(String tableName, String selection, String[] selectionArgs, boolean allFlag) {
        ArrayList<CollectImageBean> collectImageBeans = new ArrayList<CollectImageBean>();
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(tableName, null, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CollectImageBean collectImageBean = new CollectImageBean(cursor);
                    StorageBean storageBean = StorageManager.instance().getStorageBean(collectImageBean.getPortId());
                    if (storageBean.isMounted() || allFlag) {
                        collectImageBeans.add(collectImageBean);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return collectImageBeans;
    }

}
