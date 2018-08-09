package com.amt.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.amt.bean.AudioBean;
import com.amt.bean.CollectAudioBean;
import com.amt.bean.VideoBean;
import com.amt.bean.ImageBean;
import com.amt.bean.MediaBean;
import com.amt.util.DBConfig;
import com.amt.util.MediaUtil.FileType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by archermind on 2018/8/9.
 */

public class MediaDbHelper extends SQLiteOpenHelper {
    private Context mContext = null;

    public Context getContext() {
        return mContext;
    }

    public MediaDbHelper(Context context) {
        super(context, DBConfig.DATABASE_NAME, null, DBConfig.DATABASE_VERSION);
        mContext = context;
    }

    private static MediaDbHelper sMediaDbHelper = null;
    public static MediaDbHelper instance(Context context) {
        if (sMediaDbHelper == null) {
            sMediaDbHelper = new MediaDbHelper(context.getApplicationContext());
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
        long ret = sqLiteDatabase.insert(tableName, null, contentValues);
        if (ret > 0) {
            // mContext.getContentResolver().notifyChange(Uri.parse(DBConfig.getUriAddress(tableName)), null);
        }
    }

    private void deleteEx(MediaBean mediaBean) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String tableName = DBConfig.getTableName(mediaBean);
        String whereClause = MediaBean.FIELD_FILE_PATH + "=?";
        String[] whereArgs = new String[] {mediaBean.getFilePath()};
        long ret = sqLiteDatabase.delete(tableName, whereClause, whereArgs);
        if (ret > 0) {
            // mContext.getContentResolver().notifyChange(Uri.parse(DBConfig.getUriAddress(tableName)), null);
        }
    }

    private void updateEx(MediaBean mediaBean) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String tableName = DBConfig.getTableName(mediaBean);
        ContentValues contentValues = new ContentValues();
        contentValues = mediaBean.getContentValues(contentValues);
        String whereClause = MediaBean.FIELD_FILE_PATH + "=?";
        String[] whereArgs = new String[] {mediaBean.getFilePath()};
        long ret = sqLiteDatabase.update(tableName, contentValues, whereClause, whereArgs);
        if (ret > 0) {
            // mContext.getContentResolver().notifyChange(Uri.parse(DBConfig.getUriAddress(tableName)), null);
        }
    }

}
