package com.amt.media.scan;

import com.amt.media.bean.StorageBean;
import com.amt.media.database.MediaDbHelper;
import com.amt.media.util.StorageConfig;

import java.util.ArrayList;
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

    public StorageBean getStorageBean(int portId) {
        return sStorageMap.get(new Integer(portId));
    }

    public ArrayList<StorageBean> getStorageBeans() {
        ArrayList<StorageBean> storageBeans = new ArrayList<StorageBean>();
        storageBeans.add(getStorageBean(StorageConfig.PortId.SDCARD_PORT));
        storageBeans.add(getStorageBean(StorageConfig.PortId.USB1_PORT));
        storageBeans.add(getStorageBean(StorageConfig.PortId.USB2_PORT));
        return storageBeans;
    }

    /**
     * 更新磁盘扫描的状态。该方法只能在主线程中调用。
     * @param portId
     * @param state
     */
    public void updateStorageState(int portId, int state) {
        StorageBean storageBean = sStorageMap.get(new Integer(portId));
        if (storageBean.getState() == state) {
            return;
        }
        storageBean.setState(state);
        MediaDbHelper.instance().update(storageBean);
    }
}
