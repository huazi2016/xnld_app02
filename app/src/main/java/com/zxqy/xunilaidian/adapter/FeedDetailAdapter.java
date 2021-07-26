package com.zxqy.xunilaidian.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.activity.FeedImgShowActivity;
import com.zxqy.xunilaidian.entity.FeedDetailEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 反馈回复
 */
public class FeedDetailAdapter extends BaseMultiItemQuickAdapter<FeedDetailEntity.FeedDetail, BaseViewHolder> {
    private List<String> images;
    RecyclerView recPhoto;
    FeedImgShowAdapter photoAdapter;

    public FeedDetailAdapter(@Nullable List<FeedDetailEntity.FeedDetail> data) {
        super(data);
        addItemType(FeedDetailEntity.FeedDetail.LEFT, R.layout.feed_reply_item_left);
        addItemType(FeedDetailEntity.FeedDetail.RIGHT, R.layout.feed_reply_item_right);
        addItemType(FeedDetailEntity.FeedDetail.RIGHT_1, R.layout.feed_reply_item_right);
    }

    @Override
    protected void convert(BaseViewHolder helper, FeedDetailEntity.FeedDetail item) {
        images = new ArrayList<>();
//        String head = SpUtils.getInstance().getString(Constants.HEAD_IMAGE);
//        ImageView imageView = helper.getView(R.id.iv_head);
//        Glide.with(mContext).load(head).into(imageView);
        if (!TextUtils.isEmpty(item.idsFile)) {
            images = Arrays.asList(item.idsFile.split(","));
        }

        int size = 3;
        if (images != null && images.size() > 0 && images.size() < 3) {
            size = images.size();
        }

        helper.setText(R.id.tv_content, item.msg)
                .setText(R.id.tv_time, item.timeSend);
        recPhoto = helper.getView(R.id.pic_recyclerView);
        if (images != null && images.size() > 0) {
            recPhoto.setVisibility(View.VISIBLE);
            recPhoto.setLayoutManager(new GridLayoutManager(mContext, size));
            photoAdapter = new FeedImgShowAdapter(images);
            recPhoto.setAdapter(photoAdapter);
            photoAdapter.setOnItemClickListener((adapter1, view, position) -> {
//                Gson gson = new Gson();
//                intent.putExtra(FeedImgShowActivity.IMG_DATAS, gson.toJson(adapter1.getData()));
                mContext.startActivity(new Intent(mContext, FeedImgShowActivity.class)
                        .putExtra(FeedImgShowActivity.IMG_POS, position)
                        .putExtra(FeedImgShowActivity.IMG_DATAS, item.idsFile)
                );
            });
        } else {
            recPhoto.setVisibility(View.GONE);
        }
    }
}
