
package com.amt.media.scan;

import java.util.ArrayList;

import android.content.Intent;

import com.amt.media.bean.MediaBean;
import com.amt.media.bean.StorageBean;
import com.amt.media.database.MediaDbHelper;
import com.amt.media.datacache.StorageManager;
import com.amt.media.util.DBConfig;
import com.amt.media.util.MediaUtil;
import com.amt.media.util.StorageConfig;
import com.amt.util.DebugClock;
import com.amt.util.DebugLog;

// 设备拔出时，必须停止该线程
public class ID3ParseThread extends Thread {
    private static final String TAG = "ID3ParseThread";

    private MediaDbHelper mMediaDbHelper;
    
    public ID3ParseThread() {
        mMediaDbHelper = MediaDbHelper.instance();
    }

    public void run() {
        DebugClock debugClock = new DebugClock();
        CollectLogic.fixLogic();
        updateID3Info();
        debugClock.calculateTime(TAG, "ID3ParseThread#run");
        ScanManager.instance().endID3Parse();
    }

    private void updateID3Info() {
        DebugClock debugClock = new DebugClock();
        for (StorageBean storageBean : StorageManager.instance().getDefaultStorageBeans()) {
            if (storageBean.isMounted()) {
                parseMedia(storageBean, MediaUtil.FileType.AUDIO);
                parseMedia(storageBean, MediaUtil.FileType.VIDEO);
                parseMedia(storageBean, MediaUtil.FileType.IMAGE);
            }
        }
        debugClock.calculateTime(TAG, "ID3ParseThread#updateID3Info");
    }

    private void parseMedia(StorageBean storageBean, int fileType) {
        MediaDbHelper mediaDbHelper = MediaDbHelper.instance();
        String tableName = DBConfig.getTableName(storageBean.getPortId(), fileType);
        ArrayList<MediaBean> mediaBeans = mediaDbHelper.query(tableName,
                null, null, false);
        for (MediaBean mediaBean : mediaBeans) {
            mediaBean.parseID3();
        }

        mediaDbHelper.setStartFlag(true);
        for (MediaBean mediaBean : mediaBeans) {
            mediaDbHelper.update(mediaBean);
        }
        mediaDbHelper.setStartFlag(false);
    }
}
