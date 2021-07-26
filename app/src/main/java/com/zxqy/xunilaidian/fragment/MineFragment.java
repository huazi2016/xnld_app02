package com.zxqy.xunilaidian.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.activity.AboutActivity;
import com.zxqy.xunilaidian.activity.ContentActivity;
import com.zxqy.xunilaidian.activity.FeedBackListActivity;
import com.zxqy.xunilaidian.activity.LoginActivity;
import com.zxqy.xunilaidian.activity.MemberCenterActivity;
import com.zxqy.xunilaidian.base.BaseFragment;
import com.zxqy.xunilaidian.entity.AppConfigInfoEntity;
import com.zxqy.xunilaidian.utils.AppConfigUtils;
import com.zxqy.xunilaidian.utils.Constants;
import com.zxqy.xunilaidian.utils.DateUtil;
import com.zxqy.xunilaidian.utils.MessageEvent;
import com.zxqy.xunilaidian.utils.ShareUtils;
import com.zxqy.xunilaidian.utils.SpDefine;
import com.zxqy.xunilaidian.utils.SpUtils;
import com.zxqy.xunilaidian.utils.ToastUtils;
import com.zxqy.xunilaidian.utils.UserEvent;
import com.zxqy.xunilaidian.utils.listener.OnDialogClickListener;
import com.zxqy.xunilaidian.view.MyDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

