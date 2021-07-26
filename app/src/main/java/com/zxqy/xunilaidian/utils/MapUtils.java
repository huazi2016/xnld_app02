package com.zxqy.xunilaidian.utils;


import com.zxqy.xunilaidian.Application;
import com.zxqy.xunilaidian.utils.httputil.callback.CPResourceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/17.
 */

public class MapUtils {
//    private static Context mContext;

    /**
     * 获取反馈详情（结束反馈）
     */
    public static Map<String, String> getFeedbackDetail(int service_id) {
        Map<String, String> map = new HashMap<>(getCurrencyMap());
        map.put("id", String.valueOf(service_id));
        return map;
    }


    /**
     * 在Application里面初始化，就能全局调用
     *
     * @param context
     */
  /*  public static void init(Context context) {
        if (mContext == null) {
            mContext = context;
        } else {
            return;
        }
    }
*/

    /**
     * 通用Map
     * (无参的方法通用调取)
     *
     * @return
     */
    public static Map<String, String> getCurrencyMap() {
        Map<String, String> map = new HashMap<>();
//        map.put("appid", CPResourceUtils.getString("appid"));
        map.put("platform", HttpUrl.PLATFORM);
        map.put("device", DeviceUtils.
                getSpDeviceId());
        map.put("packageName", DeviceUtils.getPackageName(Application.getInstance()));
        map.put("id", SpUtils.getInstance().getString(Constants.USER_ID));
        map.put("idApp", SpUtils.getInstance().getString(Constants.ID_APP));
        map.put("versionNum", DeviceUtils.getVersionCode(Application.getInstance()));
        // 2019.11.11新增(可为空)
//        map.put("user_id", Utils.getUserId());
//        map.put("user_key", Utils.getUserKey());
        return map;
    }

    /**
     * 获取device
     * (无参的方法通用调取)
     *
     * @return
     */
    public static Map<String, String> getDeviceMap() {
        Map<String, String> map = new HashMap<>();
        map.put("appid", CPResourceUtils.getString("appid"));
        map.put("sign", null);
        map.put("device", DeviceUtils.getSpDeviceId());
        // 2019.11.11新增(可为空)
        map.put("user_id", Utils.getUserId());
        map.put("user_key", Utils.getUserKey());
        return map;
    }

    /**
     * 通用用户Map
     * (需要登陆用户信息时调取)
     *
     * @return
     */
    public static Map<String, String> getCommonUserMap() {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("user_id", Utils.getUserId());
        map.put("user_key", Utils.getUserKey());
        return map;
    }

    /**
     * 注册
     *
     * @return
     */
    public static Map<String, String> getRegistMap() {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("mac", "");
//        map.put("brand", SystemUtils.getDeviceBrand());
//        map.put("model", SystemUtils.getSystemModel());
//        map.put("widthpix", SystemUtils.getWith(mContext) + "");
//        map.put("heightpix", SystemUtils.getHeight(mContext) + "");
//        map.put("vercode", SystemUtils.getSystemVersion());
//        map.put("agent", SystemUtils.getChannelInfo(mContext) + "");
        return map;
    }

    /**
     * 版本更新
     *
     * @return
     */
    public static Map<String, String> getNewMap() {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("versionNum", DeviceUtils.getVersionCode(Application.getInstance()));
        return map;
    }

    /**
     * 意见反馈
     *
     * @param content 意见内容
     * @param phone   联系方式
     * @return
     */
    public static Map<String, String> getFeedBack(String content, String phone) {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("content", content);
        map.put("contact", phone);
        return map;
    }

    /**
     * 订单详情
     *
     * @param type   订单类型    1:支付    2:打赏
     * @param pid    商品ID
     * @param amount 打赏订单必填,支付可不填
     * @param pway   支付类型    1:微信    2:支付宝
     * @return
     */
    public static Map<String, String> getOrder(int type, int pid, float amount, int pway) {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("type", String.valueOf(type));
        map.put("pid", String.valueOf(pid));
        map.put("amount", String.valueOf(amount));
        map.put("pway", String.valueOf(pway));
        return map;
    }

    /**
     * 添加反馈
     *
     * @param title   标题  不能为空
     * @param descibe 简介
     * @param type    类型
     * @param img     base64图片  多个用，分割
     * @return
     */
    public static Map<String, String> getAddServiceMap(String title, String descibe, String type, String img) {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("title", title);
        map.put("describe", descibe);
        map.put("type", type);
        map.put("img_url", img);
        return map;
    }

