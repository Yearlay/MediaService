package com.amt.media.datacache;

import com.amt.media.bean.MediaBean;
import com.amt.media.database.MediaDbHelper;
import com.amt.mediaservice.MediaApplication;
import com.amt.util.DebugLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoadThread extends Thread {

    List<String> mLoadMsgList = Collections.synchronizedList(new ArrayList<String>());

    private volatile boolean isRunning = false;

    public void addToListAndStart(String tableName) {
        synchronized (AllMediaList.mLoadLock) {
            for (int index = 0; index < mLoadMsgList.size(); index++) {
                if (tableName.equals(mLoadMsgList.get(index))) {
                    return;
                }
            }
            mLoadMsgList.add(tableName);

            if (!isRunning) {
                isRunning = true; // 防止非常快速地调用两次addToListAndStart（来不及调用run方法）。
                try {
                    start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        AllMediaList allMediaList = AllMediaList.instance();
        synchronized (AllMediaList.mLoadLock) {
            while (mLoadMsgList.size() > 0) {
                String tableName = mLoadMsgList.remove(0);
                DebugLog.d("LoadThread", "run() tableName: " + tableName);
                ArrayList<MediaBean> mediaBeans = MediaDatas.getMediaList(tableName);
                mediaBeans.clear();
                // TODO 切换查询的方式。
                boolean useProvider = true;
                if (useProvider) {
                    MediaDBInterface mediaDBInterface = MediaDBInterface.instance(allMediaList.getContext());
                    mediaBeans.addAll(mediaDBInterface.query(tableName, null, null));
                } else {
                    mediaBeans.addAll(MediaDbHelper.instance().query(tableName, null, null, false));
                }
                DebugLog.d("LoadThread", "run() mediaBeans size: " + mediaBeans.size());
                allMediaList.mLoadHandler.obtainMessage(LoadHandler.END_LOAD_ITEM, tableName).sendToTarget();
            }
            isRunning = false;
        }
        allMediaList.mLoadHandler.sendEmptyMessage(LoadHandler.END_LOAD_THREAD);
    }
}
