package com.zxqy.xunilaidian.main.my;

import android.view.View;

import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 个人中心
 */
public class UserCenterActivity extends BaseActivity {


//    @BindView(R.id.tv_service)
//    TextView tv_service;
    private List<String> chongfList = new ArrayList<>();


    protected void initView() {
//        EventBus.getDefault().register(this);
    }

   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void setOtherWeather(MessageEvent event) throws ParseException {
        if (event != null && event.data.equals(MessageEvent.UPDATE_SET_MESSAGE_MODE)) {//刷新页面  短信模板
            refreshData();
        } else if (event != null && event.data.equals((MessageEvent.UPDATE_LOG_INFO))) {//刷新页面登录信息  会员信息
            refreshLoginInfo();
        }
    }*/


    @Override
    public void setRootView() {
        setShowStatusBar(false);

        setContentView(R.layout.activity_user_center);
    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://回退
                finish();
                break;

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
