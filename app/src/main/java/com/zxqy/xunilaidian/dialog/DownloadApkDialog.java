package com.zxqy.xunilaidian.dialog;

import android.Manifest;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;


import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.base.BaseActivity;
import com.zxqy.xunilaidian.bean.UpdateBean;
import com.zxqy.xunilaidian.utils.DeviceUtils;
import com.zxqy.xunilaidian.utils.DownLoadUtils;
import com.zxqy.xunilaidian.utils.HttpUrl;
import com.zxqy.xunilaidian.utils.NumberProgressBar;
import com.zxqy.xunilaidian.utils.PermissionUtils;
import com.zxqy.xunilaidian.utils.ToastUtils;
import com.zxqy.xunilaidian.utils.listener.OnDialogClickListener;

import java.io.File;

/**
 * 版本更新  弹框提示
 */
public class DownloadApkDialog extends BaseDialog {
    private ImageView mCancelImage, mDownLoadImage;
    private TextView mDownLoadText, mUpdateInfoText, mCancelText, mVersionText;
    private NumberProgressBar mNumberProgressBar;
    private UpdateBean.UpdateDataBean mGetNewBean;
    private Context mContext;
    private File file;
    private int currentProgress;
    private String mAuthority;
    private static BaseActivity baseNewActivity;
    private static final int RESULT_ACTION_USAGE_ACCESS_SETTINGS = 10086;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1002:
                    ToastUtils.showShortToast("下载完成");
                    if (DownloadApkDialog.this != null) {
                        DownloadApkDialog.this.dismiss();
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
                        if (!TextUtils.isEmpty(mGetNewBean.versionUrl)) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(mGetNewBean.versionUrl));
                            mContext.startActivity(intent);
                        }
                    }
                    break;
                case 1003:
                    if (mNumberProgressBar != null) {
                        mNumberProgressBar.setProgress(currentProgress);
                    }
                    break;
                case 1004:
                    ToastUtils.showShortToast("下载失败，打开浏览器进行下载更新");
                    if (DownloadApkDialog.this != null) {
                        DownloadApkDialog.this.dismiss();
                    }
                    break;
            }
        }
    };

    public DownloadApkDialog(@NonNull Context context, UpdateBean.UpdateDataBean bean, String authority ) {
        super(context);
        this.mContext = context;
        this.baseNewActivity = (BaseActivity) context;
        this.mGetNewBean = bean;
        this.mAuthority = authority;
        setCancelable(false);
    }

    public DownloadApkDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected float setWidthScale() {
        return 0.9f;
    }

    @Override
    protected AnimatorSet setEnterAnim() {
        return null;
    }

    @Override
    protected AnimatorSet setExitAnim() {
        return null;
    }

    @Override
    protected void init() {
        mCancelImage = findViewById(R.id.iv_cancel);
        mCancelText = findViewById(R.id.tv_cancel);
        mDownLoadText = findViewById(R.id.tv_update);
        mDownLoadImage = findViewById(R.id.iv_update);
        mUpdateInfoText = findViewById(R.id.tv_update_info);
        mVersionText = findViewById(R.id.tv_version);
        mNumberProgressBar = findViewById(R.id.number_progressBar);



        if (mGetNewBean != null) {
            //        首先判断是否强制更新  如果是强制更新就隐藏取消按钮
            if (mGetNewBean.isForceUpdate) {//是强制更新
                mCancelText.setVisibility(View.GONE);
            } else {//不是强制更新就判断是否要弹框，即显示还是隐藏更新
                mCancelText.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(mGetNewBean.versionIntro)) {
                mUpdateInfoText.setText(mGetNewBean.versionIntro);
            } else {
                mUpdateInfoText.setText("优化了多处功能，操作更流畅，服务更稳定，快来更新吧！");
            }
            if (!TextUtils.isEmpty(mGetNewBean.versionCode)) {
                mVersionText.setVisibility(View.VISIBLE);
                mVersionText.setText(mGetNewBean.versionCode);
            } else {
                mVersionText.setVisibility(View.GONE);
            }
        } else {
            mVersionText.setText("v3.0.0");
            mUpdateInfoText.setText("优化了多处功能，操作更流畅，服务更稳定，快来更新吧！");
        }
        mCancelText.setOnClickListener(v -> {
            if (mDownLoadText.getVisibility() == View.GONE && mDownLoadImage.getVisibility() == View.GONE) {
                ToastUtils.showShortToast("正在下载更新中，无法关闭");
            } else {
                dismiss();
            }
        });
        mCancelImage.setOnClickListener(v -> {
            if (mDownLoadText.getVisibility() == View.GONE && mDownLoadImage.getVisibility() == View.GONE) {
                ToastUtils.showShortToast("正在下载更新中，无法关闭");
            } else {
                dismiss();
            }
        });
        mDownLoadImage.setOnClickListener(v -> updateApp());
        mDownLoadText.setOnClickListener(v -> updateApp());
    }

    /**
     * 更新应用
     */
    private void updateApp() {
        if (!isGetPermissions()) {//检测权限是否获取
            ShowTipDialog("温馨提示",
                    "需要开启文件读写权限才能完成更新操作哦!",
                    "确定", new OnDialogClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void OnDialogOK() {
//                            baseNewActivity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, RESULT_ACTION_USAGE_ACCESS_SETTINGS);
                            Intent mIntent = new Intent();
                            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                            mIntent.setData(Uri.fromParts(
                                    "package", baseNewActivity.getPackageName(), null));
                            baseNewActivity.startActivityForResult(mIntent, RESULT_ACTION_USAGE_ACCESS_SETTINGS);
                        }

                        @Override
                        public void OnDialogExit() {

                        }
                    });
        } else {
            //检查网络是否可用
            if (DeviceUtils.isNetworkConnected(mContext)) {
                if (TextUtils.isEmpty(mGetNewBean.versionUrl)
//                        || !mGetNewBean.data.data.versionUrl.startsWith("http")
                ) {
                    ToastUtils.showLongToast("无效的资源链接！");
                    return;
                }
                mDownLoadText.setVisibility(View.GONE);
                mDownLoadImage.setVisibility(View.GONE);
                mCancelText.setVisibility(View.GONE);
                mNumberProgressBar.setVisibility(View.VISIBLE);

                String dowUrl = mGetNewBean.versionUrl;
                if (!mGetNewBean.versionUrl.startsWith("http")) {
                    dowUrl = HttpUrl.FILE_BASE_UPDATE + mGetNewBean.versionUrl;
                }
                //下载更新包
                DownLoadUtils.get().download(mContext,dowUrl, "Update", new DownLoadUtils.OnDownloadListener() {
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
                        currentProgress = progress;
                    }

                    @Override
                    public void onDownloadFailed() {
                        //下载失败
                        handler.sendEmptyMessage(1004);
                        if (!TextUtils.isEmpty(mGetNewBean.versionUrl)) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(mGetNewBean.versionUrl));
                            mContext.startActivity(intent);
                        }
                    }
                });
            } else {
                ToastUtils.showShortToast("当前网络不可用，请检查网络");
            }
        }


    }

    /**
     * 提示弹框
     */
    public static void ShowTipDialog(String name, String content, String btnText, OnDialogClickListener listener) {
        int[] ids = new int[]{
                R.id.dialog_bt_dis,
                R.id.dialog_bt_ok
        };
        CenterDialog dialog = new CenterDialog(baseNewActivity, R.layout.dialog_show_tips, ids, false);
        dialog.setOnCenterClickListener((dial, view) -> {
            if (view.getId() == R.id.dialog_bt_ok) {
                listener.OnDialogOK();
                if (!baseNewActivity.isFinishing()) {
                    dialog.dismiss();
                }
            }
            if (view.getId() == R.id.dialog_bt_dis) {
                if (!baseNewActivity.isFinishing()) {
                    dialog.dismiss();
                }
                listener.OnDialogExit();
            }
        });
        if (!baseNewActivity.isFinishing()) {
            dialog.show();
            dialog.setText(R.id.dialog_tv_title, name);
            dialog.setText(R.id.dialog_tv_text, content);
            dialog.setText(R.id.dialog_bt_ok, btnText);
        }
    }

    /**
     * 检测权限是否已获取
     */
    private static boolean isGetPermissions() {
//        String[] p = Build.VERSION.SDK_INT <= 28 ? getCalenderPermissions() : getCalenderPermissions();
        String[] p = getPermissions();
        for (String str : p) {
            if (!PermissionUtils.checkPermission(baseNewActivity, str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取读写权限
     */
    public static String[] getPermissions() {
        return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    protected int getContentViewId() {
        return R.layout.dialog_download;
    }

}
