package com.amt.util;

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
    public final static int DATABASE_VERSION = 1;

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
    }
}
