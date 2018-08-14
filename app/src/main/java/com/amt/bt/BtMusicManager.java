package com.amt.bt;


import com.amt.aidl.IMediaCallBack;

/**
 * Created by hww on 2018/8/13.
 */

public class BtMusicManager {

    private static BtMusicManager mSelf;
    private BtMusicInterface mBtInstance;
    private BtMusicCallBack mCallBack;

    interface BtMusicCallBack {
        void callback(int msg, int data);
    }

    private BtMusicManager() {
        mBtInstance = BtMusic_Haoke.getInstance();
        mCallBack = new BtMusicCallBack() {
            @Override
            public void callback(int msg, int data) {

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

    public boolean registerCallBack(String mode, IMediaCallBack callBack) {
        return true;
    }

    boolean unregisterCallBack(String mode) {
        return true;
    }


    /**
     * 获取连接状态avrcp和a2dp协议
     */
    private boolean isBtMusicConnected() {
        return mBtInstance.isBtMusicConnected();
    }
    /**
     * 获取连接状态hfp协议
     */
    private boolean isBtConnected() {
        return mBtInstance.isBtConnected();
    }
    /**
     * 播放
     */
    private boolean play() {
        return mBtInstance.play();
    }
    /**
     * 暂停
     */
    private boolean pause() {
        return mBtInstance.pause();
    }
    /**
     * 停止
     */
    private boolean stop() {
        return mBtInstance.stop();
    }
    /**
     * 上一曲
     */
    private boolean prev() {
        return mBtInstance.prev();
    }
    /**
     * 下一曲
     */
    private boolean next() {
        return mBtInstance.next();
    }
    /**
     * 播放状态
     */
    private boolean isPlaying() {
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
