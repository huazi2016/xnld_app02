package com.zxqy.xunilaidian.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zxqy.xunilaidian.utils.httputil.JsonCallback;
import com.zxqy.xunilaidian.utils.httputil.callback.HttpCallback;

import okhttp3.Call;

/**
 * Created by Lance on 2017/9/21.
 */

public class Utils {

    //登录
    public final static String USER_NAME="user_name";
    //手机号
    public final static String PHONE_NUMBER="phone_number";

    public static boolean isDebug=true;
    public static void getShareInfo(@NonNull final HttpCallback<Share> callback) {
        OkHttpUtils.get().url(HttpUtil.HttpShare)
                .build()
                .execute(new JsonCallback<Share>(Share.class) {
                    @Override
                    public void onResponse(Share response, int id) {
                        callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onAfter(int id) {
                        callback.onFinish();
                    }
                });
    }
    public static long stringTolong(String duration){
        String time[];
        time=duration.split(":");
        int min= Integer.parseInt(time[0]);
        int sec= Integer.parseInt(time[1]);
        long du=min*60+sec;
        return du;
    }
    /**
     * 获取当前APP版本号
     * @param context
     * @return 版本号
     * @throws Exception
     */
    public static String getVersionName(Context context) throws Exception{
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }


    /**
     * 开关是否打开
     *
     * @return
     */
    public static boolean isOpenBySwt(String code) {
        if (TextUtils.isEmpty(code)){
            return false;
        }
        try {
          /*  UpdateBean data = DataSaveUtils.getInstance().getUpdate();
            if (data != null && data.getSwt() != null) {
                for (Swt swt : data.getSwt()) {
                    if (swt.getCode().equals(code)) {
                        if (swt.getVal1() == 1)
                            return true;
                        else
                            return false;
                    }
                }
            }*/
        }catch (Exception e){
        }
        return false;
    }


    /**
     * 手机号中间变星号
     * @param text
     * @return
     */
    public static String phoneNumber(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        try {
            return text.substring(0, 3) + "****" + text.substring(7, 11);
        } catch (Exception e) {
        }
        return "";
    }




    /**
     * 数组转字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }

    /**
     * 获取用户id
     */
    public static String getUserId() {
        String userId = SpUtils.getInstance().getString(Constants.USER_ID);
        if (TextUtils.isEmpty(userId)) {
            userId = "";
        }
        return userId;
    }

    /**
     * 获取用户key
     * @return
     */
    public static String getUserKey() {
        String uKey = SpUtils.getInstance().getString(Constants.USER_KEY);
        if (TextUtils.isEmpty(uKey)) {
            uKey = "";
        }
        return uKey;
    }
}
