package com.amt.radio;

public interface RadioInterface {

    /**
     * 设置回调
     */
    void setCallBack(RadioManager.RadioCallBack callBack);

    /**
     * 获取当前收音机频率。
     */
    public int getCurFreq();

    /**
     * 设置当前收音机频率。
     */
    public void setCurFreq(int freq);

    /**
     * 获取收音机的打开关闭状态。
     */
    public boolean isEnable();

    /**
     * 打开关闭收音机。
     */
    public void setEnable(boolean enable);

    /**
     * 获取收音机波段。
     */
    public int getCurBand();

    /**
     * 设置收音机波段。
     */
    public void setCurBand(int band);

    /**
     * 获取收音区域（3ZA未使用）。
     */
    public int getCurArea();

    /**
     * 设置收音区域。
     */
    public void setCurArea(byte area);

    /**
     * 获取第index个预存台，索引从1开始，传0获取的是列表的波段类型。index <= 30。
     */
    public int getChannel(int index);

    /**
     * 扫描并预览, 播放5秒，再搜索下一个。
     */
    public void setScan();

    /**
     * 停止扫描。
     */
    public void stopScan();

    /**
     * 收音左步进
     */
    public void setPreStep();

    /**
     * 收音右步进
     */
    public void setNextStep();

    /**
     * 收音左搜索
     */
    public void setPreSearch();

    /**
     * 收音右搜索
     */
    public void setNextSearch();

    /**
     * 获取当前台是否是立体声电台。
     */
    public boolean getST();

    /**
     * 初始化频率列表。
     */
    public void scanListChannel();

    /**
     * 扫描电台。
     */
    public void scanStore();

    /**
     * 上一电台，预存台中的电台
     */
    public void setPreChannel();

    /**
     * 下一电台，预存台中的电台
     */
    public void setNextChannel();
}
