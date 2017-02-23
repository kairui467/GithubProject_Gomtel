package com.igeak.customwatchface.presenter;

import android.content.Context;

import com.igeak.android.common.api.GeakApiClient;
import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.model.WatchFace;
import com.igeak.customwatchface.model.WatchFacesModel;
import com.igeak.customwatchface.view.view.watchfaceview.WatchPreviewView;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuqiang on 16-5-19.
 */
public class WatchFaceListPresent implements IWatchFacesContract.IWatchFacesPresent{
    private IWatchFacesContract.IWatchFacesView mWatchFaceView;
    private WatchFacesModel mWatchFacesModel;
    private Context context;


    public WatchFaceListPresent(IWatchFacesContract.IWatchFacesView watchFaceView, Context context) {
        this.mWatchFaceView = watchFaceView;
        mWatchFacesModel = new WatchFacesModel(context);
        this.context = context;

    }


    @Override
    public void getWatchFaceBeanList(WatchFacesModel.FacePath facePath) {
        mWatchFacesModel.getWatchFaceBeanList(facePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<WatchFaceBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<WatchFaceBean> watchFaceBeanList) {
                        mWatchFaceView.updateWatchFaceBeanList(watchFaceBeanList);
                    }
                });
    }




    @Override
    public void loadWatchImg(final WatchPreviewView imageView, final WatchFaceBean watchFaceBean,
                             final WatchFacesModel.FacePath facePath) {
        mWatchFacesModel.loadWatchImg(watchFaceBean, facePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WatchFace>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WatchFace watchFace) {
                        mWatchFaceView.updateWatchFace(imageView, watchFace);
                    }

                });
    }

    @Override
    public void changeName(String name,List<WatchFaceBean> beanList,int position) {
        mWatchFacesModel.changeName(name,beanList,position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<WatchFaceBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<WatchFaceBean> watchFaceBeanList) {
                        mWatchFaceView.updateWatchFaceBeanList(watchFaceBeanList);
                    }
                });
    }

    @Override
    public void deleteWatchFace(List<WatchFaceBean> beanList, int position) {
        mWatchFacesModel.deleteWatchFace(beanList,position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<WatchFaceBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<WatchFaceBean> watchFaceBeanList) {
                        mWatchFaceView.updateWatchFaceBeanList(watchFaceBeanList);
                    }
                });
    }


    @Override
    public void zipFileAndRelease(GeakApiClient googleApiClient, final WatchFaceBean watchface,
                                  final WatchFacesModel.FacePath facePath) {
        mWatchFacesModel.zipFileAndRelease(googleApiClient, watchface, facePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WatchFaceBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mWatchFaceView.showThrowable(e);
                    }

                    @Override
                    public void onNext(WatchFaceBean watchFaceBean) {
                        mWatchFaceView.updateWatchSent(watchFaceBean);
                    }
                });
    }


}
