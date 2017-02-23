package com.igeak.customwatchface.presenter;

import com.igeak.customwatchface.view.IView;

/**
 * Created by xuqiang on 16-6-6.
 */
public interface IPresenter<V  extends IView>{
    /***
     * 个人建议，UI初始化和UI生命周期由android原生处理，不要再递交给present
     * @param view
     */
    //void init(V view);
}