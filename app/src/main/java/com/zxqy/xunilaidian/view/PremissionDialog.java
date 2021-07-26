package com.zxqy.xunilaidian.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.utils.Constants;


public class PremissionDialog extends Dialog implements View.OnClickListener {

    View view;
    OnViewClickListener listener;
    Context context;
    ImageView image;
    ImageView close;
    TextView tvTitle;
    TextView tvContent;
    TextView tvNext;
    String type;//权限类型 1--定位权限

    public PremissionDialog(Context context, String type) {
        super(context, R.style.AgreementDialog);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.dialog_premission, null);
//        image = view.findViewById(R.id.image);
        tvTitle = view.findViewById(R.id.tv_title);
        tvNext = view.findViewById(R.id.tv_next);
        tvContent = view.findViewById(R.id.tv_content);
        close = view.findViewById(R.id.iv_close);
        initViews();
        //设置点击外部取消
        setCanceledOnTouchOutside(false);
        setContentView(view);
        tvNext.setOnClickListener(this);
        close.setOnClickListener(this);
        switch (type) {
            case Constants.PERMISSION_SHORTCUTS:
                //定位
//            Glide.with(context).load(R.mipmap.dialog_img_dingwei).into(image);
                tvTitle.setText("需要开启您的快捷方式权限");
                tvContent.setText("以便能创建桌面快捷方式");
                break;
        }

    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(100, 0, 100, 0);
        getWindow().setAttributes(layoutParams);
    }

    private void initViews() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close:
//                listener.onAttViewClick(0);
//                dismiss();
//                break;
            case R.id.tv_next:
                listener.onAttViewClick(0);
                dismiss();
                break;
        }
    }

    public interface OnViewClickListener {
        void onAttViewClick(int day);
    }

    public void setOnViewClickListener(OnViewClickListener listener) {
        this.listener = listener;
    }
}