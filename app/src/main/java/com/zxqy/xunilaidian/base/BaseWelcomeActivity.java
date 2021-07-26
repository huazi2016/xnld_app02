package com.zxqy.xunilaidian.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.entity.AppConfigInfoEntity;
import com.zxqy.xunilaidian.utils.Constants;
import com.zxqy.xunilaidian.utils.DeviceUtils;
import com.zxqy.xunilaidian.utils.HttpUtils;
import com.zxqy.xunilaidian.utils.SpDefine;
import com.zxqy.xunilaidian.utils.SpUtils;
import com.zxqy.xunilaidian.utils.httputil.callback.BaseCallback;
import com.zxqy.xunilaidian.utils.PermissionUtils;
import com.zxqy.xunilaidian.utils.ToastUtils;
import com.zxqy.xunilaidian.utils.listener.OnDialogClickListener;
import com.zxqy.xunilaidian.view.MyDialog;

import okhttp3.Request;
import okhttp3.Response;


public abstract class BaseWelcomeActivity extends BaseGTActivity {
    protected Context mContext;
    protected BaseWelcomeActivity mActivity;

    private int RESULT_ACTION_USAGE_ACCESS_SETTINGS = 1;
    public static final int RESULT_ACTION_SETTING = 1;
    private boolean isFirstRegister; // 是否首次注册

