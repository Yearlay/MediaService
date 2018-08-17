package com.amt.media.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.amt.media.scan.ScanManager;
import com.amt.service.MediaService;
import com.amt.util.DebugLog;
import com.amt.media.util.StorageConfig;

/**
 * Created by archermind on 2018/8/13.
 */

public class MediaReceiver extends BroadcastReceiver {
    private static final String TAG = "MediaReceiver";

    public static boolean sUsb1Mounted = true;
    public static boolean sUsb2Mounted = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        DebugLog.i(TAG, "MediaReceiver onReceive action: " + action);
        String datapath = intent.getDataString();
        if (datapath != null) {
            datapath = datapath.replace("file://", "");
        }
        String storagePath = StorageConfig.getRealPathOfStorage(datapath);
        if (storagePath == null) {
            DebugLog.e(TAG, "MediaReceiver onReceive storagePath is null");
            return;
        }

        if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
            if (StorageConfig.USB1_STORAGE_PATH.equals(storagePath)) {
                sUsb1Mounted = true;
            } else if (StorageConfig.USB2_STORAGE_PATH.equals(storagePath)) {
                sUsb2Mounted = true;
            }
            startFileService(context, ScanManager.MOUNT_STORAGE, storagePath);
        } else if (action.equals(Intent.ACTION_MEDIA_EJECT) || action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
            if (StorageConfig.USB1_STORAGE_PATH.equals(storagePath)) {
                if (!sUsb1Mounted) {
                    return;
                }
                sUsb1Mounted = false;
            } else if (StorageConfig.USB2_STORAGE_PATH.equals(storagePath)) {
                if (!sUsb2Mounted) {
                    return;
                }
                sUsb2Mounted = false;
            }
            startFileService(context, ScanManager.REMOVE_STORAGE, storagePath);
        }

    }

    private void startFileService(Context context, int scanType, String storagePath) {
        Intent intents = new Intent(context, MediaService.class);
        intents.putExtra(MediaService.KEY_COMMAND_FROM, MediaService.VALUE_FROM_SCAN);
        intents.putExtra(ScanManager.SCAN_TYPE_KEY, scanType);
        intents.putExtra(ScanManager.SCAN_FILE_PATH, storagePath);
        context.startService(intents);
    }
}
