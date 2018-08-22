package com.amt.media.datacache;

import android.os.Message;

import com.amt.media.bean.AudioBean;
import com.amt.media.bean.CollectAudioBean;
import com.amt.media.bean.CollectImageBean;
import com.amt.media.bean.CollectVideoBean;
import com.amt.media.bean.ImageBean;
import com.amt.media.bean.MediaBean;
import com.amt.media.bean.VideoBean;
import com.amt.media.util.DBConfig;
import com.amt.media.util.MediaUtil;
import com.amt.util.DebugLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OperateThread extends Thread {
    private static final String TAG = "OperateThread";

    private AllMediaList mAllMediaList;
    private MediaDBInterface mMediaDbInterface;
    public OperateThread() {
        mAllMediaList = AllMediaList.instance();
        mMediaDbInterface = MediaDBInterface.instance(mAllMediaList.getContext());
    }

    public static class OperateData {
        int operateValue;
        ArrayList<MediaBean> dataList;
        OperateListener listener;
        public OperateData(int operateValue, ArrayList<MediaBean> dataList, OperateListener listener) {
            this.operateValue = operateValue;
            this.dataList = dataList;
            this.listener = listener;
        }
    }

    private List<OperateData> mOperateList = Collections.synchronizedList(new ArrayList<OperateData>());

    private volatile boolean isRunning;
    private int mCurrentprogress;

    public void addToListAndStart(OperateData operateData) {
        mOperateList.add(operateData);
        if (!isRunning) {
            isRunning = true; // 防止非常快速地调用两次addToListAndStart（来不及调用run方法）。
            try {
                start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void run() {
        while (mOperateList.size() > 0) {
            OperateData operateData = mOperateList.remove(0);
            //1删除文件； 2收藏文件； 3取消收藏文件； 可查看OperateListener中的常量说明。
            mCurrentprogress = 0;
            switch (operateData.operateValue) {
                case OperateHandler.MSG_DELETE:
                    deleteMediaFiles(operateData.dataList, operateData, this);
                    break;
                case OperateHandler.MSG_COLLECT:
                    collectMediaFiles(operateData.dataList, operateData);
                    break;
                case OperateHandler.MSG_UNCOLLECT:
                    unCollectMediaFiles(operateData.dataList, operateData);
                    break;
                case OperateHandler.MSG_COPY_TO_LOCAL:
                    if (operateData.dataList.get(0).getFileType() == MediaUtil.FileType.IMAGE) {
                        copyToLocal(operateData.dataList, operateData, this);
                    } else {
                        copyToLocalForFileSize(operateData.dataList, operateData, this);
                    }
                    break;
                default:
                    break;
            }
            mCurrentprogress = 100;
            Message message = mAllMediaList.mOperateHandler.obtainMessage(
                    OperateHandler.MSF_OPERATE_COMPLETED,
                    mCurrentprogress, OperateListener.OPERATE_SUCEESS, operateData
            );
            mAllMediaList.mOperateHandler.sendMessage(message);
        }
        isRunning = false;
        mAllMediaList.mOperateHandler.sendEmptyMessage(OperateHandler.MSG_END_THREAD);
    }


    private void deleteMediaFiles(ArrayList<MediaBean> list, OperateData operateData, Thread thread) {
        DebugLog.a(TAG, "deleteMediaFiles", "Begin --> delte files && size:" + list.size());
        int resultCode = OperateListener.OPERATE_SUCEESS;
        int fileIndex = 0;
        for (MediaBean mediaBean : list) {
            if (thread.isInterrupted()) {
                DebugLog.e(TAG, "Interrupte deleteMediaFiles break for!");
                break;
            }
            File file = new File(mediaBean.getFilePath());
            if (file.exists()) {
                if (file.canWrite()) {
                    if (file.delete()) {
                        resultCode = OperateListener.OPERATE_SUCEESS;
                    } else {
                        resultCode = OperateListener.OPERATE_DELETE_ERROR;
                        DebugLog.e(TAG, "deleteMediaFiles exception OPERATE_DELETE_ERROR： " + resultCode);
                    }
                } else {
                    resultCode = OperateListener.OPERATE_DELETE_READ_ONLY;
                    DebugLog.e(TAG, "deleteMediaFiles exception OPERATE_DELETE_READ_ONLY： " + resultCode);
                }
            } else {
                resultCode = OperateListener.OPERATE_DELETE_NOT_EXIST;
                DebugLog.e(TAG, "deleteMediaFiles exception OPERATE_DELETE_NOT_EXIST： " + resultCode);
            }
            if (resultCode == OperateListener.OPERATE_SUCEESS ||
                    resultCode == OperateListener.OPERATE_DELETE_NOT_EXIST) {
                // 如果这条记录已经收藏，删除对应的收藏表的数据。
                if (mediaBean.getCollectFlag() == 1) {
                    MediaBean collectBean = null;
                    if (mediaBean.getFileType() == MediaUtil.FileType.AUDIO) {
                        collectBean = new CollectAudioBean((AudioBean) mediaBean);
                    } else if (mediaBean.getFileType() == MediaUtil.FileType.VIDEO) {
                        collectBean = new CollectVideoBean((VideoBean) mediaBean);
                    } else if (mediaBean.getFileType() == MediaUtil.FileType.IMAGE) {
                        collectBean = new CollectImageBean((ImageBean) mediaBean);
                    }
                    mMediaDbInterface.delete(collectBean);
                }
                // 删除媒体表中的数据。
                mMediaDbInterface.delete(mediaBean);
            }
            mCurrentprogress = (fileIndex * 100) / list.size();
            Message message = mAllMediaList.mOperateHandler.obtainMessage(
                    OperateHandler.MSG_UPDATE_PROGRESS,
                    mCurrentprogress, resultCode, operateData
            );
            mAllMediaList.mOperateHandler.sendMessage(message);
            fileIndex++;
        }
        DebugLog.a(TAG, "deleteMediaFiles", "End --> delete files resultCode:" + resultCode);
    }

    private void collectMediaFiles(ArrayList<MediaBean> list, OperateData operateData) {
        DebugLog.a(TAG, "collectMediaFiles", "Begin --> collect media files && size:" + list.size());
        int resultCode = OperateListener.OPERATE_SUCEESS;
        int fileIndex = 0;
        for (MediaBean mediaBean : list) {
            if(mediaBean.getCollectFlag() == 1) {
                break;
            }
            mediaBean.setCollectFlag(1);
            mMediaDbInterface.update(mediaBean); // 先更新 对应的媒体表中的数据。

            // 再插入对应的收藏表。
            MediaBean collectBean = null;
            if (mediaBean.getFileType() == MediaUtil.FileType.AUDIO) {
                collectBean = new CollectAudioBean((AudioBean) mediaBean);
            } else if (mediaBean.getFileType() == MediaUtil.FileType.VIDEO) {
                collectBean = new CollectVideoBean((VideoBean) mediaBean);
            } else if (mediaBean.getFileType() == MediaUtil.FileType.IMAGE) {
                collectBean = new CollectImageBean((ImageBean) mediaBean);
            }
            if (collectBean != null) {
                mMediaDbInterface.insert(collectBean);
            }

            fileIndex++;
            mCurrentprogress = (fileIndex * 100) / list.size();
            Message message = mAllMediaList.mOperateHandler.obtainMessage(
                    OperateHandler.MSG_UPDATE_PROGRESS,
                    mCurrentprogress, resultCode, operateData
            );
            mAllMediaList.mOperateHandler.sendMessage(message);
        }
        DebugLog.a(TAG, "collectMediaFiles", "End --> collect media files resultCode:" + resultCode);
    }

    private void unCollectMediaFiles(ArrayList<MediaBean> list, OperateData operateData) {
        DebugLog.a(TAG, "unCollectMediaFiles", "Begin --> uncollect media files && size:" + list.size());
        int resultCode = OperateListener.OPERATE_SUCEESS;
        int fileIndex = 0;
        for (MediaBean mediaBean : list) {
            boolean isFromCollectTable = (mediaBean instanceof CollectAudioBean) ||
                    (mediaBean instanceof CollectImageBean) ||
                    (mediaBean instanceof CollectVideoBean);
            if (!isFromCollectTable) { // 来自媒体表。
                // 先更新媒体表的数据。
                mediaBean.setCollectFlag(0);
                mMediaDbInterface.update(mediaBean);
                // 然后再删除收藏表中的数据
                MediaBean collectBean = null;
                if (mediaBean.getFileType() == MediaUtil.FileType.AUDIO) {
                    collectBean = new CollectAudioBean((AudioBean) mediaBean);
                } else if (mediaBean.getFileType() == MediaUtil.FileType.VIDEO) {
                    collectBean = new CollectVideoBean((VideoBean) mediaBean);
                } else if (mediaBean.getFileType() == MediaUtil.FileType.IMAGE) {
                    collectBean = new CollectImageBean((ImageBean) mediaBean);
                }
                mMediaDbInterface.delete(collectBean);
            } else { // 来自收藏表。
                // 先删除收藏表数据。
                mMediaDbInterface.delete(mediaBean);
                // TODO 然后更新媒体表中的数据。
                String tableName = DBConfig.getTableName(mediaBean.getPortId(), mediaBean.getFileType());
                ArrayList<MediaBean> beans = mMediaDbInterface.query(tableName,
                        MediaBean.FIELD_FILE_PATH + "=?",
                        new String[]{mediaBean.getFilePath()});
                if (beans.size() == 1) {
                    MediaBean bean = beans.get(0);
                    if (bean.getCollectFlag() == 1) { // 收藏标志位1
                        bean.setCollectFlag(0);       // 修改收藏标志为0
                        mMediaDbInterface.update(bean);  // 更新数据库。
                    }
                } else {
                    DebugLog.e(TAG, "unCollectMediaFiles exception list size: " + list.size() +
                            " collect path: " + mediaBean.getFilePath());
                }
            }

            fileIndex++;
            mCurrentprogress = (fileIndex * 100) / list.size();
            Message message = mAllMediaList.mOperateHandler.obtainMessage(
                    OperateHandler.MSG_UPDATE_PROGRESS,
                    mCurrentprogress, resultCode, operateData
            );
            mAllMediaList.mOperateHandler.sendMessage(message);
        }
        DebugLog.a(TAG, "unCollectMediaFiles", "End --> uncollect media files resultCode:" + resultCode);
    }

    private void copyToLocal(ArrayList<MediaBean> list, OperateData operateData, Thread thread) {
        DebugLog.a(TAG, "copyToLocal", "Begin --> copy media files && size:" + list.size());
        int resultCode = OperateListener.OPERATE_SUCEESS;
        int fileIndex = 0;
        for (MediaBean mediaBean : list) {
            if (thread.isInterrupted()) {
                DebugLog.e(TAG, "Interrupte deleteMediaFiles break for!");
                break;
            }
            String destFilePath = MediaUtil.LOCAL_COPY_DIR + "/" +
                    mediaBean.getFilePath().substring(mediaBean.getFilePath().lastIndexOf('/') + 1);
            if (MediaUtil.pasteFileByte(thread,
                    new File(mediaBean.getFilePath()), new File(destFilePath),
                    MediaUtil.LOCAL_COPY_DIR)) { // 文件拷贝成功。
                // 如果数据库中这个文件的记录，先要执行delete，然后再执行插入insert。
                MediaBean copyMediaBean = new MediaBean(destFilePath,
                        mediaBean.getFileName(), mediaBean.getFileNamePY(), mediaBean.getFileType());
                mMediaDbInterface.delete(copyMediaBean);
                mMediaDbInterface.insert(copyMediaBean);
            } else { // 文件拷贝失败。
                resultCode = OperateListener.OPERATE_COLLECT_COPY_FILE_FAILED;
                DebugLog.e(TAG, "copyToLocal exception OPERATE_COLLECT_COPY_FILE_FAILED");
                if (thread.isInterrupted()) {
                    resultCode = OperateListener.OPERATE_SUCEESS;
                    DebugLog.e(TAG, "Interrupte deleteMediaFiles resultCode: " + resultCode);
                }
            }
            fileIndex++;
            mCurrentprogress = (fileIndex * 100) / list.size();
            Message message = mAllMediaList.mOperateHandler.obtainMessage(
                    OperateHandler.MSG_UPDATE_PROGRESS,
                    mCurrentprogress, resultCode, operateData
            );
            mAllMediaList.mOperateHandler.sendMessage(message);
            if (resultCode != OperateListener.OPERATE_SUCEESS) {
                break;
            }
        }
        DebugLog.a(TAG, "copyToLocal", "End --> copy media files resultCode:" + resultCode);
    }

    private void copyToLocalForFileSize(ArrayList<MediaBean> list, OperateData operateData, Thread thread) {
        DebugLog.a(TAG, "copyToLocalForFileSize", "Begin --> copy media files && size:" + list.size());
        int resultCode = OperateListener.OPERATE_SUCEESS;
        mCurrentprogress = 0;
        long totalSize = 0;
        long docopySize = 0;
        for (MediaBean mediaBean : list) {
            totalSize += mediaBean.getFileSize();
        }
        for (MediaBean mediaBean : list) {
            if (thread.isInterrupted()) {
                DebugLog.e(TAG, "Interrupte copyToLocalForFileSize break for!");
                break;
            }
            String destFilePath = MediaUtil.LOCAL_COPY_DIR + "/" + mediaBean.getFileName();
            boolean ret = true;
            File file = new File(MediaUtil.LOCAL_COPY_DIR);
            if (file != null && !file.exists()) {
                if (!file.mkdirs()) {
                    DebugLog.w("Yearlay", "mkdir collect path failed");
                }
            }
            File srcfile = new File(mediaBean.getFilePath());
            File tarFile = new File(destFilePath);
            // 是文件,读取文件字节流,同时记录进度
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = new FileInputStream(srcfile);// 读取源文件
                outputStream = new FileOutputStream(tarFile);// 要写入的目标文件
                System.gc();
                byte[] buffer = new byte[(int) Math.pow(2, 20)];// 每次最大读取的长度，字节，2的10次方=1MB。
                int length = -1;
                while ((length = inputStream.read(buffer)) != -1 && !thread.isInterrupted()) {
                    // 累计每次读取的大小
                    outputStream.write(buffer, 0, length);
                    docopySize += length;

                    int progress = (int) (docopySize  * 100 / totalSize);
                    if (progress != mCurrentprogress && progress < 100) {
                        mCurrentprogress = progress;
                        Message message = mAllMediaList.mOperateHandler.obtainMessage(
                                OperateHandler.MSG_UPDATE_PROGRESS,
                                mCurrentprogress, resultCode, operateData
                        );
                        mAllMediaList.mOperateHandler.sendMessage(message);
                    }
                }
            } catch (Exception e) {
                try {
                    if (outputStream != null) outputStream.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                tarFile.delete();
                e.printStackTrace();
                ret = false;
            } finally {
                try {
                    if (inputStream != null) inputStream.close();
                    if (outputStream != null) outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (thread.isInterrupted()) {
                DebugLog.e(TAG, "Interrupte copyToLocalForFileSize delete tarFile!");
                try {
                    if (outputStream != null) outputStream.close();
                    tarFile.delete();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                ret = false;
            }
            if (ret) { // 文件拷贝成功。
                // 文件拷贝成功。将拷贝后的文件信息添加到数据库中。
                MediaBean copyMediaBean = new MediaBean(destFilePath,
                        mediaBean.getFileName(), mediaBean.getFileNamePY(), mediaBean.getFileType());
                mMediaDbInterface.delete(copyMediaBean);
                mMediaDbInterface.insert(copyMediaBean);
            } else { // 文件拷贝失败。
                resultCode = OperateListener.OPERATE_COLLECT_COPY_FILE_FAILED;
                DebugLog.e(TAG, "copyToLocalForFileSize exception OPERATE_COLLECT_COPY_FILE_FAILED");
                if (thread.isInterrupted()) {
                    resultCode = OperateListener.OPERATE_SUCEESS;
                    DebugLog.e(TAG, "Interrupte copyToLocalForFileSize resultCode:" + resultCode);
                }
            }
            if (resultCode != OperateListener.OPERATE_SUCEESS) {
                break;
            }
        }
        mCurrentprogress = 100;
        Message message = mAllMediaList.mOperateHandler.obtainMessage(
                OperateHandler.MSG_UPDATE_PROGRESS,
                mCurrentprogress, resultCode, operateData
        );
        mAllMediaList.mOperateHandler.sendMessage(message);
        DebugLog.a(TAG, "copyToLocalForFileSize", "End --> copy media files resultCode:" + resultCode);
    }
}