package com.igeak.customwatchface.view.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igeak.customwatchface.R;
import com.igeak.customwatchface.model.PicOperation;
import com.igeak.customwatchface.presenter.IWatchFaceEditContract;
import com.igeak.customwatchface.presenter.WatchFaceEditPresent;
import com.igeak.customwatchface.util.MyUtils;
import com.igeak.customwatchface.view.activity.FaceEditActivity;
import com.igeak.customwatchface.view.view.DividerItemDecoration;
import com.igeak.customwatchface.view.view.watchfaceview.PointView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by xuqiang on 16-5-11.
 */
public class PointEditFragment extends Fragment implements IWatchFaceEditContract.IPointView {

    private static final int SPAN_COUNT = 3;
    RecyclerView mRecyclerView = null;
    RecycleViewAdapter adapter;

    WatchFaceEditPresent present;
    List<Map<PointView.Type, InputStream>> pointMaps = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_facelist);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.GRID, null, null)); //分隔线
        present = ((FaceEditActivity) getActivity()).present;
        adapter = new RecycleViewAdapter(pointMaps);
        pointMaps = present.loadPointImg();
        adapter.setBitmap(pointMaps);
        mRecyclerView.setAdapter(adapter);
        return rootView;
    }


    class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

        private List<Map<PointView.Type, Bitmap>> pointbitMaps = new ArrayList<>();
        private List<Map<PointView.Type, InputStream>> pointMaps;

        public void setBitmap(List<Map<PointView.Type, InputStream>> pointMaps) {
            this.pointMaps = pointMaps;
        }

        public RecycleViewAdapter(List<Map<PointView.Type, InputStream>> pointMaps) {
            this.pointMaps = pointMaps;
            setHasStableIds(true); //必须要加的代码，默认为false，当true，getItemId才有效
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.recycler_item_point,
                    null);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            final int index = i;

            final Map<PointView.Type, InputStream> pointMap = pointMaps.get(i);
            final PointView pointView = viewHolder.pointView;
            final int height = pointView.getHeight();
            final int width = pointView.getWidth();

            Observable.create(new Observable.OnSubscribe<Map<PointView.Type, Bitmap>>() {
                @Override
                public void call(Subscriber<? super Map<PointView.Type, Bitmap>> subscriber) {

                    try {
                        Map<PointView.Type, Bitmap> pointbitMap = new HashMap<>();
                        if ((pointbitMaps.size() <= index) || (pointbitMaps.get(index) == null)) {


                            Bitmap bitmaphour = PicOperation.InputStream2Bitmap(pointMap.get
                                    (PointView.Type
                                            .HOUR), width, height);
                            Bitmap bitmapminute = PicOperation.InputStream2Bitmap(pointMap.get
                                    (PointView.Type
                                            .MINUTE), width, height);
                            Bitmap bitmapsecond = PicOperation.InputStream2Bitmap(pointMap.get
                                    (PointView.Type
                                            .SECOND), width, height);

                            pointbitMap.put(PointView.Type.HOUR, bitmaphour);
                            pointbitMap.put(PointView.Type.MINUTE, bitmapminute);
                            pointbitMap.put(PointView.Type.SECOND, bitmapsecond);

                            MyUtils.addAtPos(pointbitMaps, index, pointbitMap);

                        }else {
                            pointbitMap = pointbitMaps.get(index);
                        }
                        pointView.setPointElementBitmap(pointbitMap);
                        subscriber.onNext(pointbitMap);
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }



                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Map<PointView.Type, Bitmap>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Map<PointView.Type, Bitmap> pointbitMap) {
                            pointView.invalidate();

                        }
                    });


        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //RecyclerView显示数据条数
        @Override
        public int getItemCount() {
            return pointMaps.size();
        }

        //自定义的ViewHolder,减少findViewById调用次数
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            PointView pointView;

            //在布局中找到所含有的UI组件
            public ViewHolder(View itemView) {
                super(itemView);
                pointView = (PointView) itemView.findViewById(R.id.point);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int index = (int) getItemId();
                present.changePointImg(pointbitMaps.get(index));
            }
        }
    }


}
