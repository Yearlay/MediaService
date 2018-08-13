package com.amt.media.datacache;

import com.amt.media.bean.StorageBean;

/**
 * Created by archermind on 2018/8/13.
 */

public interface StorageListener {
    public void onScanStateChange(StorageBean storageBean);
    public void onMediaCountChange(StorageBean storageBean);
}
