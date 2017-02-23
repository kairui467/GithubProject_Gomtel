package com.igeak.customwatchface.model;

import android.content.Context;

import com.igeak.android.common.api.GeakApiClient;
import com.igeak.customwatchface.Bean.WatchFaceBean;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by xuqiang on 16-5-19.
 */
public class WatchFacesModel {

    public enum FacePath {
        FACE_INNER,
        FACE_CUSTOM
    }

    private Context context = null;

    public WatchFacesModel(Context context) {
        this.context = context;
    }

    public Observable<List<WatchFaceBean>> getWatchFaceBeanList(final FacePath facePath) {
        return Observable.create(new Observable.OnSubscribe<List<WatchFaceBean>>() {
            @Override
            public void call(Subscriber<? super List<WatchFaceBean>> subscriber) {

                try {
                    List<WatchFaceBean> watchFaces;
                    if (facePath.equals(FacePath.FACE_CUSTOM)) {
                        watchFaces =  FileOperation.getWatchFaceBeanList();
                    } else {
                        watchFaces =  AssetsOperation.getWatchFaceBeanList(context);
                    }

                    if (watchFaces == null) {
                        subscriber.onError(new Exception("User = null"));
                    } else {
                        subscriber.onNext(watchFaces);
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public Observable<WatchFace> loadWatchImg(final WatchFaceBean watchFaceBean,
                                              final WatchFacesModel.FacePath facePath) {
        return Observable.create(new Observable.OnSubscribe<WatchFace>() {
            @Override
            public void call(Subscriber<? super WatchFace> subscriber) {

                WatchFace watchFace = FaceOperation.bean2Face(watchFaceBean,facePath,context);
                if (watchFace == null) {
                    subscriber.onError(new Exception("User = null"));
                } else {
                    subscriber.onNext(watchFace);
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable changeName(final String name, final List<WatchFaceBean> watchbeanlist,
                                 final int position) {
        return Observable.create(new Observable.OnSubscribe<List<WatchFaceBean>>() {
            @Override
            public void call(Subscriber<? super List<WatchFaceBean>> subscriber) {
                try {
                    WatchFaceBean bean = watchbeanlist.get(position);
                    FileOperation.changeWatchName(bean, name);

                } catch (Exception e) {
                    subscriber.onError(new Exception(e));
                }
                subscriber.onNext(watchbeanlist);
                subscriber.onCompleted();

            }
        });

    }

    public Observable deleteWatchFace(final List<WatchFaceBean> watchbeanlist,
                                      final int position) {
        return Observable.create(new Observable.OnSubscribe<List<WatchFaceBean>>() {
            @Override
            public void call(Subscriber<? super List<WatchFaceBean>> subscriber) {
                try {
                    WatchFaceBean currentbean = watchbeanlist.get(position);
                    FileOperation.deleteFolder(currentbean.getName());
                    watchbeanlist.remove(position);
                } catch (Exception e) {
                    subscriber.onError(new Exception(e));
                }
                subscriber.onNext(watchbeanlist);
                subscriber.onCompleted();
            }
        });

    }

    public Observable<WatchFaceBean> zipFileAndRelease(final GeakApiClient googleApiClient,
                                                       final WatchFaceBean watchbeanface,
                                                       final WatchFacesModel.FacePath
                                                                   facePath) {
        return FaceOperation.zipAndRelease(googleApiClient, watchbeanface, facePath,
                context);

    }

}
