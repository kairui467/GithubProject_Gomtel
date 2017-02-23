package com.igeak.customwatchface.model;

import android.content.Context;

import com.igeak.android.common.api.GeakApiClient;
import com.igeak.android.wearable.Node;
import com.igeak.android.wearable.NodeApi;
import com.igeak.android.wearable.Wearable;
import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.Const;

import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by xuqiang on 16-7-22.
 */
public class FaceOperation {
    public static WatchFace bean2Face(final WatchFaceBean watchFaceBean, final
    WatchFacesModel.FacePath facePath, Context context) {

        WatchFace watchFace = new WatchFace();
        watchFace.setName(watchFaceBean.getName());
        watchFace.setAmPm(watchFaceBean.isAmPm());
        watchFace.setHaveTemperature(watchFaceBean.isHaveTemperature());
        watchFace.setShowDate(watchFaceBean.isShowDate());
        watchFace.setShowWeek(watchFaceBean.isShowWeek());
        try {
            if (facePath.equals(WatchFacesModel.FacePath.FACE_CUSTOM)) {
                watchFace.setBackground(FileOperation.getWatchFaceElementStream(
                        watchFaceBean.getName(),
                        watchFaceBean.getBackground()));
                watchFace.setDialScale(FileOperation.getWatchFaceElementStream(
                        watchFaceBean.getName(),
                        watchFaceBean.getDialScale()));
                watchFace.setHour(FileOperation.getWatchFaceElementStream(
                        watchFaceBean.getName(),
                        watchFaceBean.getHour()));
                watchFace.setMinute(FileOperation.getWatchFaceElementStream(
                        watchFaceBean.getName(),
                        watchFaceBean.getMinute()));
                watchFace.setSecond(FileOperation.getWatchFaceElementStream(
                        watchFaceBean.getName(),
                        watchFaceBean.getSecond()));
            } else {
                watchFace.setBackground(AssetsOperation.getWatchFaceElementStream(context,
                        watchFaceBean.getName(),
                        watchFaceBean.getBackground()));
                watchFace.setDialScale(AssetsOperation.getWatchFaceElementStream(context,
                        watchFaceBean.getName(),
                        watchFaceBean.getDialScale()));
                watchFace.setHour(AssetsOperation.getWatchFaceElementStream(context,
                        watchFaceBean.getName(),
                        watchFaceBean.getHour()));
                watchFace.setMinute(AssetsOperation.getWatchFaceElementStream(context,
                        watchFaceBean.getName(),
                        watchFaceBean.getMinute()));
                watchFace.setSecond(AssetsOperation.getWatchFaceElementStream(context,
                        watchFaceBean.getName(),
                        watchFaceBean.getSecond()));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return watchFace;
    }

    public static Observable<WatchFaceBean> zipAndRelease(final GeakApiClient googleApiClient,
                                                          final WatchFaceBean watchbeanface,
                                                          final WatchFacesModel.FacePath facePath,
                                                          final Context context) {
        return Observable.create(new Observable.OnSubscribe<WatchFaceBean>() {
            @Override
            public void call(Subscriber<? super WatchFaceBean> subscriber) {
                try {

                    /**
                     * 1.打包文件，得到byte[]
                     */
                    byte[] bytes;
                    if (facePath.equals(WatchFacesModel.FacePath.FACE_CUSTOM)) {
                        bytes = FileOperation.zipFolder(watchbeanface.getName());
                    } else {
                        bytes = AssetsOperation.zipFolder(context, watchbeanface.getName());
                    }


                    /**
                     * 2.通过通道，将数据发送出去
                     */
                    GeakApiClient mGoogleApiClient = googleApiClient;


                    if (mGoogleApiClient.isConnected()) {
                        NodeApi.GetConnectedNodesResult result = Wearable.NodeApi.getConnectedNodes
                                (mGoogleApiClient).await();
                        List<Node> nodes = result.getNodes();

                        if (nodes.isEmpty()) {
                            subscriber.onError(new Exception(Const.EXCEPTION_CHECK_CONNECT));
                            return;
                        }
                        Iterator it = nodes.iterator();
                        while (it.hasNext()) {
                            Node node = (Node) it.next();
                            Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(),
                                    Const.MESSAGE_DATA_PATH,
                                    bytes);
                        }
                        if (watchbeanface == null) {
                            subscriber.onError(new Exception("User = null"));
                        }
                        subscriber.onNext(watchbeanface);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new Exception(Const.EXCEPTION_CHECK_PHONESYNC));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }


}
