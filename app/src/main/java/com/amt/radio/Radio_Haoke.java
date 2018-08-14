package com.amt.radio;

import android.app.Application;
import android.os.RemoteException;

import com.amt.mediaservice.MediaApplication;
import com.amt.util.DebugLog;
import com.haoke.aidl.ICarCallBack;
import com.haoke.define.RadioDef;
import com.haoke.define.RadioDef.Band_5;
import com.haoke.define.RadioDef.Area;
import com.haoke.serviceif.CarService_IF;

public class Radio_Haoke extends CarService_IF implements RadioInterface {
    private static final String TAG = "Radio_Haoke";
    private RadioManager.RadioCallBack mCallBack;


    private static Radio_Haoke mSelf;
    synchronized public static RadioInterface getInstance() {
        if (mSelf == null) {
            mSelf = new Radio_Haoke();
        }
        return mSelf;
    }

    private Radio_Haoke() {
        mMode = com.haoke.define.ModeDef.RADIO; // 当前Mode为Radio。
        mContext = MediaApplication.getInstance();

        mICallBack = new ICarCallBack.Stub() {
            @Override
            public void onDataChange(int mode, int func, int data) throws RemoteException {
                if (mCallBack != null) {
                    mCallBack.onRadioDataChange(mode, func, data);
                }
            }
        };
    }

    /**
     * 设置回调
     */
    @Override
    public void setCallBack(RadioManager.RadioCallBack callBack) {
        mCallBack = callBack;
    }

    /**
     * 获取当前收音机频率。
     */
    @Override
    public int getCurFreq() {
        int freq = 0;
        try {
            freq = mServiceIF.radio_getCurFreq();
        } catch (Exception e) {
            DebugLog.e(TAG, "HMI------------getCurFreq e=" + e.getMessage());
        }
        DebugLog.d(TAG, "getCurFreq freq="+freq);
        return freq;
    }

    /**
     * 设置当前收音机频率。
     *
     * @param freq
     */
    @Override
    public void setCurFreq(int freq) {
        try {
            DebugLog.d(TAG, "setCurFreq freq="+freq);
            mServiceIF.radio_setCurFreq(freq);
        } catch (Exception e) {
            DebugLog.e(TAG, "HMI------------setCurFreq e=" + e.getMessage());
        }
    }

    /**
     * 获取收音机的打开关闭状态。
     */
    @Override
    public boolean isEnable() {
        boolean enable = false;
        // int source = getCurSource();
        try {
            // if (Source.isRadioSource(source)) {
                enable = mServiceIF.radio_isEnable();
            // }
        } catch (Exception e) {
            DebugLog.e(TAG, "HMI------------isEnable e=" + e.getMessage());
        }
        DebugLog.d(TAG, "isEnable enable="+enable);
        return enable;
    }

    /**
     * 打开关闭收音机。
     *
     * @param enable
     */
    @Override
    public void setEnable(boolean enable) {
        try {
            boolean focus = true;
            DebugLog.d(TAG, "setEnable enable="+enable);
            if (enable) {
                //if (MediaInterfaceUtil.mediaCannotPlay()) {
                //    return;
                //}
                //setRecordRadioOnOff(false);
                //focus = getRadioManager().requestAudioFocus(true);
                //DebugLog.d(TAG, "setEnable enable="+enable+"; focus="+focus);
                //if (focus) {
                //    setRadioSource();
                    //exitRescanAndScan5S(true);//ENABLE_RADIO_MUTEX_LOGIC
                    mServiceIF.radio_setEnable(enable);
                //}
            } else {
                mServiceIF.radio_setEnable(enable);
            }
        } catch (Exception e) {
            DebugLog.e(TAG, "setEnable e=" + e.getMessage());
        }
    }