@UserEvent
public class MineFragment extends BaseFragment {
    @BindView(R.id.tv_exit_login)
    QMUIRoundButton tvExitLogin;
    @BindView(R.id.iv_head_image)
    ImageView ivHeadImage;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.iv_vip_mark)
    ImageView ivVipMark;
    @BindView(R.id.tv_hint_vip)
    TextView tvHintVip;
    @BindView(R.id.rr_vip)
    RelativeLayout rrVip;
    @BindView(R.id.line_bg)
    LinearLayout lineBg;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    String appToken;
    private MyDialog serviceDialog;//联系客服弹框
    private AppConfigInfoEntity.ConfigBean appConfigInfoEntity;
    private int[] serviceids;

    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getRootViewId() {
        return R.layout.activity_user_center;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initUI() {
        appToken = SpUtils.getInstance().getString(Constants.APP_TOKEN, "");
        if (!appToken.equals("")) {
            tvExitLogin.setVisibility(View.VISIBLE);
        }
        if (!SpUtils.getInstance().getString(Constants.HEAD_IMAGE, "").equals("")) {
            Glide.with(Objects.requireNonNull(getContext()))
                    .load(SpUtils.getInstance().getString(Constants.HEAD_IMAGE))
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(ivHeadImage);
        }
        String useName = SpUtils.getInstance().getString(Constants.USER_NAME, "");
        boolean our_login = SpUtils.getInstance().getBoolean(Constants.OUR_LOGIN, false);
        if (!useName.equals("")) {
            nickname.setText(SpUtils.getInstance().getString(Constants.USER_NAME));
            nickname.setTextSize(12);
        }
        if (our_login) {
            nickname.setText(SpUtils.getInstance().getString(Constants.USER_PHONE));
            nickname.setTextSize(12);
        }
        //会员
        if (SpUtils.getInstance().getInt(Constants.VIP_STATUS, 0) == 1) {
            ivVipMark.setImageDrawable(getResources().getDrawable(R.drawable.play_privilege_action_flag_vip_ic));
            String date = SpDefine.getVipInfo().timeOutdate;
            String nowDate =      DateUtil.getDateFormat(DateUtil.PATTERN_YMD, date);
            tvHintVip.setText("会员到期日：" + nowDate);
            rrVip.setVisibility(View.GONE);
            lineBg.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void initData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        switch (messageEvent.data) {
            case MessageEvent.PAY_OK:
                ivVipMark.setImageDrawable(getResources().getDrawable(R.drawable.play_privilege_action_flag_vip_ic));
                String date = SpDefine.getVipInfo().timeOutdate;
                String nowDate =      DateUtil.getDateFormat(DateUtil.PATTERN_YMD, date);
                tvHintVip.setText("会员到期日：" + nowDate);
                rrVip.setVisibility(View.GONE);
                lineBg.setVisibility(View.VISIBLE);
                break;
            case MessageEvent.WECHAT_LOGIN_OK:
                Glide.with(Objects.requireNonNull(getContext()))
                        .load(SpUtils.getInstance().getString(Constants.HEAD_IMAGE))
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(ivHeadImage);
                nickname.setText(SpUtils.getInstance().getString(Constants.USER_NAME));
                nickname.setTextSize(12);
                tvExitLogin.setVisibility(View.VISIBLE);
                break;
            case MessageEvent.UPDATE_LOG_INFO:
                if (SpUtils.getInstance().getBoolean(Constants.OUR_LOGIN)) {
                    Glide.with(Objects.requireNonNull(getContext()))
                            .load(getResources().getDrawable(R.mipmap.header))
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(ivHeadImage);
                    nickname.setText(SpUtils.getInstance().getString(Constants.USER_PHONE));
                    nickname.setTextSize(12);
                } else {
                    Glide.with(Objects.requireNonNull(getContext()))
                            .load(SpUtils.getInstance().getString(Constants.HEAD_IMAGE))
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(ivHeadImage);
                    nickname.setText(SpUtils.getInstance().getString(Constants.USER_NAME));
                    nickname.setTextSize(12);
                }
                tvExitLogin.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected boolean isLoadData() {
        return false;
    }


    @Override
    protected void initView() {

    }

    @Override
    protected void lazyLoad() {

    }

    public boolean checkApkExist(String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 联系客服
     */
    public void selectService() {
        appConfigInfoEntity = SpDefine.getAppConfigInfo();
        if (appConfigInfoEntity == null) {
            ToastUtils.showLongToast("配置信息为空，请重新进入app");
            return;
        }
        if (serviceDialog != null) {
            serviceDialog.show();
            return;
        }
        serviceids = new int[]{
                R.id.dialog_bt_dis,
                R.id.rl_wechart,
                R.id.rl_qq
        };
        serviceDialog = new MyDialog(getActivity(), R.layout.select_service_dialog, serviceids, false);
        serviceDialog.setOnCenterClickListener((dial, view) -> {
            if (view.getId() == R.id.rl_wechart) {//微信
                if (TextUtils.isEmpty(appConfigInfoEntity.qq)) {
                    ToastUtils.showLongToast("抱歉，客服微信信息为空~");
                    return;
                }

                AppConfigUtils.setPrimaryClip(getActivity(), appConfigInfoEntity.wechat, "客服微信已复制到剪切板");
                serviceDialog.dismiss();
            }
            if (view.getId() == R.id.rl_qq) {//qq
                if (TextUtils.isEmpty(appConfigInfoEntity.qq)) {
                    ToastUtils.showLongToast("抱歉，客服QQ信息为空~");
                    return;
                }
                if (checkApkExist("com.tencent.mobileqq")) {
                    ShowTipDialog("即将跳转到QQ",
                            "客服QQ:" + appConfigInfoEntity.qq,
                            "确定", new OnDialogClickListener() {
                                @Override
                                public void OnDialogOK() {
                                    startActivity(new Intent(Intent.ACTION_VIEW
                                            , Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + appConfigInfoEntity.qq + "&version=1")));
                                }

                                @Override
                                public void OnDialogExit() {
                                }
                            });
                } else {
                    ToastUtils.showLongToast("本机未安装QQ应用");
                }

                serviceDialog.dismiss();
            }
            if (view.getId() == R.id.dialog_bt_dis) {
                serviceDialog.dismiss();
            }
        });

        serviceDialog.show();
        if (!TextUtils.isEmpty(appConfigInfoEntity.wechat)) {//微信号
            serviceDialog.setText(R.id.tv_wechat, appConfigInfoEntity.wechat);
        }
        if (!TextUtils.isEmpty(appConfigInfoEntity.qq)) {//qq号
            serviceDialog.setText(R.id.tv_qq, appConfigInfoEntity.qq);
        }
    }


    @OnClick({R.id.rr_vip,R.id.nickname, R.id.tv_kefu, R.id.line_pay_vip, R.id.tv_about, R.id.tv_feedback, R.id.tv_share, R.id.iv_head_image,
            R.id.tv_good, R.id.tv_use,
            R.id.tv_secrete, R.id.tv_exit_login})
    public void onViewClicked(View view) {
        Intent intent = new Intent(mContext, ContentActivity.class);
        switch (view.getId()) {
            case R.id.tv_kefu:
                selectService();
                break;
            case R.id.line_pay_vip:
                //充值会员
            case R.id.rr_vip:
                startActivity(new Intent(getActivity(), MemberCenterActivity.class));
                break;
            case R.id.tv_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.tv_feedback:
                startActivity(new Intent(getActivity(), FeedBackListActivity.class));
                break;
            case R.id.tv_share:
                //                //                BaseConfig config = BaseConfig.getInstance(true);
                String shareUrl = "";
                ShareUtils.shareFile(getActivity(), "快来一起使用" +
                        getContext().getString(R.string.app_name) + "app，下载地址(请用浏览器打开下载):\n" + shareUrl);
                break;
            case R.id.nickname:
            case R.id.iv_head_image:
                if (!SpUtils.getInstance().getBoolean(Constants.IS_LOGIN, false)) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.tv_good:
                startActivity(new Intent("android.intent.action.VIEW", Uri
                        .parse("market://details?id=" + getActivity().getPackageName())));
                break;
            case R.id.tv_use:
                intent.putExtra("协议名称", "用户协议");
                intent.putExtra(com.zxqy.xunilaidian.Constants.KEY_FRAGMENT, com.zxqy.xunilaidian.Constants.WEB_FRAGMENT);
                intent.putExtra(com.zxqy.xunilaidian.Constants.KEY_URL, com.zxqy.xunilaidian.Constants.USER_SERVE_URL);
                mContext.startActivity(intent);
                break;
            case R.id.tv_secrete:
                intent.putExtra("协议名称", "隐私协议");
                intent.putExtra(com.zxqy.xunilaidian.Constants.KEY_FRAGMENT, com.zxqy.xunilaidian.Constants.WEB_FRAGMENT);
                intent.putExtra(com.zxqy.xunilaidian.Constants.KEY_URL, com.zxqy.xunilaidian.Constants.YS_URL);
                mContext.startActivity(intent);
                break;
            case R.id.tv_exit_login:
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("是否退出登录")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SpUtils.getInstance().putString(Constants.USER_PHONE, "");
                                SpUtils.getInstance().putBoolean(Constants.IS_LOGIN, false);
                                SpUtils.getInstance().putString(Constants.USER_ID, "");
                                SpUtils.getInstance().putString(Constants.ID_APP, "");
                                SpUtils.getInstance().putString(Constants.APP_TOKEN, "");
                                SpUtils.getInstance().putString(Constants.USER_NAME, "");
                                SpUtils.getInstance().putString(Constants.HEAD_IMAGE, "");
                                SpUtils.getInstance().putString(SpDefine.APP_CONFIG_INFO, "");
                                SpUtils.getInstance().putBoolean(Constants.OUR_LOGIN, false);
                                SpUtils.getInstance().putInt(Constants.VIP_STATUS, 0);
                                nickname.setText("点击登录");
                                ivHeadImage.setImageDrawable(getResources().getDrawable(R.mipmap.header));
                                tvExitLogin.setVisibility(View.GONE);
                                tvHintVip.setVisibility(View.VISIBLE);
                                rrVip.setVisibility(View.VISIBLE);
                                lineBg.setVisibility(View.GONE);
                                ivVipMark.setImageDrawable(getResources().getDrawable(R.drawable.download_center_not_vip));
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;
        }
    }


//    @OnClick({R.id.tv_secrete,R.id.tv_use,R.id.tv_vip,R.id.iv_head_image, R.id.iv_login, R.id.loginBg, R.id.tv_share, R.id.tv_feedback, R.id.tv_about, R.id.tv_good, R.id.tv_secrete, R.id.tv_exit_login})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.tv_use:
//                startActivity(new Intent(new Intent(getActivity(), BrowserUsActivity.class)).putExtra("URL", "file:///android_asset/useragreement.html")
//                        .putExtra("name", "用户协议"));
//                break;
//            case R.id.tv_vip:
//                //充值会员
//                startActivity(new Intent(getActivity(), MemberCenterActivity.class));
//                break;
//            case R.id.iv_head_image:
//            case R.id.iv_login:
//                //登录
//                startActivity(new Intent(getActivity(), LoginActivity.class));
//                break;
//            case R.id.loginBg:
//                break;
//            case R.id.tv_share:
//                //                BaseConfig config = BaseConfig.getInstance(true);
//                String shareUrl = "";
//                ShareUtils.shareFile(getActivity(), "快来一起使用" +
//                        getContext().getString(R.string.app_name) + "app，下载地址(请用浏览器打开下载):\n" + shareUrl);
//                break;
//            case R.id.tv_feedback:
//                //用户反馈
//                startActivity(new Intent(getActivity(), FeedBackListActivity.class));
//                break;
//            case R.id.tv_about:
//                startActivity(new Intent(getActivity(), AboutActivity.class));
//                break;
//            case R.id.tv_good:
//                break;
//            case R.id.tv_secrete:
//                startActivity(new Intent(new Intent(getActivity(), BrowserUsActivity.class)).putExtra("URL", "file:///android_asset/conceal.html")
//                        .putExtra("name", "隐私政策"));
//                break;
//            case R.id.tv_exit_login:
//                new QMUIDialog.MessageDialogBuilder(getActivity())
//                        .setTitle("提示")
//                        .setMessage("是否退出登录？")
//                        .addAction("否", new QMUIDialogAction.ActionListener() {
//                            @Override
//                            public void onClick(QMUIDialog dialog, int index) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .addAction(0, "是", QMUIDialogAction.ACTION_PROP_POSITIVE, new QMUIDialogAction.ActionListener() {
//                            @Override
//                            public void onClick(QMUIDialog dialog, int index) {
//                            }
//                        })
//                        .create(mCurrentDialogStyle).show();
//                break;
//        }
//    }

}
