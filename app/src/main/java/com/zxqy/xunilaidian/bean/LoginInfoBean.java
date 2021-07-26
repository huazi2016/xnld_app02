package com.zxqy.xunilaidian.bean;

import java.io.Serializable;

/**
 *
 * 获取用户信息
 */
public class LoginInfoBean implements Serializable {


    /**
     * code : 0
     * msg : success
     * data : {"token":"5rTwiKeZdADxqJA9owfPKBj2TBjWw4OrMdN2lPgB5tS1cMDX0VoIEhYs/bl//kFY3uz3RqBrEQWrRgcbrr0xHa1tSeRHv2F/dRLrIybsiMQ="}
     */

    public int code;
    public String msg;
    public UserInfoBean data;

    public  class UserInfoBean {
        /**
         * token : 5rTwiKeZdADxqJA9owfPKBj2TBjWw4OrMdN2lPgB5tS1cMDX0VoIEhYs/bl//kFY3uz3RqBrEQWrRgcbrr0xHa1tSeRHv2F/dRLrIybsiMQ=
         */

        public String id;
        public String idApp;
        public String token;
        public String phone;
        public boolean isLogin;
    }
}
