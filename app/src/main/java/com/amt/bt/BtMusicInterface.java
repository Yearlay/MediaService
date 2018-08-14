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
    boolean isBtMusicConnected();
    /**
     * 获取连接状态hfp协议
     */
    boolean isBtConnected();
    /**
     * 打开通道
     */
    boolean open();
    /**
     * 关闭通道
     */
    boolean close();
    /**
     * 播放
     */
    boolean play();
    /**
     * 暂停
     */
    boolean pause();
    /**
     * 停止
     */
    boolean stop();
    /**
     * 上一曲
     */
    boolean prev();
    /**
     * 下一曲
     */
    boolean next();
    /**
     * 播放状态
     */
    boolean isPlaying();
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
