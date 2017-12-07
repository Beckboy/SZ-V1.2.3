package com.junhsue.ksee.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment 基类
 * Created by longer on 17/3/16 in Junhsue.
 *
 */

public abstract class BaseFragment extends Fragment {


    private static String TAG = "BaseFragment";

    //记录页面是否已经显示过

    private boolean isVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(setLayoutId(), container, false);
        if (null != view) {
            onInitilizeView(view);
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            loadData();
            isVisible = true;
        }
    }


    protected void loadData() {
    }


    /**
     * 初始化布局
     */
    protected abstract int setLayoutId();


    /**
     * 初始化组件
     */
    protected abstract void onInitilizeView(View view);


    @Override
    public void onDestroy() {
        super.onDestroy();
        isVisible = false;
    }
}

