package com.zxqy.xunilaidian.activity;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.base.BaseActivity;
import com.zxqy.xunilaidian.utils.BitmapUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewActivity extends BaseActivity {
    @BindView(R.id.iv_preview)
    ImageView ivPreview;
    @BindView(R.id.btnStartQuery)
    Button btnStartQuery;
    int imageId;
    @BindView(R.id.relatvie_bg)
    RelativeLayout relatvieBg;
    Handler handler;
    Bitmap bitmap;
    @BindView(R.id.line_loading)
    LinearLayout lineLoading;


    @Override
    protected void initView() {

    }

    @Override
    public void setRootView() {
        setShowStatusBar(false);
        setContentView(R.layout.activity_preview);
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 0:
                        try {
                            WallpaperManager.getInstance(PreviewActivity.this).setBitmap(bitmap);
                            lineLoading.setVisibility(View.GONE);
                            finish();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };
    }

    @Override
    public void initData() {
        initHandler();
        imageId = getIntent().getIntExtra("previewId", 0);
        Glide.with(this).load(imageId).into(ivPreview);
        btnStartQuery.setOnClickListener(v -> {
            lineLoading.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bitmap = BitmapUtils.loadBitmapFromView(relatvieBg);
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                }
            }).start();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
