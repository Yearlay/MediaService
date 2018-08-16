package com.amt.media.scan;

import android.content.Intent;

import com.amt.aidl.MediaDef;
import com.amt.media.bean.StorageBean;
import com.amt.media.database.MediaDbHelper;
import com.amt.media.datacache.StorageManager;
import com.amt.media.util.MediaUtil;
import com.amt.media.util.StorageConfig;
import com.amt.util.DebugLog;

public class ScanManager {
    public static final String SCAN_TYPE_KEY = "scan_type";
    public static final String SCAN_FILE_PATH = "scan_file_path";
    public static final int SCAN_STORAGE = 1;
    public static final int REMOVE_STORAGE = 2;

    private static final String TAG = "ScanManager";

    private static ScanManager sScanManager;

    public static ScanManager instance() {
        if (sScanManager == null) {
            sScanManager = new ScanManager();
        }
        return sScanManager;
    }

    public void scanAllStorage() {
        scanStorage(StorageConfig.PortId.SDCARD_PORT);
        scanStorage(StorageConfig.PortId.USB1_PORT);
        scanStorage(StorageConfig.PortId.USB2_PORT);
    }

    private ScanThread mScanThread;
    private ScanThread getScanRootPathThread() {
        if (mScanThread == null) {
            DebugLog.i(TAG, "mScanRootPathThread is null and new ScanRootPathThread!");
            mScanThread = new ScanThread(MediaDbHelper.instance());
            mScanThread.setPriority(Thread.MIN_PRIORITY);
        }
        return mScanThread;
    }

    public void operateIntent(Intent intent) {
        String storagePath = intent.getStringExtra(SCAN_FILE_PATH);
        int portId = StorageConfig.getPortId(storagePath);
        operateScanType(intent.getIntExtra(SCAN_TYPE_KEY, 0), portId);
    }

    private void operateScanType(int scanType, int portId) {
        switch (scanType) {
            case SCAN_STORAGE:
                scanStorage(portId);
                break;
            case REMOVE_STORAGE:
                removeStorage(portId);
                break;
            default:
                break;
        }
    }

    private void scanStorage(int portId) {
        StorageBean storageBean = StorageManager.instance().getStorageBean(portId);
        if (storageBean.getState() == StorageBean.EJECT) {
            StorageManager.instance().updateStorageState(portId, StorageBean.MOUNTED);
            getScanRootPathThread().addDeviceTask(StorageConfig.getStoragePath(portId));
        } else {
            DebugLog.e(TAG, "Ignore scanStorage --> portId: " + portId
                    + " && state:" + storageBean.getState());
        }
    }

    private void removeStorage(int portId) {
        StorageManager.instance().updateStorageState(portId, StorageBean.EJECT);
    }
}
