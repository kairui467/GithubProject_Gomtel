package com.igeak.customwatchface.model;

import android.content.Context;

import com.igeak.android.common.api.GeakApiClient;
import com.igeak.customwatchface.Bean.WatchFaceBean;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by xuqiang on 16-5-19.
 */
public class WatchFaceDetailModel {
    private Context context = null;

    public WatchFaceDetailModel(Context context) {
        this.context = context;
    }


    public Observable<WatchFace> loadWatchImg(final WatchFaceBean watchFaceBean,
                                              final WatchFacesModel.FacePath facePath) {
        return Observable.create(new Observable.OnSubscribe<WatchFace>() {
            @Override
            public void call(Subscriber<? super WatchFace> subscriber) {

                WatchFace watchFace = FaceOperation.bean2Face(watchFaceBean, facePath, context);
                if (watchFace == null) {
                    subscriber.onError(new Exception("User = null"));
                } else {
                    subscriber.onNext(watchFace);
                    subscriber.onCompleted();
                }
            }
        });
    }


    public Observable<WatchFaceBean> zipFileAndSentToWatch(final GeakApiClient googleApiClient,
                                                           final WatchFaceBean watchbeanface,
                                                           final WatchFacesModel.FacePath
                                                                   facePath) {
        return FaceOperation.zipAndRelease(googleApiClient, watchbeanface, facePath,
                context);

    }
}
