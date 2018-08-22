package com.amt.media.util;

public class UriConfig {
    public static final String URI_HEAD = "content://";
    public static final String MEDIA_DB_AUTOHORITY = "com.haoke.media.contentprovider";

    public static final String URI_SDCARD_AUDIO_ADDR = URI_HEAD + MEDIA_DB_AUTOHORITY + "/" + DBConfig.DBTable.SDCARD_AUDIO;
    public static final String URI_SDCARD_VIDEO_ADDR = URI_HEAD + MEDIA_DB_AUTOHORITY + "/" + DBConfig.DBTable.SDCARD_VIDEO;
    public static final String URI_SDCARD_IMAGE_ADDR = URI_HEAD + MEDIA_DB_AUTOHORITY + "/" + DBConfig.DBTable.SDCARD_IMAGE;

    public static final int URI_SDCARD_AUDIO_TYPE = 1;
    public static final int URI_SDCARD_VIDEO_TYPE = 2;
    public static final int URI_SDCARD_IMAGE_TYPE = 3;

    public static final String URI_USB1_AUDIO_ADDR = URI_HEAD + MEDIA_DB_AUTOHORITY + "/" + DBConfig.DBTable.USB1_AUDIO;
    public static final String URI_USB1_VIDEO_ADDR = URI_HEAD + MEDIA_DB_AUTOHORITY + "/" + DBConfig.DBTable.USB1_VIDEO;
    public static final String URI_USB1_IMAGE_ADDR = URI_HEAD + MEDIA_DB_AUTOHORITY + "/" + DBConfig.DBTable.USB1_IMAGE;

    public static final int URI_USB1_AUDIO_TYPE = 4;
    public static final int URI_USB1_VIDEO_TYPE = 5;
    public static final int URI_USB1_IMAGE_TYPE = 6;

    public static final String URI_USB2_AUDIO_ADDR = URI_HEAD + MEDIA_DB_AUTOHORITY + "/" + DBConfig.DBTable.USB2_AUDIO;
    public static final String URI_USB2_VIDEO_ADDR = URI_HEAD + MEDIA_DB_AUTOHORITY + "/" + DBConfig.DBTable.USB2_VIDEO;
    public static final String URI_USB2_IMAGE_ADDR = URI_HEAD + MEDIA_DB_AUTOHORITY + "/" + DBConfig.DBTable.USB2_IMAGE;

    public static final int URI_USB2_AUDIO_TYPE = 7;
    public static final int URI_USB2_VIDEO_TYPE = 8;
    public static final int URI_USB2_IMAGE_TYPE = 9;

    public static final String URI_COLLECT_AUDIO_ADDR = URI_HEAD + MEDIA_DB_AUTOHORITY + "/" + DBConfig.DBTable.COLLECT_AUDIO;
    public static final String URI_COLLECT_VIDEO_ADDR = URI_HEAD + MEDIA_DB_AUTOHORITY + "/" + DBConfig.DBTable.COLLECT_VIDEO;
    public static final String URI_COLLECT_IMAGE_ADDR = URI_HEAD + MEDIA_DB_AUTOHORITY + "/" + DBConfig.DBTable.COLLECT_IMAGE;

    public static final int URI_COLLECT_AUDIO_TYPE = 10;
    public static final int URI_COLLECT_VIDEO_TYPE = 11;
    public static final int URI_COLLECT_IMAGE_TYPE = 12;

    public static String getTableNameByUriType(int uriType) {
        String tableName = null;
        switch (uriType) {
            case URI_SDCARD_AUDIO_TYPE:
                tableName = DBConfig.DBTable.SDCARD_AUDIO;
                break;
            case URI_SDCARD_VIDEO_TYPE:
                tableName = DBConfig.DBTable.SDCARD_AUDIO;
                break;
            case URI_SDCARD_IMAGE_TYPE:
                tableName = DBConfig.DBTable.SDCARD_IMAGE;
                break;
            case URI_USB1_AUDIO_TYPE:
                tableName = DBConfig.DBTable.USB1_AUDIO;
                break;
            case URI_USB1_VIDEO_TYPE:
                tableName = DBConfig.DBTable.USB1_VIDEO;
                break;
            case URI_USB1_IMAGE_TYPE:
                tableName = DBConfig.DBTable.USB1_IMAGE;
                break;
            case URI_USB2_AUDIO_TYPE:
                tableName = DBConfig.DBTable.USB2_AUDIO;
                break;
            case URI_USB2_VIDEO_TYPE:
                tableName = DBConfig.DBTable.USB2_VIDEO;
                break;
            case URI_USB2_IMAGE_TYPE:
                tableName = DBConfig.DBTable.USB2_IMAGE;
                break;
            case URI_COLLECT_AUDIO_TYPE:
                tableName = DBConfig.DBTable.COLLECT_AUDIO;
                break;
            case URI_COLLECT_VIDEO_TYPE:
                tableName = DBConfig.DBTable.COLLECT_VIDEO;
                break;
            case URI_COLLECT_IMAGE_TYPE:
                tableName = DBConfig.DBTable.COLLECT_IMAGE;
                break;
            default:
                break;
        }
        return tableName;
    }

    public static String getUriAddress(String tableName) {
        String address = null;
        if (DBConfig.DBTable.SDCARD_AUDIO.equals(tableName)) {
            address = URI_SDCARD_AUDIO_ADDR;
        } else if (DBConfig.DBTable.SDCARD_VIDEO.equals(tableName)) {
            address = URI_SDCARD_VIDEO_ADDR;
        } else if (DBConfig.DBTable.SDCARD_IMAGE.equals(tableName)) {
            address = URI_SDCARD_IMAGE_ADDR;
        } else if (DBConfig.DBTable.USB1_AUDIO.equals(tableName)) {
            address = URI_USB1_AUDIO_ADDR;
        } else if (DBConfig.DBTable.USB1_VIDEO.equals(tableName)) {
            address = URI_USB1_VIDEO_ADDR;
        } else if (DBConfig.DBTable.USB1_IMAGE.equals(tableName)) {
            address = URI_USB1_IMAGE_ADDR;
        } else if (DBConfig.DBTable.USB2_AUDIO.equals(tableName)) {
            address = URI_USB2_AUDIO_ADDR;
        } else if (DBConfig.DBTable.USB2_VIDEO.equals(tableName)) {
            address = URI_USB2_VIDEO_ADDR;
        } else if (DBConfig.DBTable.USB2_IMAGE.equals(tableName)) {
            address = URI_USB2_IMAGE_ADDR;
        } else if (DBConfig.DBTable.COLLECT_AUDIO.equals(tableName)) {
            address = URI_COLLECT_AUDIO_ADDR;
        } else if (DBConfig.DBTable.COLLECT_VIDEO.equals(tableName)) {
            address = URI_COLLECT_VIDEO_ADDR;
        } else if (DBConfig.DBTable.COLLECT_IMAGE.equals(tableName)) {
            address = URI_COLLECT_IMAGE_ADDR;
        }
        return address;
    }
}
