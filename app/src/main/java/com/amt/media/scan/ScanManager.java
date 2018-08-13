package com.amt.media.scan;

import com.amt.media.util.StorageConfig;

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

    public void scanStorage(int portId) {

    }
}
