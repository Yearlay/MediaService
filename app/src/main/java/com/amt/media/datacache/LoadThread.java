package com.amt.media.datacache;

import com.amt.media.bean.MediaBean;
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
                ArrayList<MediaBean> mediaBeans = allMediaList.mAllMediaHash.get(tableName);
                MediaDBInterface mediaDBInterface = MediaDBInterface.instance(allMediaList.getContext());
                if (mediaBeans == null) {
                    mediaBeans = new ArrayList<MediaBean>();
                    allMediaList.mAllMediaHash.put(tableName, mediaBeans);
                } else {
                    mediaBeans.clear();
                }
                mediaBeans.addAll(mediaDBInterface.query(tableName, null, null));
                allMediaList.mLoadHandler.obtainMessage(LoadHandler.END_LOAD_ITEM, tableName).sendToTarget();
            }
            isRunning = false;
        }
        allMediaList.mLoadHandler.sendEmptyMessage(LoadHandler.END_LOAD_THREAD);
    }
}
