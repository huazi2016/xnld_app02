package com.zxqy.xunilaidian.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.utils.HttpUrl;


import java.util.List;

public class FeedImgShowAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public FeedImgShowAdapter(@Nullable List<String> data) {
        super(R.layout.gt_item_suggest_image, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setVisible(R.id.iv_del, false);
        ImageView imageView = helper.getView(R.id.iv_pic);
        String path = item;
        if (!TextUtils.isEmpty(path)) {
            if (path.contains("http")) {
                // 图片，直接展示
                Glide.with(mContext).load(path).centerCrop()
//                        .crossFade()
                        .into(imageView);
            } else {
                // 原来的图片，拼接展示   FILE_BASE_URI
//                String host = SpUtils.getInstance().getString(Constants.COMMON_URL, HttpUrl.COMMON_URL);
//                String imgUrl = host.replace("app/", "") + path;
                Glide.with(mContext).load(HttpUrl.FILE_BASE_URI+item).centerCrop()
//                        .crossFade()
                        .into(imageView);
            }
        }
    }
}
