package com.amt.datacache;

import com.amt.bean.StorageBean;
import com.amt.util.StorageConfig;

import java.util.HashMap;

public class StorageManager {
    private static HashMap<Integer, StorageBean> sStorageMap = new HashMap<Integer, StorageBean>();
    static {
        sStorageMap.put(new Integer(StorageConfig.PortId.SDCARD_PORT),
                new StorageBean(StorageConfig.PortId.SDCARD_PORT));
        sStorageMap.put(new Integer(StorageConfig.PortId.USB1_PORT),
                new StorageBean(StorageConfig.PortId.USB1_PORT));
        sStorageMap.put(new Integer(StorageConfig.PortId.USB2_PORT),
                new StorageBean(StorageConfig.PortId.USB2_PORT));
    }

    private static StorageManager sStorageManager;
    public static StorageManager instance() {
        if (sStorageManager == null) {
            sStorageManager = new StorageManager();
        }
        return sStorageManager;
    }
}
