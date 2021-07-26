package com.zxqy.xunilaidian.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.core.content.FileProvider;


import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.bean.ResultBean;
import com.zxqy.xunilaidian.bean.UpdateBean;
import com.zxqy.xunilaidian.dialog.CenterDialog;
import com.zxqy.xunilaidian.dialog.DownloadApkDialog;
import com.zxqy.xunilaidian.dialog.LogoutDialog;
import com.zxqy.xunilaidian.utils.httputil.callback.BaseCallback;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lx on 2017/7/25.
 * 用户注销工具
 */

public class DestroyAndUpdateUtil {


    private static CenterDialog myDialog;
    private static TimeCount mTimeCount;

    private static Context mContext;
    private static ProgressDialog progressDialog;
    private static String mKey;
    private static boolean isMain;
    private static File file;
    private static String downloadUrl, mAuthority;//资源下载链接
    private static UpdateBean.UpdateDataBean updateDataBean;
//    ==========================================================版本更新开始======================================================================

    //版本更新提醒
    public static void checkNews(boolean ismain, Context context) {
        mContext = context;
        isMain = ismain;
        //版本更新提醒
        HttpUtils.getInstance().postNews(new BaseCallback<UpdateBean>() {
            @Override
            public void onRequestBefore() {
            }

            @Override
            public void onFailure(Request request, Exception e) {
                ToastUtils.showLongToast("请求失败，请重试");
            }
            @Override
            public void onSuccess(Response response, UpdateBean o) {
                if (response.isSuccessful()) {
                    if (o != null && o.code == 0 && o.data != null && !TextUtils.isEmpty(o.data.versionUrl)) {
//                    if (o != null && o.isIssucc()) {
//                        DownloadApkDialog dialog = new DownloadApkDialog(context, o,
//                                BuildConfig.APPLICATION_ID + ".TTFileProvider");
                        updateDataBean = o.data;
//                        updateDataBean.isForceUpdate = false;//模拟强制更新
//                        updateDataBean.type = 2;//模拟强制更新
                        downloadUrl = updateDataBean.versionUrl;
                        mAuthority = context.getPackageName() + ".fileprovider";
                        //        首先判断是否强制更新  如果是强制更新就隐藏取消按钮
                        if (isMain && !updateDataBean.isForceUpdate && updateDataBean.type == 2) {//首页 不是强制更新  隐藏更新
                            updateApp();
                        } else {//不是强制更新就判断是否要弹框，即显示还是隐藏更新
                            DownloadApkDialog dialog = new DownloadApkDialog(context, updateDataBean,
                                    mAuthority);
                            dialog.show();
                        }


                    } else {
                        if (!isMain) {
                            ToastUtils.showShortToast("当前已是最新版本!");
                        }
                    }
                }
            }

            @Override
            public void onError(Response response, int errorCode, Exception e, String erroMsg) {
                if (!TextUtils.isEmpty(erroMsg)) {
                    ToastUtils.showLongToast(erroMsg);
                }
            }
        });
    }

