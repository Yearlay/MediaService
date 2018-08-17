package com.amt.service;

import android.os.Handler;
import android.os.RemoteException;

import com.amt.aidl.IMediaCallBack;
import com.amt.aidl.IMediaService;
import com.amt.aidl.MediaDef;
import com.amt.bt.BtMusicManager;
import com.amt.aidl.RadioBean;
import com.amt.radio.RadioManager;
import com.amt.util.DebugLog;
import com.amt.util.MediaDefUtil;

import java.util.ArrayList;
import java.util.List;

import static com.amt.aidl.MediaDef.*;

/**
 * Created by hww on 2018/8/15.
 */

public class MediaServiceBinder extends IMediaService.Stub {
    private static final String TAG = "MediaServiceBinder";
    private ArrayList<MediaClient> mClientList;

    public MediaServiceBinder() {
        mClientList = new ArrayList<MediaClient>();
        registerCallBackToManager();
    }

    private void registerCallBackToManager() {
        BtMusicManager.registerCallBack(mManagerCallBack);
        RadioManager.registerCallBack(mManagerCallBack);
    }

    @Override
    public boolean registerCallBack(String appName, int callBackFlag, IMediaCallBack callBack) throws RemoteException {
        DebugLog.d(TAG, "registerCallBack appName = " + appName);
        if (appName == null || callBack == null) {
            return false;
        }
        synchronized (mClientList) {
            int size = mClientList.size();
            for (int i = size - 1; i >= 0; i--) {
                if (appName.equals(mClientList.get(i).mAppName)) {
                    mClientList.remove(i);
                }
            }
            mClientList.add(new MediaClient(appName, callBackFlag, callBack));
        }
        return true;
    }

    @Override
    public boolean unregisterCallBack(String appName) throws RemoteException {
        DebugLog.d(TAG, "unregisterCallBack appName = " + appName);
        if (appName == null) {
            return false;
        }
        synchronized (mClientList) {
            int size = mClientList.size();
            for (int i = size - 1; i >= 0; i--) {
                if (appName.equals(mClientList.get(i).mAppName)) {
                    mClientList.remove(i);
                }
            }
        }
        return true;
    }

    @Override
    public boolean funcBool(int funcID) throws RemoteException {
        int index = MediaDefUtil.getFuncRange(funcID);
        switch (index) {
            case FUNC_ERROR:
                break;
            case FUNC_IMAGE:
                break;
            case FUNC_VIDEO:
                break;
            case FUNC_AUDIO:
                break;
            case FUNC_RADIO:
                break;
            case FUNC_BTMUSIC:
                break;
        }
        return false;
    }

    @Override
    public long funcLong(int funcID) throws RemoteException {
        int index = MediaDefUtil.getFuncRange(funcID);
        switch (index) {
            case FUNC_ERROR:
                break;
            case FUNC_IMAGE:
                break;
            case FUNC_VIDEO:
                break;
            case FUNC_AUDIO:
                break;
            case FUNC_RADIO:
                break;
            case FUNC_BTMUSIC:
                break;
        }
        return 0;
    }

    @Override
    public int funcInt(int funcID) throws RemoteException {
        int code = ERROR_CODE_UNKNOWN;
        int index = MediaDefUtil.getFuncRange(funcID);
        switch (index) {
            case FUNC_ERROR:
                break;
            case FUNC_IMAGE:
                break;
            case FUNC_VIDEO:
                break;
            case FUNC_AUDIO:
                break;
            case FUNC_RADIO:
                code = RadioManager.funcInt(funcID);
                break;
            case FUNC_BTMUSIC:
                code = BtMusicManager.funcInt(funcID);
                break;
        }
        return code;
    }

    @Override
    public int funcIntEx(int funcID, int arg1, int arg2, int arg3) throws RemoteException {
        int code = ERROR_CODE_UNKNOWN;
        int index = MediaDefUtil.getFuncRange(funcID);
        switch (index) {
            case FUNC_ERROR:
                break;
            case FUNC_IMAGE:
                break;
            case FUNC_VIDEO:
                break;
            case FUNC_AUDIO:
                break;
            case FUNC_RADIO:
                code = RadioManager.funcIntEx(funcID, arg1, arg2, arg3);
                break;
            case FUNC_BTMUSIC:
                break;
        }
        return code;
    }

    @Override
    public int funcIntLS(int funcID, List<String> list) throws RemoteException {
        return 0;
    }

