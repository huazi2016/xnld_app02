package com.zxqy.xunilaidian.bean;

import java.io.Serializable;
import java.util.List;

/**
 图片上传token Bean
 */
public class PhotoTokenBean implements Serializable {


    /**
     * code : 200
     * msg : success
     * data : [":Kh05q5WFv82jcIQ4gJv1RwNOnTc=:eyJzY29wZSI6Im1lc3NhZ2Utbm8xIiwiZGVhZGxpbmUiOjE2MTgyODAzODR9"]
     */

    public int code;
    public String msg;
    public List<String> data;
}
