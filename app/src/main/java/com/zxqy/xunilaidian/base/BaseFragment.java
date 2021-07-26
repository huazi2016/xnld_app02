package com.zxqy.xunilaidian.base;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.utils.ToastUtils;
import com.zxqy.xunilaidian.utils.UserEvent;
import com.zxqy.xunilaidian.utils.listener.OnDialogClickListener;
import com.zxqy.xunilaidian.view.MyDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    public Activity context;
    public Context mContext;
    public Bundle savedInstanceState;
    //    public LoadingDailog dailog;
    private View rootView;

    private Unbinder mUnbinder;
    private static final int RESULT_ACTION_USAGE_ACCESS_SETTINGS = 10086;
    /**
     * 视图是否已经初初始化
     */
    protected boolean isInit = false;
    protected boolean isLoad = false;
    protected final String TAG = "BaseFragment";
    private String[] permissionstrings;
    private String permissionType;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        rootView = inflater.inflate(getRootViewId(), container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        mContext = getContext();
        this.savedInstanceState = savedInstanceState;
        if (getClass().isAnnotationPresent(UserEvent.class)) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
        initUI();
        isInit = true;
        /**初始化的时候去加载数据**/
        isCanLoadData();
        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }
    public abstract int getRootViewId();

    public abstract void initUI();

    public abstract void initData();
    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }
        if (getUserVisibleHint()) {
            if (!isLoadData()) {
                lazyLoad();
            }
            isLoad = true;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }
    protected void finish() {
        getActivity().finish();
    }


    public static String getMobileType() {
        return Build.MANUFACTURER;
    }

    /**
     * 是否要每次进来加载lazyData
     *
     * @return
     */
    protected abstract boolean isLoadData();


    /**
     * 提示弹框
     */
    public void ShowTipDialog(String name, String content, String btnText, OnDialogClickListener listener) {
        int[] ids = new int[]{
                R.id.dialog_bt_dis,
                R.id.dialog_bt_ok
        };
        MyDialog dialog = new MyDialog(getContext(), R.layout.my_dialog, ids, false);
        dialog.setOnCenterClickListener((dial, view) -> {
            if (view.getId() == R.id.dialog_bt_ok) {
                listener.OnDialogOK();
                if (!getActivity().isFinishing()) {
                    dialog.dismiss();
                }
            }
            if (view.getId() == R.id.dialog_bt_dis) {
                if (!getActivity().isFinishing()) {
                    dialog.dismiss();
                }
                listener.OnDialogExit();
            }
        });
        if (!getActivity().isFinishing()) {
            dialog.show();
            dialog.setText(R.id.dialog_tv_title, name);
            dialog.setText(R.id.dialog_tv_text, content);
            dialog.setText(R.id.dialog_bt_ok, btnText);
        }
    }
    /**
     * 获取日历读写权限
     */
    public String[] getCalenderPermissions() {
        return new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR};
    }

    protected final void logShow(String tag,String msg) {
        Log.e(tag, msg);
    }

    /**
     * sessionId 过期
     */
    protected final void LoginOut() {
        ToastUtils.showShortToast("身份已过期,请重新登录");

//        PreferencesUtil.getInstance(mActivity).clear();
//        Intent intent = new Intent(mActivity,LoginActivity.class);
//        intent.putExtra("loginout_type",1);
//        startActivity(intent);
//        JMessageClient.logout(); 极光
//        AppManagerUtil.instance().finishAllActivity();
    }

    protected abstract void initView();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null)
            mUnbinder.unbind();
        if (getClass().isAnnotationPresent(UserEvent.class)) {
            EventBus.getDefault().unregister(this);
        }
    }


    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract void lazyLoad();


    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以调用此方法
     */
    protected void stopLoad() {
    }

    protected final String getTextByEditText(EditText et) {
        return et.getText().toString().trim();
    }



    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        View v = getActivity().getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

}
