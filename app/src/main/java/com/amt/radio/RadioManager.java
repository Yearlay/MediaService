package com.amt.radio;

import android.os.Handler;
import android.os.Message;

import static com.amt.aidl.MediaDef.*;

import com.amt.aidl.MediaDef;
import com.amt.aidl.RadioBean;
import com.amt.mediaservice.MediaApplication;
import com.amt.service.MediaServiceBinder;
import com.haoke.define.RadioDef.RadioFunc;

import java.util.ArrayList;
import java.util.List;

public class RadioManager {

    private static RadioManager mSelf;
    private RadioInterface mRadioInstance;
    private RadioCallBack mRadioCallBack;
    private MediaServiceBinder.ManagerCallBack mBinderCallBack;
    private RadioDatabaseHelper mRadioDatabaseHelper;

    private ArrayList<RadioBean> mFMRadioDatas = new ArrayList<RadioBean>();
    private ArrayList<RadioBean> mAMRadioDatas = new ArrayList<RadioBean>();
    private ArrayList<RadioBean> mMCURadioDatas = new ArrayList<RadioBean>();

    private static final int FREQ_COUNT_MAX = 30;

    interface RadioCallBack {
        void onRadioDataChange(int func, int data);
    }

    private RadioManager() {
        mRadioInstance = Radio_Haoke.getInstance();
        mRadioCallBack = new RadioCallBack() {
            @Override
            public void onRadioDataChange(int func, int data) {
                boolean callback = false;
                switch (func) {
                    case RadioFunc.FREQ:
                        callback = true;
                        break;
                    case RadioFunc.BAND:
                        callback = true;
                        break;
                    case RadioFunc.AREA:
                        callback = true;
                        break;
                    case RadioFunc.ALL_CH:
                        updateMCURadioDatas();
                        break;
                    case RadioFunc.CUR_CH:
                        break;
                    case RadioFunc.ST:
                        callback = true;
                        break;
                    case RadioFunc.LOC:
                        break;
                    case RadioFunc.LISTEN:
                        break;
                    case RadioFunc.STATE:
                        callback = true;
                        break;
                    case RadioFunc.RDS:
                        break;
                    case RadioFunc.RDS_TA:
                    case RadioFunc.RDS_AF:
                        break;
                    case RadioFunc.RDS_TP:
                    case RadioFunc.RDS_EON:
                        break;
                    case RadioFunc.PARAM:
                        break;
                    case RadioFunc.ENABLE:
                        callback = true;
                        break;
                }
                if (callback && mBinderCallBack != null) {
                    mBinderCallBack.onRadioCallBack(func, data);
                }
            }
        };
        mRadioInstance.setCallBack(mRadioCallBack);

        mRadioDatabaseHelper = new RadioDatabaseHelper(MediaApplication.getInstance());

        // 初始化数据。
        reLoadFMRadioDatas(0);
        reLoadAMRadioDatas(0);
    }

    synchronized public static RadioManager getInstance() {
        if (mSelf == null) {
            mSelf = new RadioManager();
        }
        return mSelf;
    }

    public static void registerCallBack(MediaServiceBinder.ManagerCallBack callBack) {
        getInstance().mBinderCallBack = callBack;
    }