    /**
     * 获取收音机波段。
     */
    @Override
    public int getCurBand() {
        int band = Band_5.FM1;
        try {
            band = mServiceIF.radio_getCurBand();
            switch (band) {
                case Band_5.FM1:
                case Band_5.FM2:
                case Band_5.FM3:
                    break;
                case Band_5.AM1:
                case Band_5.AM2:
                    band = Band_5.FM1;
                    break;
            }
        } catch (Exception e) {
            DebugLog.e(TAG, "HMI------------getCurBand e=" + e.getMessage());
        }
        DebugLog.d(TAG, "getCurBand band="+band);
        return band;
    }

    /**
     * 设置收音机波段。
     *
     * @param band
     */
    @Override
    public void setCurBand(int band) {
        try {
            // int band = getCurBand();
            DebugLog.d(TAG, "setCurBand band="+band+"; mServiceIF="+mServiceIF);
            mServiceIF.radio_setCurBand(band);
        } catch (Exception e) {
            DebugLog.e(TAG, "HMI------------setCurBand e=" + e.getMessage());
        }
    }

    /**
     * 获取收音区域（3ZA未使用）。
     */
    @Override
    public int getCurArea() {
        int area = Area.CHINA;
        try {
            area = mServiceIF.radio_getCurArea();
        } catch (Exception e) {
            DebugLog.e(TAG, "HMI------------getCurArea e=" + e.getMessage());
        }
        DebugLog.d(TAG, "getCurArea area="+area);
        return area;
    }

    /**
     * 设置收音区域。
     *
     * @param area
     */
    @Override
    public void setCurArea(byte area) {
        try {
            DebugLog.d(TAG, "setCurArea area="+area);
            mServiceIF.radio_setCurArea(area);
        } catch (Exception e) {
            DebugLog.e(TAG, "HMI------------setCurArea e=" + e.getMessage());
        }
    }

    /**
     * 获取第index个预存台，索引从1开始，传0获取的是列表的波段类型。index <= 30。
     *
     * @param index
     */
    @Override
    public int getChannel(int index) {
        int freq = 0;
        try {
            // 索引从1开始，传0获取的是列表的波段类型
            freq = mServiceIF.radio_getChannel(index + 1);
        } catch (Exception e) {
            DebugLog.e(TAG, "HMI------------getChannel e=" + e.getMessage());
        }
        return freq;
    }

    /**
     * 扫描并预览, 播放5秒，再搜索下一个。
     */
    @Override
    public void setScan() {
        try {
            //if (!isEnable()) { //ENABLE_RADIO_MUTEX_LOGIC
            //setEnable(true);
            //}
            //boolean focus = getRadioManager().requestAudioFocus(true);
            //DebugLog.d(TAG, "setScan focus="+focus);
            //if (focus) {
            //    isRescan = false;
            //    isScan5S = true;
            //    setRadioSource();
                mServiceIF.radio_scan();
            //}
        } catch (Exception e) {
            DebugLog.e(TAG, "HMI------------setScan e=" + e.getMessage());
        }
    }

    /**
     * 停止扫描。
     */
    @Override
    public void stopScan() {
        try {
            DebugLog.d(TAG, "stopScan");
            mServiceIF.radio_stopScanStore();
        } catch (Exception e) {
            DebugLog.e(TAG, "HMI------------stopScan e=" + e.getMessage());
        }
    }

    /**
     * 收音左步进
     */
    @Override
    public void setPreStep() {
        try {
            // boolean focus = getRadioManager().requestAudioFocus(true);
            // DebugLog.d(TAG, "setPreStep focus="+focus);
            // if (focus) {
            //    setRadioSource();
                //exitRescanAndScan5S(false);//ENABLE_RADIO_MUTEX_LOGIC
                mServiceIF.radio_scanManualPre();
            // }
        } catch (Exception e) {
            DebugLog.e(TAG, "HMI------------setPreStep e=" + e.getMessage());
        }
    }

