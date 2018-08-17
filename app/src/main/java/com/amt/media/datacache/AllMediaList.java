package com.amt.media.datacache;

import android.content.Context;
import android.os.Message;

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
    protected HashMap<String, ArrayList<MediaBean>> mAllMediaHash = new HashMap<String, ArrayList<MediaBean>>();
    protected LoadHandler mLoadHandler;
    protected ArrayList<LoadListener> mLoadListeners = new ArrayList<LoadListener>();

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
        mLoadHandler = new LoadHandler();
    }

    /**
     * 添加加载的监听回调。
     * @param listener
     */
    public void registerLoadListener(LoadListener listener) {
        mLoadListeners.add(listener);
    }

    /**
     * 取消加载监听的回调。
     * @param listener
     */
    public void unRegisterLoadListener(LoadListener listener) {
        mLoadListeners.remove(listener);
    }

    /**
     * 可以在下面两个场景调用checkAllMediaDatas
     * 场景A：所有的文件扫描结束的时候也就是即将开始ID3扫描的时候。
     * 场景B：所有的ID3解析结束的时候。
     */
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
                        mLoadHandler.obtainMessage(LoadHandler.BEGIN_LOAD_ITEM, tableName).sendToTarget();
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
                mLoadHandler.obtainMessage(LoadHandler.BEGIN_LOAD_ITEM, collectTableName).sendToTarget();
            }
        }
    }

    public ArrayList<MediaBean> getMediaList(String tableName) {
        synchronized (mLoadLock) {
            ArrayList<MediaBean> mediaBeans = null;
            int portId = DBConfig.getPortId(tableName);
            StorageBean storageBean = StorageManager.instance().getStorageBean(portId);
            // storageBean == null: 是收藏表 或者 是错误地址。
            boolean collectFlag = DBConfig.isCollectTable(tableName);
            if (collectFlag ||
                    (storageBean != null && storageBean.getState() >= StorageBean.FILE_SCAN_OVER)) {
                mediaBeans = mAllMediaHash.get(tableName);
                if (mediaBeans == null) {
                    mediaBeans = new ArrayList<MediaBean>();
                    mAllMediaHash.put(tableName, mediaBeans);
                    mLoadHandler.obtainMessage(LoadHandler.BEGIN_LOAD_ITEM, tableName).sendToTarget();
                }
            }
            return  mediaBeans;
        }
    }

    // 暂时废弃。
    private ArrayList<MediaBean> getMediaList(int portId, int fileType) {
        synchronized (mLoadLock) {
            ArrayList<MediaBean> mediaBeans = null;
            StorageBean storageBean = StorageManager.instance().getStorageBean(portId);
            if (storageBean.getState() >= StorageBean.FILE_SCAN_OVER) {
                String tableName = DBConfig.getTableName(portId, fileType);
                mediaBeans = mAllMediaHash.get(tableName);
                if (mediaBeans == null) {
                    mediaBeans = new ArrayList<MediaBean>();
                    mAllMediaHash.put(tableName, mediaBeans);
                    mLoadHandler.obtainMessage(LoadHandler.BEGIN_LOAD_ITEM, tableName).sendToTarget();
                }
            } else {
                DebugLog.e(TAG, "getMediaList error info --> portId: " + portId +
                        " && state: " + storageBean.getState());
            }
            return  mediaBeans;
        }
    }

    // 暂时废弃。
    private ArrayList<MediaBean> getCollectMediaList(int fileType) {
        synchronized (mLoadLock) {
            ArrayList<MediaBean> mediaBeans = null;
            String tableName = DBConfig.getCollectTableName(fileType);
            mediaBeans = mAllMediaHash.get(tableName);
            if (mediaBeans == null) {
                mediaBeans = new ArrayList<MediaBean>();
                mAllMediaHash.put(tableName, mediaBeans);
                mLoadHandler.obtainMessage(LoadHandler.BEGIN_LOAD_ITEM, tableName).sendToTarget();
            }
            return  mediaBeans;
        }
    }

    /**
     * 数据库的增删改操作会频繁调用此函数，需要做一个去频的处理。
     * 当前的去频延时是500ms。
     * @param tableName
     */
    public void notifyChange(String tableName) {
        mLoadHandler.removeMessages(LoadHandler.BEGIN_LOAD_ITEM, tableName);
        Message message = mLoadHandler.obtainMessage(LoadHandler.BEGIN_LOAD_ITEM, tableName);
        mLoadHandler.sendMessageDelayed(message, 500);
    }
}
