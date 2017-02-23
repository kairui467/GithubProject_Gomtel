package com.igeak.customwatchface.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igeak.customwatchface.presenter.IPresenter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by xuqiang on 16-6-6.
 */
public abstract class BaseFragment extends Fragment {

    private Set<IPresenter> mAllPresenters = new HashSet<IPresenter>();

    /**
     *需要子类来实现，获取子类的IPresenter，一个activity有可能有多个IPresenter
     */
    protected abstract IPresenter[] getPresenters();

    //初始化presenters，
    protected abstract void onInitPresenters();

    private void addPresenters(){

        IPresenter[] presenters = getPresenters();
        if(presenters != null){
            for(int i = 0; i < presenters.length; i++){
                mAllPresenters.add(presenters[i]);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addPresenters();
        onInitPresenters();
        return super.onCreateView(inflater, container, savedInstanceState);

    }
}
