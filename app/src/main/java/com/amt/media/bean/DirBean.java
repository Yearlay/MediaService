package com.amt.media.bean;

import com.amt.util.DebugLog;

import java.util.ArrayList;

public class DirBean {
    private String dirPath;        // 当前文件夹路径。
    private DirBean parentDirBean; // 父节点。
    private ArrayList<DirBean> dirBeans = new ArrayList<DirBean>();       // 子文件夹
    private ArrayList<MediaBean> childBeans = new ArrayList<MediaBean>(); // 子媒体。

    public DirBean(String dirPath, DirBean parentDirBean) {
        this.dirPath = dirPath;
        this.parentDirBean = parentDirBean;
    }

    public DirBean(String dirPath, ArrayList<MediaBean> beans, DirBean parentDirBean) {
        this.dirPath = dirPath;
        this.parentDirBean = parentDirBean;
        for (MediaBean mediaBean : beans) {
            addMediaBean(mediaBean);
        }
    }

    // 递归函数。
    private void addMediaBean(MediaBean mediaBean) {
        if (dirPath.equals(getDirName(mediaBean))) {
            childBeans.add(mediaBean); // 归属于 子媒体。
        } else { // 应该就是文件夹
            String childDirPath = getChileDirName(dirPath, mediaBean);
            if (childDirPath != null) {
                boolean newFlag = true;
                for (DirBean dirBean : dirBeans) {
                    if (childDirPath.equals(dirBean.dirPath)) {
                        dirBean.addMediaBean(mediaBean);
                        newFlag = false;
                        break;
                    }
                }
                if (newFlag) {
                    DirBean dirBean = new DirBean(childDirPath, this);
                    dirBeans.add(dirBean);
                    dirBean.addMediaBean(mediaBean);
                }
            } else {
                DebugLog.e("DirBean", "Error mediaBean : " + mediaBean.getFilePath());
            }

        }
    }

    private static String getDirName(MediaBean mediaBean) {
        String filePath = mediaBean.getFilePath();
        return filePath.substring(0, filePath.lastIndexOf("/"));
    }

    private static String getChileDirName(String dirPath, MediaBean mediaBean) {
        String retStr = null;
        try {
            String filePath = mediaBean.getFilePath();
            String rootPath = dirPath + "/";
            int beginIndex = filePath.indexOf(rootPath);
            String secondStr = filePath.substring(beginIndex + rootPath.length());
            retStr = rootPath + secondStr.substring(0, secondStr.indexOf("/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retStr;
    }
}
