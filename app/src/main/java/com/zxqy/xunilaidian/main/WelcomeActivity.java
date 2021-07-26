package com.zxqy.xunilaidian.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.litesuits.orm.LiteOrm;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.zxqy.xunilaidian.Application;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.base.BaseWelcomeActivity;
import com.zxqy.xunilaidian.utils.Constants;
import com.zxqy.xunilaidian.utils.HttpUrl;
import com.zxqy.xunilaidian.utils.PermissionUtils;
import com.zxqy.xunilaidian.utils.SharedPreferencesUtil;
import com.zxqy.xunilaidian.utils.SpDefine;
import com.zxqy.xunilaidian.utils.SpUtils;
import com.zxqy.xunilaidian.utils.ToastUtils;
import com.zxqy.xunilaidian.view.AgreementDialog;

import org.litepal.LitePal;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * 启动页
 */
public class WelcomeActivity extends BaseWelcomeActivity {

    @BindView(R.id.iv_bg)
    ImageView iv_bg;
    @BindView(R.id.frl_root)
    FrameLayout frl_root;
    private Handler mHander = new Handler();

    private AgreementDialog mDialog1;//首次弹窗
    private AgreementDialog mDialog2;//第二次弹窗
    private boolean isFirstRegister;
    public static IWXAPI mWXApi;

    public static final String GIF_PATH_KEY = "gif_Path_key";
    public static String IMAGE_PATH_KEY = "image_path_key";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();//设置无状态栏
        isFirstRegister = SpUtils.getInstance().getBoolean(Constants.FIRST_REGISTER, true);
        boolean isConfirmAgreement = SpUtils.getInstance().getBoolean(Constants.IS_CONFIRM_USER_AGREEMENT, false);//是否同意用户协议

        if (!isConfirmAgreement) {
            // 还未同意过协议
            showUserDialog1();
        }else {
            getAppConfigInfo();
        }
    }

    private void initThirdData() {
        Application.liteOrm = LiteOrm.newSingleInstance(getApplicationContext(), ".db");
        //初始化极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        LitePal.initialize(this);
        //sdk
//        GeetolSDK.init(this, "http://shashidi.weiapp8.cn/app/");

        //禁止小米弹窗
//        disableAPIDialog();
        SharedPreferencesUtil.init(Application.getInstance());
        //判断是否同意了服务协议
  /*      if (SpUtils.getInstance().getBoolean(AppUtils.IS_CONFIRM_USER_AGREEMENT)) {
            //友盟统计
//            UMConfigure.preInit(this, AppUtils.UMENG_KEY, "geetol");
            // 选用AUTO页面采集模式
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        }*/

//        LitePal.initialize(this);//数据库初始化

        registerToWX();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
        ToastUtils.init(Application.getInstance());
        SpUtils.getInstance().init(Application.getInstance());
//        CPResourceUtils.init(mContext);
        //配置通用接口请求地址
        SpUtils.getInstance().putString(Constants.COMMON_URL, HttpUrl.COMMON_URL_1);
        Application.setGifPath(SharedPreferencesUtil.getString(GIF_PATH_KEY, null));
        Application.setImageDir(SharedPreferencesUtil.getString(IMAGE_PATH_KEY, null));
        UMConfigure.preInit(this, Constants.YM_APP_KEY, "Marcy");//
        //初始化组件化基础库, 所有友盟业务SDK都必须调用此初始化接口。
        //建议在宿主App的Application.onCreate函数中调用基础组件库初始化函数。
        UMConfigure.init(this, Constants.YM_APP_KEY, "Marcy", UMConfigure.DEVICE_TYPE_PHONE, "");
        // 友盟  选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }

    /**
     * 微信注册
     */
    private void registerToWX() {
        mWXApi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false);
        mWXApi.registerApp(Constants.WX_APP_ID);
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
        goMain(1500L);
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
            startActivity(new Intent(this, MainActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, time);
    }

    /**
     * 用户协议和隐私政策
     */
    private void showUserDialog1() {
        if (mDialog1 == null) {
            mDialog1 = new AgreementDialog(this, new AgreementDialog.onDialogClickListener() {
                @Override
                public void onConfirm() {
                    String config = SpUtils.getInstance().getString(SpDefine.APP_CONFIG_INFO, "");
                    initThirdData();
                    getAppConfigInfo();

                    // 进同意协议获取权限的逻辑
//                    initData();
                }

                @Override
                public void onCancel() {
                    // 进第二次弹窗
                    showUserDialog2();
                }
            });
        }
        if (!mDialog1.isShowing() && !isFinishing()) {
            mDialog1.show();
        }
    }

    private void showUserDialog2() {
        if (mDialog2 == null) {
            mDialog2 = new AgreementDialog(this, new AgreementDialog.onDialogClickListener() {
                @Override
                public void onConfirm() {
                    // 弹第一次弹窗
                    showUserDialog1();
                }

                @Override
                public void onCancel() {
                    // 关闭应用
                    finish();
                }
            });
        }
        if (!mDialog2.isShowing() && !isFinishing()) {
            mDialog2.show();
            String hint = "十分抱歉，若您不同意本应用的《隐私政策》和《用户协议》,我们将无法为您提供服务。";
            mDialog2.setButtonText("关闭应用", "查看协议", false, hint);
        }
    }

    @Override
    protected void onDestroy() {
        if (mHander != null) {
            mHander.removeMessages(0);
        }

        if (mDialog1 != null) {
            if (!isFinishing() && mDialog1.isShowing()) {
                mDialog1.dismiss();
            }
            mDialog1 = null;
        }
        if (mDialog2 != null) {
            if (!isFinishing() && mDialog2.isShowing()) {
                mDialog2.dismiss();
            }
            mDialog2 = null;
        }
        super.onDestroy();
    }

    /**
     * 检测权限是否已获取
     */
    private boolean isGetPermissions() {
        String[] p = Build.VERSION.SDK_INT <= 28 ? getPermissions28() : getPermissions();
        for (String str : p) {
            if (!PermissionUtils.checkPermission(this, str)) {
                return false;
            }
        }
        return true;
    }

}
