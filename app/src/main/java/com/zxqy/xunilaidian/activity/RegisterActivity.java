package com.zxqy.xunilaidian.activity;

import android.text.TextUtils;
import android.view.View;

import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.base.BaseActivity;
import com.zxqy.xunilaidian.utils.DateBaseBean;
import com.zxqy.xunilaidian.utils.DeviceUtils;
import com.zxqy.xunilaidian.utils.HttpUtils;
import com.zxqy.xunilaidian.utils.MessageEvent;
import com.zxqy.xunilaidian.utils.ToastUtils;
import com.zxqy.xunilaidian.utils.httputil.callback.BaseCallback;
import com.zxqy.xunilaidian.view.ForbidEmojiEditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 用户注册页面
 */
public class  RegisterActivity extends BaseActivity {


    @BindView(R.id.et_tel)
    ForbidEmojiEditText et_tel;
    @BindView(R.id.et_pwd)
    ForbidEmojiEditText et_pwd;
    @BindView(R.id.et_pwd_again)
    ForbidEmojiEditText et_pwd_again;

    private String mPhone, mPwd, mPwdAgain;

    protected void initView() {


    }


    @Override
    public void setRootView() {
        setShowStatusBar(true);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.iv_back, R.id.tv_comit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://回退
                finish();
                break;
            case R.id.tv_comit://提交
                toLogin();
                break;
        }
    }

    private void toLogin() {
        mPhone = et_tel.getText().toString().trim();
        mPwd = et_pwd.getText().toString().trim();
        mPwdAgain = et_pwd_again.getText().toString().trim();

        if (TextUtils.isEmpty(mPhone)) {
            ToastUtils.showLongToast("电话号码不能为空！");
            return;
        }
        if (!DeviceUtils.isMobileNO(mPhone)) {
            ToastUtils.showLongToast("请输入正确的手机号！");
            return;
        }
        if (TextUtils.isEmpty(mPwd)) {
            ToastUtils.showLongToast("密码不能为空！");
            return;
        }
        if (mPwd.length() < 6 || mPwd.length() > 16) {
            ToastUtils.showLongToast("密码长度应在8~16位之间！");
            return;
        }
        if (TextUtils.isEmpty(mPwdAgain)) {
            ToastUtils.showLongToast("确认密码不能为空！");
            return;
        }
        if (!mPwdAgain.equals(mPwd)) {
            ToastUtils.showLongToast("两次密码不一致！");
            return;
        }

        showLoadDialog("注册...");
        HttpUtils.getInstance().toRegister(mPhone, mPwd,
                new BaseCallback<DateBaseBean>() {
                    @Override
                    public void onRequestBefore() {

                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        canceloadDialog();
                        ToastUtils.showLongToast("请求超时");
                    }

                    @Override
                    public void onSuccess(Response response, DateBaseBean o) {
                        canceloadDialog();
                        if (o != null && (o.code == 0 || o.code == 200)) {
                            ToastUtils.showShortToast("注册成功");
                            EventBus.getDefault().post(new MessageEvent(MessageEvent.REGIST_OK,mPhone,mPwd));
                            finish();
                        } else {
                                /*if (!TextUtils.isEmpty(o.getMsg())) {
                                    ToastUtils.showShortToast(o.getMsg());
                                }*/
                            ToastUtils.showShortToast("服务异常");
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
     * 隐藏加载进度对话框
     */
    public void canceloadDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == 111) {
            finish();
        }
    }*/
}
