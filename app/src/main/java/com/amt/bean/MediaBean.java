package com.amt.bean;

import com.amt.util.StorageConfig;

import java.text.SimpleDateFormat;
import java.io.File;
import java.util.Date;

/**
 * Created by archermind on 2018/8/9.
 */
public class MediaBean {
    private int id;

    /**
     * 磁盘的挂载端口
     * 0：未知，其他。
     * 1：sdcard port.
     * 2. usb1 port.
     * 3. usb2 port.
     */
    private int portId;

    /**
     * 文件类型
     * 0：未知媒体类型。
     * 1：音乐。
     * 2：视频。
     * 3：图片。
     */
    private int fileType;

    private String filePath;
    private String fileName;
    private String fileNamePY;
    private long fileSize;

    /**
     * yyyy-MM-dd HH:mm
     */
    private String lastDate;

    /**
     * ID3信息的状态。
     * 0：ID3信息未解析。
     * 1：ID3信息已解析。
     */
    private int id3Flag;

    /**
     * 不支持标志。
     * 0：支持。
     * 1：不支持。
     */
    private int unsupportFlag;

    /**
     * 收藏标志。
     * 0：未收藏。
     * 1：已经收藏。
     */
    private int collectFlag;

    private boolean isSelected;

    public MediaBean() {}

    public MediaBean(String filePath, String fileName, String fileNamePY, int fileType) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileNamePY = fileNamePY;
        this.fileType = fileType;

        this.portId = StorageConfig.getPortId(filePath);

        File file = new File(filePath);
        this.fileSize = file.length();
        Date date = new Date(file.lastModified());
        this.lastDate = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date);
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

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNamePY() {
        return fileNamePY;
    }

    public void setFileNamePY(String fileNamePY) {
        this.fileNamePY = fileNamePY;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public int getId3Flag() {
        return id3Flag;
    }

    public void setId3Flag(int id3Flag) {
        this.id3Flag = id3Flag;
    }

    public int getUnsupportFlag() {
        return unsupportFlag;
    }

    public void setUnsupportFlag(int unsupportFlag) {
        this.unsupportFlag = unsupportFlag;
    }

    public int getCollectFlag() {
        return collectFlag;
    }

    public void setCollectFlag(int collectFlag) {
        this.collectFlag = collectFlag;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
