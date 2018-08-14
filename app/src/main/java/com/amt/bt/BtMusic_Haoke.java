package com.amt.bt;

import android.os.RemoteException;

import com.amt.aidl.MediaDef;
import com.amt.mediaservice.MediaApplication;
import com.amt.util.DebugLog;
import static com.haoke.btjar.main.BTDef.BTConnState.*;

import com.haoke.aidl.IBTCallBack;
import com.haoke.btjar.main.BTDef;
import com.haoke.serviceif.BTService_IF;

/**
 * Created by hww on 2018/8/13.
 */

class BtMusic_Haoke extends BTService_IF implements BtMusicInterface{

    private static final String TAG = "BtMusic_Haoke";
    private static BtMusic_Haoke mSelf = null;
    private BtMusicManager.BtMusicCallBack mCallBack;

    private BtMusic_Haoke() {
        mMode = com.haoke.define.ModeDef.BTMUSIC;
        mContext = MediaApplication.getInstance();
        mICallBack = new IBTCallBack.Stub() {
            @Override
            public void onDataChange(int mode, int func, int data)
                    throws RemoteException {
                if (mCallBack != null) {
                    int msg = 0;
                    switch (func) {
                        case BTDef.BTFunc.CONN_STATE://101
                            msg = MediaDef.BtMusicDef.MSG_CONNECT_STATE;
                            break;
                        //case BTDef.BTFunc.MUSIC_A2DP_STATE:  //404，声音传输
                        case BTDef.BTFunc.MUSIC_AVRCP_STATE:  //405,蓝牙控制
                            msg = MediaDef.BtMusicDef.MSG_MUSIC_CONNECT_STATE;
                            break;
                        case BTDef.BTFunc.MUSIC_PLAY_STATE://400
                            msg = MediaDef.BtMusicDef.MSG_PLAY_STATE;
                            break;
                        case BTDef.BTFunc.MUSIC_ID3_UPDATE://401
                            msg = MediaDef.BtMusicDef.MSG_ID3_UPDATE;
                            break;
                    }
                    mCallBack.callback(msg, data);
                }
            }
        };
        bindBTService();
    }

    synchronized public static BtMusicInterface getInstance() {
        if (mSelf == null) {
            mSelf = new BtMusic_Haoke();
        }
        return mSelf;
    }

    @Override
    protected void onServiceConn() {
        DebugLog.d(TAG, "onServiceConn");
    }

    /**
     * 设置回调
     */
    @Override
    public void setCallBack(BtMusicManager.BtMusicCallBack callBack) {
        mCallBack = callBack;
    }

    /**
     * 获取连接状态avrcp和a2dp协议
     */
    @Override
    public boolean isBtMusicConnected() {
        boolean enable = false;
        boolean connected = false;
        try {
            enable = mServiceIF.isBTEnable();
            if (enable) {
                int avrcp = mServiceIF.getAvrcpState();
                int a2dp = mServiceIF.getA2dpState();
                connected = (avrcp == CONNECTED) && (a2dp == CONNECTED);
            }
        } catch (Exception e) {
            DebugLog.e(TAG, "isBtMusicConnected e="+e);
        }
        DebugLog.d(TAG, "isBtMusicConnected enable="+enable+";connected="+connected);
        return connected;
    }

    /**
     * 获取连接状态hfp协议
     */
    @Override
    public boolean isBtConnected() {
        try {
            return mServiceIF.getConnState() == CONNECTED;
        } catch (Exception e) {
            DebugLog.e(TAG, "isBtConnected e="+e);
        }
        return false;
    }

    /**
     * 打开通道
     */
    @Override
    public boolean open() {
        try {
            DebugLog.v(TAG, "open()");
            mServiceIF.music_open();
        } catch (Exception e) {
            DebugLog.e(TAG, "open e="+e);
        }
        return true;
    }

