package com.zxqy.xunilaidian.utils.httputil.callback;

import java.io.IOException;

import okhttp3.Request;

/**
 * 调起微信返回信息
 */
public interface DataCallBack<T>{
    void requestFailure(Request request, IOException io);//失败

    void requestSuceess(String result) throws Exception;//成功
}
