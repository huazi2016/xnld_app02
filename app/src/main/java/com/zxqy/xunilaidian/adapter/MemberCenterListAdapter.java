package com.zxqy.xunilaidian.adapter;

import android.text.TextUtils;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.entity.AppConfigInfoEntity;

import java.util.List;

/**
 * 会员中心  支付项列表
 */
public class MemberCenterListAdapter extends BaseQuickAdapter<AppConfigInfoEntity.VipDataBean, BaseViewHolder> {

    RelativeLayout rl_root;

    public MemberCenterListAdapter(@Nullable List<AppConfigInfoEntity.VipDataBean> data) {
        super(R.layout.member_center_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppConfigInfoEntity.VipDataBean item) {

        helper.setText(R.id.tv_name, item.name)
                .setText(R.id.tv_remark, item.content)
                .setText(R.id.tv_price, "¥" + item.price)
                .setText(R.id.tv_xianshi, item.labelName)
        ;
        if (!TextUtils.isEmpty(item.labelName)) {
            helper.setVisible(R.id.tv_xianshi, true);
        } else {
            helper.setVisible(R.id.tv_xianshi, false);
        }
        rl_root = helper.getView(R.id.rl_root);
        if (item.check) {
            rl_root.setSelected(true);
        } else {
            rl_root.setSelected(false);
        }
    }
}
