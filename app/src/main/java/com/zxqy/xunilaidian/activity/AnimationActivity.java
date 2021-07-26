package com.zxqy.xunilaidian.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.base.BaseActivity;
import com.zxqy.xunilaidian.main.WelcomeActivity;
import com.zxqy.xunilaidian.utils.CommonUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnimationActivity extends BaseActivity {
    @BindView(R.id.iv_anmaiton)
    ImageView ivAnmaiton;
    private TimerTask mTimer;
    private final Timer timer = new Timer();
    private Handler handler;
    MediaPlayer player = null;

    @SuppressLint("HandlerLeak")
    private void initHandle() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 0:
                        player.release();
                        CommonUtil.openOtherApp(AnimationActivity.this, getIntent().getStringExtra("packageName"));
                        EventBus.getDefault().post("退出应用");
                        finish();
                        break;
                }
            }
        };
    }

    @Override
    public void initData() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation

//        Glide.with(this).load(R.drawable.anmation).into(ivAnmaiton);

//        mTimer = new TimerTask() {
//            @Override
//            public void run() {
//                Message message = new Message();
//                message.what = 0;
//                handler.sendMessage(message);
//            }
//        };
//        timer.scheduleAtFixedRate(mTimer, 0, 2000);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        initHandle();
        getApplication().setTheme(R.style.AppThemeTrans);
        EventBus.getDefault().post("Animation");
        AssetManager assetManager;
        player = new MediaPlayer();
        assetManager = getResources().getAssets();
        String voiceFileName = getIntent().getStringExtra("voiceFileName");
        if (getIntent().getBooleanExtra("TooOtherApp", false)) {
            if (voiceFileName != null) {
                try {
                    AssetFileDescriptor fileDescriptor = assetManager.openFd(voiceFileName);
                    player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getStartOffset());
                    player.prepare();
                    player.start();
                    int time = player.getDuration();
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessageDelayed(message, 4000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                CommonUtil.openOtherApp(AnimationActivity.this, getIntent().getStringExtra("packageName"));
                EventBus.getDefault().post("退出应用");
                finish();
            }
        }else {
            startActivity(new Intent(this, WelcomeActivity.class));
        }
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_anmation);
    }
}