    /**
     * 关闭通道
     */
    @Override
    public boolean close() {
        try {
            DebugLog.v(TAG, "close()");
            mServiceIF.music_open();
        } catch (Exception e) {
            DebugLog.e(TAG, "close e="+e);
        }
        return true;
    }

    /**
     * 播放
     */
    @Override
    public boolean play() {
        try {
            //if (MediaInterfaceUtil.mediaCannotPlay()) {
            //    return;
            //}
            //checkSendBtMusicInfoToMeter();
            //boolean focus = BTMusic_IF.getInstance().requestAudioFocus(true);
            //DebugLog.v(TAG, "music_play() focus="+focus);
            //if (focus) {
                open();
                //setBTSource();
                mServiceIF.music_play();
            //}
        } catch (Exception e) {
            DebugLog.e(TAG, "play e=" + e.getMessage());
        }
        return true;
    }

    /**
     * 暂停
     */
    @Override
    public boolean pause() {
        try {
            DebugLog.v(TAG, "pause()");
            mServiceIF.music_pause();
        } catch (Exception e) {
            DebugLog.e(TAG, "pause e=" + e.getMessage());
        }
        return true;
    }

    /**
     * 停止
     */
    @Override
    public boolean stop() {
        try {
            DebugLog.v(TAG, "stop(), but use music_pause!");
            close();
            mServiceIF.music_pause();
        } catch (Exception e) {
            DebugLog.e(TAG, "stop e=" + e.getMessage());
        }
        return true;
    }

    /**
     * 上一曲
     */
    @Override
    public boolean prev() {
        try {
            //if (MediaInterfaceUtil.mediaCannotPlay()) {
            //    return;
            //}
            //boolean focus = BTMusic_IF.getInstance().requestAudioFocus(true);
            //DebugLog.v(TAG, "music_pre() focus="+focus);
            //if (focus) {
            open();
            //setBTSource();
            mServiceIF.music_pre();
            //}
        } catch (Exception e) {
            DebugLog.e(TAG, "pre e=" + e.getMessage());
        }
        return true;
    }

    /**
     * 下一曲
     */
    @Override
    public boolean next() {
        try {
//            if (MediaInterfaceUtil.mediaCannotPlay()) {
//                return;
//            }
//            boolean focus = BTMusic_IF.getInstance().requestAudioFocus(true);
//            DebugLog.v(TAG, "music_next() focus="+focus);
//            if (focus) {
                open();
//                setBTSource();
                mServiceIF.music_next();
//            }
        } catch (Exception e) {
            DebugLog.e(TAG, "next e=" + e.getMessage());
        }
        return true;
    }

    /**
     * 播放状态
     */
    @Override
    public boolean isPlaying() {
        boolean isPlaying = false;
        try {
            if (isBtMusicConnected()) {
                isPlaying = mServiceIF.music_isPlaying();
            }
        } catch (Exception e) {
            DebugLog.e(TAG, "isPlaying e=" + e.getMessage());
        }
        DebugLog.d(TAG, "isPlaying isPlaying="+isPlaying);
        return isPlaying;
    }

    /**
     * 获取歌曲标题
     */
    @Override
    public String getTitle() {
        try {
            return mServiceIF.music_getTitle();
        } catch (Exception e) {
            DebugLog.e(TAG, "getTitle e=" + e.getMessage());
        }
        return null;
    }

    /**
     * 获取歌曲艺术家
     */
    @Override
    public String getArtist() {
        try {
            return mServiceIF.music_getArtist();
        } catch (Exception e) {
            DebugLog.e(TAG, "getArtist e=" + e.getMessage());
        }
        return null;
    }

    /**
     * 获取歌曲专辑名
     */
    @Override
    public String getAlbum() {
        try {
            return mServiceIF.music_getAlbum();
        } catch (Exception e) {
            DebugLog.e(TAG, "getAlbum e=" + e.getMessage());
        }
        return null;
    }
}
