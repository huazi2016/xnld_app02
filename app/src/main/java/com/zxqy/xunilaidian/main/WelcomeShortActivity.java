package com.zxqy.xunilaidian.main;

import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.base.BaseWelcomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 快捷方式启动页
 */
public class WelcomeShortActivity extends BaseWelcomeActivity {

    @BindView(R.id.iv_bg)
    ImageView iv_bg;
    @BindView(R.id.frl_root)
    FrameLayout frl_root;
    private Handler mHander = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_short_launcher;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //设置淡入淡出
        overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();//设置无状态栏

            jumpToNext();//跳转到首页
            showSplash();

    }

    /**
     * 展示网络图片
     */
    private void showSplash() {
    /*    List<Ads> ads = DataSaveUtils.getInstance().getAllAds();
        if (ads != null && ads.size() > 0) {
            for (Ads ad : ads) {
                if (ad.getPos().equals("8P01")) {
//                    Glide.with(this).load(ad.getImg()).into(splashImg);
//                    GlideLoadUtils.getInstance().glideLoad(SplashActivity.this, ad.getImg(), iv_bg, R.mipmap.ic_launcher);
                }
            }
        }*/
    }


    @Override
    protected void jumpToNext() {
        showSplash();
        goMain(3000L);
        new Thread(() -> getAliOss()).start();
    }

    private void getAliOss() {
  /*      HttpUtils.getInstance().getAliOss(new BaseCallback<ResultBean>() {
            @Override
            public void onRequestBefore() {
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }

            @Override
            public void onSuccess(Response response, ResultBean o) {
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
            }
        });*/
    }

    /**
     * 跳转到主页
     */
    private void goMain(long time) {
        mHander.postDelayed(() -> {
//            startActivity(new Intent(this, MainActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, time);
    }

    @Override
    protected void onDestroy() {
        if (mHander != null) {
            mHander.removeMessages(0);
        }
        super.onDestroy();
    }
}
