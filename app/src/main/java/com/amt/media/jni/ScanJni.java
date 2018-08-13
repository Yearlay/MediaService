package com.amt.media.jni;

import com.amt.media.bean.MediaBean;
import com.amt.media.database.MediaDbHelper;

public class ScanJni {
    static {
        System.loadLibrary("scanjni-lib");
    }
    public native String stringFromJni();
    public static native String getPY(String fileName);
    public native int scanRootPath(String rootPath, int onlyGetMediaSizeFlag);
    
    private static final String TAG = "ScanJni";
    
    private MediaDbHelper mMediaDbHelper;
    public ScanJni(MediaDbHelper mediaDbHelper) {
        super();
        mMediaDbHelper = mediaDbHelper;
    }

    public void insertToDb(MediaBean mediaBean) {
        /*if (DBConfig.isMediaType(fileNode.getFileType())) {
            mMediaDbHelper.addToNeedToInsertList(new TransactionTask(fileNode, TransactionTask.INSERT_TASK));
        }*/
    }
}
