package com.igeak.customwatchface.presenter;

import android.app.Activity;

import com.igeak.android.common.api.GeakApiClient;
import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.model.WatchFace;
import com.igeak.customwatchface.model.WatchFacesModel;
import com.igeak.customwatchface.view.IView;

/**
 * Created by xuqiang on 16-6-6.
 */
public interface IWatchFaceDetailContract {
    interface IWatchFaceDetailView extends IView {
        void updateWatchFaceDetailView(WatchFace watchFace);

        void updateWatchSent(WatchFaceBean watchFace);

        void showThrowable(Throwable e);
    }

    interface IWatchFaceDetailPresent extends IPresenter<IWatchFaceDetailView> {
        void loadWatchImg(final WatchFaceBean watchFaceBean,
                          final WatchFacesModel.FacePath facePath);

        void zipFileAndSentToWatch(GeakApiClient googleApiClient, final WatchFaceBean watchFaceBean,
                                   final WatchFacesModel.FacePath facePath);
    }
}
