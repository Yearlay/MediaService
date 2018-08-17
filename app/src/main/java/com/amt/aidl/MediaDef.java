package com.amt.aidl;

/**
 * Created by hww on 2018/8/14.
 */

public class MediaDef {
    // 公共定义
    public static final int FALSE = 0;
    public static final int TRUE = 1;
    public static final int SUCCESS = 2;

    // 蓝牙音乐
    public static final int FUNC_BTMUSIC = 100; // 蓝牙音乐函数ID起点
    public static final int FUNC_BTMUSIC_ISBTMUSICCONNECTED = 101;
    public static final int FUNC_BTMUSIC_ISBTCONNECTED = 102;
    public static final int FUNC_BTMUSIC_PLAY = 111;
    public static final int FUNC_BTMUSIC_PAUSE = 112;
    public static final int FUNC_BTMUSIC_STOP = 113;
    public static final int FUNC_BTMUSIC_PREV = 114;
    public static final int FUNC_BTMUSIC_NEXT = 115;
    public static final int FUNC_BTMUSIC_ISPLAYING = 121;
    public static final int FUNC_BTMUSIC_GETTITLE = 131;
    public static final int FUNC_BTMUSIC_GETARTIST = 132;
    public static final int FUNC_BTMUSIC_GETALBUM = 133;

    public static class BtMusicDef {
        public static final int CALLBACK_CONNECT_STATE = 1;
        public static final int CALLBACK_MUSIC_CONNECT_STATE = 2;
        public static final int CALLBACK_PLAY_STATE = 100;
        public static final int CALLBACK_ID3_UPDATE = 200;
        public static class PlayState {
            public static final int PLAYING = 0;
            public static final int PAUSE = 1;
        }
        public static class ConnectState {
            public static final int DISCONNECTED = 0;
            public static final int CONNECTING = 1;
            public static final int CONNECTED = 2;
        }
    }

    // 收音
    public static final int FUNC_RADIO = 200; // 收音函数ID起点
    public static final int FUNC_RADIO_GET_CUR_FREQ = 201;
    public static final int FUNC_RADIO_SET_CUR_FREQ = 202;
    public static final int FUNC_RADIO_GET_ENABLE = 203;
    public static final int FUNC_RADIO_SET_ENABLE = 204;
    public static final int FUNC_RADIO_GET_ST = 205;
    public static final int FUNC_RADIO_GET_COLLECT_STATE = 206;
    public static final int FUNC_RADIO_GET_CUR_BAND = 211;
    public static final int FUNC_RADIO_SET_CUR_BAND = 212;
    public static final int FUNC_RADIO_GET_CUR_AREA = 213;
    public static final int FUNC_RADIO_SET_CUR_AREA = 214;
    public static final int FUNC_RADIO_SET_SCAN = 221;
    public static final int FUNC_RADIO_STOP_SCAN = 222;
    public static final int FUNC_RADIO_SCAN_STORE = 223;
    public static final int FUNC_RADIO_STOP_SCAN_STORE = 224;
    public static final int FUNC_RADIO_SET_PRE_STEP = 231;
    public static final int FUNC_RADIO_SET_NEXT_STEP = 232;
    public static final int FUNC_RADIO_SET_PRE_SEARCH = 233;
    public static final int FUNC_RADIO_SET_NEXT_SEARCH = 234;
    public static final int FUNC_RADIO_SET_PRE_CHANNEL = 235;
    public static final int FUNC_RADIO_SET_NEXT_CHANNEL = 236;
    public static final int FUNC_RADIO_GET_STORE_STATION = 251;
    public static final int FUNC_RADIO_GET_COLLECT_AM_STATION = 252;
    public static final int FUNC_RADIO_GET_COLLECT_FM_STATION = 253;
    public static final int FUNC_RADIO_ADD_COLLECT_STATION = 254;
    public static final int FUNC_RADIO_DEL_COLLECT_STATION = 255;
    public static final int FUNC_RADIO_CLEAR_COLLECT_AM_STATION = 256;
    public static final int FUNC_RADIO_CLEAR_COLLECT_FM_STATION = 257;

    public static class RadioDef {
        public static final int CALLBACK_FREQ = 0;
        public static final int CALLBACK_BAND = 1;
        public static final int CALLBACK_AREA = 2;
        public static final int CALLBACK_ALL_CH = 3;
        public static final int CALLBACK_CUR_CH = 4;
        public static final int CALLBACK_ST = 5;
        public static final int CALLBACK_LOC = 6;
        public static final int CALLBACK_LISTEN = 7;
        public static final int CALLBACK_STATE = 8;
        public static final int CALLBACK_RDS = 9;
        public static final int CALLBACK_RDS_TA = 10;
        public static final int CALLBACK_RDS_AF = 11;
        public static final int CALLBACK_RDS_TP = 12;
        public static final int CALLBACK_RDS_EON = 13;
        public static final int CALLBACK_PARAM = 14;
        public static final int CALLBACK_ENABLE = 15;
        public static final int CALLBACK_STORE_STATION = 31;
        public static final int CALLBACK_COLLECT_FM_STATION = 32;
        public static final int CALLBACK_COLLECT_AM_STATION = 33;

        public static final int BAND_FM = 101;
        public static final int BAND_AM = 111;
    }

    // 本地音乐
    public static final int FUNC_AUDIO = 300; // 本地音乐函数ID起点

    // 视频
    public static final int FUNC_VIDEO = 400; // 视频函数ID起点

    // 图片
    public static final int FUNC_IMAGE = 500; // 图片函数ID起点

    public static final int FUNC_ERROR = 600; // 所有的函数ID终点

    // 回调FLAG
    public static final int CALLBACK_FLAG_ALL = 0x0;
    public static final int CALLBACK_FLAG_BT_MUSIC = 0x1;
    public static final int CALLBACK_FLAG_RADIO = 0x2;
    public static final int CALLBACK_FLAG_AUDIO = 0x4;
    public static final int CALLBACK_FLAG_VIDEO = 0x8;
    public static final int CALLBACK_FLAG_IMAGE = 0x10;
    public static final int CALLBACK_FLAG_OTHER = 0x20;

    // 错误代码
    public static final int ERROR_CODE_UNKNOWN = 10000;
    // ------- 公共
    public static final int ERROR_FILE_NOT_EXIST = 10001; // 文件不存在
    // ------- 蓝牙音乐
    public static final int ERROR_BT_DISCONNECTED = 11001; // 蓝牙未连接

}
