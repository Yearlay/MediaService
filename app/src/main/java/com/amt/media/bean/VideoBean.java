package com.amt.media.bean;

import android.content.ContentValues;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;

import com.amt.media.util.PingYingTool;
import com.amt.media.util.StorageConfig;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by archermind on 2018/8/9.
 */

public class VideoBean extends MediaBean {
    public static final String FIELD_DURATION = "duration";
    private int duration;
    public static final String FIELD_THUMBNAIL_PATH = "thumbnail_path";
    private String thumbnailPath;
    public static final String FIELD_PLAY_TIME = "playtime";
    private int playTime;

    public ContentValues getContentValues(ContentValues contentValues) {
        contentValues = super.getContentValues(contentValues);
        contentValues.put(FIELD_DURATION, duration);
        contentValues.put(FIELD_THUMBNAIL_PATH, thumbnailPath);
        contentValues.put(FIELD_PLAY_TIME, playTime);
        return contentValues;
    }

    public VideoBean(Cursor cursor) {
        super(cursor);
        duration = cursor.getInt(cursor.getColumnIndex(FIELD_DURATION));
        thumbnailPath = cursor.getString(cursor.getColumnIndex(FIELD_THUMBNAIL_PATH));
        playTime = cursor.getInt(cursor.getColumnIndex(FIELD_PLAY_TIME));
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
                    String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    if (duration != null && !duration.equals("")) {
                        setDuration(Integer.valueOf(duration));
                    }
                    // 如果Thumbnail存在，不用通过MMR解析。 Thumbnail不存在，就通过MMR解析，解析成功才设置mThumbnailPath。
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
