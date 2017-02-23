package com.igeak.customwatchface;

import android.content.Context;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by xuqiang on 16-5-19.
 */
public class WatchFaceModel {
    private Context context = null;

    public WatchFaceModel(Context context) {
        this.context = context;
    }

    public Observable<WatchFace> loadWatchimg() {
        return Observable.create(new Observable.OnSubscribe<WatchFace>() {
            @Override
            public void call(Subscriber<? super WatchFace> subscriber) {
                try {
                    WatchFaceBean watchFaceBean = FileOperation.getWatchFaceBean();
                    WatchFace watchFace = new WatchFace();
                    watchFace.setName(watchFaceBean.getName());
                    watchFace.setAmPm(watchFaceBean.isAmPm());
                    watchFace.setHaveTemperature(watchFaceBean.isHaveTemperature());
                    watchFace.setShowDate(watchFaceBean.isShowDate());
                    watchFace.setShowWeek(watchFaceBean.isShowWeek());
                    watchFace.setBackground(FileOperation.getWatchfacesElementImg(
                            watchFaceBean.getBackground()));
                    watchFace.setDialScale(FileOperation.getWatchfacesElementImg(
                            watchFaceBean.getDialScale()));
                    watchFace.setHour(FileOperation.getWatchfacesElementImg(
                            watchFaceBean.getHour()));
                    watchFace.setMinute(FileOperation.getWatchfacesElementImg(
                            watchFaceBean.getMinute()));
                    watchFace.setSecond(FileOperation.getWatchfacesElementImg(
                            watchFaceBean.getSecond()));
                    if (watchFace == null) {
                        subscriber.onError(new Exception("User = null"));
                    } else {
                        subscriber.onNext(watchFace);
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
