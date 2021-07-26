package com.zxqy.xunilaidian.activity;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.base.BaseActivity;
import com.zxqy.xunilaidian.utils.DestroyAndUpdateUtil;
import com.zxqy.xunilaidian.utils.DeviceUtils;
import com.zxqy.xunilaidian.utils.MyLog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于我们
 */
public class AboutActivity extends BaseActivity implements BaseActivity.OnPermissionResultListener{


    @BindView(R.id.tv_version)
    TextView tv_version;

//    用户协议，隐私政策勾选框
//    @BindView(R.id.iv_check)
//    ImageView iv_check;


    protected void initView() {
        tv_version.setText("当前版本："+ DeviceUtils.getVersionName(this));
    }


    @Override
    public void setRootView() {
        setShowStatusBar(true);
        setContentView(R.layout.activity_about);
    }

    @Override
    public void initData() {

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.iv_back, R.id.tv_update})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://回退
                finish();
                break;
            case R.id.tv_update://检查更新
//                if (isGetPermissions(Constants.READ_EXTERNAL) ) {//文件读写权限
//
//                } else {//没有权限
//
//                }
//                setPermissionListener(this);
//                showPreMissionDialog();
                DestroyAndUpdateUtil.checkNews(false,this);
                break;

        }
    }

    @Override
    public void onPermissionResult() {
        MyLog.e("", "onPermissionResult");
        DestroyAndUpdateUtil.checkNews(false,this);
    }

    @Override
    public void onPermissionCancle() {
        MyLog.e("", "onPermissionCancle");
    }


   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == 111) {
            finish();
        }
    }*/
}