    /**
     * 获取反馈列表
     *
     * @param pageIndex 页码
     * @param pageSize  单页条数
     * @return
     */
    public static Map<String, String> getFeedBackListMap(int pageIndex, int pageSize) {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf(pageSize));
        return map;
    }

    /**
     * 获取反馈详情
     *
     * @return
     */
    public static Map<String, String> getFeedBackDetailMap(String feedId) {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("feedbackId", feedId);
        return map;
    }

    /**
     * 结束反馈
     *
     * @return
     */
    public static Map<String, String> getFeedBackEndMap(String feedId, int status) {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("feedbackId", feedId);
        map.put("status", String.valueOf(status));
        return map;
    }


    /**
     * 添加反馈回复
     *
     * @param service_id
     * @param repley
     * @param img
     * @return
     */
    public static Map<String, String> getAddRepleyMap(int service_id, String repley, String img) {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("service_id", String.valueOf(service_id));
        map.put("desc", repley);
        map.put("img_url", img);
        return map;
    }

    /**
     * 获取验证码
     *
     * @param tel      手机号
     * @param tpl      信息模板（SMSCode已提供基本类型）
     * @param sms_sign 短信签名
     * @return
     */
    public static Map<String, String> getVarCode(String tel, String tpl, String sms_sign) {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("tel", tel);
        // 2019.11.11新增
        map.put("tpl", tpl);
        map.put("sms_sign", sms_sign);
        return map;
    }


    /**
     * 用户登陆传参
     *
     * @param name 账号
     * @param pwd  密码
     * @return
     */
    public static Map<String, String> userLogin(String name, String pwd) {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("loginname", name);
        map.put("loginpwd", pwd);
        return map;
    }


    /**
     * 手机号动态登陆
     * 2019.11.11新增
     *
     * @param tel     手机号
     * @param smscode 短信认证码
     * @param smskey  短信认证码校验key
     */
    public static Map<String, String> getUserCodeLogin(String tel, String smscode, String smskey) {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("phone", tel);
        map.put("password", smscode);
        return map;
    }

    /**
     * QQ登录
     */
    public static Map<String, String> getQQLoginMap(String openId, String avatar, String nickname) {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("openId", openId);
        map.put("avatar", avatar);
        map.put("nickname", nickname);
        return map;
    }

    /**
     * 手机号注册
     * 2019.11.11新增
     *
     * @param phone    手机号
     * @param password 密码
     */
    public static Map<String, String> getRegisterMap(String phone, String password) {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("phone", phone);
        map.put("password", password);
        return map;
    }

    /**
     * 获取图片上传
     * <p>
     * 七牛云所需的token
     *
     * @param num 所需token个数
     */
    public static Map<String, String> getPhotoTokensMap(String num) {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("num", num);
        return map;
    }

    /**
     * 获取vip套餐信息
     * <p>
     */
    public static Map<String, String> getVipListMap() {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        return map;
    }

    /**
     * 获取订单信息
     * <p>
     */
    public static Map<String, String> getOrderInfoMap(int payType, String idVip) {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("payType", payType + "");
        map.put("idVip", idVip);
        return map;
    }

    /**
     * 提交反馈
     */
    public static Map<String, String> getFeedBackMap(String title, String msg, String type, String files) {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("title", title);
        map.put("msg", msg);
        map.put("type", type);
        map.put("files", files);
        return map;
    }

    /**
     * 提交反馈
     * 回复
     */
    public static Map<String, String> getFeedBackReplyMap(String feedbackId, String msg, String files) {
        Map<String, String> map = new HashMap<>();
        map.putAll(getCurrencyMap());
        map.put("feedbackId", feedbackId);
        map.put("msg", msg);
        map.put("files", files);
        return map;
    }

    /**
     * 微信登录
     */
    public static Map<String, String> getWeChatLogin(String openid, String accessToken) {
        Map<String, String> map = new HashMap<>();
        map.putAll(MapUtils.getCurrencyMap());
        map.put("openid", openid);
        map.put("accessToken", accessToken);
        return map;
    }




    /**
     * 注销账号
     *
     * @param tel     手机号
     * @param smscode 验证码
     * @param smskey  验证码校验码
     */
    public static Map<String, String> loginOut(String tel, String smscode, String smskey) {
        Map<String, String> map = new HashMap<>(getCurrencyMap());
        map.put("tel", tel);
        map.put("smscode", smscode);
        map.put("smskey", smskey);
        return map;
    }
}
