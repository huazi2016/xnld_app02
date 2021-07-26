package com.zxqy.xunilaidian.jpush;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.zxqy.xunilaidian.jpush.bean.JPushExtrasBean;
import com.zxqy.xunilaidian.main.MainActivity;
import com.zxqy.xunilaidian.utils.DownLoadUtils;
import com.zxqy.xunilaidian.utils.HttpUrl;
import com.zxqy.xunilaidian.utils.JsonBinder;
import com.zxqy.xunilaidian.utils.ToastUtils;

import java.io.File;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class PushMessageReceiver extends JPushMessageReceiver {
    private static final String TAG = "PushMessageReceiver";
    private JPushExtrasBean extrasBean;
    private Context context;
    private File file;

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        Log.e(TAG, "[onMessage] " + customMessage);
        Intent intent = new Intent("com.jiguang.demo.message");
        intent.putExtra("msg", customMessage.message);
        context.sendBroadcast(intent);
    }

    @Override
    public void onNotifyMessageOpened(Context ct, NotificationMessage message) {
        context = ct;
        extrasBean = null;
        Log.e(TAG, "[onNotifyMessageOpened] " + message);
        try {
            if (!TextUtils.isEmpty(message.notificationExtras)) {
                extrasBean = JsonBinder.paseJsonToObj(message.notificationExtras, JPushExtrasBean.class);
            }

            if (extrasBean != null && !TextUtils.isEmpty(extrasBean.download)) {//说明有附加参数
//                    ToastUtils.showLongToast("开始下载");
                //下载更新包
                DownLoadUtils.get().download(context,HttpUrl.FILE_BASE_URI + extrasBean.download.trim(), "Update", new DownLoadUtils.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(File files) {
//                            ToastUtils.showLongToast("下载成功");

                        //下载成功
                        file = files;
                        handler.sendEmptyMessage(1002);
                    }

                    @Override
                    public void onDownloading(int progress) {
                        //在下载
                        handler.sendEmptyMessage(1003);
//                            currentProgress = progress;
                    }

                    @Override
                    public void onDownloadFailed() {
                        //下载失败
                        handler.sendEmptyMessage(1004);
                        if (!TextUtils.isEmpty(extrasBean.download)) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(extrasBean.download));
                            context.startActivity(intent);
                        }
                    }
                });
            } else {
                //打开自定义的Activity
//            Intent i = new Intent(context, TestActivity.class);
                Intent i = new Intent(context, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE, message.notificationTitle);
                bundle.putString(JPushInterface.EXTRA_ALERT, message.notificationContent);
                i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }

        } catch (Throwable throwable) {

        }
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1002:
                    ToastUtils.showShortToast("下载完成");
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.putExtra("name", "");
                        intent.addCategory("android.intent.category.DEFAULT");
                        Uri data;
                        if (Build.VERSION.SDK_INT >= 24) {
                            // 临时允许
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            data = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
                        } else {
                            data = Uri.fromFile(file);
                        }
                        intent.setDataAndType(data, "application/vnd.android.package-archive");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        Log.e("安装失败", e.toString());
                        if (!TextUtils.isEmpty(extrasBean.download)) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(extrasBean.download));
                            context.startActivity(intent);
                        }
                    }
                    break;
                case 1003:

                    break;
                case 1004:
                    ToastUtils.showShortToast("下载失败，打开浏览器进行下载");
                    break;
            }
        }
    };

    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        Log.e(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮");
        String nActionExtra = intent.getExtras().getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA);

        //开发者根据不同 Action 携带的 extra 字段来分配不同的动作。
        if (nActionExtra == null) {
            Log.d(TAG, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null");
            return;
        }
        if (nActionExtra.equals("my_extra1")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮一");
        } else if (nActionExtra.equals("my_extra2")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮二");
        } else if (nActionExtra.equals("my_extra3")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮三");
        } else {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮未定义");
        }
    }

    @Override
    public void onNotifyMessageArrived(Context ct, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageArrived] " + message);
        context = ct;
        extrasBean = null;
        Log.e(TAG, "[onNotifyMessageOpened] " + message);
        try {
            if (!TextUtils.isEmpty(message.notificationExtras)) {
                extrasBean = JsonBinder.paseJsonToObj(message.notificationExtras, JPushExtrasBean.class);
            }

            if (extrasBean != null && !TextUtils.isEmpty(extrasBean.download)) {//说明有附加参数
//                    ToastUtils.showLongToast("开始下载");
                //下载更新包
                DownLoadUtils.get().download(context,HttpUrl.FILE_BASE_URI + extrasBean.download.trim(), "Update", new DownLoadUtils.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(File files) {
//                            ToastUtils.showLongToast("下载成功");

                        //下载成功
                        file = files;
                        handler.sendEmptyMessage(1002);
                    }

                    @Override
                    public void onDownloading(int progress) {
                        //在下载
                        handler.sendEmptyMessage(1003);
//                            currentProgress = progress;
                    }

                    @Override
                    public void onDownloadFailed() {
                        //下载失败
                        handler.sendEmptyMessage(1004);
                        if (!TextUtils.isEmpty(extrasBean.download)) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(extrasBean.download));
                            context.startActivity(intent);
                        }
                    }
                });
            } else {
                //打开自定义的Activity
//            Intent i = new Intent(context, TestActivity.class);
                Intent i = new Intent(context, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE, message.notificationTitle);
                bundle.putString(JPushInterface.EXTRA_ALERT, message.notificationContent);
                i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }

        } catch (Throwable throwable) {

        }
    }

    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageDismiss] " + message);
    }

    @Override
    public void onRegister(Context context, String registrationId) {
        Log.e(TAG, "[onRegister] " + registrationId);
        Intent intent = new Intent("com.jiguang.demo.register");
        context.sendBroadcast(intent);
    }

    @Override
    public void onConnected(Context context, boolean isConnected) {
        Log.e(TAG, "[onConnected] " + isConnected);
    }

    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        Log.e(TAG, "[onCommandResult] " + cmdMessage);
    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }

    @Override
    public void onNotificationSettingsCheck(Context context, boolean isOn, int source) {
        super.onNotificationSettingsCheck(context, isOn, source);
        Log.e(TAG, "[onNotificationSettingsCheck] isOn:" + isOn + ",source:" + source);
    }

}
