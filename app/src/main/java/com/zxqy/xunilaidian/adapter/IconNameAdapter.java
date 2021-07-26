package com.zxqy.xunilaidian.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxqy.xunilaidian.R;

import java.util.List;

public class IconNameAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private int currentPosition = 0;

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public IconNameAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        LinearLayout layout = holder.getView(R.id.line_bg);
        TextView textView = holder.getView(R.id.tv_name);
        View view = holder.getView(R.id.view);
        if (holder.getAdapterPosition() == currentPosition) {
            view.setVisibility(View.VISIBLE);
            layout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }else {
            view.setVisibility(View.GONE);
            layout.setBackgroundColor(mContext.getResources().getColor(R.color.root_bg));
        }
        textView.setText(item);
    }
}
