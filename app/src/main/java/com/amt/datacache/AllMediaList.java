package com.amt.datacache;

import android.content.Context;

import com.amt.database.MediaDbHelper;
import com.amt.mediaservice.MediaApplication;

/**
 * Created by archermind on 2018/8/9.
 */

public class AllMediaList {
    private static final String TAG = "AllMediaList";

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
//        mMediaDbHelper = new MediaDbHelper(mContext);
//        mLocalHandler = new LocalHandler();
//
//        registerObserverAll();
    }
}
