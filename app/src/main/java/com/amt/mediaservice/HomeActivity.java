package com.amt.mediaservice;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.amt.media.bean.MediaBean;
import com.amt.media.bean.StorageBean;
import com.amt.media.datacache.AllMediaList;
import com.amt.media.datacache.LoadListener;
import com.amt.media.scan.StorageManager;
import com.amt.media.util.DBConfig;
import com.amt.service.MediaService;
import com.amt.util.DebugLog;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements LoadListener {
    String[] mTables = new String[] {
            DBConfig.DBTable.SDCARD_AUDIO, DBConfig.DBTable.SDCARD_VIDEO, DBConfig.DBTable.SDCARD_IMAGE,
            DBConfig.DBTable.USB1_AUDIO, DBConfig.DBTable.USB1_VIDEO, DBConfig.DBTable.USB1_IMAGE,
            DBConfig.DBTable.USB2_AUDIO, DBConfig.DBTable.USB2_VIDEO, DBConfig.DBTable.USB2_IMAGE,
            DBConfig.DBTable.COLLECT_AUDIO, DBConfig.DBTable.COLLECT_VIDEO, DBConfig.DBTable.COLLECT_IMAGE,
    };

    private ListView mSourceListView;
    private SourceAdapter mSourceAdapter;
    private String mCurrentTableName;
    private ListView mMediaListView;
    private MediaListAdapter mMediaListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mSourceListView = (ListView) findViewById(R.id.source_list);
        mMediaListView = (ListView) findViewById(R.id.media_list);

        mSourceAdapter = new SourceAdapter();
        mSourceListView.setAdapter(mSourceAdapter);

        mCurrentTableName = mTables[0];
        mMediaListAdapter = new MediaListAdapter(mCurrentTableName);
        mMediaListView.setAdapter(mMediaListAdapter);

        startService(new Intent(this, MediaService.class));
        AllMediaList.instance().registerLoadListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AllMediaList.instance().unRegisterLoadListener(this);
    }

    @Override
    public void onLoadCompleted(String tableName) {
        DebugLog.d("HomeActivity", "onLoadCompleted tableName: " + tableName);
        mSourceAdapter.notifyDataSetChanged();
        if (tableName != null && tableName.equals(mCurrentTableName)) {
            mMediaListAdapter = new MediaListAdapter(mCurrentTableName);
            mMediaListView.setAdapter(mMediaListAdapter);
        }
    }


    class SourceAdapter extends BaseAdapter implements View.OnClickListener {

        @Override
        public int getCount() {
            return mTables.length;
        }

        @Override
        public String getItem(int position) {
            return mTables[position];
        }

        @Override
        public long getItemId(int id) {
            return id;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            Button button;
            if (view == null || !(view instanceof Button)) {
                button = new Button(HomeActivity.this);
            } else {
                button = (Button) view;
            }
            String tableName = mTables[position];
            int portId = DBConfig.getPortId(tableName);
            StorageBean storageBean = AllMediaList.instance().getStorageBean(portId);

            button.setText(tableName);
            button.setTextSize(16);
            if (storageBean != null) {
                DebugLog.d("SourceAdapter", "getView storageBean state: " + storageBean.getState());
                button.setEnabled(storageBean.isMounted());
            } else {
                button.setEnabled(true);
            }
            button.setOnClickListener(this);
            return button;
        }

        @Override
        public void onClick(View view) {
            if (view instanceof Button) {
                mCurrentTableName = (String) ((Button) view).getText();
                DebugLog.d("SourceAdapter", "onClick tableName:" + mCurrentTableName);
                mMediaListAdapter = new MediaListAdapter(mCurrentTableName);
                mMediaListView.setAdapter(mMediaListAdapter);
            }
        }
    }

    class MediaListAdapter extends BaseAdapter {
        String tableName;
        ArrayList<MediaBean> mediaBeans;

        public MediaListAdapter(String tableName) {
            this.tableName = tableName;
            mediaBeans = AllMediaList.instance().getMediaList(tableName);
            if (mediaBeans != null) {
                DebugLog.d("MediaListAdapter", "mediaBeans size: " + getCount());
            } else {
                DebugLog.e("MediaListAdapter", "mediaBeans is null.");
            }
        }

        @Override
        public int getCount() {
            return mediaBeans == null ? 0 : mediaBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int id) {
            return id;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            TextView textView;
            if (view == null || !(view instanceof TextView)) {
                textView = new TextView(HomeActivity.this);
            } else {
                textView = (TextView) view;
            }
            if (position < mediaBeans.size()) {
                MediaBean mediaBean = mediaBeans.get(position);
                textView.setText(mediaBean.getFilePath());
                textView.setTextColor(Color.BLACK);
            }
            return textView;
        }
    }
}
