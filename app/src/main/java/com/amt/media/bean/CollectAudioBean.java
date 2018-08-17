package com.amt.media.bean;

import android.content.ContentValues;
import android.database.Cursor;

import com.amt.media.util.MediaUtil;

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

    public CollectAudioBean(Cursor cursor) {
        super(cursor);
        username = cursor.getString(cursor.getColumnIndex(FIELD_USERNAME));
    }

    public CollectAudioBean(AudioBean audioBean) {
        setPortId(audioBean.getPortId());
        setFileType(audioBean.getFileType());
        setFilePath(audioBean.getFilePath());
        setFileName(audioBean.getFileName());
        setFileNamePY(audioBean.getFileNamePY());
        setFileSize(audioBean.getFileSize());
        setLastDate(audioBean.getLastDate());
        setOnlyreadFlag(audioBean.getOnlyreadFlag());
        setId3Flag(audioBean.getId3Flag());
        setUnsupportFlag(audioBean.getUnsupportFlag());
        setCollectFlag(audioBean.getCollectFlag());

        setTitle(audioBean.getTitle());
        setTitlePY(audioBean.getTitlePY());
        setArtist(audioBean.getArtist());
        setArtistPY(audioBean.getArtistPY());
        setAlbum(audioBean.getAlbum());
        setAlbumPY(audioBean.getAlbumPY());
        setGenre(audioBean.getGenre());
        setComposer(audioBean.getComposer());
        setDuration(audioBean.getDuration());
        setThumbnailPath(audioBean.getThumbnailPath());

        setUsername(MediaUtil.getUserName());
    }
}
