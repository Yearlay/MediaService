package com.amt.mediaservice;

import android.app.Application;

public class MediaApplication extends Application {
	
    private static MediaApplication mSelf;
    
	@Override
    public void onCreate() {
        super.onCreate();
        mSelf = this;
        // Media_IF.getInstance(); // 初始化接口
	}

    public static MediaApplication getInstance() {
		return mSelf;
	}
}
