package com.zxqy.xunilaidian.utils;

import android.annotation.SuppressLint;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

@SuppressLint("SimpleDateFormat")
public class JsonBinder {

    /**
     *  将bean或者List集合转化成json字符串
     */
    public static String toJson(Object object){
        return JSON.toJSONString(object);
    }

    /**
     * 将json字符串转化为List集合
     */
    public static <T> List<T> paseJsonToList(String json, Class<T> tClass){
        return JSONObject.parseArray(json,tClass);
    }

    /**
     * 将json字符串转化为bean对象
     */
    public static <T> T paseJsonToObj(String json, Class<T> tClass){
        return JSONObject.parseObject(json,tClass);
    }


}
