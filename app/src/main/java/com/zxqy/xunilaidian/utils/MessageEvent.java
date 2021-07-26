package com.zxqy.xunilaidian.utils;

import android.content.pm.ResolveInfo;

/**
 * EventBus事件传递
 */
public class MessageEvent {
    public String data,phone,pwd;
    //选择应用
    public static final String SELECT_APP = "select_app";
    public static final String UPDATE_FEEDBACK_LIST = "update_feedback_list";

    //微信登录成功
    public static final String LOGIN_WECHAT = "login_wechat";

    //刷新设置页面短信模板
    public static final String UPDATE_SET_MESSAGE_MODE = "update_set_message_mode";
    //刷新页面登录信息
    public static final String UPDATE_LOG_INFO = "update_log_info";
    //微信登录成功
    public static final String WECHAT_LOGIN_OK = "wechat_login_ok";
    //支付成功
    public static final String PAY_OK = "pay_ok";


    //注册成功
    public static final String REGIST_OK = "regist_ok";
    //刷新会员信息
    public static final String UPDATE_MEMBER_INFO = "update_member_info";
    //刷新反馈详情
    public static final String UPDATE_FEEDBACK_DETAIL = "update_feedback_detail";
    public ResolveInfo info;
    public int index ;

    public MessageEvent(String data) {
        this.data = data;
    }
    public MessageEvent(String data, int index) {
        this.data = data;
        this.index = index;
    }
    public MessageEvent(String data,String phone,String pwd) {
        this.data = data;
        this.phone = phone;
        this.pwd = pwd;
    }
    public MessageEvent(String data, ResolveInfo info, int index) {
        this.info = info;
        this.data = data;
        this.index = index;
    }
}
