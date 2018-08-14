package com.amt.aidl;

/**
 * Created by hww on 2018/8/14.
 */

public class MediaDef {

    // 蓝牙音乐
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

    //

}
