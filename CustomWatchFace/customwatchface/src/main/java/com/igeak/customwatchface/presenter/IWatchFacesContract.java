package com.igeak.customwatchface.presenter;

import com.igeak.android.common.api.GeakApiClient;
import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.model.WatchFace;
import com.igeak.customwatchface.model.WatchFacesModel;
import com.igeak.customwatchface.view.IView;
import com.igeak.customwatchface.view.view.watchfaceview.WatchPreviewView;

import java.util.List;

/**
 * Created by xuqiang on 16-6-6.
 */
public interface IWatchFacesContract {

    /**
     * View开放给present的接口，present回调view时使用
     */
    interface IWatchFacesView  extends IView {
        void updateWatchFaceBeanList(List<WatchFaceBean> watchFaceBeanList);

        void updateWatchFace(WatchPreviewView imageview, WatchFace watchFace);

        void updateWatchSent(WatchFaceBean watchFace);

        void showThrowable(Throwable e);
    }


    /**
     * Present开放给view的接口，
     */
    interface IWatchFacesPresent extends IPresenter<IWatchFacesView> {
        void getWatchFaceBeanList(WatchFacesModel.FacePath facePath);

        void loadWatchImg(final WatchPreviewView imageView, final WatchFaceBean watchFaceBean,
                          final WatchFacesModel.FacePath facePath);
        void changeName(String name,List<WatchFaceBean> watchbeanlist,int position);

        void deleteWatchFace(List<WatchFaceBean> watchfaceList, int itemId);

        void zipFileAndRelease(GeakApiClient googleApiClient, final WatchFaceBean watchface,
                               final WatchFacesModel.FacePath facePath);
    }

}
