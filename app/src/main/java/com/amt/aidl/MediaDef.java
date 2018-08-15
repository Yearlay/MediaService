package com.amt.aidl;

/**
 * Created by hww on 2018/8/14.
 */

public class MediaDef {

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
        public static final int MSG_CONNECT_STATE = 1;
        public static final int MSG_MUSIC_CONNECT_STATE = 2;
        public static final int MSG_PLAY_STATE = 100;
        public static final int MSG_ID3_UPDATE = 200;
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

    public static final int CALLBACK_RADIO_DB_LOAD = 0x60;

    // 错误代码
    public static final int ERROR_CODE_UNKNOWN = 10000;
    // ------- 公共
    public static final int ERROR_FILE_NOT_EXIST = 10001; // 文件不存在
    // ------- 蓝牙音乐
    public static final int ERROR_BT_DISCONNECTED = 11001; // 蓝牙未连接

}
