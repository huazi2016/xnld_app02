package com.zxqy.xunilaidian.entity;

import java.util.List;

/**
 * App配置信息
 * Created by Administrator on 2018/1/20.
 */

public class AppConfigInfoEntity {

    /**
     * code : 0
     * msg : success
     * data : {"package":"com.zxqy.guajiduanxin","wechat":"aadadadada","qq":"1231421535131231","payWx":1,"payAlipay":1}
     */

    public int code;
    public String msg;
    public AppConfigInfo data;

    public static class AppConfigInfo {
        public ConfigBean configs;
        public List<VipDataBean> vips;
    }


    public static class ConfigBean {
        /**
         * package : com.zxqy.guajiduanxin
         * wechat : aadadadada
         * qq : 1231421535131231
         * payWx : 1
         * payAlipay : 1
         */
        public String wechat;
        public String qq;
        public String smsSuffix;//短信尾巴
        public String share;//分享链接   为空默认关闭
        public int payWx;  //pay 这个支付 1是可用 0 是不可以
        public int payAlipay;//pay 这个支付 1是可用 0 是不可以
    }

    public static class VipDataBean {
        /**
         * content : 这是用于测试的套餐
         * id : vz5w4n0camhbnh7jpkw1008q8iq3zfuj
         * label : 1
         * labelName :
         * name : 测试Vip套餐
         * packageName : com.zxqy.guajiduanxin
         * price : 0
         * status : 1
         * termValidity : 30
         * timeCreate : 2021-04-13T17:13:41+08:00
         */

        public String content;
        public String id;
        public int label;
        public String labelName;
        public String name;
        public String packageName;
        public String price;
        public int status;
        public int termValidity;//时长天数
        public String timeCreate;
        public boolean check;
    }
}