    /**
     * 获取读写权限
     */
    public String[] getPermissions28() {
        return new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
        };
    }

    /**
     * 获取读写权限
     */
    public String[] getPermissions() {
        return new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
        };
    }

    private String[] Permissions;

    /**
     * 资源id
     */
    protected abstract int getLayoutId();

    /**
     * 逻辑跳转(例如跳转到首页)
     */
    protected abstract void jumpToNext();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mActivity = this;
        setContentView(getLayoutId());
    }

    /**
     * 初始化数据
     */
    public void initData() {
//        MyApplication.getInstance().initUM();

        isFirstRegister = SpUtils.getInstance().getBoolean(Constants.FIRST_REGISTER, true);
        Permissions = Build.VERSION.SDK_INT <= 28 ? getPermissions28() : getPermissions();
        PermissionUtils.checkAndRequestMorePermissions(mActivity, Permissions,
                RESULT_ACTION_USAGE_ACCESS_SETTINGS, this::bindDevice);
    }

    /***
     * 绑定device数据
     */
    private void bindDevice() {
        if (TextUtils.isEmpty(DeviceUtils.getSpDeviceId()) && TextUtils.isEmpty(DeviceUtils.readDeviceID(mContext))) {
            // app和sd卡都没有，都存一份
            String imei = DeviceUtils.getIMEI(this);
            if (TextUtils.isEmpty(imei) || imei.equals("")) {
                imei = DeviceUtils.getUUID();
            }
            DeviceUtils.saveDeviceID(imei, mContext);
            DeviceUtils.putSpDeviceId(imei);
        } else if (TextUtils.isEmpty(DeviceUtils.getSpDeviceId()) && !TextUtils.isEmpty(DeviceUtils.readDeviceID(mContext))) {
            // sd卡有，app没有，则存一份到app
            DeviceUtils.putSpDeviceId(DeviceUtils.readDeviceID(mContext));
        } else {
            // app有，sd卡没有，则存一份到sd卡
            DeviceUtils.saveDeviceID(DeviceUtils.getSpDeviceId(), mContext);
        }
//        registerId();
    }

    /**
     * 获取App配置信息
     */
    public void getAppConfigInfo() {
        if (DeviceUtils.isNetworkConnected(this)) {
            HttpUtils.getInstance().getAppConfigInfo(new BaseCallback<AppConfigInfoEntity>() {
                @Override
                public void onRequestBefore() {

                }
                @Override
                public void onFailure(Request request, Exception e) {
                    showRestartDialog();
                }

                @Override
                public void onSuccess(Response response, AppConfigInfoEntity result) {
                    if (result.code == 0 || result.code == 200) {
                        SpDefine.setAppConfigInfo(result.data.configs);
                        SpDefine.setAppConVipfigInfo(result.data);
                        // 数据获取成功跳转到下个页面
//                        getAliOssData();
                        jumpToNext();
                    } else {
                        // 更新失败，弹出提示
                        if (!TextUtils.isEmpty(result.msg)) {
                            ToastUtils.showShortToast(result.msg);
                        }
                    }
                }

                @Override
                public void onError(Response response, int errorCode, Exception e, String erroStr) {
                    //                    showRestartDialog();
                    if (!TextUtils.isEmpty(erroStr)) {
                        ToastUtils.showShortToast(erroStr);
                    }

                }
            });
        } else {
//            showRestartDialog();
            ToastUtils.showShortToast("当前网络不可用，请检查网络");
        }
    }

    /**
     * 注册设备id
     */
    private void registerId() {
       /* if (isFirstRegister) {
            if (Utils.isNetworkAvailable(this)) {
                HttpUtils.getInstance().postRegister(new BaseCallback<ResultBean>() {
                    @Override
                    public void onRequestBefore() {
                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        showRestartDialog();
                    }

                    @Override
                    public void onSuccess(Response response, ResultBean o) {
                        if (o != null) {
                            if (o.isIssucc()) {
                                // 注册成功，调取App数据接口
                                getUpdateInfo();
                                SpUtils.getInstance().putBoolean(Contants.FIRST_REGISTER, false);
                            } else {
                                // 注册失败，弹出提示
                                if (!TextUtils.isEmpty(o.getMsg())) {
                                    ToastUtils.showShortToast(o.getMsg());
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e) {
                        showRestartDialog();
                    }
                });
            } else {
                showRestartDialog();
            }
        } else {
            getUpdateInfo();
        }*/
    }

    /**
     * 获取App数据信息
     */
    public void getUpdateInfo() {
      /*  UpdateBean updateBean = DataSaveUtils.getInstance().getUpdate();
        if (Utils.isNetworkAvailable(this)) {
            HttpUtils.getInstance().postUpdate(new BaseCallback<UpdateBean>() {
                @Override
                public void onRequestBefore() {
                }

                @Override
                public void onFailure(Request request, Exception e) {
                    if (updateBean != null) {
                        getAliOssData();
                        jumpToNext();
                        return;
                    }
                    showRestartDialog();
                }

                @Override
                public void onSuccess(Response response, UpdateBean o) {
                    if (o != null && o.getIssucc()) {
                        // 数据获取成功跳转到下个页面
                        getAliOssData();
                        jumpToNext();
                    } else {
                        // 更新失败，弹出提示
                        if (!TextUtils.isEmpty(o.getMsg())) {
                            ToastUtils.showShortToast(o.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Response response, int errorCode, Exception e) {
                    if (updateBean != null) {
                        getAliOssData();
                        jumpToNext();
                        return;
                    }
                    showRestartDialog();
                }
            });
        } else {
            if (updateBean != null) {
                getAliOssData();
                jumpToNext();
                return;
            }
            showRestartDialog();
        }
        checkLogin();*/
    }

    /**
     * 调用更新接口
     */
    public void update() {

    }

    /**
     * 校验登陆
     */
    private void checkLogin() {
       /* if (Utils.isNetworkAvailable(this)) {
            if (!TextUtils.isEmpty(Utils.getUserId())) {
                // 登录过
                HttpUtils.getInstance().checkLogin(new BaseCallback<ResultBean>() {
                    @Override
                    public void onRequestBefore() {
                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                    }

                    @Override
                    public void onSuccess(Response response, ResultBean o) {
                        if (o != null) {
                            if (o.isIssucc()) {
                                Log.e("校验登录:", "已经登录过");
                            } else {
                                if (!TextUtils.isEmpty(o.getMsg())) {
                                    ToastUtils.showShortToast(o.getMsg());
                                }
                                Log.e("校验登录:", "已在别机登录，本机下线");
                            }
                        }
                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e) {
                    }
                });
            }
        }*/
    }

    /**
     * 获取阿里oss数据
     */
    private void getAliOssData() {
/*        String ossData = SpUtils.getInstance().getString(Contants.ALI_OSS_PARAM);
        // 若已经获取过阿里oss数据，则无需再获取
        if (!TextUtils.isEmpty(ossData) && !ossData.equals("null")) return;
        HttpUtils.getInstance().getAliOss(new BaseCallback<ResultBean>() {
            @Override
            public void onRequestBefore() {
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }

            @Override
            public void onSuccess(Response response, ResultBean o) {
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
            }
        });*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RESULT_ACTION_USAGE_ACCESS_SETTINGS) {
            PermissionUtils.onRequestMorePermissionsResult(mContext, permissions,
                    new PermissionUtils.PermissionCheckCallBack() {
                        @Override
                        public void onHasPermission() {
                            bindDevice();
                        }

                        @Override
                        public void onUserHasAlreadyTurnedDown(String... permission) {
                            ShowTipDialog("温馨提示",
                                    "授予权限才能正常使用本软件哦，点击确定继续授权，若不同意将退出应用",
                                    "确定", new OnDialogClickListener() {
                                        @Override
                                        public void OnDialogOK() {
                                            PermissionUtils.requestMorePermissions(mActivity,
                                                    permissions, RESULT_ACTION_USAGE_ACCESS_SETTINGS);
                                        }

                                        @Override
                                        public void OnDialogExit() {
                                            // 退出APP
                                            finish();
                                        }
                                    });
                        }

                        @Override
                        public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                            ShowTipDialog("温馨提示",
                                    "授予权限才能正常使用本软件哦，请到设置中允许权限",
                                    "确定", new OnDialogClickListener() {
                                        @Override
                                        public void OnDialogOK() {
                                            Intent mIntent = new Intent();
                                            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                            mIntent.setData(Uri.fromParts("package", getPackageName(), null));
                                            startActivityForResult(mIntent, RESULT_ACTION_SETTING);
                                        }

                                        @Override
                                        public void OnDialogExit() {
                                            // 退出APP
                                            finish();
                                        }
                                    });
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ACTION_SETTING) {
            PermissionUtils.checkAndRequestMorePermissions(mActivity, Permissions,
                    RESULT_ACTION_USAGE_ACCESS_SETTINGS, this::bindDevice);
        }
    }

    /**
     * 提示弹框
     */
    public void ShowTipDialog(String name, String content, String btnText, OnDialogClickListener listener) {
        int[] ids = new int[]{
                R.id.dialog_bt_dis,
                R.id.dialog_bt_ok
        };
        MyDialog dialog = new MyDialog(mActivity, R.layout.dialog_show_tips, ids, false);
        dialog.setOnCenterClickListener((dial, view) -> {
            if (view.getId() == R.id.dialog_bt_ok) {
                listener.OnDialogOK();
                if (!isFinishing()) {
                    dialog.dismiss();
                }
            }
            if (view.getId() == R.id.dialog_bt_dis) {
                if (!isFinishing()) {
                    dialog.dismiss();
                }
                listener.OnDialogExit();
            }
        });
        if (!isFinishing()) {
            dialog.show();
            dialog.setText(R.id.dialog_tv_title, name);
            dialog.setText(R.id.dialog_tv_text, content);
            dialog.setText(R.id.dialog_bt_ok, btnText);
        }
    }

    /**
     * 提示网络问题重启应用弹框
     */
    public void showRestartDialog() {
        int[] ids = new int[]{R.id.dialog_bt_ok};
        MyDialog dialog = new MyDialog(mActivity, R.layout.gt_dialog_restart_app, ids, false);
        dialog.setOnCenterClickListener((dial, view) -> {
            if (view.getId() == R.id.dialog_bt_ok) {
                if (!isFinishing()) {
                    dialog.dismiss();
                }
                finish();
            }
        });
        if (!isFinishing()) {
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
        }
    }
}
