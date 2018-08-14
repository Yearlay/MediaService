package com.amt.radio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class RadioDatabaseHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "RadioLoveStore";
    private final static String TABLE_NAME_AM = "RadioAM";
    private final static String TABLE_NAME_FM = "RadioFM";
    private static final int DBVERSION = 2;

    public RadioDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DBVERSION);
    }

    public String getTableName(RadioBean radioBean) {
        return (radioBean.getRadioType() == RadioBean.RadioType.AMType) ?
                TABLE_NAME_AM : TABLE_NAME_FM;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(getCreateTableString(TABLE_NAME_AM));
        sqLiteDatabase.execSQL(getCreateTableString(TABLE_NAME_FM));
    }

    private String getCreateTableString(String table) {
        String sqlStr = "create table " + table + "(";
        sqlStr += RadioBean.FIELD_USERNAME + " text,";
        sqlStr += RadioBean.FIELD_FREQ + " text,";
        sqlStr += RadioBean.FIELD_NAME + " text";
        sqlStr += ")";
        return sqlStr;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_AM);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FM);
        sqLiteDatabase.execSQL(getCreateTableString(TABLE_NAME_AM));
        sqLiteDatabase.execSQL(getCreateTableString(TABLE_NAME_FM));
    }

    /**
     * 通知内容观察者tableName表数据有变化。
     * @param tableName
     */
    public void notifyChange(String tableName) {
        // TODO 通知内容观察者tableName表数据有变化。
        // mContext.getContentResolver().notifyChange(Uri.parse(DBConfig.getUriAddress(tableName)), null);
    }

    public void insert(RadioBean radioBean) {
        String tableName = getTableName(radioBean);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = radioBean.getContentValues();
        long ret = sqLiteDatabase.insert(tableName, null, contentValues);
        if (ret > 0) {
            notifyChange(tableName);
        }
    }

    public void delete(RadioBean radioBean) {
        String tableName = getTableName(radioBean);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = RadioBean.FIELD_USERNAME + "=? AND " + RadioBean.FIELD_FREQ + "=?";
        String[] whereArgs = new String[] {radioBean.getUsername(), radioBean.getFreq()};
        long ret = sqLiteDatabase.delete(tableName, whereClause, whereArgs);
        if (ret > 0) {
            notifyChange(tableName);
        }
    }

    public void update(RadioBean radioBean) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String tableName = getTableName(radioBean);
        ContentValues contentValues = new ContentValues();
        contentValues = radioBean.getContentValues();
        String whereClause = RadioBean.FIELD_USERNAME + "=? AND " + RadioBean.FIELD_FREQ + "=?";
        String[] whereArgs = new String[] {radioBean.getUsername(), radioBean.getFreq()};
        long ret = sqLiteDatabase.update(tableName, contentValues, whereClause, whereArgs);
        if (ret > 0) {
            notifyChange(tableName);
        }
    }

    public ArrayList<RadioBean> queryFM(String selection, String[] selectionArgs) {
        return query(TABLE_NAME_FM, selection, selectionArgs);
    }

    public ArrayList<RadioBean> queryAM(String selection, String[] selectionArgs) {
        return query(TABLE_NAME_AM, selection, selectionArgs);
    }

    public ArrayList<RadioBean> query(String tableName, String selection, String[] selectionArgs) {
        ArrayList<RadioBean> radioBeans = new ArrayList<RadioBean>();
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(tableName, null, selection, selectionArgs,
                    null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    radioBeans.add(new RadioBean(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return radioBeans;
    }
}
