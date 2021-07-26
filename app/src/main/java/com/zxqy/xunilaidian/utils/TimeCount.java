package com.zxqy.xunilaidian.utils;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * @author Marcy
 * @date 2021/3/18
 * Description:
 */
public class TimeCount extends CountDownTimer {
    private TextView mCodeText;
    public TimeCount(long millisInFuture, long countDownInterval, TextView mCodeText) {
        super(millisInFuture, countDownInterval);
        this.mCodeText = mCodeText;
    }

    @Override
    public void onFinish() {
        mCodeText.setText("获取验证码");
        mCodeText.setEnabled(true);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTick(long millisUntilFinished) {
        mCodeText.setText(millisUntilFinished / 1000 + "s");
    }
}
