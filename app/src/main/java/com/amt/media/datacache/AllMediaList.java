package com.amt.media.datacache;

import android.content.Context;

import com.amt.media.bean.MediaBean;
import com.amt.media.bean.StorageBean;
import com.amt.media.database.MediaDbHelper;
import com.amt.media.util.DBConfig;
import com.amt.media.util.MediaUtil;
import com.amt.media.util.StorageConfig;
import com.amt.mediaservice.MediaApplication;
import com.amt.util.DebugLog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by archermind on 2018/8/9.
 */

public class AllMediaList {
    private static final String TAG = "AllMediaList";

    protected static Object mLoadLock = new Object();

    // KEY是tableName。
    private HashMap<String, ArrayList<MediaBean>> mAllMediaHash = new HashMap<String, ArrayList<MediaBean>>();

    private Context mContext;
    private MediaDbHelper mMediaDbHelper;

    private static AllMediaList mSelf;

    public static AllMediaList instance(Context context) {
        if (mSelf == null) {
            mSelf = new AllMediaList(context.getApplicationContext());
        }
        return mSelf;
    }

    public static AllMediaList instance() {
        return instance(MediaApplication.getInstance());
    }

    private AllMediaList(Context context) {
        mContext = context;
        mMediaDbHelper = new MediaDbHelper(mContext);
//        mLocalHandler = new LocalHandler();
//
//        registerObserverAll();
    }

    public void checkAllMediaDatas() {
        int[] fileTypes = new int[] {
                MediaUtil.FileType.AUDIO,
                MediaUtil.FileType.VIDEO,
                MediaUtil.FileType.IMAGE
        };
        for (StorageBean storageBean : StorageManager.instance().getDefaultStorageBeans()) {
            if (storageBean.getState() >= StorageBean.FILE_SCAN_OVER) {
                for (int fileType : fileTypes) {
                    String tableName = DBConfig.getTableName(storageBean.getPortId(), fileType);
                    ArrayList<MediaBean> mediaBeans = mAllMediaHash.get(tableName);
                    if (mediaBeans == null) {
                        mediaBeans = new ArrayList<MediaBean>();
                        mAllMediaHash.put(tableName, mediaBeans);
                        // TODO 发起 BEGIN_LOAD_THREAD 操作。
                    }
                }
            }
        }
        // 收藏表数据检查。
        for (int fileType : fileTypes) {
            String collectTableName = DBConfig.getCollectTableName(fileType);
            ArrayList<MediaBean> collectMediaBeans = mAllMediaHash.get(collectTableName);
            if (collectMediaBeans == null) {
                collectMediaBeans = new ArrayList<MediaBean>();
                mAllMediaHash.put(collectTableName, collectMediaBeans);
                // TODO 发起 BEGIN_LOAD_THREAD 操作。
            }
        }
    }

    public ArrayList<MediaBean> getMediaList(int portId, int fileType) {
        synchronized (mLoadLock) {
            ArrayList<MediaBean> mediaBeans = null;
            StorageBean storageBean = StorageManager.instance().getStorageBean(portId);
            if (storageBean.getState() >= StorageBean.FILE_SCAN_OVER) {
                String tableName = DBConfig.getTableName(portId, fileType);
                mediaBeans = mAllMediaHash.get(tableName);
                if (mediaBeans == null) {
                    mediaBeans = new ArrayList<MediaBean>();
                    mAllMediaHash.put(tableName, mediaBeans);
                    // TODO 发起 BEGIN_LOAD_THREAD 操作。
                }
            } else {
                DebugLog.e(TAG, "getMediaList error info --> portId: " + portId +
                        " && state: " + storageBean.getState());
            }
            return  mediaBeans;
        }
    }

    public ArrayList<MediaBean> getCollectMediaList(int fileType) {
        synchronized (mLoadLock) {
            ArrayList<MediaBean> mediaBeans = null;
            String tableName = DBConfig.getCollectTableName(fileType);
            mediaBeans = mAllMediaHash.get(tableName);
            if (mediaBeans == null) {
                mediaBeans = new ArrayList<MediaBean>();
                mAllMediaHash.put(tableName, mediaBeans);
                // TODO 发起 BEGIN_LOAD_THREAD 操作。
            }
            return  mediaBeans;
        }
    }
}
