package com.amt.media.scan;

import android.provider.MediaStore;

import com.amt.media.bean.MediaBean;
import com.amt.media.bean.StorageBean;
import com.amt.media.database.MediaDbHelper;
import com.amt.media.datacache.StorageManager;
import com.amt.media.util.DBConfig;
import com.amt.media.util.MediaUtil;
import com.amt.media.util.StorageConfig;
import com.amt.util.DebugClock;
import com.amt.util.DebugLog;

import java.io.File;
import java.util.ArrayList;

/**
 * 修复下面场景：
 * 场景_A：U_A插入USB_1；收藏了Audio_A；拔出U_A插入到USB_2; 需要更新收藏的Audio_A的信息。
 * 场景_B：U_A插入USB_1；收藏了Audio_A；拔出U_A，将U_B插入USB_1; 拔出U_B，重新插入U_A。
 */
public class CollectLogic {
    private static final String TAG = "CollectLogic";

    public static void fixLogic() {
        DebugClock debugClock = new DebugClock();
        // 优先更新收藏表。
        fixA(MediaUtil.FileType.AUDIO);
        fixA(MediaUtil.FileType.VIDEO);
        fixA(MediaUtil.FileType.IMAGE);
        // 再根据收藏表更新媒体表。
        fixB(MediaUtil.FileType.AUDIO);
        fixB(MediaUtil.FileType.VIDEO);
        fixB(MediaUtil.FileType.IMAGE);
        debugClock.calculateTime(TAG, "CollectLogic#fixLogic");
    }

    /**
     * 场景_A：U_A插入USB_1；收藏了Audio_A；拔出U_A插入到USB_2; 需要更新收藏的Audio_A的信息。
     * 更新字段：filePath 和 portId
     * @param fileType
     */
    private static void fixA(int fileType) {
        String tableName = null;
        if (fileType == MediaUtil.FileType.AUDIO) {
            tableName = DBConfig.DBTable.COLLECT_AUDIO;
        } else if (fileType == MediaUtil.FileType.VIDEO) {
            tableName = DBConfig.DBTable.COLLECT_VIDEO;
        } else if (fileType == MediaUtil.FileType.IMAGE) {
            tableName = DBConfig.DBTable.COLLECT_IMAGE;
        }
        MediaDbHelper mediaDbHelper = MediaDbHelper.instance();
        // 获得收藏表中的所有数据，allFlag为true。
        ArrayList<MediaBean> collectList = mediaDbHelper.query(tableName,
                null, null, true);
        if (collectList.size() > 0) {
            mediaDbHelper.setStartFlag(true);
            for (MediaBean collectBean : collectList) {
                File oldFile = new File(collectBean.getFilePath());
                if (!oldFile.exists()) { // 对应的文件不存在的情况。
                    DebugLog.e(TAG, "This file is not exist: " + collectBean.getFilePath());
                    int oldPortId = collectBean.getPortId();
                    String oldDevicePath = StorageConfig.getStoragePath(oldPortId);
                    String filePath = collectBean.getFilePath();
                    String secondPath = filePath.substring(filePath.indexOf(oldDevicePath) + oldDevicePath.length(), filePath.length());

                    File checkFile = null;
                    int newPortId = StorageConfig.PortId.NULL;
                    if (MediaUtil.DEVICE_PATH_USB_1.equals(oldDevicePath)) {
                        checkFile = new File(MediaUtil.DEVICE_PATH_USB_2 + secondPath);
                        newPortId = StorageConfig.PortId.USB2_PORT;
                    } else if (MediaUtil.DEVICE_PATH_USB_2.equals(oldDevicePath)) {
                        checkFile = new File(MediaUtil.DEVICE_PATH_USB_1 + secondPath);
                        newPortId = StorageConfig.PortId.USB1_PORT;
                    }
                    if (checkFile != null && checkFile.exists()) { // 在另外的一个U盘中存在。
                        String newFilePath = checkFile.getAbsolutePath();
                        collectBean.setFilePath(newFilePath); // 更新filePath。
                        collectBean.setPortId(newPortId);     // 更新portId。
                        mediaDbHelper.update(collectBean);
                    }
                }
            }
            mediaDbHelper.setStartFlag(false);
        }
    }

    /**
     * 场景_B：U_A插入USB_1；收藏了Audio_A；拔出U_A，将U_B插入USB_1; 拔出U_B，重新插入U_A。
     * @param fileType
     */
    private static void fixB(int fileType) {
        String collectTableName = null;
        if (fileType == MediaUtil.FileType.AUDIO) {
            collectTableName = DBConfig.DBTable.COLLECT_AUDIO;
        } else if (fileType == MediaUtil.FileType.VIDEO) {
            collectTableName = DBConfig.DBTable.COLLECT_VIDEO;
        } else if (fileType == MediaUtil.FileType.IMAGE) {
            collectTableName = DBConfig.DBTable.COLLECT_IMAGE;
        }
        MediaDbHelper mediaDbHelper = MediaDbHelper.instance();
        // 只获得设备Mounted状态的数据，所以allFlag为false。
        ArrayList<MediaBean> collectList = mediaDbHelper.query(collectTableName,
                null, null, false);

        if (collectList.size() > 0) {
            mediaDbHelper.setStartFlag(true);
            for (MediaBean collectBean : collectList) {
                String tableName = DBConfig.getTableName(collectBean.getPortId(), fileType);
                // TODO 效率较低。后续考虑替代方案。
                ArrayList<MediaBean> list = mediaDbHelper.query(tableName,
                        MediaBean.FIELD_FILE_PATH + "=?",
                        new String[]{collectBean.getFilePath()}, false);
                if (list.size() == 1) {
                    MediaBean mediaBean = list.get(0);
                    if (mediaBean.getCollectFlag() != 1) {
                        mediaBean.setCollectFlag(1);
                        mediaDbHelper.update(mediaBean);
                    }
                } else {
                    DebugLog.e(TAG, "fixB exception list size: " + list.size() +
                            " collect path: " + collectBean.getFilePath());
                }
            }
            mediaDbHelper.setStartFlag(false);
        }
    }
}
