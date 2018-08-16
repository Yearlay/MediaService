package com.amt.media.scan;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

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

    // 扫描的类型：扫描磁盘，磁盘拔出，ID3解析。
    public static final int SCAN_STORAGE = 1;
    public static final int REMOVE_STORAGE = 2;
    public static final int ID3_PARSE = 3;

    private static final String TAG = "ScanManager";

    private static ScanManager sScanManager;
    private ID3ParseThread mID3ParseThread;
    private ScanThread mScanThread;

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
        int scanType = intent.getIntExtra(SCAN_TYPE_KEY, 0);
        mHandler.obtainMessage(scanType, portId, 0).sendToTarget();
    }

    public void beginID3Parse() {
        mHandler.obtainMessage(ID3_PARSE).sendToTarget();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int portId = msg.arg1;
            switch (msg.what) {
                case SCAN_STORAGE:
                    scanStorage(portId);
                    break;
                case REMOVE_STORAGE:
                    removeStorage(portId);
                    break;
                case ID3_PARSE:
                    beginID3ParseThread();
                    break;
                default:
                    break;
            }
        }
    };

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

    /**
     * 当所有的文件扫描操作结束之后，触发ID3扫描的线程。
     */
    public void beginID3ParseThread() {
        mScanThread = null;
        interruptID3ParseThread();
        mID3ParseThread = new ID3ParseThread();
        mID3ParseThread.setPriority(Thread.MIN_PRIORITY);
        mID3ParseThread.start();
    }

    private void interruptID3ParseThread() {
        if (mID3ParseThread != null && mID3ParseThread.isAlive()) {
            DebugLog.i(TAG, "interruptID3ParseThread");
            mID3ParseThread.interrupt();
        }
    }
}
