package com.igeak.customwatchface;

import android.content.Context;

import java.io.IOException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by xuqiang on 16-5-19.
 */
public class WatchFacePresent {
    private IWatchFaceDetailView mWatchFaceView;
    private WatchFaceModel mWatchFaceModel;
    private Context context;


    public WatchFacePresent(Context context, IWatchFaceDetailView watchFaceView) {
        this.mWatchFaceView = watchFaceView;
        mWatchFaceModel = new WatchFaceModel(context);
        this.context = context;
    }

    public void loadWatchimg() {
        mWatchFaceModel.loadWatchimg()
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
                        mWatchFaceView.updateWatchFaceView(watchFace);
                    }

                });
    }

    public void handleMessage(byte[] bytes) throws IOException {

        FileUtil.unZipFile(FileUtil.getFileFromBytes(bytes));
        loadWatchimg();
    }

    public void initwatch() throws Exception {
        if (!FileOperation.getCustomWatchfacesFolder().exists()) {
            FileOperation.assert2Folder(context);
        }
    }


    public interface IWatchFaceDetailView {
        void updateWatchFaceView(WatchFace watchFace);

    }

}