    public static int funcInt(int funcID) {
        int code = ERROR_CODE_UNKNOWN;
        RadioManager manager = getInstance();
        switch (funcID) {
            case FUNC_RADIO_GET_CUR_FREQ:
                code = manager.getCurFreq();
                break;
            case FUNC_RADIO_GET_ENABLE:
                code = manager.getEnable();
                break;
            case FUNC_RADIO_GET_ST:
                code = manager.getST();
                break;
            case FUNC_RADIO_GET_CUR_BAND:
                code = manager.getCurBand();
                break;
            case FUNC_RADIO_GET_CUR_AREA:
                code = manager.getCurArea();
                break;
            case FUNC_RADIO_SCAN_STORE:
                code = manager.scanStore();
                break;
            case FUNC_RADIO_SET_SCAN:
                code = manager.setScan();
                break;
            case FUNC_RADIO_STOP_SCAN:
                code = manager.stopScan();
                break;
            case FUNC_RADIO_SET_PRE_STEP:
                code = manager.setPreStep();
                break;
            case FUNC_RADIO_SET_NEXT_STEP:
                code = manager.setNextStep();
                break;
            case FUNC_RADIO_SET_PRE_SEARCH:
                code = manager.setPreSearch();
                break;
            case FUNC_RADIO_SET_NEXT_SEARCH:
                code = manager.setNextSearch();
                break;
            case FUNC_RADIO_SET_PRE_CHANNEL:
                code = manager.setPreChannel();
                break;
            case FUNC_RADIO_SET_NEXT_CHANNEL:
                code = manager.setNextChannel();
                break;
            case FUNC_RADIO_CLEAR_COLLECT_AM_STATION:
                code = manager.clearCollectAMStation();
                break;
            case FUNC_RADIO_CLEAR_COLLECT_FM_STATION:
                code = manager.clearCollectFMStation();
                break;
        }
        return code;
    }

    public static int funcIntEx(int funcID, int arg1, int arg2, int arg3) {
        int code = ERROR_CODE_UNKNOWN;
        RadioManager manager = getInstance();
        switch (funcID) {
            case FUNC_RADIO_SET_CUR_FREQ:
                code = manager.setCurFreq(arg1);
                break;
            case FUNC_RADIO_SET_ENABLE:
                code = manager.setEnable(arg1);
                break;
            case FUNC_RADIO_SET_CUR_BAND:
                code = manager.setCurBand(arg1);
                break;
            case FUNC_RADIO_SET_CUR_AREA:
                code = manager.setCurArea(arg1);
                break;
        }
        return code;
    }

    public static int funcRadioIntR(int funcID, RadioBean radioBean) {
        int code = MediaDef.ERROR_CODE_UNKNOWN;
        RadioManager manager = getInstance();
        switch (funcID) {
            case FUNC_RADIO_GET_COLLECT_STATE:
                code = manager.isCollect(radioBean);
                break;
        }
        return code;
    }

    public static int funcRadioIntLR(int funcID, List<RadioBean> list) {
        int code = MediaDef.ERROR_CODE_UNKNOWN;
        RadioManager manager = getInstance();
        switch (funcID) {
            case FUNC_RADIO_GET_STORE_STATION:
                code = manager.getMCURadioStoreStation(list);
                break;
            case FUNC_RADIO_GET_COLLECT_AM_STATION:
                code = manager.getAMRadioCollectStation(list);
                break;
            case FUNC_RADIO_GET_COLLECT_FM_STATION:
                code = manager.getFMRadioCollectStation(list);
                break;
            case FUNC_RADIO_ADD_COLLECT_STATION:
                code = manager.collectRadio(list);
                break;
            case FUNC_RADIO_DEL_COLLECT_STATION:
                code = manager.unCollectRadio(list);
                break;
        }
        return code;
    }

    /**
     * 获取当前收音机频率。
     */
    private int getCurFreq() {
        return mRadioInstance.getCurFreq();
    }

    /**
     * 设置当前收音机频率。
     */
    private int setCurFreq(int freq) {
        return mRadioInstance.setCurFreq(freq);
    }

    /**
     * 获取收音机的打开关闭状态。
     */
    private int getEnable() {
        return mRadioInstance.getEnable();
    }

    /**
     * 打开关闭收音机。
     */
    private int setEnable(int enable) {
        return mRadioInstance.setEnable(enable);
    }

    /**
     * 获取收音机波段。
     */
    private int getCurBand() {
        return mRadioInstance.getCurBand();
    }

    /**
     * 设置收音机波段。
     */
    private int setCurBand(int band) {
        return mRadioInstance.setCurBand(band);
    }

    /**
     * 获取收音区域（3ZA未使用）。
     */
    private int getCurArea() {
        return mRadioInstance.getCurArea();
    }

