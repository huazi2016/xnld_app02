package com.zxqy.xunilaidian.adapter;

import android.graphics.Color;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.entity.FeedTypeEntity;

import java.util.List;

/**
 * 反馈类型
 */
public class FeedTypeAdapter extends BaseQuickAdapter<FeedTypeEntity, BaseViewHolder> {
    public FeedTypeAdapter(@Nullable List<FeedTypeEntity> data) {
        super(R.layout.feed_type_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FeedTypeEntity item) {
        helper.setText(R.id.tv_feed_type, item.getType());
        if (item.isChecked()) {
            helper.setBackgroundRes(R.id.tv_feed_type, R.drawable.feed_yx);
            helper.setTextColor(R.id.tv_feed_type, Color.parseColor("#FFFFFF"));
        } else {
            helper.setBackgroundRes(R.id.tv_feed_type, R.drawable.feed_wx);
            helper.setTextColor(R.id.tv_feed_type, mContext.getResources().getColor(R.color.title_color));
        }
    }
}
