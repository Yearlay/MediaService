
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
        updateCollectDatas();
        updateID3Info();
    }

    /**
     * 修复下面场景：
     * 场景_A：U_A插入USB_1；收藏了Audio_A；拔出U_A插入到USB_2; 需要更新收藏的Audio_A的信息。
     * 场景_B：U_A插入USB_1；收藏了Audio_A；拔出U_A，将U_B插入USB_1; 拔出U_B，重新插入U_A。
     */
    public void updateCollectDatas() {
        DebugClock debugClock = new DebugClock();
          // 收藏表，需要校验一下。
//        mMediaDbHelper.setStartFlag(true);
//        mMediaDbHelper.updateCollectInfoByFileExist(FileType.AUDIO);
//        mMediaDbHelper.updateCollectInfoByFileExist(FileType.VIDEO);
//        mMediaDbHelper.updateCollectInfoByFileExist(FileType.IMAGE);
//        mMediaDbHelper.setStartFlag(false);
        // 需要针对收藏表来检验对应的媒体表。
//        mMediaDbHelper.setStartFlag(true);
//        mMediaDbHelper.updateMediaInfoAccordingToCollect(FileType.AUDIO); // 参照"table_audio19"表更新对应的媒体表。
//        mMediaDbHelper.updateMediaInfoAccordingToCollect(FileType.VIDEO); // 参照"table_video19"表更新对应的媒体表。
//        mMediaDbHelper.updateMediaInfoAccordingToCollect(FileType.IMAGE); // 参照"table_image19"表更新对应的媒体表。
//        mMediaDbHelper.setStartFlag(false);
        debugClock.calculateTime(TAG, "ID3ParseThread#updateCollectDatas");
    }

    private void updateID3Info() {
        ArrayList<StorageBean> storageBeans = new ArrayList<StorageBean>();
        storageBeans.add(StorageManager.instance().getStorageBean(StorageConfig.PortId.SDCARD_PORT));
        storageBeans.add(StorageManager.instance().getStorageBean(StorageConfig.PortId.USB1_PORT));
        storageBeans.add(StorageManager.instance().getStorageBean(StorageConfig.PortId.USB2_PORT));

        DebugClock debugClock = new DebugClock();
        for (StorageBean storageBean : storageBeans) {
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
