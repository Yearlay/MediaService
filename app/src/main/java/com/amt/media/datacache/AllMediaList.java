package com.amt.media.datacache;

import android.content.Context;

import com.amt.media.bean.MediaBean;
import com.amt.media.database.MediaDbHelper;
import com.amt.mediaservice.MediaApplication;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by archermind on 2018/8/9.
 */

public class AllMediaList {
    private static final String TAG = "AllMediaList";

    private Object mLoadLock = new Object();

    private HashMap<String, ArrayList<MediaBean>> mAllMediaHash = new HashMap<String, ArrayList<MediaBean>>();

    private Context mContext;
    private MediaDbHelper mMediaDbHelper;

    private static AllMediaList sAllMediaList;

    public static AllMediaList instance(Context context) {
        if (sAllMediaList == null) {
            sAllMediaList = new AllMediaList(context.getApplicationContext());
        }
        return sAllMediaList;
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
}
