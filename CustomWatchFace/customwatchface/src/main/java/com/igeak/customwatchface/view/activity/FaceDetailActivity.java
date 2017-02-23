package com.igeak.customwatchface.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.igeak.android.common.api.GeakApiClient;
import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.MyApplication;
import com.igeak.customwatchface.model.WatchFace;
import com.igeak.customwatchface.model.WatchFacesModel;
import com.igeak.customwatchface.presenter.IWatchFaceDetailContract;
import com.igeak.customwatchface.presenter.WatchFaceDetailPresent;
import com.igeak.customwatchface.R;
import com.igeak.customwatchface.view.view.watchfaceview.WatchPreviewView;

public class FaceDetailActivity extends BaseActivity implements IWatchFaceDetailContract
        .IWatchFaceDetailView {

    RecyclerView mRecyclerView = null;
    RecycleViewAdapter mRecycleViewAdapter = null;
    WatchFaceDetailPresent present = null;
    WatchFaceBean watchfacebean = null;
    WatchFace watchface;
    String[] data = {"12小时制", "显示天气", "显示日期", "显示星期"};
    WatchFacesModel.FacePath facePath;
    WatchPreviewView watchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detail);

        Intent intent = getIntent();
        watchfacebean = intent.getParcelableExtra(Const.INTENT_EXTRA_KEY_WATCHFACE);
        facePath = intent.getBooleanExtra(Const.INTENT_EXTRA_KEY_ISCUSTOM, false) ? WatchFacesModel
                .FacePath.FACE_CUSTOM : WatchFacesModel.FacePath.FACE_INNER;
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_facelist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecycleViewAdapter = new RecycleViewAdapter(watchfacebean);
        watchView = (WatchPreviewView) findViewById(R.id.watch_view);

        present = new WatchFaceDetailPresent(this.getApplicationContext(), this);

        setTitle(watchfacebean.getName()); //更新title
        setFun1(getResources().getString(R.string.option_release), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send2Watch(v);
            }
        });
        present.loadWatchImg(watchfacebean, facePath);
    }


    public void send2Watch(View v) {
        MyApplication myApplication = (MyApplication) getApplication();
        GeakApiClient googleApiClient = myApplication.mGoogleApiclent;
        present.zipFileAndSentToWatch(googleApiClient, watchfacebean, facePath);
    }

    @Override
    public void updateWatchFaceDetailView(WatchFace watchFace) {
        this.watchface = watchFace;
        watchView.setElements(watchface);
        watchView.invalidate();

        if (!mRecycleViewAdapter.isSetAdapter()) {
            mRecyclerView.setAdapter(mRecycleViewAdapter);
        } else {
            mRecycleViewAdapter.notifyDataSetChanged(); //更新功能
        }
    }

    @Override
    public void updateWatchSent(WatchFaceBean watchFace) {
        Toast.makeText(this, watchFace.getName() + getString(R.string.toast_has_sent), Toast
                .LENGTH_LONG).show();
        finish();
    }

    @Override
    public void showThrowable(Throwable e) {
        if (e.getMessage().equals(Const.EXCEPTION_CHECK_CONNECT)) {
            Toast.makeText(this, getString(R.string.toast_query_connect_watch), Toast
                    .LENGTH_LONG).show();
        } else if (e.getMessage().equals(Const.EXCEPTION_CHECK_PHONESYNC)) {
            Toast.makeText(this, getString(R.string.toast_query_open_phone_sync), Toast
                    .LENGTH_LONG).show();
        }
        finish();
    }


    class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
        private WatchFaceBean watchFaceBean = null;

        public boolean isSetAdapter() {
            return watchFaceBean != null;
        }

        public RecycleViewAdapter(WatchFaceBean watchFaceBean) {
            this.watchFaceBean = watchFaceBean;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = getLayoutInflater().inflate(R.layout.recycler_item_detail, null);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.textView.setText(data[i]);
        }

        @Override
        public int getItemCount() {
            return data.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView textView;
            ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.tv_watchname);
                imageView = (ImageView) itemView.findViewById(R.id.watch_preview);
            }

        }
    }
}
