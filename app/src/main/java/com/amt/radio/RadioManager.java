package com.amt.radio;

import android.os.Handler;
import android.os.Message;

import com.amt.aidl.MediaDef;
import com.amt.aidl.RadioBean;
import com.amt.mediaservice.MediaApplication;
import com.amt.service.MediaServiceBinder;
import com.haoke.define.RadioDef;

import java.util.ArrayList;
import java.util.List;

public class RadioManager {

    interface RadioCallBack {
        void onRadioDataChange(int func, int data);
    }

    private RadioInterface mRadioInstance;
    private RadioCallBack mRadioCallBack;
    private MediaServiceBinder.ManagerCallBack mBinderCallBack;
    private RadioDatabaseHelper mRadioDatabaseHelper;

    private ArrayList<RadioBean> mFMRadioDatas = new ArrayList<RadioBean>();
    private ArrayList<RadioBean> mAMRadioDatas = new ArrayList<RadioBean>();
    private ArrayList<RadioBean> mMCURadioDatas = new ArrayList<RadioBean>();

    private static final int FREQ_COUNT_MAX = 30;

    private static RadioManager mSelf;
    synchronized public static RadioManager getInstance() {
        if (mSelf == null) {
            mSelf = new RadioManager();
        }
        return mSelf;
    }

    public static void registerCallBack(MediaServiceBinder.ManagerCallBack callBack) {
        getInstance().mBinderCallBack = callBack;
    }

