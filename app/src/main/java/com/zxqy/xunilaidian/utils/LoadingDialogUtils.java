package com.zxqy.xunilaidian.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import com.zxqy.xunilaidian.R;


/**
 * Created by Administrator on 2017/2/23.
 * 加载进度等待框
 */

public class LoadingDialogUtils extends Dialog {
    private TextView message;

    public LoadingDialogUtils(Context context) {
        super(context);
        /**设置对话框背景透明*/
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //去掉标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_dialog_util);
        message = (TextView) findViewById(R.id.tv_load_message);
        //点击外面不消失
       /* setCanceledOnTouchOutside(false);
        setCancelable(false);*/
    }

    /**
     * 为加载进度个对话框设置不同的提示消息
     *
     * @param mes 给用户展示的提示信息
     * @return build模式设计，可以链式调用
     */
    public LoadingDialogUtils setMessage(String mes) {
        message.setText(mes);
        return this;
    }
}
