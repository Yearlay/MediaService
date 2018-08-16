package com.amt.radio;

import android.os.RemoteException;

import com.amt.aidl.MediaDef;
import com.amt.mediaservice.MediaApplication;
import com.amt.util.DebugLog;
import com.haoke.aidl.ICarCallBack;
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
                    mCallBack.onRadioDataChange(func, data);
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
            DebugLog.e(TAG, "getCurFreq e=" + e.getMessage());
        }
        DebugLog.d(TAG, "getCurFreq freq="+freq);
        return freq;
    }

    /**
     * 设置当前收音机频率。
     */
    @Override
    public int setCurFreq(int freq) {
        int code = 0;
        try {
            DebugLog.d(TAG, "setCurFreq freq="+freq);
            mServiceIF.radio_setCurFreq(freq);
        } catch (Exception e) {
            DebugLog.e(TAG, "setCurFreq e=" + e.getMessage());
            code = MediaDef.ERROR_CODE_UNKNOWN;
        }
        return code;
    }

    /**
     * 获取收音机的打开关闭状态。
     */
    @Override
    public int getEnable() {
        boolean enable = false;
        // int source = getCurSource();
        try {
            // if (Source.isRadioSource(source)) {
                enable = mServiceIF.radio_isEnable();
            // }
        } catch (Exception e) {
            DebugLog.e(TAG, "getEnable e=" + e.getMessage());
        }
        DebugLog.d(TAG, "getEnable enable="+enable);
        return enable ? MediaDef.TRUE : MediaDef.FALSE;
    }

    /**
     * 打开关闭收音机。
     *
     * @param enable
     */
    @Override
    public int setEnable(int enable) {
        int code = MediaDef.ERROR_CODE_UNKNOWN;
        try {
            boolean focus = true;
            DebugLog.d(TAG, "setEnable enable="+enable);
            if (enable == MediaDef.TRUE) {
                //if (MediaInterfaceUtil.mediaCannotPlay()) {
                //    return;
                //}
                //setRecordRadioOnOff(false);
                //focus = getRadioManager().requestAudioFocus(true);
                //DebugLog.d(TAG, "setEnable enable="+enable+"; focus="+focus);
                //if (focus) {
                //    setRadioSource();
                    //exitRescanAndScan5S(true);//ENABLE_RADIO_MUTEX_LOGIC
                    mServiceIF.radio_setEnable(true);
                //}
            } else {
                mServiceIF.radio_setEnable(false);
            }
            code = enable;
        } catch (Exception e) {
            DebugLog.e(TAG, "setEnable e=" + e.getMessage());
        }
        return code;
    }

    /**
     * 获取收音机波段。
     */
    @Override
    public int getCurBand() {
        int band = MediaDef.RadioDef.BAND_FM;
        try {
            band = mServiceIF.radio_getCurBand();
            switch (band) {
                case Band_5.FM1:
                case Band_5.FM2:
                case Band_5.FM3:
                    band = MediaDef.RadioDef.BAND_FM;
                    break;
                case Band_5.AM1:
                case Band_5.AM2:
                    band = MediaDef.RadioDef.BAND_AM;
                    break;
            }
        } catch (Exception e) {
            DebugLog.e(TAG, "getCurBand e=" + e.getMessage());
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
    public int setCurBand(int band) {
        int code = MediaDef.ERROR_CODE_UNKNOWN;
        try {
            // int band = getCurBand();
            DebugLog.d(TAG, "setCurBand band="+band+"; mServiceIF="+mServiceIF);
            mServiceIF.radio_setCurBand(band);
            code = band;
        } catch (Exception e) {
            DebugLog.e(TAG, "setCurBand e=" + e.getMessage());
        }
        return code;
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
            DebugLog.e(TAG, "getCurArea e=" + e.getMessage());
        }
        DebugLog.d(TAG, "getCurArea area="+area);
        return area;
    }

    /**
     * 设置收音区域。
     */
    @Override
    public int setCurArea(int area) {
        int code = MediaDef.ERROR_CODE_UNKNOWN;
        try {
            DebugLog.d(TAG, "setCurArea area="+area);
            byte b = (byte) (area & 0xff);
            mServiceIF.radio_setCurArea(b);
            code = area;
        } catch (Exception e) {
            DebugLog.e(TAG, "setCurArea e=" + e.getMessage());
        }
        return code;
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
            DebugLog.e(TAG, "getChannel e=" + e.getMessage());
        }
        return freq;
    }

    /**
     * 扫描并预览, 播放5秒，再搜索下一个。
     */
    @Override
    public int setScan() {
        int code = MediaDef.ERROR_CODE_UNKNOWN;
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
            code = MediaDef.SUCCESS;
        } catch (Exception e) {
            DebugLog.e(TAG, "setScan e=" + e.getMessage());
        }
        return code;
    }

    /**
     * 停止扫描。
     */
    @Override
    public int stopScan() {
        int code = MediaDef.ERROR_CODE_UNKNOWN;
        try {
            DebugLog.d(TAG, "stopScan");
            mServiceIF.radio_stopScanStore();
            code = MediaDef.SUCCESS;
        } catch (Exception e) {
            DebugLog.e(TAG, "stopScan e=" + e.getMessage());
        }
        return code;
    }

    /**
     * 收音左步进
     */
    @Override
    public int setPreStep() {
        int code = MediaDef.ERROR_CODE_UNKNOWN;
        try {
            // boolean focus = getRadioManager().requestAudioFocus(true);
            // DebugLog.d(TAG, "setPreStep focus="+focus);
            // if (focus) {
            //    setRadioSource();
                //exitRescanAndScan5S(false);//ENABLE_RADIO_MUTEX_LOGIC
                mServiceIF.radio_scanManualPre();
            // }
            code = MediaDef.SUCCESS;
        } catch (Exception e) {
            DebugLog.e(TAG, "setPreStep e=" + e.getMessage());
        }
        return code;
    }

    /**
     * 收音右步进
     */
    @Override
    public int setNextStep() {
        int code = MediaDef.ERROR_CODE_UNKNOWN;
        try {
            //boolean focus = getRadioManager().requestAudioFocus(true);
            //DebugLog.d(TAG, "setNextStep focus="+focus);
            //if (focus) {
            //    setRadioSource();
                //exitRescanAndScan5S(false);//ENABLE_RADIO_MUTEX_LOGIC
                mServiceIF.radio_scanManualNext();
            //}
            code = MediaDef.SUCCESS;
        } catch (Exception e) {
            DebugLog.e(TAG, "setNextStep e=" + e.getMessage());
        }
        return code;
    }

    /**
     * 收音左搜索
     */
    @Override
    public int setPreSearch() {
        int code = MediaDef.ERROR_CODE_UNKNOWN;
        try {
            //boolean focus = getRadioManager().requestAudioFocus(true);
            //DebugLog.d(TAG, "setPreSearch focus="+focus);
            //if (focus) {
            //    setRadioSource();
                mServiceIF.radio_scanAutoPre();
            //}
            code = MediaDef.SUCCESS;
        } catch (Exception e) {
            DebugLog.e(TAG, "setPreSearch e=" + e.getMessage());
        }
        return code;
    }

    /**
     * 收音右搜索
     */
    @Override
    public int setNextSearch() {
        int code = MediaDef.ERROR_CODE_UNKNOWN;
        try {
            //boolean focus = getRadioManager().requestAudioFocus(true);
            //DebugLog.d(TAG, "setNextSearch focus="+focus);
            //if (focus) {
            //    setRadioSource();
                mServiceIF.radio_scanAutoNext();
            //}
            code = MediaDef.SUCCESS;
        } catch (Exception e) {
            DebugLog.e(TAG, "setNextSearch e=" + e.getMessage());
        }
        return code;
    }

    /**
     * 获取当前台是否是立体声电台。
     */
    @Override
    public int getST() {
        int code = MediaDef.FALSE;
        try {
            code = mServiceIF.radio_getST() == 1 ? MediaDef.TRUE : MediaDef.FALSE;
        } catch (Exception e) {
            DebugLog.e(TAG, "getST e=" + e.getMessage());
        }
        DebugLog.d(TAG, "getST code="+code);
        return code;
    }

    /**
     * 初始化频率列表。
     */
    @Override
    public int scanListChannel() {
        int code = MediaDef.ERROR_CODE_UNKNOWN;
        try {
            DebugLog.d(TAG, "scanListChannel");
            mServiceIF.radio_scanListChannel();
            code = MediaDef.SUCCESS;
        } catch (Exception e) {
            DebugLog.e(TAG, "scanListChannel e=" + e.getMessage());
        }
        return code;
    }

    /**
     * 扫描电台。
     */
    @Override
    public int scanStore() {
        int code = MediaDef.ERROR_CODE_UNKNOWN;
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
            code = MediaDef.SUCCESS;
        } catch (Exception e) {
            DebugLog.e(TAG, "scanStore e=" + e.getMessage());
        }
        return code;
    }

    /**
     * 上一电台，预存台中的电台
     */
    @Override
    public int setPreChannel() {
        int code = MediaDef.ERROR_CODE_UNKNOWN;
        try {
            //boolean focus = getRadioManager().requestAudioFocus(true);
            //DebugLog.d(TAG, "setPreChannel focus="+focus);
            //if (focus) {
            //    setRadioSource();
                mServiceIF.radio_setPreChannel();
                //mServiceIF.radio_setEnable(true);
            //}
            code = MediaDef.SUCCESS;
        } catch (Exception e) {
            DebugLog.e(TAG, "setPreChannel e=" + e.getMessage());
        }
        return code;
    }

    /**
     * 下一电台，预存台中的电台
     */
    @Override
    public int setNextChannel() {
        int code = MediaDef.ERROR_CODE_UNKNOWN;
        try {
            //boolean focus = getRadioManager().requestAudioFocus(true);
            //DebugLog.d(TAG, "setNextChannel focus="+focus);
            //if (focus) {
            //    setRadioSource();
                mServiceIF.radio_setNextChannel();
                //mServiceIF.radio_setEnable(true);
            //}
            code = MediaDef.SUCCESS;
        } catch (Exception e) {
            DebugLog.e(TAG, "setNextChannel e=" + e.getMessage());
        }
        return code;
    }

    @Override
    protected void onServiceConn() {

    }
}
