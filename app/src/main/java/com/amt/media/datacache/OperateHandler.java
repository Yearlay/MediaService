package com.amt.media.datacache;

import android.os.Handler;
import android.os.Message;

public class OperateHandler extends Handler {
    protected static final int MSG_DELETE = 1;
    protected static final int MSG_COLLECT = 2;
    protected static final int MSG_UNCOLLECT = 3;
    protected static final int MSG_COPY_TO_LOCAL = 4;
    protected static final int MSG_UPDATE_PROGRESS = 5;
    protected static final int MSF_OPERATE_COMPLETED = 6;
    protected static final int MSG_END_THREAD = 7;

    private OperateThread mOperateThread;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case MSG_DELETE:
            case MSG_COLLECT:
            case MSG_UNCOLLECT:
            case MSG_COPY_TO_LOCAL:
                if (mOperateThread == null) {
                    mOperateThread = new OperateThread();
                }
                mOperateThread.addToListAndStart((OperateThread.OperateData) msg.obj);
                break;
            case MSG_UPDATE_PROGRESS:
            case MSF_OPERATE_COMPLETED:
                OperateThread.OperateData operateData = (OperateThread.OperateData) msg.obj;
                operateData.listener.onOperateCompleted(operateData.operateValue, msg.arg1, msg.arg2);
                break;
            case MSG_END_THREAD:
                mOperateThread = null;
                break;
            default:
                break;
        }
    }
}
