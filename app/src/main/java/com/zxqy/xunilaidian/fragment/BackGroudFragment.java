package com.zxqy.xunilaidian.fragment;

import android.os.Bundle;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.zxqy.xunilaidian.Constants;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.base.BaseFragment;
import com.zxqy.xunilaidian.bean.EventData;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class BackGroudFragment extends BaseFragment {

    @BindView(R.id.stroke_color_picker)
    ColorPicker strokeColorPicker;
    @BindView(R.id.sv_bar)
    SVBar svBar;
    @BindView(R.id.opacity_bar)
    OpacityBar opacityBar;
    int alpha = 255;
    public static BackGroudFragment newInstance() {
        Bundle args = new Bundle();
        BackGroudFragment fragment = new BackGroudFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_bgcolor;
    }

    @Override
    public void initUI() {
        EventData data = new EventData();
        data.setType(Constants.BG_COLOR);
        opacityBar.setOpacity(255);
        opacityBar.setOnOpacityChangedListener(new OpacityBar.OnOpacityChangedListener() {
            @Override
            public void onOpacityChanged(int opacity) {
                alpha = opacity;
                //透明度
                data.setAlpha(opacity);
                EventBus.getDefault().post(data);
            }
        });
        strokeColorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                data.setAlpha(alpha);
                data.setColor(color);
                EventBus.getDefault().post(data);
            }
        });
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
