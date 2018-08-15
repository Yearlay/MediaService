package com.amt.bt;

/**
 * Created by hww on 2018/8/13.
 */

interface BtMusicInterface {
    /**
     * 设置回调
     */
    void setCallBack(BtMusicManager.BtMusicCallBack callBack);
    /**
     * 获取连接状态avrcp和a2dp协议
      */
    int isBtMusicConnected();
    /**
     * 获取连接状态hfp协议
     */
    int isBtConnected();
    /**
     * 打开通道
     */
    int open();
    /**
     * 关闭通道
     */
    int close();
    /**
     * 播放
     */
    int play();
    /**
     * 暂停
     */
    int pause();
    /**
     * 停止
     */
    int stop();
    /**
     * 上一曲
     */
    int prev();
    /**
     * 下一曲
     */
    int next();
    /**
     * 播放状态
     */
    int isPlaying();
    /**
     * 获取歌曲标题
     */
    String getTitle();
    /**
     * 获取歌曲艺术家
     */
    String getArtist();
    /**
     * 获取歌曲专辑名
     */
    String getAlbum();
}
