package com.zxqy.xunilaidian.entity;


import android.content.Context;
import android.content.Intent;

import com.zxqy.xunilaidian.activity.LoginActivity;
import com.zxqy.xunilaidian.utils.Constants;
import com.zxqy.xunilaidian.utils.ShareUtils;
import com.zxqy.xunilaidian.utils.SpDefine;
import com.zxqy.xunilaidian.utils.SpUtils;
import com.zxqy.xunilaidian.utils.ToastUtils;

/**
 * 首页
 * Created by huanghaibin on 2017/12/4.
 */
public class HomeItemEntity {

    public String name;
    public int icn;
    public Class aClass;
    public Context context;
    public boolean hasLogin;//是否需要登录才能进入

    public HomeItemEntity() {
    }

    public HomeItemEntity(Context context, String name, int icn, Class aClass, boolean hasLogin) {
        this.context = context;
        this.name = name;
        this.icn = icn;
        this.aClass = aClass;
        this.hasLogin = hasLogin;
    }
    public HomeItemEntity(Context context, String name, int icn) {
        this.context = context;
        this.name = name;
        this.icn = icn;
        this.aClass = aClass;
        this.hasLogin = hasLogin;
    }

    public void startActivity() {
        if (aClass != null) {
            if (hasLogin && !SpUtils.getInstance().getBoolean(Constants.IS_LOGIN)) {//是否登录
                ToastUtils.showLongToast("请先登录~");
                context.startActivity(new Intent(context, LoginActivity.class));
                return;
            }
            context.startActivity(new Intent(context, aClass));
        } else if (name.contains("分享")) {
            ShareUtils.shareText(context, "",  SpDefine.getAppConfigInfo().share);
        } else {
            ToastUtils.showLongToast("此功能正在开发中，敬请期待~");
        }
    }
}
