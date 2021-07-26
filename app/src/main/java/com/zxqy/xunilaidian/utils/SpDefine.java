package com.zxqy.xunilaidian.utils;

import android.text.TextUtils;

import com.zxqy.xunilaidian.bean.UserInfoBean;
import com.zxqy.xunilaidian.entity.AppConfigInfoEntity;


/**
 * 文件名：SpDefine
 * 创建者： lyq
 * 创建日期： 2020/11/4 14:53
 * 描述： 数据存储
 */

public class SpDefine {

    // 发送短信模板  设置的数据
    public static final String MSG_MODE_ENTITY = "msg_mode_entity";

    // 用户信息
    public static final String USER_INFO = "user_info";
    // 用户VIP信息
    public static final String VIP_INFO = "vip_info";
    //app配置信息
    public static final String APP_CONFIG_INFO = "app_config_info";
    //app vip配置信息
    public static final String APP_CONFIG_VIP_INFO = "app_config_vip_info";



    /**
     * 保存用户信息
     */
    public static void setUserInfo(UserInfoBean.UserInfo bean) {
        if (bean != null) {
            SpUtils.getInstance().putString(USER_INFO, JsonBinder.toJson(bean));
        } else {
            SpUtils.getInstance().remove(USER_INFO);
        }
    }

    /**
     * 保存用户VIP信息
     */
    public static void setVipInfo(UserInfoBean.VipInfo vip) {
        if (vip != null) {
            SpUtils.getInstance().putString(VIP_INFO, JsonBinder.toJson(vip));
        } else {
            SpUtils.getInstance().remove(VIP_INFO);
        }
    }

    /**
     * 保存App配置信息
     */
    public static void setAppConfigInfo(AppConfigInfoEntity.ConfigBean info) {
        if (info != null) {
            SpUtils.getInstance().putString(APP_CONFIG_INFO, JsonBinder.toJson(info));
        } else {
            SpUtils.getInstance().remove(APP_CONFIG_INFO);
        }
    }


    /**
     * 获取App配置信息
     */
    public static AppConfigInfoEntity.ConfigBean getAppConfigInfo() {
        if (!TextUtils.isEmpty(SpUtils.getInstance().getString(APP_CONFIG_INFO))) {
            return JsonBinder.paseJsonToObj(SpUtils.getInstance().getString(APP_CONFIG_INFO), AppConfigInfoEntity.ConfigBean.class);
        }
        return null;
    }

    /**
     * 保存App vip配置信息
     */
    public static void setAppConVipfigInfo(AppConfigInfoEntity.AppConfigInfo info) {
        if (info != null) {
            SpUtils.getInstance().putString(APP_CONFIG_VIP_INFO, JsonBinder.toJson(info));
        } else {
            SpUtils.getInstance().remove(APP_CONFIG_VIP_INFO);
        }
    }
    /**
     * 获取App  vip配置信息
     */
    public static AppConfigInfoEntity.AppConfigInfo getAppVipConfigInfo() {
        if (!TextUtils.isEmpty(SpUtils.getInstance().getString(APP_CONFIG_VIP_INFO))) {
            return JsonBinder.paseJsonToObj(SpUtils.getInstance().getString(APP_CONFIG_VIP_INFO), AppConfigInfoEntity.AppConfigInfo.class);
        }
        return null;
    }

    /**
     * 获取VIP信息
     */
    public static UserInfoBean.VipInfo getVipInfo() {
        if (!TextUtils.isEmpty(SpUtils.getInstance().getString(VIP_INFO))) {
            return JsonBinder.paseJsonToObj(SpUtils.getInstance().getString(VIP_INFO), UserInfoBean.VipInfo.class);
        }
        return null;
    }

    /**
     * 保存用户信息
     */
    public static UserInfoBean.UserInfo getUserInfo() {
        if (!TextUtils.isEmpty(SpUtils.getInstance().getString(USER_INFO))) {
            return JsonBinder.paseJsonToObj(SpUtils.getInstance().getString(USER_INFO), UserInfoBean.UserInfo.class);
        }
        return null;
    }

}
