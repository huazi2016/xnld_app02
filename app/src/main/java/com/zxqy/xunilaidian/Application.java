package com.zxqy.xunilaidian;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import androidx.multidex.MultiDexApplication;

import com.litesuits.orm.LiteOrm;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.zxqy.xunilaidian.utils.Constants;
import com.zxqy.xunilaidian.utils.HttpUrl;
import com.zxqy.xunilaidian.utils.SharedPreferencesUtil;
import com.zxqy.xunilaidian.utils.SpUtils;
import com.zxqy.xunilaidian.utils.ToastUtils;

import org.litepal.LitePal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.jpush.android.api.JPushInterface;

public class Application extends MultiDexApplication {

    public static Context mContext;
    private static Application appInstance;
    //    public static IWXAPI mWXApi;
    private static String gifPath = null;
    public static LiteOrm liteOrm;
    public static int identity = -1;//微信登录  1 登录 2 绑定
    public static final String GIF_PATH_KEY = "gif_Path_key";
    private static String imageDir = null;
    public static String IMAGE_PATH_KEY="image_path_key";
    public static IWXAPI mWXApi;
    public static Application getInstance() {
        if (null == appInstance) {
            appInstance = new Application();
        }
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        liteOrm = LiteOrm.newSingleInstance(getApplicationContext(), ".db");
        ToastUtils.init(appInstance);
        SpUtils.getInstance().init(appInstance);
        boolean isConfirmAgreement = SpUtils.getInstance().getBoolean(Constants.IS_CONFIRM_USER_AGREEMENT, false);//是否同意用户协议
        if (isConfirmAgreement) {
            //初始化极光推送
            JPushInterface.setDebugMode(true);
            JPushInterface.init(this);

            LitePal.initialize(this);
            //sdk
            SharedPreferencesUtil.init(this);

            registerToWX();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                builder.detectFileUriExposure();
            }
            initSmartRefreshLayout();

//        CPResourceUtils.init(mContext);
            //配置通用接口请求地址
            SpUtils.getInstance().putString(Constants.COMMON_URL, HttpUrl.COMMON_URL_1);
            gifPath = SharedPreferencesUtil.getString(GIF_PATH_KEY, null);
            imageDir=SharedPreferencesUtil.getString(IMAGE_PATH_KEY,null);
            UMConfigure.preInit(this, Constants.YM_APP_KEY, "Marcy");//
            //初始化组件化基础库, 所有友盟业务SDK都必须调用此初始化接口。
            //建议在宿主App的Application.onCreate函数中调用基础组件库初始化函数。
            UMConfigure.init(this, Constants.YM_APP_KEY, "Marcy", UMConfigure.DEVICE_TYPE_PHONE, "");
            // 友盟  选用AUTO页面采集模式
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        }

    }

    /**
     * 微信注册
     */
    private void registerToWX() {
        mWXApi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false);
        mWXApi.registerApp(Constants.WX_APP_ID);
    }


    public static LiteOrm getLiteOrm() {
        return liteOrm;
    }

    /**
     * 下拉刷新加载更多初始化
     */
    private void initSmartRefreshLayout() {
//        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate));
        //设置全局的Footer构建器
//        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate));
    }
    /**
     * 微信注册
     */
  /*  private void registerToWX() {
        mWXApi = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID, false);
        mWXApi.registerApp(AppConfig.WX_APP_ID);
    }*/

    /**
     * application里面 调用  反射 禁止弹窗 解决 detected problems with api  9.0手机（小米）
     */
    private void disableAPIDialog() {
        try {
            Class clazz = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = clazz.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object activityThread = currentActivityThread.invoke(null);
            Field mHiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int width, height;

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        Application.width = width;
    }

    public static int getVideoWidth() {
        return width * 3 / 5 + 5;
    }

    public static int getVideoHeight() {
        return height * 3 / 6;
    }

    public static String getGifPath() {
        return gifPath;
    }

    public static void setGifPath(String gifPath) {
        Application.gifPath = gifPath;
    }

    public static String getImageDir() {
        return imageDir;
    }

    public static void setImageDir(String imageDir) {
        Application.imageDir = imageDir;
    }
}
