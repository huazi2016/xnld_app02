package com.zxqy.xunilaidian.adapter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.bean.FeedMsgBean;
import com.zxqy.xunilaidian.entity.FeedBackListEntity;
import com.zxqy.xunilaidian.entity.FeedDetailEntity;
import com.zxqy.xunilaidian.utils.HttpUtils;
import com.zxqy.xunilaidian.utils.ToastUtils;
import com.zxqy.xunilaidian.utils.httputil.callback.BaseCallback;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * 反馈列表
 */
public class FeedbackListAdapter extends BaseQuickAdapter<FeedBackListEntity.FeedBackDataBean.FeedBackBean, BaseViewHolder> {
    Handler handler;
    List<FeedMsgBean> msgBeanList;
    private int count;
    List<FeedDetailEntity.FeedDetail> beanList;
    List<TextView> textViewList;
    List<RelativeLayout> relativeLayoutList;

    public List<FeedMsgBean> getMsgBeanList() {
        return msgBeanList;
    }

    public void setMsgBeanList(List<FeedMsgBean> msgBeanList) {
        this.msgBeanList = msgBeanList;
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull @NotNull Message msg) {
                switch (msg.what) {
                    case 0:
                        int position = (int) msg.obj;
                        relativeLayoutList.get(position).setVisibility(View.VISIBLE);
                        textViewList.get(position).setText(String.valueOf(beanList.size()));
                        break;
                }
            }
        };
    }

    private List<String> typeStr;

    public FeedbackListAdapter(@Nullable List<FeedBackListEntity.FeedBackDataBean.FeedBackBean> data) {
        super(R.layout.feed_list_item, data);
        initHandler();
        textViewList = new ArrayList<>();
        relativeLayoutList = new ArrayList<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, FeedBackListEntity.FeedBackDataBean.FeedBackBean item) {
        typeStr = Arrays.asList(mContext.getResources().getStringArray(R.array.feed_back_type));
        helper.setText(R.id.tv_time, item.timeSubmit)
                .setText(R.id.tv_feed_type, typeStr.get(item.type - 1))
                .setText(R.id.tv_title, item.title);
        getFeedMsg(helper.getAdapterPosition(), item.id);
        RelativeLayout relative_count = helper.getView(R.id.relatvie_count);
        TextView textView_count = helper.getView(R.id.tv_count);
        textViewList.add(textView_count);
        relativeLayoutList.add(relative_count);
        if (item.status == 1) {//未完成
            helper.setGone(R.id.tv_end, false);
        } else {//已完成
            helper.setGone(R.id.tv_end, true);
        }
    }

    private void getFeedMsg(int position, String feedId) {
        HttpUtils.getInstance().postFeedBackDetail(feedId, new BaseCallback<FeedDetailEntity>() {
            @Override
            public void onRequestBefore() {
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }

            @Override
            public void onSuccess(Response response, FeedDetailEntity result) {
//                MyLog.e("feedDetailadapter", "result==" + result.data.size());
                Log.d("feedDetailadapter", "result==" + result.data.size());
                if ((result.code == 0 || result.code == 200) && result.data != null && result.data.size() > 0) {
                    beanList = new ArrayList<>();
                    for (FeedDetailEntity.FeedDetail feedDetailEntity:result.data) {
                        if (feedDetailEntity.typeSubmitter == 2) {
                            beanList.add(feedDetailEntity);
                        }
                    }
                    if (beanList.size()>0) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = position;
                        handler.sendMessage(message);
                    }


                } else {
                    Log.d("反馈详情为空", "反馈详情为空");
                }
            }

            @Override
            public void onError(Response response, int errorCode, Exception e, String erroStr) {
                if (!TextUtils.isEmpty(erroStr)) {
                    ToastUtils.showLongToast(erroStr);
                }

            }
        });
    }
}

