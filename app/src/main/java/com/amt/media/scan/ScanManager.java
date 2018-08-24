package com.amt.media.scan;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.amt.media.bean.StorageBean;
import com.amt.media.database.MediaDbHelper;
import com.amt.media.datacache.StorageManager;
import com.amt.media.util.MediaUtil;
import com.amt.media.util.StorageConfig;
import com.amt.mediaservice.MediaApplication;
import com.amt.util.DebugLog;

import java.util.ArrayList;

public class ScanManager {
    public static final String SCAN_TYPE_KEY = "scan_type";
    public static final String SCAN_FILE_PATH = "scan_file_path";

    // 扫描的类型：扫描磁盘，磁盘拔出，ID3解析。
    public static final int REMOVE_STORAGE = 1;
    public static final int MOUNT_STORAGE = 2;
    public static final int BEGIN_SCAN_STORAGE = 3;
    public static final int END_SCAN_STORAGE = 4;
    public static final int BEGIN_ID3_PARSE = 5;
    public static final int END_ID3_PARSE = 6;

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
        if (StorageConfig.fileCheckMounted(StorageConfig.SDCARD_STORAGE_PATH)) {
            DebugLog.e(TAG, "scanAllStorage mountStorage SDCARD_PORT");
            mountStorage(StorageConfig.PortId.SDCARD_PORT);
        }
        if (StorageConfig.fileCheckMounted(StorageConfig.USB1_STORAGE_PATH)) {
            DebugLog.e(TAG, "scanAllStorage mountStorage USB1_PORT");
            mountStorage(StorageConfig.PortId.USB1_PORT);
        }
        if (StorageConfig.fileCheckMounted(StorageConfig.USB2_STORAGE_PATH)) {
            DebugLog.e(TAG, "scanAllStorage mountStorage USB2_PORT");
            mountStorage(StorageConfig.PortId.USB2_PORT);
        }
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
        DebugLog.d(TAG, "operateIntent storagePath: " + storagePath + " && portId:" + portId);
        mHandler.obtainMessage(intent.getIntExtra(SCAN_TYPE_KEY, 0), portId, 0).sendToTarget();
    }

    public void endScanStorage(int portId, int scanState) {
        mHandler.obtainMessage(END_SCAN_STORAGE, portId, scanState);
    }

    public void beginID3Parse() {
        mHandler.obtainMessage(BEGIN_ID3_PARSE).sendToTarget();
    }

    public void endID3Parse() {
        mHandler.obtainMessage(END_ID3_PARSE).sendToTarget();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int portId = msg.arg1;
            switch (msg.what) {
                case REMOVE_STORAGE:
                    removeStorage(portId);
                    break;
                case MOUNT_STORAGE:
                    mountStorage(portId);
                    break;
                case BEGIN_SCAN_STORAGE:
                    beginScanStorage(portId);
                    break;
                case END_SCAN_STORAGE:
                    endScanStorageEx(msg.arg1, msg.arg2);
                    break;
                case BEGIN_ID3_PARSE:
                    beginID3ParseEx();
                    break;
                case END_ID3_PARSE:
                    endID3ParseEx();
                    break;
                default:
                    break;
            }
        }
    };

    private void removeStorage(int portId) {
        DebugLog.d(TAG, "removeStorage portId: " + portId);
        StorageManager.instance().updateStorageState(portId, StorageBean.EJECT);
    }

    private void mountStorage(int portId) {
        DebugLog.d(TAG, "mountStorage portId: " + portId);
        StorageManager.instance().updateStorageState(portId, StorageBean.MOUNTED);
        mHandler.obtainMessage(BEGIN_SCAN_STORAGE, portId, 0).sendToTarget();
    }

    private void beginScanStorage(int portId) {
        DebugLog.d(TAG, "beginScanStorage portId: " + portId);
        StorageBean storageBean = StorageManager.instance().getStorageBean(portId);
        if (storageBean.getState() == StorageBean.MOUNTED) {
            StorageManager.instance().updateStorageState(portId, StorageBean.FILE_SCANNING);
            String storagePath = StorageConfig.getStoragePath(portId);
            DebugLog.d(TAG, "beginScanStorage storagePath:" + storagePath);
            getScanRootPathThread().addDeviceTask(storagePath);
        } else {
            DebugLog.e(TAG, "Ignore beginScanStorage --> portId: " + portId
                    + " && state:" + storageBean.getState());
        }
    }

    private void endScanStorageEx(int portId, int scanState) {
        DebugLog.d(TAG, "endScanStorageEx portId: " + portId + " && scanState:" + scanState);
        StorageManager.instance().updateStorageState(portId, scanState);
    }

    /**
     * 当所有的文件扫描操作结束之后，触发ID3扫描的线程。
     */
    public void beginID3ParseEx() {
        mScanThread = null;
        interruptID3ParseThread();
        mID3ParseThread = new ID3ParseThread();
        mID3ParseThread.setPriority(Thread.MIN_PRIORITY);
        mID3ParseThread.start();

        for (StorageBean storageBean : StorageManager.instance().getDefaultStorageBeans()) {
            if (storageBean.isMounted() && storageBean.getState() != StorageBean.SCAN_ERROR) {
                StorageManager.instance().updateStorageState(storageBean.getPortId(), StorageBean.ID3_PARSING);
            }
        }
    }

    private void interruptID3ParseThread() {
        if (mID3ParseThread != null && mID3ParseThread.isAlive()) {
            DebugLog.i(TAG, "interruptID3ParseThread");
            mID3ParseThread.interrupt();
        }
    }

    public void endID3ParseEx() {
        for (StorageBean storageBean : StorageManager.instance().getDefaultStorageBeans()) {
            if (storageBean.isMounted() && storageBean.getState() != StorageBean.SCAN_ERROR) {
                StorageManager.instance().updateStorageState(storageBean.getPortId(), StorageBean.ID3_PARSE_OVER);
            }
        }
    }
}
