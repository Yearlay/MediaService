package com.amt.radio;

import com.amt.mediaservice.MediaApplication;

import java.util.ArrayList;

public class RadioManager {

    interface RadioCallBack {
        void onRadioDataChange(int mode, int func, int data);
    }

    private RadioInterface mRadioInstance;
    private RadioCallBack mRadioCallBack;
    private RadioDatabaseHelper mRadioDatabaseHelper;

    private ArrayList<RadioBean> mFMRadioDatas = new ArrayList<RadioBean>();
    private ArrayList<RadioBean> mAMRadioDatas = new ArrayList<RadioBean>();

    public RadioManager() {
        mRadioInstance = Radio_Haoke.getInstance();
        mRadioCallBack = new RadioCallBack() {
            @Override
            public void onRadioDataChange(int mode, int func, int data) {
                // TODO
            }
        };
        mRadioInstance.setCallBack(mRadioCallBack);

        mRadioDatabaseHelper = new RadioDatabaseHelper(MediaApplication.getInstance());
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

    private void initFMRadioDatas() {
        mFMRadioDatas.clear();
        mFMRadioDatas.addAll(mRadioDatabaseHelper.queryFM(null, null));
    }

    private void initAMRadioDatas() {
        mAMRadioDatas.clear();
        mAMRadioDatas.addAll(mRadioDatabaseHelper.queryAM(null, null));
    }
}
