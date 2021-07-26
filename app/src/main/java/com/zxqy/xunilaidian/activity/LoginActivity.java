package com.zxqy.xunilaidian.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.tencent.connect.UserInfo;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.tauth.DefaultUiListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zxqy.xunilaidian.Application;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.base.BaseActivity;
import com.zxqy.xunilaidian.bean.LoginInfoBean;
import com.zxqy.xunilaidian.bean.UserInfoBean;
import com.zxqy.xunilaidian.entity.QQLoginEntity;
import com.zxqy.xunilaidian.entity.QQLoginTokenEntity;
import com.zxqy.xunilaidian.entity.WechatLoginEntity;
import com.zxqy.xunilaidian.utils.Constants;
import com.zxqy.xunilaidian.utils.DeviceUtils;
import com.zxqy.xunilaidian.utils.HttpUtils;
import com.zxqy.xunilaidian.utils.JsonBinder;
import com.zxqy.xunilaidian.utils.MessageEvent;
import com.zxqy.xunilaidian.utils.MyAppUtil;
import com.zxqy.xunilaidian.utils.SpDefine;
import com.zxqy.xunilaidian.utils.SpUtils;
import com.zxqy.xunilaidian.utils.TimeCount;
import com.zxqy.xunilaidian.utils.ToastUtils;
import com.zxqy.xunilaidian.utils.httputil.callback.BaseCallback;
import com.zxqy.xunilaidian.view.ForbidEmojiEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;

import static com.tencent.connect.common.Constants.KEY_RESTORE_LANDSCAPE;
import static com.tencent.connect.common.Constants.KEY_SCOPE;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {


    @BindView(R.id.et_tel)
    ForbidEmojiEditText mTelEdit;
    @BindView(R.id.et_code)
    ForbidEmojiEditText mCodeEdit;
    @BindView(R.id.tv_code)
    TextView mCodeText;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.card_login)
    CardView cardLogin;

    private Tencent mTencent;
    private QQLoginTokenEntity qqLoginTokenEntity;
    private QQLoginEntity qqLoginEntity;
    //    用户协议，隐私政策勾选框
//    @BindView(R.id.iv_check)
//    ImageView iv_check;
    // 倒数
    private TimeCount mTimeCount;
    private String mCode;


    protected void initView() {
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setOtherWeather(MessageEvent event) throws ParseException {
        if (event != null && event.data.equals(MessageEvent.WECHAT_LOGIN_OK)) {//微信登录成功，关闭页面
            finish();
        } else if (event != null && event.data.equals(MessageEvent.REGIST_OK)) {//注册成功，把用户名 密码自动输入上去
            mTelEdit.setText(event.phone);
            mCodeEdit.setText(event.pwd);
        }
    }

    @Override
    public void setRootView() {
        setShowStatusBar(true);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initData() {
    }


    @OnClick({R.id.ll_phoneLogin, R.id.ll_wechatLogin, R.id.ll_qqLogin, R.id.iv_back, R.id.tv_login, R.id.tv_regist, R.id.iv_wechatLogin, R.id.iv_QQLogin})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_phoneLogin:
                cardLogin.setVisibility(View.GONE);
                llPhone.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_back://回退
                finish();
                break;
            case R.id.tv_login://提交
                toLogin();
                break;
            case R.id.tv_regist://注册
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.iv_wechatLogin://微信登录
            case R.id.ll_wechatLogin:
                if (!MyAppUtil.isWeChatInstalled(this)) {
                    ToastUtils.showLongToast("您还未安装微信");
                    return;
                }
                Application.identity = 1;
                SendAuth.Req req = new SendAuth.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());  //transaction字段用与唯一标示一个请求
                req.scope = "snsapi_userinfo";
                req.state = "wx_login"; //这个字段可以任意更改
                Application.mWXApi.sendReq(req);
                break;
            case R.id.iv_QQLogin://QQ登录
            case R.id.ll_qqLogin:
                mTencent = Tencent.createInstance(Constants.QQ_APP_ID, this, Constants.QQ_APP_AUTHORITIES);
                if (mTencent == null) {
                    ToastUtils.showLongToast("数据初始化失败");
                    return;
                }
                if (!mTencent.isQQInstalled(this)) {
                    ToastUtils.showLongToast("您还未安装微信");
                    return;
                }
                HashMap<String, Object> params = new HashMap<String, Object>();
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                    params.put(KEY_RESTORE_LANDSCAPE, true);
                }

                params.put(KEY_SCOPE, "all");
