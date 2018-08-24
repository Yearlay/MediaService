package com.amt.media.datacache;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.amt.media.bean.MediaBean;
import com.amt.media.bean.StorageBean;
import com.amt.media.scan.StorageManager;
import com.amt.media.util.DBConfig;
import com.amt.media.util.MediaUtil;
import com.amt.media.util.UriConfig;
import com.amt.mediaservice.MediaApplication;
import com.amt.util.DebugLog;

import java.util.ArrayList;

/**
 * Created by archermind on 2018/8/9.
 */

public class AllMediaList {
    private static final String TAG = "AllMediaList";

    protected static Object mLoadLock = new Object();

    // KEY是tableName。
    protected LoadHandler mLoadHandler;
    protected ArrayList<LoadListener> mLoadListeners = new ArrayList<LoadListener>();
    protected OperateHandler mOperateHandler;
    private ArrayList<MediaContentObserver> mObserverList = new ArrayList<MediaContentObserver>();
    private StorageContentObserver mStorageContentObserver;

    private Context mContext;
    public Context getContext() {
        return mContext;
    }

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
        mLoadHandler = new LoadHandler();
        mOperateHandler = new OperateHandler();

        registerObserverAll();
    }

    private void registerObserverAll() {
        unRegisterObserverAll();
        registerObserver(UriConfig.URI_SDCARD_AUDIO_ADDR);
        registerObserver(UriConfig.URI_SDCARD_VIDEO_ADDR);
        registerObserver(UriConfig.URI_SDCARD_IMAGE_ADDR);

        registerObserver(UriConfig.URI_USB1_AUDIO_ADDR);
        registerObserver(UriConfig.URI_USB1_VIDEO_ADDR);
        registerObserver(UriConfig.URI_USB1_IMAGE_ADDR);

        registerObserver(UriConfig.URI_USB2_AUDIO_ADDR);
        registerObserver(UriConfig.URI_USB2_VIDEO_ADDR);
        registerObserver(UriConfig.URI_USB2_IMAGE_ADDR);

        registerObserver(UriConfig.URI_COLLECT_AUDIO_ADDR);
        registerObserver(UriConfig.URI_COLLECT_VIDEO_ADDR);
        registerObserver(UriConfig.URI_COLLECT_IMAGE_ADDR);

        mStorageContentObserver = new StorageContentObserver(new Handler());
        mContext.getContentResolver().registerContentObserver(
                Uri.parse(UriConfig.URI_STORAGE_ADDR), true, mStorageContentObserver);
    }

    private void registerObserver(String UriStr) {
        MediaContentObserver observer = new MediaContentObserver(new Handler());
        mContext.getContentResolver().registerContentObserver(Uri.parse(UriStr), true, observer);
        mObserverList.add(observer);
    }


    private void unRegisterObserverAll() {
        while (mObserverList.size() > 0) {
            MediaContentObserver observer = mObserverList.remove(0);
            mContext.getContentResolver().unregisterContentObserver(observer);
        }
        if (mStorageContentObserver != null) {
            mContext.getContentResolver().unregisterContentObserver(mStorageContentObserver);
        }
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
        for (StorageBean storageBean : mStorageContentObserver.storageBeans) {
            if (storageBean.getState() >= StorageBean.FILE_SCAN_OVER) {
                for (int fileType : fileTypes) {
                    String tableName = DBConfig.getTableName(storageBean.getPortId(), fileType);
                    mLoadHandler.obtainMessage(LoadHandler.BEGIN_LOAD_ITEM, tableName).sendToTarget();
                }
            }
        }
        // 收藏表数据检查。
        for (int fileType : fileTypes) {
            String collectTableName = DBConfig.getCollectTableName(fileType);
            mLoadHandler.obtainMessage(LoadHandler.BEGIN_LOAD_ITEM, collectTableName).sendToTarget();
        }
    }

    public ArrayList<MediaBean> getMediaList(String tableName) {
        synchronized (mLoadLock) {
            ArrayList<MediaBean> mediaBeans = null;
            int portId = DBConfig.getPortId(tableName);
            StorageBean storageBean = getStorageBean(portId);
            // storageBean == null: 是收藏表 或者 是错误地址。
            boolean collectFlag = DBConfig.isCollectTable(tableName);
            DebugLog.e(TAG, "getMediaList tableName: " + tableName);
            if (collectFlag ||
                    (storageBean != null && storageBean.getState() >= StorageBean.FILE_SCAN_OVER)) {
                mediaBeans = MediaDatas.getMediaList(tableName);
                if (mediaBeans.size() == 0) {
                    DebugLog.e(TAG, "getMediaList mediaBeans.size() == 0  BEGIN_LOAD_ITEM tableName:" + tableName);
                    mLoadHandler.obtainMessage(LoadHandler.BEGIN_LOAD_ITEM, tableName).sendToTarget();
                }
                DebugLog.e(TAG, "getMediaList mediaBeans size: " + mediaBeans.size());
            }
            if (mediaBeans == null) {
                mediaBeans = new ArrayList<MediaBean>();
            }
            return mediaBeans;
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

    /**
     * 删除操作，针对集合对象。
     */
    public void deleteMediaFiles(ArrayList<MediaBean> beans, OperateListener listener) {
        int operateValue = OperateHandler.MSG_DELETE;
        Message message = mOperateHandler.obtainMessage(operateValue,
                new OperateThread.OperateData(operateValue, beans, listener));
        mOperateHandler.sendMessage(message);
    }

    /**
     * 收藏操作，MediaBean。
     */
    public void collectMediaFile(MediaBean mediaBean, OperateListener listener) {
        ArrayList<MediaBean> dataList = new ArrayList<MediaBean>();
        dataList.add(mediaBean);
        collectMediaFiles(dataList, listener);
    }

    /**
     * 收藏操作，针对集合对象。
     */
    public void collectMediaFiles(ArrayList<MediaBean> beans, OperateListener listener) {
        int operateValue = OperateHandler.MSG_COLLECT;
        Message message = mOperateHandler.obtainMessage(operateValue,
                new OperateThread.OperateData(operateValue, beans, listener));
        mOperateHandler.sendMessage(message);
    }

    /**
     * 取消收藏操作，针对FileNode对象。
     */
    public void uncollectMediaFile(MediaBean mediaBean, OperateListener listener) {
        ArrayList<MediaBean> dataList = new ArrayList<MediaBean>();
        dataList.add(mediaBean);
        uncollectMediaFiles(dataList, listener);
    }

    /**
     * 取消收藏操作，针对集合对象。
     */
    public void uncollectMediaFiles(ArrayList<MediaBean> beans, OperateListener listener) {
        int operateValue = OperateHandler.MSG_UNCOLLECT;
        Message message = mOperateHandler.obtainMessage(operateValue,
                new OperateThread.OperateData(operateValue, beans, listener));
        mOperateHandler.sendMessage(message);
    }

    /**
     * 拷贝操作，针对集合对象。
     */
    public void copyToLocal(ArrayList<MediaBean> beans, OperateListener listener) {
        int operateValue = OperateHandler.MSG_COPY_TO_LOCAL;
        Message message = mOperateHandler.obtainMessage(operateValue,
                new OperateThread.OperateData(operateValue, beans, listener));
        mOperateHandler.sendMessage(message);
    }

    class MediaContentObserver extends ContentObserver {
        public MediaContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            String uriAddr = uri.toString();
            DebugLog.d(TAG, "MediaContentObserver onChange uri: " + uriAddr);
            String tableName = UriConfig.getTableName(uriAddr);
            int portId = DBConfig.getPortId(tableName);
            StorageBean storageBean = getStorageBean(portId);
            if (storageBean.getState() >= StorageBean.ID3_PARSE_OVER) {
                DebugLog.d(TAG, "ID3_PARSE_OVER onChange uri: " + uriAddr);
                notifyChange(tableName);
            }
        }
    }

    public StorageBean getStorageBean(int portId) {
        StorageBean storageBean = null;
        if (mStorageContentObserver != null) {
            for (StorageBean bean : mStorageContentObserver.storageBeans) {
                if (bean.getPortId() == portId) {
                    storageBean = bean;
                    break;
                }
            }
        }
        return storageBean;
    }

    class StorageContentObserver extends ContentObserver {
        private ArrayList<StorageBean> storageBeans = new ArrayList<StorageBean>();

        public StorageContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            String uriAddr = uri.toString();
            DebugLog.d(TAG, "StorageContentObserver onChange uri: " + uriAddr);
            ArrayList<StorageBean> newBeans = MediaDBInterface.instance(mContext).queryStorageBeans();
            storageBeans.clear();
            storageBeans.addAll(newBeans);
            DebugLog.d(TAG, "StorageContentObserver onChange newBeans size: " + storageBeans.size());
            for (StorageBean storageBean : storageBeans) {
                DebugLog.d(TAG, storageBean.toString());
            }
        }
    }
}
