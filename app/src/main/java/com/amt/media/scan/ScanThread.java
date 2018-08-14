
package com.amt.media.scan;

import java.util.ArrayList;

import com.amt.media.bean.StorageBean;
import com.amt.media.database.MediaDbHelper;
import com.amt.media.datacache.AllMediaList;
import com.amt.media.datacache.StorageManager;
import com.amt.media.jni.ScanJni;
import com.amt.media.util.MediaUtil;
import com.amt.media.util.StorageConfig;
import com.amt.util.DebugClock;
import com.amt.util.DebugLog;

// 设备拔出时，必须停止该线程
public class ScanThread extends Thread {
    private static final String TAG = "ScanThread";

    private MediaDbHelper mediaDbHelper;

    private ArrayList<String> mTaskList = new ArrayList<String>();

    public void addDeviceTask(String filePath) {
        if (mTaskList.size() > 0) {
            for (String storagePath : mTaskList) {
                if (storagePath.equals(filePath)) {
                    return;
                }
            }
        }
        DebugLog.i(TAG, "ScanThread#addDeviceTask filePath: " + filePath);
        mTaskList.add(filePath);
        if (!isAlive()) {
            try {
                start();
            } catch (Exception e) {
            }
        } else {
            DebugLog.i(TAG, "ScanThread#addDeviceTask running, TaskList size: " + mTaskList.size());
        }
    }
    private String mScanPath;

    public String getScanPath() {
        return mScanPath;
    }

    public ScanThread(MediaDbHelper mediaDbHelper) {
        this.mediaDbHelper = mediaDbHelper;
    }

    public void run() {
        while (mTaskList.size() > 0) {
            doDeviceTasks();
        }
    }
    
    private void doDeviceTasks() {
        while (mTaskList.size() > 0) {
            mScanPath = mTaskList.get(0);
            DebugLog.i(TAG, "Begin scan storagePath: " + mScanPath);
            scanStorage(mScanPath);
            DebugLog.i(TAG, "End scan storagePath: " + mScanPath);
            mTaskList.remove(mScanPath);
        }
        mScanPath = null;
    }
    
    private void scanStorage(String storagePath) {
        DebugLog.i(TAG, "scanStorage Path: " + storagePath);
        AllMediaList allMediaList = AllMediaList.instance(mediaDbHelper.getContext());
        int portId = StorageConfig.getPortId(storagePath);
        StorageBean storageBean = StorageManager.instance().getStorageBean(portId);
        if (storageBean.getState() >= StorageBean.MOUNTED) {
            // 更新扫描状态为"文件正在扫描中"。
            StorageManager.instance().updateStorageState(portId, StorageBean.FILE_SCANNING);
        }
        int mediaCount = 0;
        int scanState = StorageBean.FILE_SCANNING;
        try {
            // TODO：清空数据库表的内容是临时的做法，需要针对重启设备校验文件来设计方案。
            int imageCount = mediaDbHelper.queryMedia(portId, MediaUtil.FileType.IMAGE).size();
            int audioCount = mediaDbHelper.queryMedia(portId, MediaUtil.FileType.AUDIO).size();
            int videoCount = mediaDbHelper.queryMedia(portId, MediaUtil.FileType.VIDEO).size();
            DebugLog.d(TAG, " Scan check imageCount: " + imageCount
            		+ " && audioCount: " + audioCount + " && videoCount:" + videoCount);
            mediaCount = jniScanRootPath(storagePath, 1);
            if (mediaCount != (imageCount + audioCount + videoCount)) {
                DebugLog.d(TAG, " Scan check failed; Begin rescan !!!!!");
                mediaDbHelper.clearStorageData(portId);
                mediaDbHelper.setStartFlag(true);
                mediaCount = jniScanRootPath(storagePath, 0);
                mediaDbHelper.setStartFlag(false);
            } else {
                DebugLog.d(TAG, " Scan check successful!!!");
            }
            scanState = StorageBean.FILE_SCAN_OVER;
        } catch (Exception e) {
            scanState = StorageBean.SCAN_ERROR;
            e.printStackTrace();
        }
        // FILE_SCAN_OVER 或者是 SCAN_ERROR。
        StorageManager.instance().updateStorageState(portId, scanState);
    }

    private int jniScanRootPath(String filePath, int onlyGetMediaSizeFlag) {
        // TODO: 如果是目录重新扫描，应该是需要先删除与这个目录有关的数据库记录的。
        DebugClock debugClock = new DebugClock();
        ScanJni scanJni = new ScanJni(mediaDbHelper);
        int count = scanJni.scanRootPath(filePath, onlyGetMediaSizeFlag);
        debugClock.calculateTime(TAG, getClass().getName()+"#jniScanRootPath onlyGetMediaSizeFlag:" + onlyGetMediaSizeFlag);
        DebugLog.d(TAG, "#jniScanRootPath count:" + count);
        return count;
    }
}
