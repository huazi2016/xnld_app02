package com.zxqy.xunilaidian.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.activity.ContentActivity;
import com.zxqy.xunilaidian.utils.Constants;
import com.zxqy.xunilaidian.utils.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZL on 2019/9/25
 * <p>
 * 用户协议对话框
 */

public class AgreementDialog extends Dialog {
    @BindView(R.id.tv_hint)
    TextView mHintText;
    @BindView(R.id.tv_cancel)
    PressedTextView tvCancel;
    @BindView(R.id.tv_agree)
    PressedTextView tvAgree;

    private boolean saveStatus = true; // 保存协议状态
    private Context context;
    private onDialogClickListener mOnDialogClickListener;

    public onDialogClickListener getOnDialogClickListener() {
        return mOnDialogClickListener;
    }

    public void setOnDialogClickListener(onDialogClickListener mOnDialogClickListener) {
        this.mOnDialogClickListener = mOnDialogClickListener;
    }

    public AgreementDialog(Context context) {
        super(context, R.style.AgreementDialog);
        this.context = context;
    }

    public AgreementDialog(Context context, onDialogClickListener listener) {
        super(context, R.style.Dialog);
        this.context = context;
        this.mOnDialogClickListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context, R.layout.dialog_agreement, null);
        setCancelable(false);//点击外部不可dismiss
        setCanceledOnTouchOutside(false);
        Window window = this.getWindow();
        window.setContentView(view);
        ButterKnife.bind(this, view);
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        if (saveStatus) {
            initTextView();
        }
    }

    /**
     * 初始化webView
     */
    private void initTextView() {
        String name = context.getResources().getString(R.string.app_name);
        String text = "尊敬的用户欢迎使用" + name +"!"+ name  +
                "非常重视您的隐私和个人信息保护。在" +
                "你使用前，请认真阅读《用户协议》和" +"《隐私政策》，您阅读及同意全部条款"+"后方可使用本软件";

        //设置文字
        int result1 = text.indexOf("《隐私政策》");
        int result2 = text.indexOf("《用户协议》");
        SpannableStringBuilder style = new SpannableStringBuilder();
        style.append(text);
        //隐私政策
        ClickableSpan PrivacyPolicy = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(context, ContentActivity.class);
                intent.putExtra("协议名称", "隐私协议");
                intent.putExtra(com.zxqy.xunilaidian.Constants.KEY_FRAGMENT, com.zxqy.xunilaidian.Constants.WEB_FRAGMENT);
                intent.putExtra(com.zxqy.xunilaidian.Constants.KEY_URL, com.zxqy.xunilaidian.Constants.YS_URL);
                context.startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        //用户协议
        ClickableSpan ServiceAgreement = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(context, ContentActivity.class);
                intent.putExtra("协议名称", "用户协议");
                intent.putExtra(com.zxqy.xunilaidian.Constants.KEY_FRAGMENT, com.zxqy.xunilaidian.Constants.WEB_FRAGMENT);
                intent.putExtra(com.zxqy.xunilaidian.Constants.KEY_URL, com.zxqy.xunilaidian.Constants.USER_SERVE_URL);
                context.startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        //隐私政策
        style.setSpan(PrivacyPolicy, result1, result1 + 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(ServiceAgreement, result2, result2 + 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mHintText.setText(style);
        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#55A0DA"));
        ForegroundColorSpan foregroundColorSpan01 = new ForegroundColorSpan(Color.parseColor("#55A0DA"));
        style.setSpan(foregroundColorSpan, result1, result1 + 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(foregroundColorSpan01, result2, result2 + 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //配置给TextView
        mHintText.setMovementMethod(LinkMovementMethod.getInstance());
        mHintText.setText(style);
        mHintText.setHighlightColor(Color.parseColor("#00000000"));//去掉点击后的背景颜色为透明
    }

    @OnClick({R.id.tv_cancel, R.id.tv_agree})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_cancel:
                SpUtils.getInstance().putBoolean(Constants.IS_CONFIRM_USER_AGREEMENT, false);
                if (mOnDialogClickListener != null) {
                    mOnDialogClickListener.onCancel();
                }
                dismiss();
                break;
            case R.id.tv_agree:
                if (saveStatus) {
                    SpUtils.getInstance().putBoolean(Constants.IS_CONFIRM_USER_AGREEMENT, true);
                }
                if (mOnDialogClickListener != null) {
                    mOnDialogClickListener.onConfirm();
                }
                dismiss();
                break;
        }
    }

    public interface onDialogClickListener {
        void onConfirm();

        void onCancel();
    }

    public void setButtonText(String cancel, String confirm, boolean save, String hint) {
        tvCancel.setText(cancel);
        tvAgree.setText(confirm);
        saveStatus = save;
        mHintText.setText(hint);
    }
}
