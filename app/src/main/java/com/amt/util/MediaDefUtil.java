package com.amt.util;

import static com.amt.aidl.MediaDef.*;
/**
 * Created by hww on 2018/8/15.
 */

public class MediaDefUtil {

    public static int getFuncRange(int funcID) {
        if (funcID > FUNC_ERROR) {
            return FUNC_ERROR;
        } else if (funcID > FUNC_IMAGE) {
            return FUNC_IMAGE;
        } else if (funcID > FUNC_VIDEO) {
            return FUNC_VIDEO;
        } else if (funcID > FUNC_AUDIO) {
            return FUNC_AUDIO;
        } else if (funcID > FUNC_RADIO) {
            return FUNC_RADIO;
        } else if (funcID > FUNC_BTMUSIC) {
            return FUNC_BTMUSIC;
        }
        return FUNC_ERROR;
    }
}
