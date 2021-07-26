package com.zxqy.xunilaidian.utils;

/**
 * Created by Administrator on 2017/10/11.
 */

public class HttpUrl {
    /**
     * 通用接口
     */
//    public static String COMMON_URL = "http://app.wm002.cn/app/";//正式接口


//    public static String COMMON_URL_1 = "http://192.168.0.105:8081/app/v1/";//李娇航本地
//    public static String COMMON_URL_1 = "http://192.168.0.108:8001/app/v1";//李娇航本地
//    public static String COMMON_URL_1 = "http://hk.04v.cn:88/app/v1";//正式接口
    public static String COMMON_URL_1 = "https:/app.04v.cn/app/v1";//正式接口
    public static String COMMON_URL_2 = "http://hk.7vs.net:80/app/v1";//正式接口----备用

    public static String FILE_BASE_URI = "http://up.04v.cn/";//文件下载  七牛域名
    public static String FILE_BASE_UPDATE = "http://";//文件下载  七牛域名


    /**
     * 接口参数  标志  1表示安卓
     */
    public static String PLATFORM = "1";

    /**
     * 微信登录
     */
    public static String USER_WECHAT_LOGIN = "wxlogin";

    /**
     * 检查更新
     */
    public static String GETNEW = "version/checkupdate";

    /**
     * 获取验证码
     */
    public static String GET_VARCODE = "getvarcode";

    /**
     * 设备注册
     */
    public static String REGIST_DEVICE = "reg";

    /**
     * 更新数据
     */
    public static String UPDATE = "update";

    /**
     * 账号密码登录
     */
    public static String USER_LOGIN_PHONE = "login";
    /**
     * QQ登录
     */
    public static String USER_LOGIN_QQ = "qqlogin";
    /**
     * 获取文件上传的token
     */
    public static String GET_FILE_TOKEN = "file/token";
    /**
     * 提交反馈
     */
    public static String SUBMIT_FEEDBACK = "feedback/submit";
    /**
     * 反馈回复
     */
    public static String SUBMIT_FEEDBACK_REPLY = "feedback/reply";
    /**
     * 获取vip套餐信息
     */
    public static String VIP_LIST = "vip/list";
    /**
     * 反馈列表数据
     */
    public static String FEEDBACK_LIST = "feedback/query";
    /**
     * 反馈详情数据
     */
    public static String FEEDBACK_DETAIL = "feedback/details";
    /**
     * 结束反馈
     */
    public static String FEEDBACK_MARK = "feedback/mark";
    /**
     * 获取订单信息
     */
    public static String GET_ORDERINFO = "pay/pay";
    /**
     * 注册
     */
    public static String TO_REGISTER = "signup";
    /**
     * 根据用户id获取用户信息
     */
    public static String USER_INFO = "user/info";
    /**
     * 获取app配置信息
     */
    public static String APP_CONFIG_INFO = "version/config";

}
