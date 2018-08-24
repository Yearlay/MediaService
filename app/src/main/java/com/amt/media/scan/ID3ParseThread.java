
package com.amt.media.scan;

import java.util.ArrayList;

import com.amt.media.bean.MediaBean;
import com.amt.media.bean.StorageBean;
import com.amt.media.database.MediaDbHelper;
import com.amt.media.util.DBConfig;
import com.amt.media.util.MediaUtil;
import com.amt.util.DebugClock;

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
        for (StorageBean storageBean : StorageManager.instance().getStorageBeans()) {
            if (storageBean.isMounted()) {
                parseMedia(storageBean, MediaUtil.FileType.AUDIO);
                parseMedia(storageBean, MediaUtil.FileType.VIDEO);
                parseMedia(storageBean, MediaUtil.FileType.IMAGE);
            }
        }
        debugClock.calculateTime(TAG, "ID3ParseThread#updateID3Info");
    }

    private void parseMedia(StorageBean storageBean, int fileType) {
        String tableName = DBConfig.getTableName(storageBean.getPortId(), fileType);
        ArrayList<MediaBean> mediaBeans = mMediaDbHelper.query(tableName,
                null, null, false);
        for (MediaBean mediaBean : mediaBeans) {
            mediaBean.parseID3();
        }

        mMediaDbHelper.setStartFlag(true);
        for (MediaBean mediaBean : mediaBeans) {
            mMediaDbHelper.update(mediaBean);
        }
        mMediaDbHelper.setStartFlag(false);
    }
}