    /**
     * 直接
     * 更新应用
     */
    private static void updateApp() {
        //检查网络是否可用
        if (DeviceUtils.isNetworkConnected(mContext)) {
            if (TextUtils.isEmpty(downloadUrl)
//                        || !mGetNewBean.data.data.versionUrl.startsWith("http")
                    && updateDataBean.type != 2
            ) {
                ToastUtils.showLongToast("无效的资源链接！");
                return;
            }

            if (!downloadUrl.startsWith("http")) {
                downloadUrl = HttpUrl.FILE_BASE_URI + downloadUrl;
            }
            //下载更新包
            DownLoadUtils.get().download(mContext,downloadUrl, "Update", new DownLoadUtils.OnDownloadListener() {
                @Override
                public void onDownloadSuccess(File files) {
                    //下载成功
                    file = files;
                    handler.sendEmptyMessage(1002);
                }

                @Override
                public void onDownloading(int progress) {
                    //在下载
                    handler.sendEmptyMessage(1003);
//                        currentProgress = progress;
                }

                @Override
                public void onDownloadFailed() {
                    //下载失败
                    handler.sendEmptyMessage(1004);
                    if (!TextUtils.isEmpty(downloadUrl)) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(downloadUrl));
                        mContext.startActivity(intent);
                    }
                }
            });
        } else {
            ToastUtils.showShortToast("当前网络不可用，请检查网络");
        }
    }

    static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1002:
                    if (updateDataBean.type != 2) {
                        ToastUtils.showShortToast("下载完成");
                    }

                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.putExtra("name", "");
                        intent.addCategory("android.intent.category.DEFAULT");
                        Uri data;
                        if (Build.VERSION.SDK_INT >= 24) {
                            // 临时允许
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            data = FileProvider.getUriForFile(mContext, mAuthority, file);
                        } else {
                            data = Uri.fromFile(file);
                        }
                        intent.setDataAndType(data, "application/vnd.android.package-archive");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    } catch (Exception e) {
                        Log.e("安装失败", e.toString());
                        if (!TextUtils.isEmpty(downloadUrl)) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(downloadUrl));
                            mContext.startActivity(intent);
                        }
                    }
                    break;
                case 1003:
//                    if (mNumberProgressBar != null) {
//                        mNumberProgressBar.setProgress(currentProgress);
//                    }
                    break;
                case 1004:
                    if (updateDataBean.type != 2) {
                        ToastUtils.showShortToast("下载失败，打开浏览器进行下载更新");
                    }

//                    if (DownloadApkDialog.this != null) {
//                        DownloadApkDialog.this.dismiss();
//                    }
                    break;
            }
        }
    };