//                params.put(KEY_QRCODE, mQrCk.isChecked());//是否支持扫码
                mTencent.logout(this);//先退出登录
                if (!mTencent.isSessionValid()) {

                    //login
                    mTencent.login(this, loginListener, params);
                }
                break;
        }
    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            qqLoginTokenEntity = JsonBinder.paseJsonToObj(values.toString(), QQLoginTokenEntity.class);
            if (qqLoginTokenEntity == null && !TextUtils.isEmpty(qqLoginTokenEntity.access_token) && !TextUtils.isEmpty(qqLoginTokenEntity.expires_in + "")
                    && !TextUtils.isEmpty(qqLoginTokenEntity.openid)) {
                ToastUtils.showLongToast("返回为空, 登录失败");
                return;
            }
            mTencent.setAccessToken(qqLoginTokenEntity.access_token
                    , qqLoginTokenEntity.expires_in + "");
            mTencent.setOpenId(qqLoginTokenEntity.openid);

            updateUserInfo();
        }
    };

    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            showLoadDialog("QQ登录中...");

            IUiListener listener = new DefaultUiListener() {
                @Override
                public void onError(UiError e) {
                    canceloadDialog();
                }

                @Override
                public void onComplete(final Object response) {
                    canceloadDialog();
                    if (response != null) {
                        qqLoginEntity = JsonBinder.paseJsonToObj(response.toString(), QQLoginEntity.class);
                        toQQLogin();
                    } else {
                        ToastUtils.showLongToast("QQ信息获取失败~");
                    }
                }

                @Override
                public void onCancel() {
                    canceloadDialog();
                }
            };
            UserInfo info = new UserInfo(this, mTencent.getQQToken());
            info.getUserInfo(listener);

        } else {
            ToastUtils.showLongToast("QQ信息获取失败~");
        }
    }

    /**
     * 隐藏加载进度对话框
     */
    public void canceloadDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * 通过QQ登录
     */
    private void toQQLogin() {
        HttpUtils.getInstance().toQQLogin(
                qqLoginTokenEntity.openid,
                qqLoginEntity.figureurl_qq,
                qqLoginEntity.nickname,
                new BaseCallback<WechatLoginEntity>() {
                    @Override
                    public void onRequestBefore() {
                        showLoadDialog("QQ登录中...");
                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        canceloadDialog();
                        ToastUtils.showShortToast("服务异常请重试！");
                    }

                    @Override
                    public void onSuccess(Response response, WechatLoginEntity o) {
                        canceloadDialog();
                        if (o != null) {
                            SpUtils.getInstance().putString(Constants.USER_ID, o.data.id);
                            SpUtils.getInstance().putString(Constants.ID_APP, o.data.idApp);
                            SpUtils.getInstance().putString(Constants.APP_TOKEN, o.data.token);
                            getUserInfo();
                        } else {
                            ToastUtils.showLongToast("QQ登录失败~");
                        }
                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e, String erroMsg) {
                        canceloadDialog();
                        if (!TextUtils.isEmpty(erroMsg)) {
                            ToastUtils.showLongToast(erroMsg);
                        }
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
                canceloadDialog();
                ToastUtils.showLongToast("刷新用户信息失败~");
            }

            @Override
            public void onSuccess(Response response, UserInfoBean resultBean) {
                canceloadDialog();
                //获取用户信息成功
                if (resultBean.data != null && resultBean.code == 0 || resultBean.code == 200) {
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
                    SpUtils.getInstance().putString(Constants.HEAD_IMAGE, resultBean.data.avatar);
                    SpUtils.getInstance().putString(Constants.USER_NAME, resultBean.data.nickname);
                    SpUtils.getInstance().putBoolean(Constants.IS_LOGIN, true);
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.UPDATE_LOG_INFO));
                    ToastUtils.showLongToast("登录成功");
                    finish();
                } else {
                    ToastUtils.showLongToast(resultBean.msg);
                }

            }

            @Override
            public void onError(Response response, int errorCode, Exception e, String erroMsg) {
                canceloadDialog();
                if (!TextUtils.isEmpty(erroMsg)) {
                    ToastUtils.showLongToast(erroMsg);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private class BaseUiListener extends DefaultUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                ToastUtils.showLongToast("返回为空, 登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (jsonResponse.length() == 0) {
                ToastUtils.showLongToast("返回为空, 登录失败");
                return;
            }
//            ToastUtils.showLongToast(response.toString() + "登录成功");
            // 有奖分享处理
//            handlePrizeShare();
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            ToastUtils.showLongToast("登录异常！ ");
//            ToastUtils.showLongToast("onError: " + e.errorDetail);
//            Util.dismissDialog();
        }

        @Override
        public void onCancel() {
            ToastUtils.showLongToast("取消登录");
//            Util.dismissDialog();
            /*if (isServerSideLogin) {
                isServerSideLogin = false;
            }*/
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateGiftList(MessageEvent event) {
        if (event != null && event.data.equals(MessageEvent.LOGIN_WECHAT)) {
            //微信绑定成功
//            Accomplished(AppConfig.BDWXZH);
            finish();
        }
    }

    private void toLogin() {
        if (TextUtils.isEmpty(mTelEdit.getText().toString().trim())) {
            ToastUtils.showLongToast("电话号码不能为空！");
            return;
        }

        if (!DeviceUtils.isMobileNO(mTelEdit.getText().toString().trim())) {
            ToastUtils.showLongToast("请输入正确的手机号！");
            return;
        }
        if (TextUtils.isEmpty(mCodeEdit.getText().toString().trim())) {
            ToastUtils.showLongToast("密码不能为空！");
            return;
        }
        showLoadDialog("登录中...");
        HttpUtils.getInstance().userCodeLogin(mTelEdit.getText().toString().trim(), mCodeEdit.getText().toString().trim(), "",
                new BaseCallback<LoginInfoBean>() {
                    @Override
                    public void onRequestBefore() {

                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        canceloadDialog();
                        ToastUtils.showShortToast("服务异常请重试！");
                    }

                    @Override
                    public void onSuccess(Response response, LoginInfoBean o) {
                        canceloadDialog();
                        if (o != null && o.code == 0) {
                            ToastUtils.showShortToast("登录成功");
                            o.data.isLogin = true;
                            o.data.phone = mTelEdit.getText().toString().trim();
//                            SpDefine.setUserInfo(o.data);
                            SpUtils.getInstance().putString(Constants.USER_PHONE, mTelEdit.getText().toString().trim());
                            SpUtils.getInstance().putBoolean(Constants.IS_LOGIN, true);
                            SpUtils.getInstance().putString(Constants.USER_ID, o.data.id);
                            SpUtils.getInstance().putString(Constants.ID_APP, o.data.idApp);
                            SpUtils.getInstance().putString(Constants.APP_TOKEN, o.data.token);
                            SpUtils.getInstance().putBoolean(Constants.OUR_LOGIN, true);
                            getUserInfo();//获取用户信息


                        } else {
                                /*if (!TextUtils.isEmpty(o.getMsg())) {
                                    ToastUtils.showShortToast(o.getMsg());
                                }*/
                            ToastUtils.showShortToast("接口异常");
                        }
                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e, String erroMsg) {
                        canceloadDialog();
                        if (!TextUtils.isEmpty(erroMsg)) {
                            ToastUtils.showLongToast(erroMsg);
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_LOGIN ||
                requestCode == com.tencent.connect.common.Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
