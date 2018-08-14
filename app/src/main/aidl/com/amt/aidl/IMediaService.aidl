// IMediaService.aidl
package com.amt.aidl;

import com.amt.aidl.IMediaCallBack;
import android.os.Parcelable;

interface IMediaService {

    boolean registerCallBack(String mode, IMediaCallBack callBack);
    boolean unregisterCallBack(String mode);

    boolean funcBool(int funcID);

    int funcInt(int funcID);
    int funcIntEx(int funcID, int arg1, int arg2, int arg3);

    String funcStr(int funcID);
    String funcStrEx(int funcID, String arg1, String arg2, String arg3);

    int funcList(int funcID, in List<String> list);
    List<String> funcListEx(int funcID, int arg1, int arg2, int arg3);

}
