package com.amt.media.scan;

import com.amt.aidl.MediaDef;
import com.amt.media.bean.StorageBean;
import com.amt.media.database.MediaDbHelper;
import com.amt.media.datacache.StorageManager;
import com.amt.media.util.MediaUtil;
import com.amt.media.util.StorageConfig;
import com.amt.util.DebugLog;

public class ScanManager {
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

    public void scanStorage(int portId) {
        StorageManager.instance().updateStorageState(portId, StorageBean.MOUNTED);
        getScanRootPathThread().addDeviceTask(StorageConfig.getStoragePath(portId));
    }

    public void removeStorage(int portId) {
        StorageManager.instance().updateStorageState(portId, StorageBean.EJECT);
    }
}
