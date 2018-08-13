package com.amt.util;

public class DebugLog {
    public static final boolean DEBUG = AmtConfig.ENABLE_DEBUG_LOG;

    public static void d(String tags, String str) {
        if (DEBUG) {
            android.util.Log.d(tags, str);
        }
    }
    
    public static void e(String tags, String str) {
        android.util.Log.e(tags, str);
    }
    
    public static void v(String tags, String str) {
        if (DEBUG) {
            android.util.Log.v(tags, str);
        }
    }
    
    public static void w(String tags, String str) {
        if (DEBUG) {
            android.util.Log.w(tags, str);
        }
    }
    
    public static void i(String tags, String str) {
        if (DEBUG) {
            android.util.Log.i(tags, str);
        }
    }
    
    public static void a(String className, String methodName, String discription) {
        if (DEBUG) {
            android.util.Log.i(className, methodName + ": " + discription);
        }
    }
}