    private RadioManager() {
        mRadioInstance = Radio_Haoke.getInstance();
        mRadioCallBack = new RadioCallBack() {
            @Override
            public void onRadioDataChange(int func, int data) {
                switch (func) {
                    case RadioDef.RadioFunc.ALL_CH:
                        updateMCURadioDatas();
                        break;
                    default:
                        break;
                }
                if (mBinderCallBack != null) {
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

    /**
     * 获取当前收音机频率。
     */
    private int getCurFreq() {
        return mRadioInstance.getCurFreq();
    }

    /**
     * 设置当前收音机频率。
     */
    private void setCurFreq(int freq) {
        mRadioInstance.setCurFreq(freq);
    }

    /**
     * 获取收音机的打开关闭状态。
     */
    private boolean isEnable() {
        return mRadioInstance.isEnable();
    }

    /**
     * 打开关闭收音机。
     */
    private void setEnable(boolean enable) {
        mRadioInstance.setEnable(enable);
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
    private void setCurBand(int band) {
        mRadioInstance.setCurBand(band);
    }

    /**
     * 获取收音区域（3ZA未使用）。
     */
    public int getCurArea() {
        return mRadioInstance.getCurArea();
    }

    /**
     * 设置收音区域。
     */
    private void setCurArea(byte area) {
        mRadioInstance.setCurArea(area);
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
    private void setScan() {
        mRadioInstance.setScan();
    }

    /**
     * 停止扫描。
     */
    private void stopScan() {
        mRadioInstance.stopScan();
    }

    /**
     * 收音左步进
     */
    private void setPreStep() {
        mRadioInstance.setPreStep();
    }

    /**
     * 收音右步进
     */
    private void setNextStep() {
        mRadioInstance.setNextStep();
    }

    /**
     * 收音左搜索
     */
    private void setPreSearch() {
        mRadioInstance.setPreSearch();
    }

    /**
     * 收音右搜索
     */
    private void setNextSearch() {
        mRadioInstance.setNextSearch();
    }

    /**
     * 获取当前台是否是立体声电台。
     */
    private boolean getST() {
        return mRadioInstance.getST();
    }

    /**
     * 初始化频率列表。
     */
    private void scanListChannel() {
        mRadioInstance.scanListChannel();
    }

    /**
     * 扫描电台。
     */
    private void scanStore() {
        mRadioInstance.scanStore();
    }

    /**
     * 上一电台，预存台中的电台
     */
    private void setPreChannel() {
        mRadioInstance.setPreChannel();
    }

    /**
     * 下一电台，预存台中的电台
     */
    private void setNextChannel() {
        mRadioInstance.setNextChannel();
    }

    /**
     * 获取所有的FM的频道，存放到mFMRadioDatas中。
     */
    public void reLoadFMRadioDatas(int delayTime) {
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
    public void reLoadAMRadioDatas(int delayTime) {
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
                        mBinderCallBack.onRadioCallBack(MediaDef.CALLBACK_RADIO_DB_LOAD, RadioBean.RadioType.FM_TYPE);
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
                        mBinderCallBack.onRadioCallBack(MediaDef.CALLBACK_RADIO_DB_LOAD, RadioBean.RadioType.AM_TYPE);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 获得所有的AM电台
     */
    public ArrayList<RadioBean> getAMRadioDatas() {
        return mAMRadioDatas;
    }

    /**
     * 获得所有的FM电台
     */
    public ArrayList<RadioBean> getFMRadioDatas() {
        return mFMRadioDatas;
    }

    /**
     * 获得所有的FM电台
     */
    public ArrayList<RadioBean> getMCURadioDatas() {
        return mMCURadioDatas;
    }

    /**
     * 收藏指定的电台（支持批量收藏）
     */
    public void collectRadio(List<RadioBean> radioBeanList) {
        boolean updateAMFlag = false;
        boolean updateFMFlag = false;
        for (RadioBean radioBean : radioBeanList) {
            updateAMFlag = updateAMFlag || (radioBean.getRadioType() == RadioBean.RadioType.AM_TYPE);
            updateFMFlag = updateFMFlag || (radioBean.getRadioType() == RadioBean.RadioType.FM_TYPE);
            mRadioDatabaseHelper.insert(radioBean);
        }
        if (updateAMFlag) {
            reLoadAMRadioDatas(0);
        }
        if (updateFMFlag) {
            reLoadFMRadioDatas(0);
        }
    }

    /**
     * 取消收藏指定的电台（支持批量取消）
     */
    public void unCollectRadio(List<RadioBean> radioBeanList) {
        boolean updateAMFlag = false;
        boolean updateFMFlag = false;
        for (RadioBean radioBean : radioBeanList) {
            updateAMFlag = updateAMFlag || (radioBean.getRadioType() == RadioBean.RadioType.AM_TYPE);
            updateFMFlag = updateFMFlag || (radioBean.getRadioType() == RadioBean.RadioType.FM_TYPE);
            mRadioDatabaseHelper.delete(radioBean);
        }
        if (updateAMFlag) {
            reLoadAMRadioDatas(0);
        }
        if (updateFMFlag) {
            reLoadFMRadioDatas(0);
        }
    }

    /**
     * 判断指定的电台是否是收藏电台
     * @param radioBean
     * @return
     */
    public boolean isCollect(RadioBean radioBean) {
        boolean retFlag = false;
        ArrayList<RadioBean> radioBeans = radioBean.getRadioType() == RadioBean.RadioType.AM_TYPE ?
                mAMRadioDatas : mFMRadioDatas;
        for (RadioBean bean : radioBeans) {
            if (bean.getFreq() == radioBean.getFreq()) {
                retFlag = true;
                break;
            }
        }
        return retFlag;
    }

    /**
     * 清空AM电台。
     */
    public void clearAMRadios() {
        mRadioDatabaseHelper.clearAM();
        mAMRadioDatas.clear();
        reLoadAMRadioDatas(0);
    }

    /**
     * 清空FM电台。
     */
    public void clearFMRadios() {
        mRadioDatabaseHelper.clearFM();
        mFMRadioDatas.clear();
        reLoadFMRadioDatas(0);
    }

    private void updateMCURadioDatas() {
        mMCURadioDatas.clear();
        for (int index = 0; index < FREQ_COUNT_MAX; index++) {
            int freq = getChannel(index);
            RadioBean radioBean = new RadioBean(null, String.valueOf(freq),
                    null, RadioBean.RadioType.MCU_TYPE);
            mMCURadioDatas.add(radioBean);
        }
        if (mBinderCallBack != null) {
            mBinderCallBack.onRadioCallBack(MediaDef.CALLBACK_RADIO_DB_LOAD, RadioBean.RadioType.MCU_TYPE);
        }
    }
}
