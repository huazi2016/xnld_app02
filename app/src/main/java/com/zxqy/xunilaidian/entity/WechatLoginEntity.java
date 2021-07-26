package com.zxqy.xunilaidian.entity;


/**
 * 黑名单实体类
 * Created by huanghaibin on 2017/12/4.
 */
public class WechatLoginEntity {


    /**
     * code : 0
     * msg : success
     * data : {"id":"1vlym87caupq4w52ek0100gtmgfufhig","token":"w8CllE0haEDfXwJOMdN1hchBLD+WbeRnUb40m3xsK3d/2Ro91as4r1QfWVKnCnWPtY2ovpizY9sF5adBxtLqDKRmO6Ou30aB5JVa/6eLIJY="}
     */

    public int code;
    public String msg;
    public DataEntity data;

    public static class DataEntity {
        /**
         * id : 1vlym87caupq4w52ek0100gtmgfufhig
         * token : w8CllE0haEDfXwJOMdN1hchBLD+WbeRnUb40m3xsK3d/2Ro91as4r1QfWVKnCnWPtY2ovpizY9sF5adBxtLqDKRmO6Ou30aB5JVa/6eLIJY=
         */

        public String id;
        public String token;
        public String idApp;
    }
}
