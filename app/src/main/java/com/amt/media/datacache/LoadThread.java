package com.amt.media.datacache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoadThread extends Thread {

    class LoadData {
        boolean collectFlag;
        int portId;
        int fileType;

        public LoadData(boolean collectFlag, int portId, int fileType) {
            this.collectFlag = collectFlag;
            this.portId = portId;
            this.fileType = fileType;
        }
    }

    List<LoadData> mLoadMsgList = Collections.synchronizedList(new ArrayList<LoadData>());

    public void addToListAndStart(LoadData data) {
        for (int index = 0; index < mLoadMsgList.size(); index++) {
            LoadData loadData = mLoadMsgList.get(index);
            if (loadData.collectFlag == data.collectFlag &&
                    loadData.portId == data.portId &&
                    loadData.fileType == data.fileType) {
                return;
            }
        }
        mLoadMsgList.add(data);
    }

    @Override
    public void run() {
        synchronized (AllMediaList.mLoadLock) {
            while (mLoadMsgList.size() > 0) {
                LoadData data = mLoadMsgList.remove(0);
                // TODO
            }
        }
    }
}