    /**
     * 收音右步进
     */
    @Override
    public void setNextStep() {
        try {
            //boolean focus = getRadioManager().requestAudioFocus(true);
            //DebugLog.d(TAG, "setNextStep focus="+focus);
            //if (focus) {
            //    setRadioSource();
                //exitRescanAndScan5S(false);//ENABLE_RADIO_MUTEX_LOGIC
                mServiceIF.radio_scanManualNext();
            //}
        } catch (Exception e) {
            DebugLog.e(TAG, "HMI------------setNextStep e=" + e.getMessage());
        }
    }

    /**
     * 收音左搜索
     */
    @Override
    public void setPreSearch() {
        try {
            //boolean focus = getRadioManager().requestAudioFocus(true);
            //DebugLog.d(TAG, "setPreSearch focus="+focus);
            //if (focus) {
            //    setRadioSource();
                mServiceIF.radio_scanAutoPre();
            //}
        } catch (Exception e) {
            DebugLog.e(TAG, "HMI------------setPreSearch e=" + e.getMessage());
        }
    }

    /**
     * 收音右搜索
     */
    @Override
    public void setNextSearch() {
        try {
            //boolean focus = getRadioManager().requestAudioFocus(true);
            //DebugLog.d(TAG, "setNextSearch focus="+focus);
            //if (focus) {
            //    setRadioSource();
                mServiceIF.radio_scanAutoNext();
            //}
        } catch (Exception e) {
            DebugLog.e(TAG, "HMI------------setNextSearch e=" + e.getMessage());
        }
    }

    /**
     * 获取当前台是否是立体声电台。
     */
    @Override
    public boolean getST() {
        boolean value = false;
        try {
            value = mServiceIF.radio_getST() == 1 ? true : false;
        } catch (Exception e) {
            DebugLog.e(TAG, "HMI------------getST e=" + e.getMessage());
        }
        DebugLog.d(TAG, "getST value="+value);
        return value;
    }

    /**
     * 初始化频率列表。
     */
    @Override
    public void scanListChannel() {
        try {
            DebugLog.d(TAG, "scanListChannel");
            mServiceIF.radio_scanListChannel();
        } catch (Exception e) {
            DebugLog.e(TAG, "HMI------------scanListChannel e=" + e.getMessage());
        }
    }

    /**
     * 扫描电台。
     */
    @Override
    public void scanStore() {
        try {
            //boolean focus = getRadioManager().requestAudioFocus(true);
            //DebugLog.d(TAG, "scanStore focus="+focus);
            //if (focus) {
            //    isRescan = true;
            //    isScan5S = false;
            //    setRadioSource();
            //    setRecordRadioOnOff(false);
                mServiceIF.radio_scanStore();
            //}
        } catch (Exception e) {
            DebugLog.e(TAG, "HMI------------scanStore e=" + e.getMessage());
        }
    }

    /**
     * 上一电台，预存台中的电台
     */
    @Override
    public void setPreChannel() {
        try {
            //boolean focus = getRadioManager().requestAudioFocus(true);
            //DebugLog.d(TAG, "setPreChannel focus="+focus);
            //if (focus) {
            //    setRadioSource();
                mServiceIF.radio_setPreChannel();
                //mServiceIF.radio_setEnable(true);
            //}
        } catch (Exception e) {
            DebugLog.e(TAG, "HMI------------setPreChannel e=" + e.getMessage());
        }
    }

    /**
     * 下一电台，预存台中的电台
     */
    @Override
    public void setNextChannel() {
        try {
            //boolean focus = getRadioManager().requestAudioFocus(true);
            //DebugLog.d(TAG, "setNextChannel focus="+focus);
            //if (focus) {
            //    setRadioSource();
                mServiceIF.radio_setNextChannel();
                //mServiceIF.radio_setEnable(true);
            //}
        } catch (Exception e) {
            DebugLog.e(TAG, "HMI------------setNextChannel e=" + e.getMessage());
        }
    }

    @Override
    protected void onServiceConn() {

    }
}
