package com.zxqy.xunilaidian.fragment;

import android.os.Bundle;

import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.base.BaseFragment;

public class WallpaperFragment extends BaseFragment {

    public static WallpaperFragment newInstance() {
        Bundle args = new Bundle();
        WallpaperFragment fragment = new WallpaperFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getRootViewId() {
        return R.layout.fragment_wall;
    }

    @Override
    public void initUI() {

    }

    private void initWallBgAdapter() {
    }

    private void initWallAdapter() {
    }

    @Override
    public void initData() {

    }

    @Override
    protected boolean isLoadData() {
        return false;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void lazyLoad() {

    }

}
