package com.zxqy.xunilaidian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.adapter.FeedbackListAdapter;
import com.zxqy.xunilaidian.base.BaseActivity;
import com.zxqy.xunilaidian.bean.FeedMsgBean;
import com.zxqy.xunilaidian.entity.FeedBackListEntity;
import com.zxqy.xunilaidian.utils.HttpUtils;
import com.zxqy.xunilaidian.utils.MessageEvent;
import com.zxqy.xunilaidian.utils.ToastUtils;
import com.zxqy.xunilaidian.utils.httputil.callback.BaseCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.text.ParseException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 反馈列表
 */
public class FeedBackListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rc_list)
    RecyclerView rc_list;//列表
    @BindView(R.id.tv_empty)
    TextView tv_empty;//
    @BindView(R.id.root_pull_layout)
    PullToRefreshLayout root_pull_layout;//下拉刷新  加载更多  控件
    @BindView(R.id.animationView)
    LottieAnimationView animationView;
    private FeedbackListAdapter adapter;
    private int curent_page = 1;//当前页 从1开始
    private int pageSize = 15;//每页请求的数据条数
    private List<FeedMsgBean> msgBeanList;
    @Override
    public void setRootView() {
        setShowStatusBar(true);
        setContentView(R.layout.activity_feedback_list);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        rc_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FeedbackListAdapter(null);
        adapter.setOnItemClickListener(this);
        msgBeanList = LitePal.findAll(FeedMsgBean.class);
        adapter.setMsgBeanList(msgBeanList);
        rc_list.setAdapter(adapter);
        root_pull_layout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                refreshDatas(true);
            }

            @Override
            public void loadMore() {
                refreshDatas(false);
            }

        });
        refreshDatas(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setOtherWeather(MessageEvent event) throws ParseException {
        if (event != null && event.data.equals(MessageEvent.UPDATE_FEEDBACK_LIST)) {//刷新列表
            refreshDatas(true);
        }
    }


    private void refreshDatas(boolean isReFresh) {
        if (isReFresh) {//下拉刷新
            curent_page = 1;
        } else {
            curent_page++;
        }
        showLoadDialog("请求中...");
        HttpUtils.getInstance().postFeedBackList(curent_page, pageSize, new BaseCallback<FeedBackListEntity>() {
            @Override
            public void onRequestBefore() {
                canceloadDialog();
            }

            @Override
            public void onFailure(Request request, Exception e) {
                canceloadDialog();
                root_pull_layout.finishRefresh();
                root_pull_layout.finishLoadMore();
            }

            @Override
            public void onSuccess(Response response, FeedBackListEntity o) {
                canceloadDialog();
                root_pull_layout.finishRefresh();
                root_pull_layout.finishLoadMore();
                if (isReFresh) {
                    if (o != null && o.data != null && o.data.list != null && o.data.list.size() > 0) {
                        tv_empty.setVisibility(View.GONE);
                        rc_list.setVisibility(View.VISIBLE);
                        adapter.replaceData(o.data.list);
                    } else {
                        tv_empty.setVisibility(View.VISIBLE);
                        rc_list.setVisibility(View.GONE);
                        ToastUtils.showShortToast("当前没有反馈");
                    }

                } else {
                    if (o != null && o.data != null && o.data.list != null && o.data.list.size() > 0) {
                        adapter.addData(o.data.list);
                        tv_empty.setVisibility(View.GONE);
                        animationView.setVisibility(View.GONE);
                    } else {
                        ToastUtils.showShortToast("没有更多数据了");
                        tv_empty.setVisibility(View.GONE);
                        animationView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onError(Response response, int errorCode, Exception e, String erroStr) {
                if (erroStr.equals("登录超时，请重新登录！")) {
                    ToastUtils.showShortToast("登录超时，请重新登录！");
                    startActivity(new Intent(FeedBackListActivity.this, LoginActivity.class));
                    finish();
                } else {
                    canceloadDialog();
                    root_pull_layout.finishRefresh();
                    root_pull_layout.finishLoadMore();
                    if (curent_page == 1 && !TextUtils.isEmpty(erroStr)) {
                        ToastUtils.showLongToast(erroStr);
                    } else {
                        ToastUtils.showLongToast("无更多数据");
                    }
                }

            }
        });
    }

    public void canceloadDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_add})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://回退
                finish();
                break;
            case R.id.tv_add://添加反馈
                startActivity(new Intent(FeedBackListActivity.this, FeedBackAddActivity.class));
                break;
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startActivity(new Intent(FeedBackListActivity.this, FeedBackDetailActivity.class)
                .putExtra("feedId", ((FeedBackListEntity.FeedBackDataBean.FeedBackBean) adapter.getData().get(position)).id)
                .putExtra("status", ((FeedBackListEntity.FeedBackDataBean.FeedBackBean) adapter.getData().get(position)).status)
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}