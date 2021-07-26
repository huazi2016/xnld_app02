package com.zxqy.xunilaidian.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.entity.HomeItemEntity;

import java.util.List;

public class HomeMainAdapter extends BaseQuickAdapter<HomeItemEntity, BaseViewHolder> {
    public HomeMainAdapter(@Nullable List<HomeItemEntity> data) {

        super(R.layout.home_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeItemEntity item) {

        helper.setText(R.id.tv_name, item.name)
                .setImageResource(R.id.iv_icn, item.icn)
                ;

    }
}
