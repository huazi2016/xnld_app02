package com.zxqy.xunilaidian.bean;

import java.io.Serializable;

/**
 * 通过用户id
 * 用户详情信息
 */
public class UserInfoBean implements Serializable {

    /**
     * code : 0
     * msg : success
     * data : {"idUser":"canvmqe3txw0100sp7a4l430l8swfpep","nickname":"zxIoxwjYZSmpRcGCjL","avatar":"","timeCreate":"2021-04-23 10:35:15","timeUpdate":null,"email":"","name":"","birthday":null,"balance":"","score":0,"userfrom":"unknown","phone":""}
     */

    public int code;
    public String msg;
    public UserInfo data;

    public static class UserInfo implements Serializable {
        /**
         * idUser : canvmqe3txw0100sp7a4l430l8swfpep
         * nickname : zxIoxwjYZSmpRcGCjL
         * avatar :
         * timeCreate : 2021-04-23 10:35:15
         * timeUpdate : null
         * email :
         * name :
         * birthday : null
         * balance :
         * score : 0
         * userfrom : unknown
         * phone :
         */

        public String idUser;
        public String nickname;
        public String avatar;
        public String timeCreate;
        public String timeUpdate;
        public String email;
        public String name;
        public String birthday;
        public String balance;
        public int score;
        public String userfrom;
        public String idApp;
        public String phone;
        public VipInfo vip;
    }
    public static class VipInfo implements Serializable {

        /**
         * id : 174qrmpcay55dw8wcpw600v09o07pfdk
         * idApp : 1nl6hvn144c7akcaj0u31iks40700blj
         * idUser : WX1lzespqcawhbptcfuwk100p24t2kn87g
         * status : 2
         * timeCreate : 2021-04-27 10:12:19
         * timeOutdate : 2022-04-27 00:00:00
         * idConfig : 1oy1fozcamhdk4q0me0100ufuma50vqd
         */

        public String id;
        public String idApp;
        public String idUser;
        public int status;// 1 表示是激活和没过期的   2是过期的
        public String timeCreate;
        public String timeOutdate;
        public String idConfig;
    }
}
