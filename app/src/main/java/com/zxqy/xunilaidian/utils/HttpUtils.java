package com.zxqy.xunilaidian.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zxqy.xunilaidian.utils.httputil.callback.BaseCallback;
import com.zxqy.xunilaidian.utils.httputil.callback.CPResourceUtils;
import com.zxqy.xunilaidian.utils.httputil.callback.DataCallBack;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/10/11.
 */

public class HttpUtils {
    public static final int GET_HTTP_TYPE = 1;//get请求
    public static final int POST_HTTP_TYPE = 2;//post请求
    public static final int UPLOAD_HTTP_TYPE = 3;//上传请求
    public static final int DOWNLOAD_HTTP_TYPE = 4;//下载请求
    private static HttpUtils mHttpUtils;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private Request request = null;
    private MessageDigest alga;
    private Map<String, String> resultMap;
    private String string;
    private boolean isHave;
    private Gson gson;
    private String commonUrl, uri;
    private String erroMsg = "";
    private List<String> urls;

    @SuppressLint("AllowAllHostnameVerifier")
    private HttpUtils() {
        try {
//            urls = new ArrayList<>();
//            urls.add(HttpUrl.COMMON_URL_1);
//            urls.add(HttpUrl.COMMON_URL_2);
            mOkHttpClient = new OkHttpClient();
            mOkHttpClient.newBuilder()
//                    .addInterceptor(new RetryAndChangeUrlInterceptor(urls.get(0), urls))
//                    .addNetworkInterceptor(new RetryAndChangeUrlInterceptor(urls.get(0), urls))
//                    .addNetworkInterceptor(new LoggingInterceptor())
                    .connectTimeout(60, TimeUnit.MINUTES)
                    .readTimeout(60, TimeUnit.MINUTES)
                    .writeTimeout(60, TimeUnit.MINUTES)
                    .hostnameVerifier(new AllowAllHostnameVerifier())
                    .build()
            ;
            mHandler = new Handler(Looper.getMainLooper());
            gson = new Gson();
            alga = MessageDigest.getInstance("SHA-1");
            //初始化域名
            commonUrl = SpUtils.getInstance().getString(Constants.COMMON_URL, HttpUrl.COMMON_URL_1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HttpUtils getInstance() {
        if (mHttpUtils == null) {
            synchronized (HttpUtils.class) {
                if (mHttpUtils == null) {
                    mHttpUtils = new HttpUtils();
                }
            }
        }
        return mHttpUtils;
//        return new HttpUtils();
    }

    /**
     * 提供对外调用的请求接口
     *
     * @param callBack   回调接口
     * @param url        路径
     * @param type       请求类型
     * @param paramKey   请求参数
     * @param paramValue 请求值
     */
    public static void httpsNetWorkRequest(final DataCallBack callBack, final String url, final int type,
                                           final String[] paramKey, final Object[] paramValue) {
        getInstance().inner_httpsNetWorkRequest(callBack, url, type, paramKey, paramValue);
    }

    /**
     * 内部处理请求的方法
     *
     * @param callBack   回调接口
     * @param url        路径
     * @param type       请求类型
     * @param paramKey   请求参数
     * @param paramValue 请求值
     */
    private void inner_httpsNetWorkRequest(final DataCallBack callBack, final String url, final int type,
                                           final String[] paramKey, final Object[] paramValue) {
        RequestBody requestBody = null;
        FormBody.Builder builder = new FormBody.Builder();
        Map<String, String> map = new TreeMap<String, String>();
        map.put("appid", CPResourceUtils.getString("appid"));
        map.put("sign", null);
        map.put("device", DeviceUtils.getSpDeviceId());
        if (paramKey != null) {
            for (int i = 0; i < paramKey.length; i++) {
                map.put(paramKey[i], String.valueOf(paramValue[i]));
            }
            resultMap = sortMapByKey(map);
        }
        String str = "";
        int num = 0;
        boolean isFirst = true;
        switch (type) {
            case GET_HTTP_TYPE:
                request = new Request.Builder().url(commonUrl + url).build();
                break;
            case POST_HTTP_TYPE:
                /**
                 * 循环遍历获取key值，拼接sign字符串
                 */
                for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                    if (entry.getValue() == null) {
                        continue;
                    }
                    num++;
                    if (isFirst) {
                        str += entry.getKey() + "=" + Base64.encodeToString(entry.getValue().getBytes(), Base64.DEFAULT).trim();
                        isFirst = !isFirst;
                    } else {
                        str = str.trim();
                        str += "&" + entry.getKey() + "=" + Base64.encodeToString(entry.getValue().getBytes(), Base64.DEFAULT).trim();
                        if (num == resultMap.size() - 1) {
                            str += "&" + "key" + "=" + CPResourceUtils.getString("appkey");
                        }
                    }
                }
                str = str.replace("\n", "");//去除换行
                str = str.replace("\\s", "");//去除空格
                isFirst = !isFirst;
                alga.update(str.getBytes());
                /**
                 * 循环遍历value值，添加到表单
                 */
                for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value == null) {
                        value = "";
                    }
                    if (key.equals("sign")) {
                        value = Utils.byte2hex(alga.digest());
                    } else if (key.equals("key")) {
                        continue;
                    }
                    builder.add(key, value);
                }
                requestBody = builder.build();
                request = new Request.Builder().url(commonUrl + url).post(requestBody).build();
                break;
            case UPLOAD_HTTP_TYPE:
                MultipartBody.Builder multipartBody = new MultipartBody.Builder("-----").setType(MultipartBody.FORM);
                if (paramKey != null && paramValue != null) {
                    for (int i = 0; i < paramKey.length; i++) {
                        multipartBody.addFormDataPart(paramKey[i], String.valueOf(paramValue[i]));
                    }
                    requestBody = multipartBody.build();
                }
                request = new Request.Builder().url(commonUrl + url).post(requestBody).build();
                break;
            default:
                break;
        }
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = null;
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    deliverDataFailure(request, e, callBack);
                }
                deliverDataSuccess(result, callBack);
            }
        });
    }

    /**
     * 分发失败的时候回调
     */
    private void deliverDataFailure(final Request request, final IOException e, final DataCallBack callBack) {
        mHandler.post(() -> {
            if (callBack != null) {
                callBack.requestFailure(request, e);
            }
        });
    }

    /**
     * 分发成功的时候回调
     */
    private void deliverDataSuccess(final String result, final DataCallBack callBack) {
        mHandler.post(() -> {
            if (callBack != null) {
                try {
                    callBack.requestSuceess(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * map根据key值比较大小
     */
    private static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>((str1, str2) -> str1.compareTo(str2));
        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * 内部处理Map集合
     * 得到from表单 (post请求)
     */
    private RequestBody getRequestBody(Map<String, String> map) {
        RequestBody requestBody = null;
        FormBody.Builder builder = new FormBody.Builder();
        resultMap = sortMapByKey(map);
        Log.e("请求参数：", "map:" + resultMap.toString());
        StringBuilder str = new StringBuilder();
        int num = 0;
        boolean isFirst = true;
        /**
         * 循环遍历获取key值，拼接sign字符串
         */
        for (Map.Entry<String, String> entry : resultMap.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            num++;
            if (isFirst) {
                String value = Base64.encodeToString(entry.getValue().getBytes(), Base64.DEFAULT).trim();
                str.append(entry.getKey()).append("=").append(value);
                isFirst = false;
            } else {
                str = new StringBuilder(str.toString().trim());
                String value = Base64.encodeToString(entry.getValue().getBytes(), Base64.DEFAULT).trim();
                str.append("&").append(entry.getKey()).append("=").append(value);
                if (num == resultMap.size() - 1) {
//                    str.append("&" + "key" + "=").append(CPResourceUtils.getString("appkey"));
                    str.append("&" + "key" + "=");
                }
            }
        }
        String key_value = str.toString().replace("\n", "") //去除换行
                .replace("\\s", "");//去除空格
        isFirst = !isFirst;
        /**
         * 循环遍历value值，添加到表单
         */
        for (Map.Entry<String, String> entry : resultMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value == null) {
                value = "";
            }
            if (key.equals("sign")) {
                alga.update(key_value.getBytes());
                value = Utils.byte2hex(alga.digest());
            } else if (key.equals("key")) {
                continue;
            }
            String finalValue = value.replace("+", "%2B")
                    .replace("\n", "")
                    .replace("\\s", "");
            builder.add(key, finalValue);
        }
        requestBody = builder.build();
        return requestBody;
    }


    /**
     * 获取反馈列表
     */
    public void postFeedBackList(int pageIndex, int pageSize, BaseCallback callback) {
        post(HttpUrl.FEEDBACK_LIST, MapUtils.getFeedBackListMap(pageIndex, pageSize), callback);
    }

    /**
     * 获取反馈详情
     */
    public void postFeedBackDetail(String feedId, BaseCallback callback) {
        post(HttpUrl.FEEDBACK_DETAIL, MapUtils.getFeedBackDetailMap(feedId), callback);
    }

    /**
     * 结束反馈
     */
    public void postFeedBackEnd(String feedId, int status, BaseCallback callback) {
        post(HttpUrl.FEEDBACK_MARK, MapUtils.getFeedBackEndMap(feedId, status), callback);
    }

    /**
     * 提供给外部调用的注册接口
     *
     * @param callback 回调函数
     */
    public void postRegister(BaseCallback callback) {
        post(HttpUrl.REGIST_DEVICE, MapUtils.getRegistMap(), callback);
    }

    /**
     * 提供给外部调用的更新数据接口
     *
     * @param callback 回调函数
     */
    public void postUpdate(BaseCallback callback) {
        post(HttpUrl.UPDATE, MapUtils.getCurrencyMap(), callback);
    }

    /**
     * 获取app配置信息
     *
     * @param callback 回调函数
     */
    public void getAppConfigInfo(BaseCallback callback) {
        post(HttpUrl.APP_CONFIG_INFO, MapUtils.getCurrencyMap(), callback);
    }

    /**
     * 提供给外部调用的版本更新接口
     *
     * @param callback 回调函数
     */
    public void postNews(BaseCallback callback) {
        post(HttpUrl.GETNEW, MapUtils.getNewMap(), callback);
    }

    /**
     * 提供外部调用的获取验证码接口
     *
     * @param tel      手机号
     * @param tpl      信息模板（SMSCode已提供基本类型）
     * @param sms_sign 短信签名
     * @param callback 回调函数
     */
    public void getVarCode(String tel, String tpl, String sms_sign, BaseCallback callback) {
        post(HttpUrl.GET_VARCODE, MapUtils.getVarCode(tel, tpl, sms_sign), callback);
    }

    /**
     * 动态码登录接口
     * 2019.11.11新增
     *
     * @param tel      手机号
     * @param pwd      密码
     * @param smskey   短信认证码校验key
     * @param callback
     */
    public void userCodeLogin(String tel, String pwd, String smskey, BaseCallback callback) {
        post(HttpUrl.USER_LOGIN_PHONE, MapUtils.getUserCodeLogin(tel, pwd, smskey), callback);
    }

    /**
     * QQ登录
     *
     * @param callback
     */
    public void toQQLogin(String openId, String avatar, String nickname, BaseCallback callback) {
        post(HttpUrl.USER_LOGIN_QQ, MapUtils.getQQLoginMap(openId, avatar, nickname), callback);
    }

    /**
     * 用户注册
     * 2019.11.11新增
     *
     * @param phone    手机号
     * @param password 密码
     * @param callback
     */
    public void toRegister(String phone, String password, BaseCallback callback) {
        post(HttpUrl.TO_REGISTER, MapUtils.getRegisterMap(phone, password), callback);
    }

    /**
     * 获取图片上传需要的token
     * 2019.11.11新增
     *
     * @param num      所需token个数
     * @param callback
     */
    public void getPhotoTokens(int num, BaseCallback callback) {
        post(HttpUrl.GET_FILE_TOKEN, MapUtils.getPhotoTokensMap(num + ""), callback);
    }

    /**
     * 获取vip套餐信息
     * 2019.11.11新增
     *
     * @param callback
     */
    public void getVIPList(BaseCallback callback) {
        post(HttpUrl.VIP_LIST, MapUtils.getVipListMap(), callback);
    }

    /**
     * 获取订单信息
     * 2019.11.11新增
     *
     * @param payType 支付类型 1 支付宝  2 微信
     */
    public void getOrderInfo(int payType, String idVip, BaseCallback callback) {
        post(HttpUrl.GET_ORDERINFO, MapUtils.getOrderInfoMap(payType, idVip), callback);
    }

    /**
     * 提交反馈到服务器
     * 2019.11.11新增
     *
     * @param callback
     */
    public void comitFeedBack(String title, String msg, String type, String files, BaseCallback callback) {
        post(HttpUrl.SUBMIT_FEEDBACK, MapUtils.getFeedBackMap(title, msg, type, files), callback);
    }

    /**
     * 反馈回复
     * 2019.11.11新增
     *
     * @param callback
     */
    public void comitFeedBackReply(String feedbackId, String msg, String files, BaseCallback callback) {
        post(HttpUrl.SUBMIT_FEEDBACK_REPLY, MapUtils.getFeedBackReplyMap(feedbackId, msg, files), callback);
    }

    /**
     * 微信登录
     */
    public void wechatLogin(String openid, String accessToken, BaseCallback callback) {
//        alga.digest();
        post(HttpUrl.USER_WECHAT_LOGIN, MapUtils.getWeChatLogin(openid, accessToken), callback);
    }

    /**
     * 通过用户id
     * 获取用户信息
     */
    public void getUserInfo(BaseCallback callback) {
//        alga.digest();
        post(HttpUrl.USER_INFO, MapUtils.getCurrencyMap(), callback);
    }

    /**
     * 针对微信加密机制的问题，提供一个外部方法来解决
     */
    public void changeDigest() {
        if (alga != null) {
            alga.digest();
        }
    }

    /**
     * 内部提供的post请求方法
     *
     * @param requestType 请求路径
     * @param callback    回调函数
     */
    public void post(String requestType, Map<String, String> params, final BaseCallback callback) {
        if (requestType.startsWith("http")) {
            uri = requestType;
        } else {
            uri = commonUrl +"/" +requestType;
        }
        //请求之前调用(例如加载动画)
        callback.onRequestBefore();
        erroMsg = "请求失败，请重试";
        mOkHttpClient
                .newCall(getRequest(uri, params))
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //返回失败
                        callbackFailure(call.request(), callback, e);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            //返回成功回调
                            try {
                                String result = response.body().string();
                                MyLog.e("", "result==" + result);
                                if (!TextUtils.isEmpty(result)) {
                                    DateBaseBean baseBean = JsonBinder.paseJsonToObj(result, DateBaseBean.class);
                                    if (!TextUtils.isEmpty(baseBean.msg)) {
                                        erroMsg = baseBean.msg;
                                    }
                                    if (baseBean.code == 0 || baseBean.code == 200 || baseBean.code == 1) {//说明成功了  1是没有查询到数据
                                        if (callback.mType == String.class) {
                                            //如果我们需要返回String类型
                                            callbackSuccess(response, result, callback);
                                        } else {
                                            //如果返回是其他类型,则用Gson去解析
                                            try {
                                                Object o = gson.fromJson(result, callback.mType);
                                                callbackSuccess(response, o, callback);
                                            } catch (JsonSyntaxException e) {

                                                e.printStackTrace();
                                                callbackError(response, callback, e, erroMsg);
                                            }
                                        }
                                    } else if (baseBean.code == -401) {//登录超时
                                        erroMsg = "登录超时，请重新登录！";
                                        callbackError(response, callback, null, erroMsg);
//                                MyApplication.getInstance().startActivity(new Intent(MyApplication.getInstance(), LoginActivity.class));
                                    } else {
                                        if (!TextUtils.isEmpty(baseBean.msg)) {
                                            ToastUtils.showLongToast(baseBean.msg);
//                                    e.printStackTrace();
                                        }
                                        callbackError(response, callback, null, erroMsg);
                                    }
                                } else {
                                    callbackError(response, callback, null, erroMsg);
                                }


/*
                        if (requestType.equals(API.USER_LOGIN) || requestType.equals(API.USER_LOGIN_CODE)) {
                            // 保存用户信息
                            LoginInfoBean info = GsonUtils.getFromClass(result, LoginInfoBean.class);
                            if (info != null && info.isIssucc()) {
                                Utils.setLoginInfo(info.getData().getUser_id(),
                                        info.getData().getUkey(),
                                        info.getData().getHeadimg());
                            }
                        } else if (requestType.equals(API.GET_ALIOSS)) {
                            // 获取阿里云信息
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("issucc")) {
                                    String data = jsonObject.getString("data");
                                    if (!TextUtils.isEmpty(data)) {
                                        Log.e("请求返回数据", "阿里云数据：" + data);
                                        Utils.setAliOssParam(data);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (requestType.equals(API.UPDATE)) {
                            // 获取所有数据
                            UpdateBean updateBean = GsonUtils.getFromClass(result, UpdateBean.class);
                            if (updateBean != null) {
                                DataSaveUtils.getInstance().saveAppData(updateBean);
                            }
                        } else if (requestType.equals(API.USER_LOGIN_CHECK)) {
                            // 校验登陆
                            ResultBean resultBean = GsonUtils.getFromClass(result, ResultBean.class);
                            if (resultBean != null && !resultBean.isIssucc()) {
                                // 已在别的设备登陆，清空本机登陆状态
                                Utils.setLoginInfo("", "", "");
                            }
                        }
*/


                            } catch (Exception e) {
                                e.printStackTrace();
                                callbackError(response, callback, e, erroMsg);
                            }
                        } else {
//                            if (response.code()==404) {
//                                if (commonUrl.equals(HttpUrl.COMMON_URL_1)) { //重新配置通用接口请求地址
//                                    SpUtils.getInstance().putString(Constants.COMMON_URL, HttpUrl.COMMON_URL_2);
//                                } else {
//                                    SpUtils.getInstance().putString(Constants.COMMON_URL, HttpUrl.COMMON_URL_1);
//                                }
//                            }
                            callbackError(response, callback, null, erroMsg);
                        }
                    }
                });
    }

    /**
     * 得到Request
     *
     * @param url    请求路径
     * @param params from表单
     */
    private Request getRequest(String url, Map<String, String> params) {
        //可以从这么划分get和post请求，暂时只支持post
        Log.e("请求参数：", "url:" + url);
        return new Request.Builder().url(url).post(getRequestBody(params))
                .addHeader("Authorization", "Bearer " + SpUtils.getInstance().getString(Constants.APP_TOKEN))
                .build();
    }

    /**
     * 在主线程中执行成功回调
     *
     * @param response 请求响应
     * @param o        类型
     * @param callback 回调函数
     */
    private void callbackSuccess(final Response response, final Object o, final BaseCallback<Object> callback) {
        mHandler.post(() -> callback.onSuccess(response, o));
    }

    /**
     * 在主线程中执行错误回调
     *
     * @param response 请求响应
     * @param callback 回调函数
     * @param e        响应错误异常
     */
    private void callbackError(final Response response, final BaseCallback callback, Exception e, String erroMsg) {
        mHandler.post(() -> callback.onError(response, response.code(), e, erroMsg));
    }

    /**
     * 在主线程中执行失败回调
     *
     * @param request  请求链接
     * @param callback 回调韩素和
     * @param e        响应错误异常
     */
    private void callbackFailure(final Request request, final BaseCallback callback, final Exception e) {
        mHandler.post(() -> callback.onFailure(request, e));
    }
}