//    ========================版本更新结束======================================================================


    //    ========================注销流程开始======================================================================

    /**
     * 注销流程1
     */
    public static void logout(Context context) {
        mContext = context;
        int[] ids = new int[]{R.id.tv_cancel, R.id.tv_next};
        myDialog = new CenterDialog(context, R.layout.logout_1, ids, false);

        myDialog.setOnCenterClickListener((dialog1, view) -> {
            if (view.getId() == R.id.tv_cancel) {
                // 取消
                myDialog.dismiss();
            } else if (view.getId() == R.id.tv_next) {
                // 下一步
                logout2();
//                goLogOut(tel,code,key);
                myDialog.dismiss();
            }
        });
        myDialog.show();
        String content = "\u3000\u3000" + "尊敬的用户您好：注销账号后应用内" +
                "<font color='red'>所有的个人数据将会被清空</font>" +
                "，包括付费获得的会员等一切数据，且" +
                "<font color='red'>无法恢复</font>" +
                "，请您谨慎操作。如因注销账号造成一切损失，我们" +
                "<font color='red'>不承担任何责任</font>" +
                "，您" +
                "<font color='red'>要继续注销账号</font>" +
                "吗？";
        TextView tv = myDialog.getTextView(R.id.tv_hint);
        TextView tv_next = myDialog.getTextView(R.id.tv_next);
        tv.setText(Html.fromHtml(content));
        tv_next.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_next.getPaint().setAntiAlias(true);//抗锯齿
    }

    /**
     * 注销流程2
     */
    private static void logout2() {
//        String mobile = SpUtils.getInstance().getString(USER_NAME);
        String dlName = SpUtils.getInstance().getString(Constants.USER_PHONE);
     /*   if (TextUtils.isEmpty(dlName)) {
            ToastUtils.showShortToast("用户信息为空请先登录！");
            return;
        }*/
        int[] ids = new int[]{R.id.tv_next, R.id.tv_cancel, R.id.et_tel, R.id.et_code, R.id.tv_code};
        LogoutDialog dialog = new LogoutDialog(mContext, R.layout.logout_2, ids, true);
        dialog.setOnCenterClickListener((dialog1, view) -> {

            if (view.getId() == R.id.tv_code) {
                String tel = dialog.getEditText(R.id.et_tel);
                Log.e("注销", "dlName==" + dlName);
                Log.e("注销", "tel==" + tel);
                if (TextUtils.isEmpty(tel)) {
                    ToastUtils.showShortToast("请输入手机号");
                    return;
                }

                if (!DeviceUtils.isMobileNO(tel)) {
                    ToastUtils.showShortToast("请输入正确的手机号");
                    return;
                }
                if (!tel.contains(dlName)) {
                    ToastUtils.showShortToast("请输入本机已登录的手机号");
                    return;
                }


                HttpUtils.getInstance().getVarCode(tel, "", "",
                        new BaseCallback<ResultBean>() {
                            @Override
                            public void onRequestBefore() {
                            }

                            @Override
                            public void onFailure(Request request, Exception e) {
                            }

                            @Override
                            public void onSuccess(Response response, ResultBean o) {
                                if (o != null && o.issucc) {
                                    if (mTimeCount == null) {
                                        mTimeCount = new TimeCount(dialog, 59 * 1000, 1000);
                                        mTimeCount.start();
                                    }
                                    // 倒计时开启
                                    dialog.setEnabled(R.id.tv_code, false);
                                    mKey = o.code;
                                    ToastUtils.showShortToast("验证码发送成功");
                                } else {
                                    if (o != null && !TextUtils.isEmpty(o.msg)) {
                                        ToastUtils.showShortToast(o.msg);
                                    }
                                }
                            }

                            @Override
                            public void onError(Response response, int errorCode, Exception e, String erroMsg) {
                                if (!TextUtils.isEmpty(erroMsg)) {
                                    ToastUtils.showLongToast(erroMsg);
                                }
                            }
                        });
            } else if (view.getId() == R.id.tv_cancel) {
                // 取消
                dialog.dismiss();
            } else if (view.getId() == R.id.tv_next) {
//                logout3("", "", "");
                // 下一步
                String tel = dialog.getEditText(R.id.et_tel);
                String code = dialog.getEditText(R.id.et_code);
                if (TextUtils.isEmpty(tel)) {
                    ToastUtils.showShortToast("请输入手机号");
                    return;
                }
                if (!DeviceUtils.isMobileNO(tel)) {
                    ToastUtils.showShortToast("请输入正确的手机号");
                    return;
                }
                if (!tel.contains(dlName)) {
                    ToastUtils.showShortToast("请输入本机已登录的手机号");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    ToastUtils.showShortToast("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(mKey)) {
                    ToastUtils.showShortToast("验证码无效");
                    return;
                }
                logout3(tel, code, mKey);
//                logout1();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 注销流程3
     */
    private static void logout3(String tel, String code, String key) {
        int[] ids = new int[]{R.id.tv_next, R.id.tv_cancel};
//        MyCenterDialog dialog = new MyCenterDialog(mContext, R.layout.logout_3, ids, false);
        CenterDialog dialog = new CenterDialog(mContext, R.layout.logout_1, ids, false);
        dialog.setOnCenterClickListener((dialog1, view) -> {
            if (view.getId() == R.id.tv_cancel) {
                // 取消
                dialog.dismiss();
            } else if (view.getId() == R.id.tv_next) {
//                ToastUtils.showShortToast("开始注销");
                goLogOut(tel, code, key);
            }
        });
        dialog.show();
        String content = "\u3000\u3000" + "注销后，应用" +
                "<font color='red'>数据全部删除</font>" +
                "，如需继续使用请重新登录，登录后会自动创建一个" +
                "<font color='red'>全新的账号</font>" +
                "，您" +
                "<font color='red'>确定要注销</font>" +
                "吗？";
        TextView tv = dialog.getTextView(R.id.tv_hint);
        tv.setText(Html.fromHtml(content));
    }

    private static void goLogOut(String tel, String code, String key) {
        // 确认注销
        HttpHelper.loginOut(tel, code, key, new BaseCallback<ResultBean>() {
            @Override
            public void onRequestBefore() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage("注销账号中...");
                progressDialog.show();
            }

            @Override
            public void onFailure(Request request, Exception e) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }

            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                if (resultBean != null && resultBean.issucc) {
                    // 注销账号
                    // 这里需要清空缓存数据
//                    Utils.setLoginInfo("", "", "");
//                    SpUtils.getInstance().putString(com.jtjsb.storycollect.Utils.Utils.USER_NAME, "");
//                    SpUtils.getInstance().putBoolean(com.jtjsb.storycollect.Utils.Utils.USER_NAME, false);
//                    SpUtils.getInstance().putString(com.jtjsb.storycollect.Utils.Utils.PHONE_NUMBER, "");
//                            SQLdaoManager.deleteAll();

                    HttpUtils.getInstance().postRegister(new BaseCallback<ResultBean>() {
                        @Override
                        public void onRequestBefore() {
                        }

                        @Override
                        public void onFailure(Request request, Exception e) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                        }

                        @Override
                        public void onSuccess(Response response, ResultBean o) {
                            if (o != null) {
                                if (o.issucc) {
                                    // 注册成功，调取App数据接口
                                    HttpUtils.getInstance().postUpdate(new BaseCallback<UpdateBean>() {
                                        @Override
                                        public void onRequestBefore() {
                                        }

                                        @Override
                                        public void onFailure(Request request, Exception e) {
                                            if (progressDialog != null && progressDialog.isShowing()) {
                                                progressDialog.dismiss();
                                                progressDialog = null;
                                            }
                                        }

                                        @Override
                                        public void onSuccess(Response response, UpdateBean o) {
                                            if (o != null && o.code == 200) {
                                                ToastUtils.showShortToast("账号注销成功！");
                                                //清空数据
//                                                SpDefine.setUserInfo(null);
                                                SpUtils.getInstance().putString(Constants.APP_TOKEN, "");
                                                SpUtils.getInstance().putString(Constants.USER_PHONE, "");
                                                SpUtils.getInstance().putBoolean(Constants.IS_LOGIN, false);
                                                SpUtils.getInstance().putString(Constants.USER_ID, "");
                                                SpUtils.getInstance().putString(Constants.ID_APP, "");
                                                // 这里要通知刷新页面数据
                                                EventBus.getDefault().post(new MessageEvent(MessageEvent.UPDATE_LOG_INFO));
                                                myDialog.dismiss();
                                                if (progressDialog != null && progressDialog.isShowing()) {
                                                    progressDialog.dismiss();
                                                    progressDialog = null;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onError(Response response, int errorCode, Exception e, String erroMsg) {
                                            if (progressDialog != null && progressDialog.isShowing()) {
                                                progressDialog.dismiss();
                                                progressDialog = null;
                                            }
                                            if (!TextUtils.isEmpty(erroMsg)) {
                                                ToastUtils.showLongToast(erroMsg);
                                            }
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onError(Response response, int errorCode, Exception e, String erroMsg) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            if (!TextUtils.isEmpty(erroMsg)) {
                                ToastUtils.showLongToast(erroMsg);
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Response response, int errorCode, Exception e, String erroMsg) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                if (!TextUtils.isEmpty(erroMsg)) {
                    ToastUtils.showLongToast(erroMsg);
                }
            }
        });
    }

    /**
     * 倒计时
     */
    static class TimeCount extends CountDownTimer {
        private LogoutDialog logoutDialog;

        public TimeCount(LogoutDialog dialog, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.logoutDialog = dialog;
        }

        @Override
        public void onFinish() {
            logoutDialog.setText(R.id.tv_code, "获取验证码");
            logoutDialog.setEnabled(R.id.tv_code, true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            logoutDialog.setText(R.id.tv_code, millisUntilFinished / 1000 + "s");
        }
    }


    //    ========================注销流程结束======================================================================

}
