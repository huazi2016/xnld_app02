package com.zxqy.xunilaidian.wxapi;

import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.base.BaseActivity;
import com.zxqy.xunilaidian.bean.UserInfoBean;
import com.zxqy.xunilaidian.bean.WxAccessTokenBean;
import com.zxqy.xunilaidian.bean.WxUserInfoBean;
import com.zxqy.xunilaidian.entity.WechatLoginEntity;
import com.zxqy.xunilaidian.Application;
import com.zxqy.xunilaidian.utils.Constants;
import com.zxqy.xunilaidian.utils.DeviceUtils;
import com.zxqy.xunilaidian.utils.HttpDefine;
import com.zxqy.xunilaidian.utils.HttpUtils;
import com.zxqy.xunilaidian.utils.MessageEvent;
import com.zxqy.xunilaidian.utils.SpDefine;
import com.zxqy.xunilaidian.utils.SpUtils;
import com.zxqy.xunilaidian.utils.ToastUtils;
import com.zxqy.xunilaidian.utils.httputil.callback.BaseCallback;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Request;
import okhttp3.Response;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {


    private static final int RETURN_MSG_TYPE_LOGIN = 1; //登录
    private static final int RETURN_MSG_TYPE_SHARE = 2; //分享


    @Override
    protected void initView() {
        //这句没有写,是不能执行回调的方法的
        Application.mWXApi.handleIntent(getIntent(), this);
    }

    @Override
    public void setRootView() {
        setShowStatusBar(true);
        setContentView(R.layout.activity_wxpay_entry);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onReq(BaseReq baseReq) {
        // 微信发送请求到第三方应用时，会回调到该方法
    }

    @Override
    public void onResp(BaseResp baseResp) {
        // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
        //app发送消息给微信，处理返回消息的回调
        int type = baseResp.getType(); //类型：分享还是登录
        Log.d("微信回调结果", +baseResp.errCode + "type:---->" + type);
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //用户拒绝授权
                ToastUtils.showLongToast("拒绝授权微信登录");
                finish();
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                String message = "";
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    message = "取消了微信登录";
                } else if (type == RETURN_MSG_TYPE_SHARE) {
                    message = "取消了微信分享";
                }
                ToastUtils.showLongToast(message);
                finish();
                break;
            case BaseResp.ErrCode.ERR_OK:
                //用户同意
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    //用户换取access_token的code，仅在ErrCode为0时有效
                    String code = ((SendAuth.Resp) baseResp).code;
                    Log.e("微信用户同意登录请求", "code:------>" + code);
                    //这里拿到了这个code，去做2次网络请求获取access_token和用户个人信息
                    getAccessToken(code);
                } else if (type == RETURN_MSG_TYPE_SHARE) {
                    ToastUtils.showLongToast("微信分享成功");
                    finish();
                }
                break;
            case BaseResp.ErrCode.ERR_BAN:
                ToastUtils.showLongToast("微信授权失败");
                finish();
                break;
        }
    }

    /**
     * 获取access_token：
     *
     * @param code 用户获取access_token的code，仅在ErrCode为0时有效
     */
    private void getAccessToken(final String code) {
        try {
            //检查网络是否可用
            if (DeviceUtils.isNetworkConnected(this)) {
                HttpDefine.getWxToken(code, new BaseCallback<WxAccessTokenBean>() {
                    @Override
                    public void onRequestBefore() {
                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        ToastUtils.showLongToast("微信获取数据失败");
                        finish();
                    }

                    @Override
                    public void onSuccess(Response response, WxAccessTokenBean wxAccessToken) {
                        //获取微信端 账号信息 此步骤放到服务端了
//                        getWxMessage(wxAccessToken.getAccess_token(), wxAccessToken.getOpenid());
                        Log.e("微信获取access_token:", wxAccessToken.toString());
                        loginToApp(wxAccessToken);
                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e, String erroMsg) {
                        ToastUtils.showLongToast("微信获取数据失败");
                        finish();
                    }
                });
            }
        } catch (Exception e) {
            ToastUtils.showLongToast("微信获取数据失败");
            Log.d("微信", "警告异常" + e.toString());
            finish();
        }
    }


    /**
     * 登录应用
     */
    private void loginToApp(WxAccessTokenBean info) {
//        HttpUtils.getInstance().wechatLogin(info.getOpenid(), info.getAccess_token(), new BaseCallback<DataResultBean<LoginInfo>>() {
        HttpUtils.getInstance().wechatLogin(info.getOpenid(), info.getAccess_token(), new BaseCallback<WechatLoginEntity>() {
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {
                ToastUtils.showLongToast("微信获取数据失败");
                finish();
            }

            @Override
            public void onSuccess(Response response, WechatLoginEntity o) {

                if (o != null) {
                    SpUtils.getInstance().putString(Constants.USER_ID, o.data.id);
                    SpUtils.getInstance().putString(Constants.APP_TOKEN, o.data.token);
                    SpUtils.getInstance().putString(Constants.ID_APP, o.data.idApp);
                    getUserInfo();
                } else {
                    ToastUtils.showLongToast("微信登录失败~");
                }
            }

            @Override
            public void onError(Response response, int errorCode, Exception e, String erroMsg) {
                ToastUtils.showLongToast("微信获取数据失败");
                finish();
            }
        });
    }


    /**
     * 刷新用户信息
     */
    private void getUserInfo() {

        HttpUtils.getInstance().getUserInfo(new BaseCallback<UserInfoBean>() {
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {
                ToastUtils.showLongToast("微信获取数据失败");
                finish();
            }

            @Override
            public void onSuccess(Response response, UserInfoBean resultBean) {
                //获取用户信息成功
                if (resultBean.code == 0 || resultBean.code == 200 && resultBean.data != null) {

                    SpDefine.setUserInfo(resultBean.data);
                    SpDefine.setVipInfo(resultBean.data.vip);
                    if (resultBean.data.vip != null) {//
                        SpUtils.getInstance().putInt(Constants.VIP_STATUS, resultBean.data.vip.status);
                        //会员ui更新
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.PAY_OK));
                    } else {
                        SpUtils.getInstance().putInt(Constants.VIP_STATUS, 3);
                    }
                    if (!TextUtils.isEmpty(resultBean.data.phone)) {
                        SpUtils.getInstance().putString(Constants.USER_PHONE, resultBean.data.phone);
                    }
                    SpUtils.getInstance().putBoolean(Constants.IS_LOGIN, true);
                    SpUtils.getInstance().putString(Constants.HEAD_IMAGE,resultBean.data.avatar);
                    SpUtils.getInstance().putString(Constants.USER_NAME,resultBean.data.nickname);

                    EventBus.getDefault().post(new MessageEvent(MessageEvent.UPDATE_LOG_INFO));
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.WECHAT_LOGIN_OK));
                    ToastUtils.showLongToast("微信登录成功");
                    finish();

                } else {
                    ToastUtils.showLongToast(resultBean.msg);
                }

            }

            @Override
            public void onError(Response response, int errorCode, Exception e, String erroMsg) {
                ToastUtils.showLongToast("微信获取数据失败");
                finish();
            }
        });
    }

    /**
     * 获取微信登录，用户授权后的个人信息
     * 此步骤放到服务端了
     */
    private void getWxMessage(String access_token, String openid) {
        try {
            //检查网络是否可用
            if (DeviceUtils.isNetworkConnected(this)) {
                HttpDefine.getWxUserInfo(access_token, openid, new BaseCallback<WxUserInfoBean>() {
                    @Override
                    public void onRequestBefore() {
                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        ToastUtils.showLongToast("微信获取数据失败1");
                        finish();
                    }

                    @Override
                    public void onSuccess(Response response, WxUserInfoBean wxMessageBean) {
                        if (wxMessageBean != null) {
                            if (Application.identity == 1) {
                                ToastUtils.showLongToast("微信登录成功");
//                                loginToApp(wxMessageBean);
                                finish();

                            } else if (Application.identity == 2) {
                                bindWechat(wxMessageBean);
                            }

                        } else {
                            ToastUtils.showLongToast("微信获取数据失败2");
                            finish();
                        }
                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e, String erroMsg) {
                        ToastUtils.showLongToast("微信获取数据失败3");
                        finish();
                    }
                });
            }
        } catch (Exception e) {
            ToastUtils.showLongToast("微信获取数据失败4");
            finish();
        }
    }

    /**
     * 绑定微信
     */
    private void bindWechat(WxUserInfoBean info) {
       /* HttpDefine.bindWechat(info.getOpenid(), info.getNickname(), info.getSex(), info.getHeadimgurl(), new BaseCallback<DataResultBean<TaskGetBean>>() {
            @Override
            public void onRequestBefore() {
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }

            @Override
            public void onSuccess(Response response, DataResultBean<TaskGetBean> resultBean) {
                if (resultBean.getIssucc()) {
                    //绑定微信
                    MyAppUtil.showCenterToast("绑定成功");
                    SpUtils.getInstance().putString(AppConfig.WXNAME, info.getNickname());
                    SpUtils.getInstance().putString(AppConfig.WXHEAD, info.getHeadimgurl());
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.BIND_WECHAT, resultBean.getData().getIntegral()));
                    finish();
                } else {
                    MyAppUtil.showCenterToast(resultBean.getMsg());
                    finish();
                }
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
            }
        });*/
    }
}
