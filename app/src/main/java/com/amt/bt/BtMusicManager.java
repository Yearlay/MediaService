package com.amt.bt;

import com.amt.aidl.IMediaCallBack;
import com.amt.aidl.MediaDef;
import com.amt.service.MediaServiceBinder;

import static com.amt.aidl.MediaDef.*;

/**
 * Created by hww on 2018/8/13.
 */

public class BtMusicManager {

    private static BtMusicManager mSelf;
    private BtMusicInterface mBtInstance;
    private BtMusicCallBack mCallBack;
    private MediaServiceBinder.ManagerCallBack mBinderCallBack;

    interface BtMusicCallBack {
        void callback(int func, int data);
    }

    private BtMusicManager() {
        mBtInstance = BtMusic_Haoke.getInstance();
        mCallBack = new BtMusicCallBack() {
            @Override
            public void callback(int func, int data) {
                if (mBinderCallBack != null) {
                    mBinderCallBack.onBtMusicCallBack(func, data);
                }
            }
        };
        mBtInstance.setCallBack(mCallBack);
    }

    synchronized public static BtMusicManager getInstance() {
        if (mSelf == null) {
            mSelf = new BtMusicManager();
        }
        return mSelf;
    }

    public static void registerCallBack(MediaServiceBinder.ManagerCallBack callBack) {
        getInstance().mBinderCallBack = callBack;
    }

    public static int funcInt(int funcID) {
        int code = MediaDef.ERROR_CODE_UNKNOWN;
        BtMusicManager manager = getInstance();
        switch (funcID) {
            case FUNC_BTMUSIC_ISBTMUSICCONNECTED:
                code = manager.isBtMusicConnected();
                break;
            case FUNC_BTMUSIC_ISBTCONNECTED:
                code = manager.isBtConnected();
                break;
            case FUNC_BTMUSIC_PLAY:
                code = manager.play();
                break;
            case FUNC_BTMUSIC_PAUSE:
                code = manager.pause();
                break;
            case FUNC_BTMUSIC_STOP:
                code = manager.stop();
                break;
            case FUNC_BTMUSIC_PREV:
                code = manager.prev();
                break;
            case FUNC_BTMUSIC_NEXT:
                code = manager.next();
                break;
            case FUNC_BTMUSIC_ISPLAYING:
                code = manager.isPlaying();
                break;
        }
        return code;
    }

    public static String funcStr(int funcID) {
        String code = null;
        BtMusicManager manager = getInstance();
        switch (funcID) {
            case FUNC_BTMUSIC_GETTITLE:
                code = manager.getTitle();
                break;
            case FUNC_BTMUSIC_GETARTIST:
                code = manager.getArtist();
                break;
            case FUNC_BTMUSIC_GETALBUM:
                code = manager.getAlbum();
                break;
        }
        return code;
    }

    public boolean registerCallBack(String mode, IMediaCallBack callBack) {
        return true;
    }

    boolean unregisterCallBack(String mode) {
        return true;
    }


    /**
     * 获取连接状态avrcp和a2dp协议
     */
    private int isBtMusicConnected() {
        return mBtInstance.isBtMusicConnected();
    }
    /**
     * 获取连接状态hfp协议
     */
    private int isBtConnected() {
        return mBtInstance.isBtConnected();
    }
    /**
     * 播放
     */
    private int play() {
        return mBtInstance.play();
    }
    /**
     * 暂停
     */
    private int pause() {
        return mBtInstance.pause();
    }
    /**
     * 停止
     */
    private int stop() {
        return mBtInstance.stop();
    }
    /**
     * 上一曲
     */
    private int prev() {
        return mBtInstance.prev();
    }
    /**
     * 下一曲
     */
    private int next() {
        return mBtInstance.next();
    }
    /**
     * 播放状态
     */
    private int isPlaying() {
        return mBtInstance.isPlaying();
    }
    /**
     * 获取歌曲标题
     */
    private String getTitle() {
        return mBtInstance.getTitle();
    }
    /**
     * 获取歌曲艺术家
     */
    private String getArtist() {
        return mBtInstance.getArtist();
    }
    /**
     * 获取歌曲专辑名
     */
    private String getAlbum() {
        return mBtInstance.getAlbum();
    }

}
