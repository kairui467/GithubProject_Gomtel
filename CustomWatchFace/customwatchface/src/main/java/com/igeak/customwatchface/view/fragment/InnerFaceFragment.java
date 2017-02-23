package com.igeak.customwatchface.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.igeak.android.common.api.GeakApiClient;
import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.MyApplication;
import com.igeak.customwatchface.model.WatchFace;
import com.igeak.customwatchface.model.WatchFacesModel;
import com.igeak.customwatchface.presenter.IWatchFacesContract;
import com.igeak.customwatchface.presenter.WatchFaceListPresent;
import com.igeak.customwatchface.R;
import com.igeak.customwatchface.view.activity.FaceEditActivity;
import com.igeak.customwatchface.view.view.ItemDecorationAlbumColumns;
import com.igeak.customwatchface.view.view.watchfaceview.WatchPreviewView;
import com.igeak.customwatchface.view.activity.FaceDetailActivity;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuqiang on 16-5-11.
 */
public class InnerFaceFragment extends Fragment implements IWatchFacesContract.IWatchFacesView {

    private static final int SPAN_COUNT = 2;
    RecyclerView mRecyclerView = null;
    RecycleViewAdapter mRecycleViewAdapter = null;

    WatchFaceListPresent present = null;
    WatchFacesModel.FacePath facePath = WatchFacesModel.FacePath.FACE_INNER;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_facelist, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_facelist);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new ItemDecorationAlbumColumns(
                getResources().getDimensionPixelSize(R.dimen.divider_spacing_horizon),
                getResources().getDimensionPixelSize(R.dimen.divider_spacing_vertical),
                SPAN_COUNT)); //设置分隔线
        mRecycleViewAdapter = new RecycleViewAdapter();

        present = new WatchFaceListPresent(this, getActivity().getApplicationContext());
        present.getWatchFaceBeanList(facePath);
        return rootView;
    }


    @Override
    public void updateWatchFaceBeanList(List<WatchFaceBean> watchFaceBeanList) {

        if (!mRecycleViewAdapter.isSetAdapter()) {
            mRecycleViewAdapter.setWatchList(watchFaceBeanList);
            mRecyclerView.setAdapter(mRecycleViewAdapter);
        } else {
            mRecycleViewAdapter.setWatchList(watchFaceBeanList);
            mRecycleViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateWatchFace(final WatchPreviewView imageView, final WatchFace watchFace) {


        Observable.create(new Observable.OnSubscribe<WatchPreviewView>() {
            @Override
            public void call(Subscriber<? super WatchPreviewView> subscriber) {
                imageView.setElements(watchFace);
                subscriber.onNext(imageView);
                subscriber.onCompleted();

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WatchPreviewView>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WatchPreviewView imageView) {
                        imageView.invalidate();
                    }
                });


    }

    class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

        private List<WatchFaceBean> beanList = null;

        public boolean isSetAdapter() {
            return beanList != null;
        }

        public void setWatchList(List<WatchFaceBean> beanList) {
            this.beanList = beanList;
        }

        public RecycleViewAdapter() {
            setHasStableIds(true);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.recycler_item_face_inner,
                    null);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            WatchFaceBean faceBean = beanList.get(i);
            viewHolder.mWatchNameTv.setText(faceBean.getName());
            present.loadWatchImg(viewHolder.mWatchPreView, faceBean, facePath);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return beanList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            WatchPreviewView mWatchPreView;
            TextView mWatchNameTv;
            ImageButton mCreateFaceIb;
            ImageButton mReleaseIb;

            public ViewHolder(View itemView) {
                super(itemView);
                mWatchNameTv = (TextView) itemView.findViewById(R.id.tv_watchname);
                mWatchPreView = (WatchPreviewView) itemView.findViewById(R.id.watch_preview);
                mCreateFaceIb = (ImageButton) itemView.findViewById(R.id.ib_createface);
                mReleaseIb = (ImageButton) itemView.findViewById(R.id.ib_release_red);
                mCreateFaceIb.setOnClickListener(this);
                mWatchPreView.setOnClickListener(this);
                mReleaseIb.setOnClickListener(this);
            }


            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.watch_preview) {

                    Intent intent = new Intent(getContext(), FaceDetailActivity.class);
                    intent.putExtra(Const.INTENT_EXTRA_KEY_WATCHFACE, beanList.get((int)
                            getItemId()));
                    intent.putExtra(Const.INTENT_EXTRA_KEY_ISCUSTOM, facePath.equals
                            (WatchFacesModel.FacePath.FACE_CUSTOM));
                    startActivity(intent);
                } else if (v.getId() == R.id.ib_createface) {
                    Intent intent = new Intent(getContext(), FaceEditActivity.class);
                    intent.putExtra(Const.INTENT_EXTRA_KEY_WATCHFACE, beanList.get((int)
                            getItemId()));
                    intent.putExtra(Const.INTENT_EXTRA_KEY_ISCUSTOM, facePath.equals
                            (WatchFacesModel.FacePath.FACE_CUSTOM));
                    startActivity(intent);
                } else if (v.getId() == R.id.ib_release_red) {
                    send2Watch(beanList.get((int) getItemId()));
                }
            }

        }
    }

    public void send2Watch(WatchFaceBean watchfacebean) {
        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        GeakApiClient googleApiClient = myApplication.mGoogleApiclent;
        present.zipFileAndRelease(googleApiClient, watchfacebean, facePath);
    }


    @Override
    public void updateWatchSent(WatchFaceBean watchFace) {
        Toast.makeText(getActivity().getApplicationContext()
                , watchFace.getName() + getString(R.string.toast_has_sent)
                , Toast.LENGTH_LONG).show();

    }

    @Override
    public void showThrowable(Throwable e) {
        if (e.getMessage().equals(Const.EXCEPTION_CHECK_CONNECT)) {
            Toast.makeText(getContext().getApplicationContext(), getString(R.string
                    .toast_query_connect_watch), Toast
                    .LENGTH_LONG).show();
        } else if (e.getMessage().equals(Const.EXCEPTION_CHECK_PHONESYNC)) {
            Toast.makeText(getContext().getApplicationContext(), getString(R.string
                    .toast_query_open_phone_sync), Toast
                    .LENGTH_LONG).show();
        }

    }

}
