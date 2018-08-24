package com.amt.media.util;

import com.amt.media.bean.AudioBean;
import com.amt.media.bean.CollectAudioBean;
import com.amt.media.bean.CollectImageBean;
import com.amt.media.bean.CollectVideoBean;
import com.amt.media.bean.ImageBean;
import com.amt.media.bean.MediaBean;
import com.amt.media.bean.VideoBean;

/**
 * Created by archermind on 2018/8/9.
 */

public class DBConfig {
    /**
     * 数据库的名称："media_db"
     */
    public final static String DATABASE_NAME = "media_db";
    /**
     * 数据库的版本号：当前是1。
     */
    public final static int DATABASE_VERSION = 3;

    public class DBTable {
        public final static String SDCARD_AUDIO = "sdcard_audio";
        public final static String SDCARD_VIDEO = "sdcard_video";
        public final static String SDCARD_IMAGE = "sdcard_image";

        public final static String USB1_AUDIO = "usb1_audio";
        public final static String USB1_VIDEO = "usb1_video";
        public final static String USB1_IMAGE = "usb1_image";

        public final static String USB2_AUDIO = "usb2_audio";
        public final static String USB2_VIDEO = "usb2_video";
        public final static String USB2_IMAGE = "usb2_image";

        public final static String COLLECT_AUDIO = "collect_audio";
        public final static String COLLECT_VIDEO = "collect_video";
        public final static String COLLECT_IMAGE = "collect_image";

        public final static String STORAGR = "storage";
    }

    public static String getTableName(int portId, int fileType) {
        String tableName = null;
        if (portId == StorageConfig.PortId.SDCARD_PORT) {
            if (fileType == MediaUtil.FileType.AUDIO) {
                tableName = DBTable.SDCARD_AUDIO;
            } else if (fileType == MediaUtil.FileType.VIDEO) {
                tableName = DBTable.SDCARD_VIDEO;
            } else if (fileType == MediaUtil.FileType.IMAGE) {
                tableName = DBTable.SDCARD_IMAGE;
            }
        } else if (portId == StorageConfig.PortId.USB1_PORT) {
            if (fileType == MediaUtil.FileType.AUDIO) {
                tableName = DBTable.USB1_AUDIO;
            } else if (fileType == MediaUtil.FileType.VIDEO) {
                tableName = DBTable.USB1_VIDEO;
            } else if (fileType == MediaUtil.FileType.IMAGE) {
                tableName = DBTable.USB1_IMAGE;
            }
        } else if (portId == StorageConfig.PortId.USB2_PORT) {
            if (fileType == MediaUtil.FileType.AUDIO) {
                tableName = DBTable.USB2_AUDIO;
            } else if (fileType == MediaUtil.FileType.VIDEO) {
                tableName = DBTable.USB2_VIDEO;
            } else if (fileType == MediaUtil.FileType.IMAGE) {
                tableName = DBTable.USB2_IMAGE;
            }
        }
        return tableName;
    }

    public static String getCollectTableName(int fileType) {
        String tableName = null;
        if (fileType == MediaUtil.FileType.AUDIO) {
            tableName = DBTable.COLLECT_AUDIO;
        } else if (fileType == MediaUtil.FileType.VIDEO) {
            tableName = DBTable.COLLECT_VIDEO;
        } else if (fileType == MediaUtil.FileType.IMAGE) {
            tableName = DBTable.COLLECT_IMAGE;
        }
        return tableName;
    }

