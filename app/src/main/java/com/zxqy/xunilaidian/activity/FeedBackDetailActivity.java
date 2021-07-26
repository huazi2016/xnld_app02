package com.zxqy.xunilaidian.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.adapter.FeedDetailAdapter;
import com.zxqy.xunilaidian.base.BaseActivity;
import com.zxqy.xunilaidian.entity.FeedBackComtiEntity;
import com.zxqy.xunilaidian.entity.FeedDetailEntity;
import com.zxqy.xunilaidian.utils.HttpUtils;
import com.zxqy.xunilaidian.utils.MessageEvent;
import com.zxqy.xunilaidian.utils.ToastUtils;
import com.zxqy.xunilaidian.utils.httputil.callback.BaseCallback;
import com.zxqy.xunilaidian.utils.listener.OnDialogClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 反馈详情
 */
public class FeedBackDetailActivity extends BaseActivity {

    @BindView(R.id.rc_data)
    RecyclerView rc_data;//反馈内容

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;//
    @BindView(R.id.tv_end)
    TextView tv_end;//
    @BindView(R.id.tv_reply)
    TextView tv_reply;//

    private FeedDetailAdapter adapter;

    private String feedId;
    private int status;//1 是未完成 2 是完成

    @Override
    public void setRootView() {
        setShowStatusBar(true);
        setContentView(R.layout.feed_detail);
    }

    @Override
    protected void initView() {
        feedId = getIntent().getStringExtra("feedId");
        status = getIntent().getIntExtra("status",1);
        if (TextUtils.isEmpty(feedId)) {
            ToastUtils.showLongToast("数据异常");
            finish();
            return;
        }
        if (status==2) {//已完成
            tv_end.setVisibility(View.GONE);
            tv_reply.setVisibility(View.GONE);
        }

        rc_data.setNestedScrollingEnabled(false);
        rc_data.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FeedDetailAdapter(new ArrayList<>());
        rc_data.setAdapter(adapter);

        EventBus.getDefault().register(this);
        refreshDatas();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setOtherWeather(MessageEvent event) throws ParseException {
        if (event != null && event.data.equals(MessageEvent.UPDATE_FEEDBACK_DETAIL)) {//刷新
            refreshDatas();
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
    private void refreshDatas() {
        showLoadDialog("请求中...");
        HttpUtils.getInstance().postFeedBackDetail(feedId, new BaseCallback<FeedDetailEntity>() {
            @Override
            public void onRequestBefore() {
            }

            @Override
            public void onFailure(Request request, Exception e) {
                canceloadDialog();
            }

            @Override
            public void onSuccess(Response response, FeedDetailEntity result) {
                canceloadDialog();
//                MyLog.e("feedDetail", "result==" + result);
                if ((result.code == 0 || result.code == 200) && result.data != null && result.data.size() > 0) {
                    adapter.replaceData(result.data);
                    scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
                } else {
                    ToastUtils.showLongToast("反馈详情为空");
                    finish();
                }
             /*   if (o != null && o.data != null && o.data.list != null && o.data.list.size() > 0) {
                    tv_empty.setVisibility(View.GONE);
                } else {
                    tv_empty.setVisibility(View.VISIBLE);
                    ToastUtils.showShortToast("当前没有反馈");
                }*/
            }

            @Override
            public void onError(Response response, int errorCode, Exception e, String erroStr) {
                canceloadDialog();
                if (!TextUtils.isEmpty(erroStr)) {
                    ToastUtils.showLongToast(erroStr);
                }

            }
        });
    }

    /**
     * 结束反馈
     */
    private void feedEnd() {
        showLoadDialog("请求中...");
        HttpUtils.getInstance().postFeedBackEnd(feedId,2, new BaseCallback<FeedBackComtiEntity>() {
            @Override
            public void onRequestBefore() {
            }

            @Override
            public void onFailure(Request request, Exception e) {
                canceloadDialog();
            }

            @Override
            public void onSuccess(Response response, FeedBackComtiEntity result) {
                canceloadDialog();
                if (result.code == 0 || result.msg.equals("success")) {
                    ToastUtils.showLongToast("提交成功！");
                    tv_end.setVisibility(View.GONE);
                    tv_reply.setVisibility(View.GONE);
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.UPDATE_FEEDBACK_LIST));
                } else {
                    if (!TextUtils.isEmpty(result.msg)) {
                        ToastUtils.showShortToast(result.msg);
                    }
                }
            }

            @Override
            public void onError(Response response, int errorCode, Exception e, String erroStr) {
                canceloadDialog();
                if (!TextUtils.isEmpty(erroStr)) {
                    ToastUtils.showLongToast(erroStr);
                }

            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_reply, R.id.tv_end})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://回退
                finish();
                break;
            case R.id.tv_reply://回复反馈
                startActivity(new Intent(FeedBackDetailActivity.this, FeedReplyActivity.class)
                        .putExtra("feedId", feedId)
                );
                break;
            case R.id.tv_end://结束反馈
                ShowTipDialog("温馨提示",
                        "您确定反馈内容已解决吗？",
                        "确定", new OnDialogClickListener() {
                            @Override
                            public void OnDialogOK() {
                                feedEnd();
                            }

                            @Override
                            public void OnDialogExit() {
                            }
                        });
                break;
        }
    }

    @Override
    public void initData() {

    }

}