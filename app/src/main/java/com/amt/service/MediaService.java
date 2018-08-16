package com.amt.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amt.bt.BtMusicManager;
import com.amt.media.scan.ScanManager;
import com.amt.radio.RadioManager;

/**
 * Created by archermind on 2018/8/13.
 */

public class MediaService extends Service {
    public static final String TAG = "MediaService";

    public static final String KEY_COMMAND_FROM = "isfrom";
    public static final int VALUE_FROM_SCAN = 1;

    private MediaServiceBinder mBinder = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        BtMusicManager.getInstance();
        mBinder = new MediaServiceBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int fromValue = intent.getIntExtra(KEY_COMMAND_FROM, 0);
            switch (fromValue) {
                case VALUE_FROM_SCAN:
                    ScanManager.instance().operateIntent(intent);
                    break;
                default:
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
