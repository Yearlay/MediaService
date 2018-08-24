package com.amt.media.datacache;

import com.amt.media.bean.StorageBean;
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

    private ArrayList<StorageListener> mStorageListenerList = new ArrayList<StorageListener>();

    private static StorageManager sStorageManager;
    public static StorageManager instance() {
        if (sStorageManager == null) {
            sStorageManager = new StorageManager();
        }
        return sStorageManager;
    }

    public StorageManager() {

    }

    public StorageBean getStorageBean(int portId) {
        return sStorageMap.get(new Integer(portId));
    }

    public ArrayList<StorageBean> getDefaultStorageBeans() {
        ArrayList<StorageBean> storageBeans = new ArrayList<StorageBean>();
        storageBeans.add(getStorageBean(StorageConfig.PortId.SDCARD_PORT));
        storageBeans.add(getStorageBean(StorageConfig.PortId.USB1_PORT));
        storageBeans.add(getStorageBean(StorageConfig.PortId.USB2_PORT));
        return storageBeans;
    }

    /**
     * 注册磁盘状态的监听
     * @param storageListener
     */
    public void registerStorageListener(StorageListener storageListener) {
        mStorageListenerList.add(storageListener);
    }

    /**
     * 注销磁盘状态的监听
     * @param storageListener
     */
    public void unRegisterStorageListener(StorageListener storageListener) {
        mStorageListenerList.remove(storageListener);
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

        for (StorageListener storageListener : mStorageListenerList) {
            storageListener.onScanStateChange(storageBean);
        }
    }

    /**
     * 更新磁盘媒体的数量。该方法只能在主线程中调用。
     * @param portId
     * @param audioCount
     * @param videoCount
     * @param imageCount
     */
    public void updateStorageMediaCount(int portId, int audioCount, int videoCount, int imageCount) {
        StorageBean storageBean = sStorageMap.get(new Integer(portId));
        if (storageBean.getAudioCount() == audioCount &&
                storageBean.getVideoCount() == videoCount &&
                storageBean.getImageCount() == imageCount) {
            return;
        }
        storageBean.setAudioCount(audioCount);
        storageBean.setVideoCount(videoCount);
        storageBean.setImageCount(imageCount);

        for (StorageListener storageListener : mStorageListenerList) {
            storageListener.onMediaCountChange(storageBean);
        }
    }
}
