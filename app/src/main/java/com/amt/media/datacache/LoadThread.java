package com.amt.media.datacache;

import android.provider.MediaStore;

import com.amt.media.bean.MediaBean;
import com.amt.media.database.MediaDbHelper;
import com.amt.media.util.DBConfig;
import com.amt.media.util.MediaUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoadThread extends Thread {

    List<String> mLoadMsgList = Collections.synchronizedList(new ArrayList<String>());

    public void addToListAndStart(String tableName) {
        for (int index = 0; index < mLoadMsgList.size(); index++) {
            if (tableName.equals(mLoadMsgList.get(index))) {
                return;
            }
        }
        mLoadMsgList.add(tableName);
    }

    @Override
    public void run() {
        synchronized (AllMediaList.mLoadLock) {
            while (mLoadMsgList.size() > 0) {
                String tableName = mLoadMsgList.remove(0);
                AllMediaList allMediaList = AllMediaList.instance();
                MediaDbHelper mediaDbHelper = MediaDbHelper.instance();
                ArrayList<MediaBean> mediaBeans = allMediaList.mAllMediaHash.get(tableName);
                // mediaBeans不可能为空。不用加判空逻辑。
                mediaBeans.clear();
                mediaBeans.addAll(mediaDbHelper.query(tableName, null, null, false));
                allMediaList.mLoadHandler.obtainMessage(LoadHandler.END_LOAD_ITEM, tableName);
            }
        }
    }
}
