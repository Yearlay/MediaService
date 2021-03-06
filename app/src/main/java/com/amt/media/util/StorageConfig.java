package com.amt.media.util;

import java.io.File;

/**
 * Created by archermind on 2018/8/9.
 */

public class StorageConfig {
    // 设备MountService给过来的路径（可配置，非常量）
    public static String SDCARD_ROOT_PATH_OLD = "/storage/internal_sd";
    public static String USB1_STORAGE_PATH_OLD = "/storage/usb_storage";
    public static String USB2_STORAGE_PATH_OLD = "/storage/usb_storage1";

    // 设备真实路径（可配置，非常量）
    public static String SDCARD_ROOT_PATH = "/mnt/media_rw/internal_sd/0";
    public static String SDCARD_STORAGE_PATH = SDCARD_ROOT_PATH + "/media";
    public static String USB1_STORAGE_PATH = "/mnt/media_rw/usb_storage";
    public static String USB2_STORAGE_PATH = "/mnt/media_rw/usb_storage1";

    public static class PortId {
        public static final int NULL = 0;
        public static final int SDCARD_PORT = 1;
        public static final int USB1_PORT = 2;
        public static final int USB2_PORT = 3;
    }

    public static String getRealPathOfStorage(String path) {
        String storagePath = null;
        if (SDCARD_ROOT_PATH_OLD.equals(path)) {
            storagePath = SDCARD_STORAGE_PATH;
        } else if (USB1_STORAGE_PATH_OLD.equals(path)) {
            storagePath = USB1_STORAGE_PATH;
        } else if (USB2_STORAGE_PATH_OLD.equals(path)) {
            storagePath = USB2_STORAGE_PATH;
        }
        return storagePath;
    }

    // 根据路径来获取设备类型
    public static int getPortId(String filePath) {
        int portID = PortId.NULL;
        if (filePath == null) {
            return portID;
        }
        if (filePath.startsWith(SDCARD_STORAGE_PATH)) {
            return PortId.SDCARD_PORT;
        } else if (filePath.startsWith(USB2_STORAGE_PATH)) {
            return PortId.USB2_PORT;
        } else if (filePath.startsWith(USB1_STORAGE_PATH)) {
            return PortId.USB1_PORT;
        }
        return portID;
    }

    public static String getStoragePath(int portId) {
        String storagePath = null;
        if (portId == PortId.SDCARD_PORT) {
            storagePath = SDCARD_STORAGE_PATH;
        } else if (portId == PortId.USB1_PORT) {
            storagePath = USB1_STORAGE_PATH;
        } else if (portId == PortId.USB2_PORT) {
            storagePath = USB2_STORAGE_PATH;
        }
        return storagePath;
    }

    /**
     *  此方法不可靠：U盘拔出一段时间之后，可能返回true;
     */
    public static boolean fileCheckMounted(String storagePath) {
        if (storagePath == null) {
            return false;
        }
        if (storagePath.contains(StorageConfig.SDCARD_STORAGE_PATH)) {
            return true;
        }
        File file = new File(storagePath);
        if (file.exists() && file.isDirectory() && file.canRead()
                && file.canExecute()) {
            return true;
        }
        return false;
    }
}
