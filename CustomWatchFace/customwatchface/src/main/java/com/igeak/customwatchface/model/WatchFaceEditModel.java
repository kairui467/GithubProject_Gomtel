package com.igeak.customwatchface.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.igeak.android.common.api.GeakApiClient;
import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.util.PicUtil;
import com.igeak.customwatchface.view.view.watchfaceview.PointView;
import com.igeak.customwatchface.view.view.watchfaceview.WatchPreviewView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by xuqiang on 16-5-27.
 */
public class WatchFaceEditModel {
    private Context context;
    private Map<WatchPreviewView.Type, Bitmap> modifyMaps;
    private WatchFaceBean watchfacebean;

    public WatchFaceEditModel(Context context) {
        this.context = context;
        modifyMaps = new HashMap<>();
    }

    public Observable<WatchFace> loadWatchImg(final WatchFaceBean watchFaceBean, final
    WatchFacesModel.FacePath facePath) {
        this.watchfacebean = watchFaceBean;
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

    /**
     * 加载背景的数据
     *
     * @return
     * @throws Exception
     */
    public List<InputStream> loadBackImg() throws Exception {
        List<InputStream> backImgs = new ArrayList<InputStream>();
        String[] backgrounds;
        backgrounds = context.getAssets().list(Const.BACK_FOLDER_NAME);
        for (String str : backgrounds) {
            InputStream is = context.getAssets().open(Const.BACK_FOLDER_NAME + "/" + str);
            backImgs.add(is);
        }
        return backImgs;
    }

    public List<InputStream> loadScaleImg() throws Exception {
        List<InputStream> backImgs = new ArrayList<>();
        String[] backgrounds;
        backgrounds = context.getAssets().list(Const.SCALE_FOLDER_NAME);

        for (String str : backgrounds) {
            InputStream is = context.getAssets().open(Const.SCALE_FOLDER_NAME + "/" + str);
            backImgs.add(is);
        }

        return backImgs;
    }

    public List<Map<PointView.Type, InputStream>> loadPointImg() throws Exception {
        List<Map<PointView.Type, InputStream>> pointImgs = new ArrayList<>();
        String[] backgrounds;
        backgrounds = context.getAssets().list(Const.POINT_FOLDER_NAME);

        for (String str : backgrounds) {
            Map<PointView.Type, InputStream> map = new HashMap<>();

            map.put(PointView.Type.HOUR, context.getAssets().open
                    (Const.POINT_FOLDER_NAME + "/" + str +
                            "/" + Const.HOUR_NAME + Const.PNG_EXNAME));
            map.put(PointView.Type.MINUTE, context.getAssets().open
                    (Const.POINT_FOLDER_NAME + "/" + str +
                            "/" + Const.MINUTE_NAME + Const.PNG_EXNAME));
            map.put(PointView.Type.SECOND, context.getAssets().open
                    (Const.POINT_FOLDER_NAME + "/" + str +
                            "/" + Const.SECOND_NAME + Const.PNG_EXNAME));
            pointImgs.add(map);
        }

        return pointImgs;
    }

    public void saveBackImg(Bitmap bitmap) {
        modifyMaps.put(WatchPreviewView.Type.BACKGROUND, bitmap);
    }

    public void saveScaleImg(Bitmap bitmap) {
        modifyMaps.put(WatchPreviewView.Type.DIALSCALE, bitmap);
    }

    public void savePointImg(Map<PointView.Type, Bitmap> point) {
        modifyMaps.put(WatchPreviewView.Type.HOUR, point.get(PointView.Type.HOUR));
        modifyMaps.put(WatchPreviewView.Type.MINUTE, point.get(PointView.Type.MINUTE));
        modifyMaps.put(WatchPreviewView.Type.SECOND, point.get(PointView.Type.SECOND));
    }

    public Observable<WatchFaceBean> savewatch(final String name) {
        return Observable.create(new Observable.OnSubscribe<WatchFaceBean>() {
            @Override
            public void call(Subscriber<? super WatchFaceBean> subscriber) {
                try {

                    for (WatchPreviewView.Type type : modifyMaps.keySet()) {
                        String faceElement;
                        switch (type) {
                            case BACKGROUND:
                                faceElement = watchfacebean.getBackground();
                                break;
                            case DIALSCALE:
                                faceElement = watchfacebean.getDialScale();
                                break;
                            case HOUR:
                                faceElement = watchfacebean.getHour();
                                break;
                            case MINUTE:
                                faceElement = watchfacebean.getMinute();
                                break;
                            case SECOND:
                                faceElement = watchfacebean.getSecond();
                                break;
                            default:
                                faceElement = watchfacebean.getBackground();
                        }

                        PicUtil.saveBitmapToFile(
                                modifyMaps.get(type),
                                FileOperation.getWatchFaceElementFile(
                                        watchfacebean.getName()
                                        , faceElement
                                )
                        );
                    }
                    FileOperation.changeWatchName(watchfacebean, name);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                subscriber.onNext(watchfacebean);
                subscriber.onCompleted();

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

    public Observable<WatchFaceBean> saveAndSend(final String name, final GeakApiClient
            googleApiClient,
                                                 final WatchFaceBean watchbeanface,
                                                 final WatchFacesModel.FacePath
                                                         facePath) {
        WatchFacesModel.FacePath myfacePath = facePath;
        try {
            if (facePath.equals(WatchFacesModel.FacePath.FACE_INNER)) {
                AssetsOperation.assert2Folder(context, watchfacebean.getName());
                myfacePath = WatchFacesModel.FacePath.FACE_CUSTOM; //一旦编辑保存，就已经成为CUSTOM模式
            }
            for (WatchPreviewView.Type type : modifyMaps.keySet()) {
                String faceElement;
                switch (type) {
                    case BACKGROUND:
                        faceElement = watchfacebean.getBackground();
                        break;
                    case DIALSCALE:
                        faceElement = watchfacebean.getDialScale();
                        break;
                    case HOUR:
                        faceElement = watchfacebean.getHour();
                        break;
                    case MINUTE:
                        faceElement = watchfacebean.getMinute();
                        break;
                    case SECOND:
                        faceElement = watchfacebean.getSecond();
                        break;
                    default:
                        faceElement = watchfacebean.getBackground();
                }

                PicUtil.saveBitmapToFile(
                        modifyMaps.get(type),
                        FileOperation.getWatchFaceElementFile(
                                watchfacebean.getName()
                                , faceElement
                        )
                );
            }
            FileOperation.changeWatchName(watchfacebean, name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return FaceOperation.zipAndRelease(googleApiClient, watchbeanface, myfacePath,
                context);

    }


    public Observable<WatchFaceBean> createNewFace(final String name) {
        return Observable.create(new Observable.OnSubscribe<WatchFaceBean>() {
            @Override
            public void call(Subscriber<? super WatchFaceBean> subscriber) {
                try {
                    AssetsOperation.assert2Folder(context, watchfacebean.getName());
                    for (WatchPreviewView.Type type : modifyMaps.keySet()) {
                        String faceElement;
                        switch (type) {
                            case BACKGROUND:
                                faceElement = watchfacebean.getBackground();
                                break;
                            case DIALSCALE:
                                faceElement = watchfacebean.getDialScale();
                                break;
                            case HOUR:
                                faceElement = watchfacebean.getHour();
                                break;
                            case MINUTE:
                                faceElement = watchfacebean.getMinute();
                                break;
                            case SECOND:
                                faceElement = watchfacebean.getSecond();
                                break;
                            default:
                                faceElement = watchfacebean.getBackground();
                        }

                        PicUtil.saveBitmapToFile(
                                modifyMaps.get(type),
                                FileOperation.getWatchFaceElementFile(
                                        watchfacebean.getName()
                                        , faceElement
                                )
                        );

                    }
                    FileOperation.changeWatchName(watchfacebean, name);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                subscriber.onNext(watchfacebean);
                subscriber.onCompleted();

            }
        });
    }


}
