package com.amt.util;

public class DebugClock {
    private long mStartTime;
    
    public long getStartTime() {
        return mStartTime;
    }

    public DebugClock() {
        if (!DebugLog.DEBUG) return;
        mStartTime = System.currentTimeMillis();
    }
    
    public DebugClock(long startTime) {
        if (!DebugLog.DEBUG) return;
        this.mStartTime = startTime;
    }
    
    public void markTime() {
        if (!DebugLog.DEBUG) return;
        mStartTime = System.currentTimeMillis();
    }
    
    public long calculateTime(String tags, String runningInfo) {
        if (!DebugLog.DEBUG) return 0;
        long takingTime = System.currentTimeMillis() - mStartTime;
        DebugLog.i(tags, runningInfo + " taking time: " + takingTime + "ms");
        return takingTime;
    }
}
