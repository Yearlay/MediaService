package com.amt.media.bean;

import com.amt.media.util.StorageConfig;

/**
 * Created by archermind on 2018/8/9.
 */

public class StorageBean {
    private int id;
    /**
     * 磁盘的挂载端口
     * 0：未知，其他。
     * 1：sdcard port.
     * 2. usb1 port.
     * 3. usb2 port.
     */
    private int portId;
    private String storagePath;
    private String storageName;
    private long storageSize;

    /**
     * 0: 可读可写。 1：只读。
     */
    private int onlyReadFlag;

    public static final int EJECT = 0;
    public static final int MOUNTED = 1;
    public static final int FILE_SCANNING = 2;
    public static final int SCAN_ERROR = 3;
    public static final int FILE_SCAN_OVER = 4;
    public static final int ID3_PARSING = 5;
    public static final int ID3_PARSE_OVER = 6;
    /**
     * 磁盘的状态：
     * 0：Eject状态。未挂载状态。
     * 1：已经挂载，未进行扫描。
     * 2：正在进行文件扫描。
     * 3：文件扫描错误状态：不会进行ID3扫描，扫描结束。
     * 4：完成文件扫描。未进行媒体ID3解析。
     * 5：文件扫描完成。正在进行媒体ID3解析。
     * 6：文件扫描完成。ID3解析完成。
     */
    private int state;

    private int audioCount;
    private int videoCount;
    private int imageCount;

    public StorageBean(int portId) {
        this.portId = portId;
        this.storagePath = StorageConfig.getStoragePath(portId);
    }

    public void updateStorageInfo() { //TODO
        this.storageName = "";
        this.storageSize = 0;
        this.onlyReadFlag = 0;
    }

    public void updateMediaCount(int audioCount, int videoCount, int imageCount) {
        this.audioCount = audioCount;
        this.videoCount = videoCount;
        this.imageCount = imageCount;
    }

    @Override
    public String toString() {
        return "StorageBean{" +
                "id=" + id +
                ", portId=" + portId +
                ", storagePath='" + storagePath + '\'' +
                ", storageName='" + storageName + '\'' +
                ", storageSize=" + storageSize +
                ", onlyReadFlag=" + onlyReadFlag +
                ", state=" + state +
                ", audioCount=" + audioCount +
                ", videoCount=" + videoCount +
                ", imageCount=" + imageCount +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPortId() {
        return portId;
    }

    public void setPortId(int portId) {
        this.portId = portId;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public long getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(long storageSize) {
        this.storageSize = storageSize;
    }

    public int getOnlyReadFlag() {
        return onlyReadFlag;
    }

    public void setOnlyReadFlag(int onlyReadFlag) {
        this.onlyReadFlag = onlyReadFlag;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getAudioCount() {
        return audioCount;
    }

    public void setAudioCount(int audioCount) {
        this.audioCount = audioCount;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(int videoCount) {
        this.videoCount = videoCount;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public boolean isMounted() {
        return state >= MOUNTED;
    }

}