    /**
     * 设置收音区域。
     */
    private int setCurArea(int area) {
        return mRadioInstance.setCurArea(area);
    }

    /**
     * 获取第index个预存台，索引从1开始，传0获取的是列表的波段类型。index <= 30。
     */
    private int getChannel(int index) {
        return mRadioInstance.getChannel(index);
    }

    /**
     * 扫描并预览, 播放5秒，再搜索下一个。
     */
    private int setScan() {
        return mRadioInstance.setScan();
    }

    /**
     * 停止扫描。
     */
    private int stopScan() {
        return mRadioInstance.stopScan();
    }

    /**
     * 收音左步进
     */
    private int setPreStep() {
        return mRadioInstance.setPreStep();
    }

    /**
     * 收音右步进
     */
    private int setNextStep() {
        return mRadioInstance.setNextStep();
    }

    /**
     * 收音左搜索
     */
    private int setPreSearch() {
        return mRadioInstance.setPreSearch();
    }

    /**
     * 收音右搜索
     */
    private int setNextSearch() {
        return mRadioInstance.setNextSearch();
    }

    /**
     * 获取当前台是否是立体声电台。
     */
    private int getST() {
        return mRadioInstance.getST();
    }

    /**
     * 初始化频率列表。
     */
    private int scanListChannel() {
        return mRadioInstance.scanListChannel();
    }

    /**
     * 扫描电台。
     */
    private int scanStore() {
        return mRadioInstance.scanStore();
    }

    /**
     * 上一电台，预存台中的电台
     */
    private int setPreChannel() {
        return mRadioInstance.setPreChannel();
    }

    /**
     * 下一电台，预存台中的电台
     */
    private int setNextChannel() {
        return mRadioInstance.setNextChannel();
    }

    /**
     * 获取所有的FM的频道，存放到mFMRadioDatas中。
     */
    private void reLoadFMRadioDatas(int delayTime) {
        mLoadHandler.removeMessages(BEGIN_LOAD_FM_DATA);
        Message message = mLoadHandler.obtainMessage(BEGIN_LOAD_FM_DATA);
        if (delayTime == 0) {
            mLoadHandler.sendMessage(message);
        } else {
            mLoadHandler.sendMessageDelayed(message, delayTime);
        }
    }

    /**
     * 获取所有的AM的频道，存放到mAMRadioDatas中。
     */
    private void reLoadAMRadioDatas(int delayTime) {
        mLoadHandler.removeMessages(BEGIN_LOAD_AM_DATA);
        Message message = mLoadHandler.obtainMessage(BEGIN_LOAD_AM_DATA);
        if (delayTime == 0) {
            mLoadHandler.sendMessage(message);
        } else {
            mLoadHandler.sendMessageDelayed(message, delayTime);
        }
    }

