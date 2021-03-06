package com.amt.media.datacache;

import android.os.Handler;
import android.os.Message;

import com.amt.util.DebugLog;

public class LoadHandler extends Handler {
    protected static final int BEGIN_LOAD_ITEM = 1;
    protected static final int END_LOAD_ITEM = 2;
    protected static final int END_LOAD_THREAD = 3;

    static LoadThread mLoadThread;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case BEGIN_LOAD_ITEM: // 发起加载单个表的数据的请求。
                beginLoadItem((String) msg.obj);
                break;
            case END_LOAD_ITEM: // 单个表的数据加载结束。
                endLoadItem((String) msg.obj);
                break;
            case END_LOAD_THREAD: // 加载线程执行结束。
                DebugLog.d("LoadHandler", "END_LOAD_THREAD");
                break;
            default:
                break;
        }
    }

    private void beginLoadItem(String tableName) {
        if (mLoadThread == null) {
            mLoadThread = new LoadThread();
        }
        mLoadThread.addToListAndStart(tableName);
    }

    private void endLoadItem(String tableName) {
        AllMediaList allMediaList = AllMediaList.instance();
        for (LoadListener loadListener : allMediaList.mLoadListeners) {
            loadListener.onLoadCompleted(tableName);
        }
    }
}