    public static String getTableName(MediaBean mediaBean) {
        String tableName = null;
        if (mediaBean instanceof CollectAudioBean ||
                mediaBean instanceof CollectVideoBean ||
                mediaBean instanceof CollectImageBean) {
            if (mediaBean.getFileType() == MediaUtil.FileType.AUDIO) {
                tableName = DBTable.COLLECT_AUDIO;
            } else if (mediaBean.getFileType() == MediaUtil.FileType.VIDEO) {
                tableName = DBTable.COLLECT_VIDEO;
            } else if (mediaBean.getFileType() == MediaUtil.FileType.IMAGE) {
                tableName = DBTable.COLLECT_IMAGE;
            }
        } else if (mediaBean.getPortId() == StorageConfig.PortId.SDCARD_PORT) {
            if (mediaBean.getFileType() == MediaUtil.FileType.AUDIO) {
                tableName = DBTable.SDCARD_AUDIO;
            } else if (mediaBean.getFileType() == MediaUtil.FileType.VIDEO) {
                tableName = DBTable.SDCARD_VIDEO;
            } else if (mediaBean.getFileType() == MediaUtil.FileType.IMAGE) {
                tableName = DBTable.SDCARD_IMAGE;
            }
        } else if (mediaBean.getPortId() == StorageConfig.PortId.USB1_PORT) {
            if (mediaBean.getFileType() == MediaUtil.FileType.AUDIO) {
                tableName = DBTable.USB1_AUDIO;
            } else if (mediaBean.getFileType() == MediaUtil.FileType.VIDEO) {
                tableName = DBTable.USB1_VIDEO;
            } else if (mediaBean.getFileType() == MediaUtil.FileType.IMAGE) {
                tableName = DBTable.USB1_IMAGE;
            }
        } else if (mediaBean.getPortId() == StorageConfig.PortId.USB2_PORT) {
            if (mediaBean.getFileType() == MediaUtil.FileType.AUDIO) {
                tableName = DBTable.USB2_AUDIO;
            } else if (mediaBean.getFileType() == MediaUtil.FileType.VIDEO) {
                tableName = DBTable.USB2_VIDEO;
            } else if (mediaBean.getFileType() == MediaUtil.FileType.IMAGE) {
                tableName = DBTable.USB2_IMAGE;
            }
        }
        return tableName;
    }

    public static int getPortId(String tableName) {
        int portId = StorageConfig.PortId.NULL;
        if (DBTable.SDCARD_AUDIO.equals(tableName) ||
                DBTable.SDCARD_VIDEO.equals(tableName) ||
                DBTable.SDCARD_IMAGE.equals(tableName)) {
            portId = StorageConfig.PortId.SDCARD_PORT;
        } else if (DBTable.USB1_AUDIO.equals(tableName) ||
                DBTable.USB1_VIDEO.equals(tableName) ||
                DBTable.USB1_IMAGE.equals(tableName)) {
            portId = StorageConfig.PortId.USB1_PORT;
        } else if (DBTable.USB2_AUDIO.equals(tableName) ||
                DBTable.USB2_VIDEO.equals(tableName) ||
                DBTable.USB2_IMAGE.equals(tableName)) {
            portId = StorageConfig.PortId.USB2_PORT;
        }
        return portId;
    }

    public static int getFileType(String tableName) {
        int fileType = MediaUtil.FileType.NULL;
        if (DBTable.SDCARD_AUDIO.equals(tableName) ||
                DBTable.USB1_AUDIO.equals(tableName) ||
                DBTable.USB2_AUDIO.equals(tableName) ||
                DBTable.COLLECT_AUDIO.equals(tableName)) {
            fileType = MediaUtil.FileType.AUDIO;
        } else if (DBTable.SDCARD_VIDEO.equals(tableName) ||
                DBTable.USB1_VIDEO.equals(tableName) ||
                DBTable.USB2_VIDEO.equals(tableName) ||
                DBTable.COLLECT_VIDEO.equals(tableName)) {
            fileType = MediaUtil.FileType.VIDEO;
        } else if (DBTable.SDCARD_IMAGE.equals(tableName) ||
                DBTable.USB1_IMAGE.equals(tableName) ||
                DBTable.USB2_IMAGE.equals(tableName) ||
                DBTable.COLLECT_IMAGE.equals(tableName)) {
            fileType = MediaUtil.FileType.IMAGE;
        }
        return fileType;
    }

    public static boolean isCollectTable(String tableName) {
        return DBTable.COLLECT_AUDIO.equals(tableName) ||
                DBTable.COLLECT_VIDEO.equals(tableName) ||
                DBTable.COLLECT_IMAGE.equals(tableName);
    }
}
