package com.amt.media.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.amt.media.util.DBConfig;
import com.amt.media.util.UriConfig;
import com.amt.mediaservice.MediaApplication;

public class MediaContentProvider extends ContentProvider {

    private static final UriMatcher mUriMatcher  = new UriMatcher(UriMatcher.NO_MATCH);
    private MediaDbHelper mMediaDbHelper = null;

    static {
        mUriMatcher.addURI(UriConfig.MEDIA_DB_AUTOHORITY, DBConfig.DBTable.SDCARD_AUDIO, UriConfig.URI_SDCARD_AUDIO_TYPE);
        mUriMatcher.addURI(UriConfig.MEDIA_DB_AUTOHORITY, DBConfig.DBTable.SDCARD_VIDEO, UriConfig.URI_SDCARD_VIDEO_TYPE);
        mUriMatcher.addURI(UriConfig.MEDIA_DB_AUTOHORITY, DBConfig.DBTable.SDCARD_IMAGE, UriConfig.URI_SDCARD_IMAGE_TYPE);

        mUriMatcher.addURI(UriConfig.MEDIA_DB_AUTOHORITY, DBConfig.DBTable.USB1_AUDIO, UriConfig.URI_USB1_AUDIO_TYPE);
        mUriMatcher.addURI(UriConfig.MEDIA_DB_AUTOHORITY, DBConfig.DBTable.USB1_VIDEO, UriConfig.URI_USB1_VIDEO_TYPE);
        mUriMatcher.addURI(UriConfig.MEDIA_DB_AUTOHORITY, DBConfig.DBTable.USB1_IMAGE, UriConfig.URI_USB1_IMAGE_TYPE);

        mUriMatcher.addURI(UriConfig.MEDIA_DB_AUTOHORITY, DBConfig.DBTable.USB2_AUDIO, UriConfig.URI_USB2_AUDIO_TYPE);
        mUriMatcher.addURI(UriConfig.MEDIA_DB_AUTOHORITY, DBConfig.DBTable.USB2_VIDEO, UriConfig.URI_USB2_VIDEO_TYPE);
        mUriMatcher.addURI(UriConfig.MEDIA_DB_AUTOHORITY, DBConfig.DBTable.USB2_IMAGE, UriConfig.URI_USB2_IMAGE_TYPE);

        mUriMatcher.addURI(UriConfig.MEDIA_DB_AUTOHORITY, DBConfig.DBTable.SDCARD_AUDIO, UriConfig.URI_SDCARD_AUDIO_TYPE);
        mUriMatcher.addURI(UriConfig.MEDIA_DB_AUTOHORITY, DBConfig.DBTable.SDCARD_VIDEO, UriConfig.URI_SDCARD_VIDEO_TYPE);
        mUriMatcher.addURI(UriConfig.MEDIA_DB_AUTOHORITY, DBConfig.DBTable.SDCARD_IMAGE, UriConfig.URI_SDCARD_IMAGE_TYPE);

        mUriMatcher.addURI(UriConfig.MEDIA_DB_AUTOHORITY, DBConfig.DBTable.STORAGR, UriConfig.URI_STORAGR_TYPE);
    }

    @Override
    public boolean onCreate() {
        Context context = MediaApplication.getInstance();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        String tableName = UriConfig.getTableNameByUriType(mUriMatcher.match(uri));
        SQLiteDatabase db = MediaDbHelper.instance().getWritableDatabase();
        if (tableName != null) {
            cursor = db.query(tableName, projection, selection, selectionArgs, null, null, null);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        String tableName = UriConfig.getTableNameByUriType(mUriMatcher.match(uri));
        SQLiteDatabase db = MediaDbHelper.instance().getWritableDatabase();
        if (tableName != null) {
            db.insert(tableName, null, contentValues);
        }
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = UriConfig.getTableNameByUriType(mUriMatcher.match(uri));
        SQLiteDatabase db = MediaDbHelper.instance().getWritableDatabase();
        int ret = 0;
        if (tableName != null) {
            db.delete(tableName, selection, selectionArgs);
        }
        return ret;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = UriConfig.getTableNameByUriType(mUriMatcher.match(uri));
        SQLiteDatabase db = MediaDbHelper.instance().getWritableDatabase();
        int ret = 0;
        if (tableName != null) {
            ret = db.update(tableName, contentValues, selection, selectionArgs);
        }
        return ret;
    }
}
