package com.amt.radio;

interface RadioInterface {

    /**
     * 设置回调
     */
    void setCallBack(RadioManager.RadioCallBack callBack);

    /**
     * 获取当前收音机频率。
     */
    int getCurFreq();

    /**
     * 设置当前收音机频率。
     */
    int setCurFreq(int freq);

    /**
     * 获取收音机的打开关闭状态。
     */
    int getEnable();

    /**
     * 打开关闭收音机。
     */
    int setEnable(int enable);

    /**
     * 获取收音机波段。
     */
    int getCurBand();

    /**
     * 设置收音机波段。
     */
    int setCurBand(int band);

    /**
     * 获取收音区域（3ZA未使用）。
     */
    int getCurArea();

    /**
     * 设置收音区域。
     */
    int setCurArea(int area);

    /**
     * 获取第index个预存台，索引从1开始，传0获取的是列表的波段类型。index <= 30。
     */
    int getChannel(int index);

    /**
     * 扫描并预览, 播放5秒，再搜索下一个。
     */
    int setScan();

    /**
     * 停止扫描预览。
     */
    int stopScan();

    /**
     * 扫描电台。
     */
    int scanStore();

    /**
     * 停止扫描电台。
     */
    int stopScanStore();

    /**
     * 收音左步进
     */
    int setPreStep();

    /**
     * 收音右步进
     */
    int setNextStep();

    /**
     * 收音左搜索
     */
    int setPreSearch();

    /**
     * 收音右搜索
     */
    int setNextSearch();

    /**
     * 获取当前台是否是立体声电台。
     */
    int getST();

    /**
     * 初始化频率列表。
     */
    int scanListChannel();

    /**
     * 上一电台，预存台中的电台
     */
    int setPreChannel();

    /**
     * 下一电台，预存台中的电台
     */
    int setNextChannel();
}
