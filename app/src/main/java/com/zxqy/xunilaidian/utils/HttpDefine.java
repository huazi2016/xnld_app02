package com.zxqy.xunilaidian.utils;


import com.zxqy.xunilaidian.bean.WxAccessTokenBean;
import com.zxqy.xunilaidian.bean.WxUserInfoBean;
import com.zxqy.xunilaidian.utils.httputil.callback.BaseCallback;

import java.util.HashMap;
import java.util.Map;

public class HttpDefine {


    /**
     * 获取当前APP所有定义的任务列表
     */
    public static final String API_GET_ALL_GIFT = "integral.task.gets";

    /**
     * 获取公共资源
     */
    public static final String API_GROUP = "pub.resource.getlist";

    /**
     * 获取自己的积分变动记录
     */
    public static final String API_GET_LOGS = "integral.data.get_logs";

    /**
     * 获取公共资源分组
     */
    public static final String API_ALL_GROUP = "pub.resource.get_groups";
    /**
     * 增加下载次数
     */
    public static final String API_ADD_DOWN = "pub.resource.adddown";
    /**
     * 获取早起打卡的项目
     */
    public static final String API_MORNING_CLOCK = "integral.morningclock.gets";
    /**
     * 取早起打卡的项目-我参与过的
     */
    public static final String API_MORNING_MYGETS = "integral.morningclock.mygets";
    /**
     * 获取早起打卡-我的数据
     */
    public static final String API_MORNING_MYDATA = "integral.morningclock.mydata";

    /**
     * 早起打卡报名
     */
    public static final String API_MORNING_ENROLL = "integral.morningclock.enroll";

    /**
     * 早起打卡打卡
     */
    public static final String API_MORNING_CLOCK_CLOCK = "integral.morningclock.clock";

    /**
     * 获取定时抽奖的项目
     */
    public static final String API_LUCKDRAW_GET = "integral.luckdraw.gets";
    /**
     * 获取定时抽奖的项目 自己参与的
     */
    public static final String API_LUCKDRAW_MYGETS = "integral.luckdraw.mygets";
    /**
     * 获取用户最近7天的签到记录
     */
    public static final String API_DATA_GET_REPORTS = "integral.data.get_reports";

    /**
     * 每日签到
     */
    public static final String API_DATA_REPORTS = "integral.daily.report";

    /**
     * 获取自己的信息
     */
    public static final String API_DATA_GET_INFO = "integral.data.get_info";

    /**
     * 获取定时抽奖的项目详情
     */
    public static final String API_LUCKDRAW_DETAIL = "integral.luckdraw.get";

    /**
     * 定时抽奖报名
     */
    public static final String API_LUCKDRAW_ENROLL = "integral.luckdraw.enroll";

    /**
     * 定时抽奖项目获取报名名单
     */
    public static final String API_LUCKDRAW_JOIN = "integral.luckdraw.logs";

    /**
     * 邀请码绑定接口
     */
    public static final String API_BIND_CODE = "integral.task.bind_code";

    /**
     * 获取吃饭打卡的项目
     */
    public static final String API_EAT_FOOD = "integral.timing.gets";

    /**
     * 任务完成后提交接口
     */
    public static final String API_ACCOMPLISHED = "integral.task.accomplished";

    /**
     * 吃饭打卡项目打卡
     */
    public static final String API_EAT_FOOD_CLOCK = "integral.timing.clock";
    /**
     * 设置自己的信息
     */
    public static final String API_SET_INFO = "integral.data.set_info";

    /**
     * 获取微信token
     */
    public static final String WX_GET_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    /**
     * 获取微信用户信息
     */
    public static final String WX_GET_USERINFO = "https://api.weixin.qq.com/sns/userinfo";

    /**
     * 获取未来五日空气质量
     */
    public static final String API_QUALITY = "http://106.37.208.228:8082/CityForecast";

    /**
     * 获取可提现项目的列表
     */
    public static final String API_EXCHANGGE_ITEMS = "integral.exchangge_items";

    /**
     * 用户提现
     */
    public static final String API_EXCHANGE = "integral.exchange";

    /**
     * 提现记录
     */
    public static final String API_EXCHANGE_LOG = "integral.exchange_log";

    /**
     * 获取空气质量监测站点信息接口
     */
    public static final String API_GET_AIR_SITES = "weather.get_air_sites";

    /**
     * 获取空气质量数据接口
     */
    public static final String API_GET_AIR_AQI = "weather.get_air_aqi";

    /**
     * 上传空气质量数据
     */
    public static final String API_UP_AIR_AQI = "weather.up_air_aqi";

    /**
     * 获取空气质量排行榜数据接口
     */
    public static final String API_RK_AQI = "weather.rk_aqi";

    /**
     * 获取笑话数据
     */
    public static final String API_FUNNY_JOKE = "funny.joke.get_items";

    /**
     * 绑定微信号
     */
    public static final String API_BIND_WECHAT = "integral.data.bind_wechat";


    /**
     * 绑定手机号码
     */
    public static final String API_BIND_PHONE = "integral.data.bind_tel";

    /**
     * 获取幸运球列表
     */
    public static final String API_GET_LUCK_INTEGRAL = "integral.data.get_luck_integral";

    /**
     * 激活幸运球
     */
    public static final String API_SET_LUCK_INTEGRAL = "integral.data.set_luck_integral";

    /**
     * 微信通过code获取access_token
     */
    public static void getWxToken(String code, BaseCallback<WxAccessTokenBean> callback) {
        Map<String, String> params = new HashMap();
        params.put("appid", Constants.WX_APP_ID); //应用唯一标识，在微信开放平台提交应用审核通过后获得
        params.put("secret", Constants.WX_APP_SECRET); //应用密钥AppSecret，在微信开放平台提交应用审核通过后获得
        params.put("code", code); //填写第一步获取的code参数
        params.put("grant_type", "authorization_code"); //填authorization_code
        HttpUtils.getInstance().post(WX_GET_TOKEN, params, callback);
    }

    /**
     * 微信获取用户信息
     */
    public static void getWxUserInfo(String access_token, String openid, BaseCallback<WxUserInfoBean> callback) {
        Map<String, String> params = new HashMap();
        params.put("access_token", access_token);
        params.put("openid", openid);
        HttpUtils.getInstance().post(WX_GET_USERINFO, params, callback);
    }

}
