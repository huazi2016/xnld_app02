package com.zxqy.xunilaidian.utils.httputil.callback;


public abstract class HttpCallback<T> {
    public abstract void onSuccess(T t);

    public abstract void onFail(Exception e);

    public void onFinish() {
    }
}
