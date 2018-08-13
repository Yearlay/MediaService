package com.amt.media.bean;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by archermind on 2018/8/9.
 */

public class AudioBean extends MediaBean {
    public static final String FIELD_AUDIO_TITLE = "title";
    private String title;
    public static final String FIELD_AUDIO_TITLE_PY = "title_py";
    private String titlePY;
    public static final String FIELD_AUDIO_ARTIST = "artist";
    private String artist;
    public static final String FIELD_AUDIO_ARTIST_PY = "artist_py";
    private String artistPY;
    public static final String FIELD_AUDIO_ALBUM = "album";
    private String album;
    public static final String FIELD_AUDIO_ALBUM_PY = "album_py";
    private String albumPY;
    public static final String FIELD_AUDIO_COMPOSER = "composer";
    private String composer;
    public static final String FIELD_AUDIO_GENRE = "genre";
    private String genre;
    public static final String FIELD_DURATION = "duration";
    private int duration;
    public static final String FIELD_THUMBNAIL_PATH = "thumbnail_path";
    private String thumbnailPath;
    public static final String FIELD_PLAY_TIME = "playtime";
    private int playTime;

    public AudioBean() {
        super();
    }

    public ContentValues getContentValues(ContentValues contentValues) {
        contentValues = super.getContentValues(contentValues);
        contentValues.put(FIELD_AUDIO_TITLE, title);
        contentValues.put(FIELD_AUDIO_TITLE_PY, titlePY);
        contentValues.put(FIELD_AUDIO_ARTIST, artist);
        contentValues.put(FIELD_AUDIO_ARTIST_PY, artistPY);
        contentValues.put(FIELD_AUDIO_ALBUM, album);
        contentValues.put(FIELD_AUDIO_ALBUM_PY, albumPY);
        contentValues.put(FIELD_AUDIO_COMPOSER, composer);
        contentValues.put(FIELD_AUDIO_GENRE, genre);
        contentValues.put(FIELD_DURATION, duration);
        contentValues.put(FIELD_THUMBNAIL_PATH, thumbnailPath);
        contentValues.put(FIELD_PLAY_TIME, playTime);
        return contentValues;
    }

    public AudioBean(Cursor cursor) {
        super(cursor);
        title = cursor.getString(cursor.getColumnIndex(FIELD_AUDIO_TITLE));
        titlePY = cursor.getString(cursor.getColumnIndex(FIELD_AUDIO_TITLE_PY));
        artist = cursor.getString(cursor.getColumnIndex(FIELD_AUDIO_ARTIST));
        artistPY = cursor.getString(cursor.getColumnIndex(FIELD_AUDIO_ARTIST_PY));
        album = cursor.getString(cursor.getColumnIndex(FIELD_AUDIO_ALBUM));
        albumPY = cursor.getString(cursor.getColumnIndex(FIELD_AUDIO_ALBUM_PY));
        composer = cursor.getString(cursor.getColumnIndex(FIELD_AUDIO_COMPOSER));
        genre = cursor.getString(cursor.getColumnIndex(FIELD_AUDIO_GENRE));
        duration = cursor.getInt(cursor.getColumnIndex(FIELD_DURATION));
        thumbnailPath = cursor.getString(cursor.getColumnIndex(FIELD_THUMBNAIL_PATH));
        playTime = cursor.getInt(cursor.getColumnIndex(FIELD_PLAY_TIME));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitlePY() {
        return titlePY;
    }

    public void setTitlePY(String titlePY) {
        this.titlePY = titlePY;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtistPY() {
        return artistPY;
    }

    public void setArtistPY(String artistPY) {
        this.artistPY = artistPY;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumPY() {
        return albumPY;
    }

    public void setAlbumPY(String albumPY) {
        this.albumPY = albumPY;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }
}
