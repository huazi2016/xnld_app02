package com.zxqy.xunilaidian.base;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.gyf.immersionbar.ImmersionBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.utils.AppManagerUtil;
import com.zxqy.xunilaidian.utils.Constants;
import com.zxqy.xunilaidian.utils.LoadingDialogUtils;
import com.zxqy.xunilaidian.utils.PermissionUtils;
import com.zxqy.xunilaidian.utils.UserEvent;
import com.zxqy.xunilaidian.utils.listener.OnDialogClickListener;
import com.zxqy.xunilaidian.view.MyDialog;
import com.zxqy.xunilaidian.view.PremissionDialog;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import butterknife.ButterKnife;

public abstract class BaseActivity extends BaseGTActivity  {
    public Bundle save;
    public LoadingDialogUtils dialog;
    private static final int DEFAULT_TIMEOUT = 6;

    private String permissionType;
    private String[] permissionstrings;
    private PremissionDialog premissionDialog;
    protected Activity mActivity;
    private OnPermissionResultListener permissionResultListener;

    //    private boolean showStatusBar = true;
    private static final int RESULT_ACTION_USAGE_ACCESS_SETTINGS = 10086;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        save = savedInstanceState;
        mActivity = this;
        AppManagerUtil.instance().addActivity(this);
        if (getClass().isAnnotationPresent(UserEvent.class)) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
        //设置为无标题栏
        setRootView();
        ButterKnife.bind(this);
        getLoadingDailog();

        initView();
        initData();
    }

    public void getLoadingDailog() {
//        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(this)
//                .setMessage("正在加载")
//                .setCancelable(true)
//                .setCancelOutside(true);
//
//        iosDialog = loadBuilder.create();
        dialog = new LoadingDialogUtils(this);
    }

    /**
     * 显示加载进度对话框
     *
     * @param message
     */
    public void showLoadDialog(String message) {
        if (dialog == null) {
            dialog = new LoadingDialogUtils(mActivity).setMessage(message);
        }
        dialog.show();
    }

    /**
     * //状态栏字体是否深色
     *
     * @param isBlackText
     */
    public void setShowStatusBar(boolean isBlackText) {
        ImmersionBar.with(this).statusBarDarkFont(isBlackText).transparentStatusBar().fullScreen(false).init();
    }

    protected final void showLog(String tag, String msg) {
        Log.e(tag, msg);
    }

    protected abstract void initView();


    public abstract void setRootView();

    public abstract void initData();

    protected final String getTextByEditText(EditText et) {
        return et.getText().toString().trim();
    }

    @Override
    public void onBackPressed() {
        AppManagerUtil.instance().finishActivity(this);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManagerUtil.instance().finishActivity(this);
//根据 Tag 取消请求
        OkHttpUtils.getInstance().cancelTag(this);
        if (getClass().isAnnotationPresent(UserEvent.class)) {
            EventBus.getDefault().unregister(this);
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
        MyDialog dialog = new MyDialog(this, R.layout.my_dialog, ids, false);
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

    @Override
    public void finish() {
//        hideSoftInputFromWindow(this);
        super.finish();
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    private void hideSoftInputFromWindow(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(INPUT_METHOD_SERVICE);
        try {
            imm.hideSoftInputFromWindow(Objects.requireNonNull(activity.getCurrentFocus())
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将日期格式化成2017-01-07的形式
     *
     * @return
     */
    public static String formatDate(Date data) {
        SimpleDateFormat format_1 = new SimpleDateFormat("yyyy-MM-dd");

        return format_1.format(data);

    }

    public String getUnNullString(String str, String def) {
        return TextUtils.isEmpty(str) ? def : str;
    }

    public String getUnNullString(String str) {
        return TextUtils.isEmpty(str) ? "" : str;
    }


    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        View v = this.getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 检测权限是否已获取
     */
    public boolean isGetPermissions(String permissionType) {
        this.permissionType = permissionType;
        switch (permissionType) {
            case Constants.PERMISSION_SHORTCUTS://快捷方式权限
                permissionstrings = new String[]{Manifest.permission.INSTALL_SHORTCUT, Manifest.permission.UNINSTALL_SHORTCUT};
                break;
        }

//        String[] p = Build.VERSION.SDK_INT <= 28 ? getPermissions28() : getPermissions();
        for (String str : permissionstrings) {
            if (!PermissionUtils.checkPermission(this, str)) {
                return false;
            }
        }
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showPreMissionDialog() {
        if (premissionDialog == null) {
            premissionDialog = new PremissionDialog(this, permissionType);
            premissionDialog.setOnViewClickListener(day -> requestPermissions(permissionstrings, RESULT_ACTION_USAGE_ACCESS_SETTINGS));
        }
        premissionDialog.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RESULT_ACTION_USAGE_ACCESS_SETTINGS) {
            PermissionUtils.onRequestPermissionResult(this, permissionstrings[0], grantResults, new PermissionUtils.PermissionCheckCallBack() {
                @Override
                public void onHasPermission() {
                    if (permissionResultListener != null) {
                        permissionResultListener.onPermissionResult();
                    }
                }

                @Override
                public void onUserHasAlreadyTurnedDown(String... permission) {
                    ShowTipDialog("温馨提示",
                            "授予此权限才能正常使用此功能哦，点击确定继续授权",
                            "确定", new OnDialogClickListener() {
                                @Override
                                public void OnDialogOK() {
                                    PermissionUtils.requestMorePermissions(BaseActivity.this,
                                            permissions, RESULT_ACTION_USAGE_ACCESS_SETTINGS);
                                }

                                @Override
                                public void OnDialogExit() {
                                    if (permissionResultListener != null) {
                                        permissionResultListener.onPermissionCancle();
                                    }
                                }
                            });
                }

                @Override
                public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {//勾选不再提示后的弹框
                    ShowTipDialog("温馨提示",
                            "授予权限才能使用软件喔，请到设置中允许权限",
                            "确定", new OnDialogClickListener() {
                                @Override
                                public void OnDialogOK() {
                                    Intent mIntent = new Intent();
                                    mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                    mIntent.setData(Uri.fromParts("package", BaseActivity.this.getPackageName(), null));
                                    startActivityForResult(mIntent, RESULT_ACTION_USAGE_ACCESS_SETTINGS);
                                }

                                @Override
                                public void OnDialogExit() {
                                    if (permissionResultListener != null) {
                                        permissionResultListener.onPermissionCancle();
                                    }
                                }
                            });
                }
            });
        }
    }

    /**
     * 权限请求回调
     *
     * @param listener
     */
    public void setPermissionListener(OnPermissionResultListener listener) {
        permissionResultListener = listener;
    }

    public static interface OnPermissionResultListener {
        void onPermissionResult();

        void onPermissionCancle();
    }
}
