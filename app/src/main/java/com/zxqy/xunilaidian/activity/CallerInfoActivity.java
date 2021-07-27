package com.zxqy.xunilaidian.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.base.BaseActivity;

import butterknife.BindView;

public class CallerInfoActivity extends BaseActivity {

    @BindView(R.id.ivTitleBack)
    ImageView ivTitleBack;
    @BindView(R.id.tvMiddleTitle)
    TextView tvMiddleTitle;

    public static void launchActivity(Context context) {
        Intent intent =  new Intent(context, CallerInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvMiddleTitle.setText("来电者");
    }

    @Override
    public void setRootView() {
        setShowStatusBar(true);
        setContentView(R.layout.activity_caller_info);
    }

    @Override
    public void initData() {

    }
}