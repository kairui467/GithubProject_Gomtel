package com.igeak.customwatchface.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.igeak.android.common.api.GeakApiClient;
import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.MyApplication;
import com.igeak.customwatchface.R;
import com.igeak.customwatchface.model.WatchFace;
import com.igeak.customwatchface.model.WatchFacesModel;
import com.igeak.customwatchface.presenter.IWatchFacesContract;
import com.igeak.customwatchface.presenter.WatchFaceListPresent;
import com.igeak.customwatchface.view.activity.FaceDetailActivity;
import com.igeak.customwatchface.view.activity.FaceEditActivity;
import com.igeak.customwatchface.view.activity.MenuActivity;
import com.igeak.customwatchface.view.view.ItemDecorationAlbumColumns;
import com.igeak.customwatchface.view.view.ShareDialog;
import com.igeak.customwatchface.view.view.watchfaceview.WatchPreviewView;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuqiang on 16-5-11.
 */
public class CustomFaceFragment extends Fragment implements IWatchFacesContract.IWatchFacesView {

    private static final int SPAN_COUNT = 2;
    RecyclerView mRecyclerView = null;
    RecycleViewAdapter mRecycleViewAdapter = null;
    WatchFaceListPresent present = null;
    private WatchFacesModel.FacePath facePath = WatchFacesModel.FacePath.FACE_CUSTOM;

    private int currentIndex = 0;
    private List<WatchFaceBean> beanList;


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
                SPAN_COUNT));
        mRecycleViewAdapter = new RecycleViewAdapter();
        present = new WatchFaceListPresent(this, getActivity().getApplicationContext());
        present.getWatchFaceBeanList(facePath);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        present.getWatchFaceBeanList(facePath);
    }

    @Override
    public void updateWatchFaceBeanList(List<WatchFaceBean> watchFaceBeanList) {
        beanList = watchFaceBeanList;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REQUEST_MENU) {

            switch (resultCode) {
                case Const.RESULT_CODE_EDIT:
                    Intent intent = new Intent(getContext(), FaceEditActivity.class);
                    intent.putExtra(Const.INTENT_EXTRA_KEY_WATCHFACE, beanList.get(currentIndex));
                    intent.putExtra(Const.INTENT_EXTRA_KEY_ISCUSTOM, facePath.equals
                            (WatchFacesModel.FacePath.FACE_CUSTOM));
                    startActivity(intent);
                    break;
                case Const.RESULT_CODE_RELEASE:
                    send2Watch(beanList.get(currentIndex));
                    break;
                case Const.RESULT_CODE_RENAME:
                    renameOperation();
                    break;
                case Const.RESULT_CODE_SHARE:
                    ShareDialog shareDialog = new ShareDialog(getActivity(), null);
                    shareDialog.showShareDialog(null);

                    break;
                case Const.RESULT_CODE_DELETE:
                    deleteOperation();
                    break;
                default:
                    break;
            }
        } else if (requestCode == Const.REQUEST_EDIT) {
            present.getWatchFaceBeanList(facePath); //从edit界面返回需要重新刷新下界面
        }

    }

    private void deleteOperation() {
        present.deleteWatchFace(beanList, currentIndex);
    }

    private void renameOperation() {
        final EditText et = new EditText(getContext());
        String name = beanList.get(currentIndex).getName();
        et.setText(name);
        et.setSelection(name.length());
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.dialog_rename))
                .setView(et)
                .setPositiveButton(getString(R.string.dialog_yes), new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = et.getText().toString();
                        present.changeName(name, beanList, currentIndex);
                    }
                })
                .setNegativeButton(getString(R.string.dialog_cancel), null)
                .show();
    }


    class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

        private List<WatchFaceBean> watchFaceBeanList = null;

        public boolean isSetAdapter() {
            return watchFaceBeanList != null;
        }

        public void setWatchList(List<WatchFaceBean> watchFaceBeanList) {
            this.watchFaceBeanList = watchFaceBeanList;
        }

        public RecycleViewAdapter() {
            setHasStableIds(true);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = getActivity().getLayoutInflater().inflate(R.layout
                    .recycler_item_face_custom, null);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            WatchFaceBean faceBean = watchFaceBeanList.get(i);
            viewHolder.textView.setText(faceBean.getName());
            present.loadWatchImg(viewHolder.imageView, faceBean, facePath);
            viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color
                    .item_background));
        }


        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public int getItemCount() {
            return watchFaceBeanList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView textView;
            WatchPreviewView imageView;
            ImageButton imb;
            ImageButton mReleaseIb;
            View itemView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.tv_watchname);
                imageView = (WatchPreviewView) itemView.findViewById(R.id.watch_preview);
                imb = (ImageButton) itemView.findViewById(R.id.ib_optionmenu);
                mReleaseIb = (ImageButton) itemView.findViewById(R.id.ib_release_red);
                imb.setOnClickListener(this);
                imageView.setOnClickListener(this);
                mReleaseIb.setOnClickListener(this);
                this.itemView = itemView;
            }


            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.watch_preview) {

                    Intent intent = new Intent(getContext(), FaceDetailActivity.class);
                    intent.putExtra(Const.INTENT_EXTRA_KEY_WATCHFACE, watchFaceBeanList.get((int)
                            getItemId()));
                    intent.putExtra(Const.INTENT_EXTRA_KEY_ISCUSTOM, facePath.equals
                            (WatchFacesModel.FacePath.FACE_CUSTOM));
                    startActivity(intent);
                } else if (v.getId() == R.id.ib_optionmenu) {
                    Intent intent = new Intent(getContext(), MenuActivity.class);
                    startActivityForResult(intent, Const.REQUEST_MENU);
                    currentIndex = (int) getItemId();
                    itemView.setBackgroundColor(getResources().getColor(R.color
                            .item_background_select));
                } else if (v.getId() == R.id.ib_release_red) {
                    send2Watch(watchFaceBeanList.get((int) getItemId()));
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