    private static final int BEGIN_LOAD_FM_DATA = 1;
    private static final int END_LOAD_FM_DATA = 2;
    private static final int BEGIN_LOAD_AM_DATA = 3;
    private static final int END_LOAD_AM_DATA = 4;
    private Handler mLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BEGIN_LOAD_FM_DATA:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<RadioBean> radioBeans = mRadioDatabaseHelper.queryFM(null, null);
                            mLoadHandler.obtainMessage(END_LOAD_FM_DATA, radioBeans).sendToTarget();
                        }
                    }).start();
                    break;
                case END_LOAD_FM_DATA:
                    mFMRadioDatas.clear();
                    mFMRadioDatas.addAll((ArrayList<RadioBean>) msg.obj);
                    if (mBinderCallBack != null) {
                        mBinderCallBack.onRadioCallBack(RadioDef.CALLBACK_COLLECT_FM_STATION, 0);
                    }
                    break;
                case BEGIN_LOAD_AM_DATA:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<RadioBean> radioBeans = mRadioDatabaseHelper.queryAM(null, null);
                            mLoadHandler.obtainMessage(END_LOAD_AM_DATA, radioBeans).sendToTarget();
                        }
                    }).start();
                    break;
                case END_LOAD_AM_DATA:
                    mAMRadioDatas.clear();
                    mAMRadioDatas.addAll((ArrayList<RadioBean>) msg.obj);
                    if (mBinderCallBack != null) {
                        mBinderCallBack.onRadioCallBack(RadioDef.CALLBACK_COLLECT_AM_STATION, 0);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 获得所有的收藏的AM电台
     */
    private int getAMRadioCollectStation(List<RadioBean> list) {
        list.addAll(mAMRadioDatas);
        return MediaDef.SUCCESS;
    }

    /**
     * 获得所有的收藏的FM电台
     */
    private int getFMRadioCollectStation(List<RadioBean> list) {
        list.addAll(mFMRadioDatas);
        return MediaDef.SUCCESS;
    }

    /**
     * 获得所有的搜索到的FM电台
     */
    private int getMCURadioStoreStation(List<RadioBean> list) {
        list.addAll(mMCURadioDatas);
        return MediaDef.SUCCESS;
    }

    /**
     * 收藏指定的电台（支持批量收藏）
     */
    private int collectRadio(List<RadioBean> radioBeanList) {
        boolean updateAMFlag = false;
        boolean updateFMFlag = false;
        for (RadioBean radioBean : radioBeanList) {
            updateAMFlag = updateAMFlag || radioBean.isAmFreq();
            updateFMFlag = updateFMFlag || radioBean.isFmFreq();
            mRadioDatabaseHelper.insert(radioBean);
        }
        if (updateAMFlag) {
            reLoadAMRadioDatas(0);
        }
        if (updateFMFlag) {
            reLoadFMRadioDatas(0);
        }
        return MediaDef.SUCCESS;
    }

    /**
     * 取消收藏指定的电台（支持批量取消）
     */
    private int unCollectRadio(List<RadioBean> radioBeanList) {
        boolean updateAMFlag = false;
        boolean updateFMFlag = false;
        for (RadioBean radioBean : radioBeanList) {
            updateAMFlag = updateAMFlag || radioBean.isAmFreq();
            updateFMFlag = updateFMFlag || radioBean.isFmFreq();
            mRadioDatabaseHelper.delete(radioBean);
        }
        if (updateAMFlag) {
            reLoadAMRadioDatas(0);
        }
        if (updateFMFlag) {
            reLoadFMRadioDatas(0);
        }
        return MediaDef.SUCCESS;
    }

    /**
     * 判断指定的电台是否是收藏电台
     */
    private int isCollect(RadioBean radioBean) {
        boolean retFlag = false;
        ArrayList<RadioBean> radioBeans = radioBean.isFmFreq() ?
                mFMRadioDatas : mAMRadioDatas;
        for (RadioBean bean : radioBeans) {
            if (bean.getFreq() == radioBean.getFreq()) {
                retFlag = true;
                break;
            }
        }
        return retFlag ? MediaDef.TRUE : MediaDef.FALSE;
    }

    /**
     * 清空AM电台。
     */
    private int clearCollectAMStation() {
        mRadioDatabaseHelper.clearAM();
        mAMRadioDatas.clear();
        reLoadAMRadioDatas(0);
        return MediaDef.SUCCESS;
    }

    /**
     * 清空FM电台。
     */
    private int clearCollectFMStation() {
        mRadioDatabaseHelper.clearFM();
        mFMRadioDatas.clear();
        reLoadFMRadioDatas(0);
        return MediaDef.SUCCESS;
    }

    private void updateMCURadioDatas() {
        mMCURadioDatas.clear();
        boolean isFmFreq = ((getCurBand() == RadioDef.BAND_FM) ? true : false);
        for (int index = 0; index < FREQ_COUNT_MAX; index++) {
            int freq = getChannel(index);
            RadioBean radioBean = new RadioBean(null, String.valueOf(freq),
                    null, isFmFreq);
            mMCURadioDatas.add(radioBean);
        }
        if (mBinderCallBack != null) {
            mBinderCallBack.onRadioCallBack(MediaDef.RadioDef.CALLBACK_STORE_STATION, 0);
        }
    }
}
