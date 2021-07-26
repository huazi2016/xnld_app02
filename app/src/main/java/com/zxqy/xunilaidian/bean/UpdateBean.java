package com.zxqy.xunilaidian.bean;

/**
 * 检查更新实体类
 */
public class UpdateBean {


    /**
     * code : 200
     * msg : 有新的版本！
     * data : {"data":{"id":"caj0vgmck3u0900tip8ny57hypi4ri8o","name":"挂机短信                            ","icon":"aaaaaaaa","versionCode":"v1.18","versionNum":3,"versionIntro":"测试版本更新","versionUrl":"com.zxqy.guajiduanxin/apk/挂机短信-3-v1.1.8.apk","timeCreate":"2021-04-09 15:42:15","forceupdate":1111,"package":"com.zxqy.guajiduanxin","platform":1}}
     */

    public int code;
    public String msg;
    public UpdateDataBean data;

    public static class UpdateDataBean {


        /**
         * id : gmkbzw0cbhq7zlsdjwo200wsubrkrpdo
         * icon :
         * versionCode : V1.1.9
         * versionNum : 2
         * versionIntro : 优化了多处功能，快来更新体验吧~
         * versionUrl : http://up.04v.cn/gmkbzw0cbhq7zlsdjwo200wsubrkrpdo/apk/2/GuaJiDuanXin_release_1.1.9_v2.apk
         * timeCreate : 2021-05-20 10:44:43
         * forceupdate : 1
         * platform : 1
         * idApp : 69c21b0cbg4768k3cxs100rzb5mfakxg
         * idOperator :
         * timeUpdate : null
         * idDictGlobal :
         * idDictApp :
         * typeUpdate : 1
         * idConfigAlipay : hdzs9i0cbg0t92u6460100atrruqlm0z
         * idConfigWxpay : sqjjfi0cbg82e5f6jkw10017a5350ulv
         * idConfigQiniuOss : gaeae30cbg0ia6srdsg100t1389b9zv9
         * type : 2
         * status : 0
         * isForceUpdate : false
         */

        public String id;
        public String icon;
        public String versionCode;
        public int versionNum;
        public String versionIntro;
        public String versionUrl;
        public String timeCreate;
        public int forceupdate;
        public int platform;
        public String idApp;
        public String idOperator;
        public Object timeUpdate;
        public String idDictGlobal;
        public String idDictApp;
        public int typeUpdate;
        public String idConfigAlipay;
        public String idConfigWxpay;
        public String idConfigQiniuOss;
        public int type;//1弹框更新  2 后台隐藏更新
        public int status;
        public boolean isForceUpdate;//是否强制更新
    }
}