    @Override
    public String funcStr(int funcID) throws RemoteException {
        String code = null;
        int index = MediaDefUtil.getFuncRange(funcID);
        switch (index) {
            case FUNC_ERROR:
                break;
            case FUNC_IMAGE:
                break;
            case FUNC_VIDEO:
                break;
            case FUNC_AUDIO:
                break;
            case FUNC_RADIO:
                break;
            case FUNC_BTMUSIC:
                code = BtMusicManager.funcStr(funcID);
                break;
        }
        return code;
    }

    @Override
    public String funcStrEx(int funcID, int arg1, int arg2, int arg3) throws RemoteException {
        return null;
    }

    @Override
    public String funcStrSSS(int funcID, String arg1, String arg2, String arg3) throws RemoteException {
        return null;
    }

    @Override
    public List<String> funcListSEx(int funcID, int arg1, int arg2, int arg3) throws RemoteException {
        return null;
    }

    @Override
    public int funcRadioIntLR(int funcID, List<RadioBean> list) throws RemoteException {
        return RadioManager.funcRadioIntLR(funcID, list);
    }

    static class MediaClient {
        public String mAppName = null;
        public int mFlag = MediaDef.CALLBACK_FLAG_ALL;
        public IMediaCallBack mCallBack = null;

        public MediaClient(String appName, int callBackFlag, IMediaCallBack callBack) {
            mAppName = appName;
            mFlag = callBackFlag;
            mCallBack = callBack;
        }
    }

    public interface ManagerCallBack {
        void onBtMusicCallBack(int func, int data);
        void onRadioCallBack(int func, int data);
        void onAudioCallBack(int func, int data);
        void onVideoCallBack(int func, int data);
        void onImageCallBack(int func, int data);
        void onOtherCallBack(int func, int data);
    }

    private ManagerCallBack mManagerCallBack = new ManagerCallBack() {
        public void onBtMusicCallBack(int func, int data) {
            mHandler.obtainMessage(CALLBACK_FLAG_BT_MUSIC, func, data).sendToTarget();
        }
        public void onRadioCallBack(int func, int data) {
            mHandler.obtainMessage(CALLBACK_FLAG_RADIO, func, data).sendToTarget();
        }
        public void onAudioCallBack(int func, int data) {
            mHandler.obtainMessage(CALLBACK_FLAG_AUDIO, func, data).sendToTarget();
        }
        public void onVideoCallBack(int func, int data) {
            mHandler.obtainMessage(CALLBACK_FLAG_VIDEO, func, data).sendToTarget();
        }
        public void onImageCallBack(int func, int data) {
            mHandler.obtainMessage(CALLBACK_FLAG_IMAGE, func, data).sendToTarget();
        }
        public void onOtherCallBack(int func, int data) {
            mHandler.obtainMessage(CALLBACK_FLAG_OTHER, func, data).sendToTarget();
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            removeMessages(msg.what);
            dispatchDataToClients(msg.what, msg.arg1, msg.arg2);
        };
    };

    private void dispatchDataToClients(int flag, int data0, int data1) {
        synchronized (mClientList) {
            for (int i = 0; i < mClientList.size(); i++) {
                MediaClient client = mClientList.get(i);
                IMediaCallBack callBack = client.mCallBack;
                int clientFlag = client.mFlag;
                if (clientFlag == CALLBACK_FLAG_ALL) {
                } else if ((clientFlag & flag) == flag) {
                } else {
                    continue;
                }
                DebugLog.d(TAG, "dispatchDataToClients appName="+client.mAppName+"; flag="+flag);
                try {
                    switch (flag) {
                        case CALLBACK_FLAG_BT_MUSIC:
                            callBack.onBtMusicCallBack(data0, data1);
                            break;
                        case CALLBACK_FLAG_RADIO:
                            callBack.onRadioCallBack(data0, data1);
                            break;
                        case CALLBACK_FLAG_AUDIO:
                            callBack.onAudioCallBack(data0, data1);
                            break;
                        case CALLBACK_FLAG_VIDEO:
                            callBack.onVideoCallBack(data0, data1);
                            break;
                        case CALLBACK_FLAG_IMAGE:
                            callBack.onImageCallBack(data0, data1);
                            break;
                        case CALLBACK_FLAG_OTHER:
                            callBack.onOtherCallBack(data0, data1);
                            break;
                    }
                } catch (Exception e) {
                    DebugLog.e(TAG, "dispatchDataToClients e=" + e.getMessage());
                    DebugLog.e(TAG, "dispatchDataToClients clientList.remove appName="
                            + mClientList.get(i).mAppName);
                    mClientList.remove(i);
                    i--;
                }
            }
        }
    }
}
