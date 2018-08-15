// IMediaService.aidl
package com.amt.aidl;

import com.amt.aidl.IMediaCallBack;
import com.amt.aidl.RadioBean;

interface IMediaService {

    boolean registerCallBack(String appName, int callBackFlag, IMediaCallBack callBack);
    boolean unregisterCallBack(String appName);

    // 函数名规范：func + 返回值类型 [ + 参数类型首字母 ... + Ex(参数个数扩展,int类型)]

    boolean funcBool(int funcID);
    long funcLong(int funcID);

    int funcInt(int funcID);
    int funcIntEx(int funcID, int arg1, int arg2, int arg3);
    int funcIntLS(int funcID, in List<String> list);

    String funcStr(int funcID);
    String funcStrEx(int funcID, int arg1, int arg2, int arg3);
    String funcStrSSS(int funcID, String arg1, String arg2, String arg3);

    List<String> funcListSEx(int funcID, int arg1, int arg2, int arg3);

    //int funcIntLSS(int funcID, in List<String, String> list);
    List<RadioBean> getRadioDatas(int radioType);
}
