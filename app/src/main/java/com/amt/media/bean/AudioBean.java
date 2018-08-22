package com.amt.media.bean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;

import com.amt.media.datacache.StorageManager;
import com.amt.media.util.PingYingTool;
import com.amt.media.util.StorageConfig;
import com.amt.mediaservice.MediaApplication;
import com.amt.util.DebugLog;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

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

    public AudioBean(MediaBean mediaBean) {
        setPortId(mediaBean.getPortId());
        setFileType(mediaBean.getFileType());
        setFilePath(mediaBean.getFilePath());
        setFileName(mediaBean.getFileName());
        setFileNamePY(mediaBean.getFileNamePY());
        setFileSize(mediaBean.getFileSize());
        setLastDate(mediaBean.getLastDate());
        setOnlyreadFlag(mediaBean.getOnlyreadFlag());
        setId3Flag(mediaBean.getId3Flag());
        setUnsupportFlag(mediaBean.getUnsupportFlag());
        setCollectFlag(mediaBean.getCollectFlag());
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

    public void parseID3() {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        FileInputStream is = null;
        try {
            File file = new File(getFilePath());
            if (file.exists()) {
                is = new FileInputStream(getFilePath());
                FileDescriptor fd = is.getFD();
                retriever.setDataSource(fd);

                if (getId3Flag() == 0) {
                    String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                    if (title != null && !title.equals("")) {
                        setTitle(title);
                    }
                    setArtist(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                    setAlbum(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                    setComposer(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER));
                    setGenre(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
                    String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    if (duration != null && !duration.equals("")) {
                        setDuration(Integer.valueOf(duration));
                    }
                    if (title != null) {
                        String py = PingYingTool.parseString(title);
                        setTitlePY(py);
                    }
                    if (getArtist() != null) {
                        String py = PingYingTool.parseString(getArtist());
                        if (py != null && py.length() > 0) {
                            py = py.toUpperCase();
                            setArtistPY(py);
                        }
                    }
                    if (getAlbum() != null) {
                        String py = PingYingTool.parseString(getAlbum());
                        if (py != null && py.length() > 0) {
                            py = py.toUpperCase();
                            setAlbumPY(py);
                        }
                    }
                    // 如果Thumbnail存在，不用通过MMR解析。
                    // Thumbnail不存在，就通过MMR解析，解析成功才设置mThumbnailPath。
                    String bitmapPath = StorageConfig.getStoragePath(getPortId()) +
                            "/.geelyCache/" + getMD5Str(is);
                    if (saveBitmap(bitmapPath, retriever)) {
                        thumbnailPath = bitmapPath;
                    } else {
                        thumbnailPath = null;
                    }
                    setId3Flag(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setId3Flag(-1);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            retriever.release();
        }
    }
}
