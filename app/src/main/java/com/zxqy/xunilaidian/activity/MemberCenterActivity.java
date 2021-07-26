package com.zxqy.xunilaidian.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.adapter.HomeMainAdapter;
import com.zxqy.xunilaidian.adapter.MemberCenterListAdapter;
import com.zxqy.xunilaidian.base.BaseActivity;
import com.zxqy.xunilaidian.bean.OrderInfoBean;
import com.zxqy.xunilaidian.bean.PayResult;
import com.zxqy.xunilaidian.bean.UserInfoBean;
import com.zxqy.xunilaidian.entity.AppConfigInfoEntity;
import com.zxqy.xunilaidian.entity.HomeItemEntity;
import com.zxqy.xunilaidian.utils.Constants;
import com.zxqy.xunilaidian.utils.DeviceUtils;
import com.zxqy.xunilaidian.utils.HttpUtils;
import com.zxqy.xunilaidian.utils.MessageEvent;
import com.zxqy.xunilaidian.utils.MyAppUtil;
import com.zxqy.xunilaidian.utils.MyLog;
import com.zxqy.xunilaidian.utils.SpDefine;
import com.zxqy.xunilaidian.utils.SpUtils;
import com.zxqy.xunilaidian.utils.ToastUtils;
import com.zxqy.xunilaidian.utils.Utils;
import com.zxqy.xunilaidian.utils.httputil.callback.BaseCallback;
import com.zxqy.xunilaidian.view.CircleCheckBox;
import com.zxqy.xunilaidian.view.PressedTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 会员中心
 */
public class MemberCenterActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.rc_privilege)
    RecyclerView rc_privilege;//特权列表
    @BindView(R.id.rc_list)
    RecyclerView rc_list;//会员列表
    @BindView(R.id.tv_name)
    TextView tv_name;//用户名
    @BindView(R.id.tv_end_date)
    TextView tv_end_date;//会员列表
    @BindView(R.id.iv_header)
    ImageView iv_header;//会员列表

    List<AppConfigInfoEntity.VipDataBean> datas;
    @BindView(R.id.check_wechat)
    CircleCheckBox checkWechat;
    @BindView(R.id.check_ali)
    CircleCheckBox checkAli;
    @BindView(R.id.tv_pay)
    PressedTextView tvPay;
    @BindView(R.id.card_pay)
    CardView cardPay;
    private MemberCenterListAdapter adapter;

    List<HomeItemEntity> privilege_datas;
    private HomeMainAdapter privilege_adapter;

    private String idVip;
    RoundedCorners roundedCorners;
    RequestOptions coverRequestOptions;
    private AppConfigInfoEntity.AppConfigInfo appConfigInfoEntity;
    private AppConfigInfoEntity.ConfigBean appConfigInfo;

    @Override
    public void setRootView() {
        setShowStatusBar(false);
        setContentView(R.layout.activity_member_center);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        rc_list.setLayoutManager(new LinearLayoutManager(this));
        rc_privilege.setLayoutManager(new GridLayoutManager(this, 4));
        roundedCorners = new RoundedCorners(200);//数字为圆角度数
        coverRequestOptions = new RequestOptions()
                .transforms(new CenterCrop(), roundedCorners)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
                .skipMemoryCache(true);//不做内存缓存
        refreshDatas();
        refreshLoginInfo();
        setVipList();
        checkAli.setOnCircleCheckedChangeListener(new CircleCheckBox.OnCircleCheckedChangeListener() {
            @Override
            public void onCircleCheckedChange(boolean isChecked) {
                checkAli.setCircleChecked(true);
                checkWechat.setCircleChecked(false);
            }
        });
        checkWechat.setOnCircleCheckedChangeListener(new CircleCheckBox.OnCircleCheckedChangeListener() {
            @Override
            public void onCircleCheckedChange(boolean isChecked) {
                checkWechat.setCircleChecked(true);
                checkAli.setCircleChecked(false);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setOtherWeather(MessageEvent event) throws ParseException {
        if (event != null && event.data.equals((MessageEvent.UPDATE_LOG_INFO))) {//刷新页面登录信息  会员信息
            refreshLoginInfo();
        } else if (event != null && event.data.equals((MessageEvent.UPDATE_MEMBER_INFO))) {//刷新页面信息  会员信息
            getUserInfo();
        }
    }

    private void setVipList() {
        appConfigInfoEntity = SpDefine.getAppVipConfigInfo();
        appConfigInfo = SpDefine.getAppConfigInfo();
        if (appConfigInfoEntity != null && appConfigInfoEntity.vips != null && appConfigInfoEntity.vips.size() > 0) {
            datas = appConfigInfoEntity.vips;
            datas.get(datas.size() - 1).check = true;//默认选中最后一项
            idVip = datas.get(datas.size() - 1).id;
            adapter = new MemberCenterListAdapter(datas);
            adapter.setOnItemClickListener(this);
            rc_list.setAdapter(adapter);
            //支付配置信息
            if (appConfigInfo != null) {
                cardPay.setVisibility(View.VISIBLE);
            }
        } else {
            getVIPList();//请求接口
//            ToastUtils.showLongToast("VIP套餐信息为空，请重新登录APP");
        }


    }

    /**
     * 刷新用户登录及会员信息
     */
    private void refreshLoginInfo() {
        if (SpUtils.getInstance().getBoolean(Constants.IS_LOGIN)) {
            tv_name.setVisibility(View.VISIBLE);
            tv_end_date.setVisibility(View.VISIBLE);
            if (SpDefine.getUserInfo() != null && !TextUtils.isEmpty(SpDefine.getUserInfo().nickname)) {

                tv_name.setText(SpDefine.getUserInfo().nickname);

            } else if (!TextUtils.isEmpty(SpUtils.getInstance().getString(Constants.USER_PHONE))) {
                tv_name.setText(Utils.phoneNumber(SpUtils.getInstance().getString(Constants.USER_PHONE)));
            } else {
                tv_name.setText("游客");
            }
            if (SpDefine.getVipInfo() != null) {
                if (SpDefine.getVipInfo().status == 1) {//会员未过期
                    tv_end_date.setText("会员到期日：" + SpDefine.getVipInfo().timeOutdate);
                } else {
                    tv_end_date.setText("会员已过期");
                }
            } else {
                tv_end_date.setText("非会员");
            }

            if (SpDefine.getUserInfo() != null && !TextUtils.isEmpty(SpDefine.getUserInfo().avatar)) {
                MyLog.e("", "avatar==" + SpDefine.getUserInfo().avatar);
//                http://qzapp.qlogo.cn/qzapp/101948168/9AFA9E0C8191CE1B2A803573855788C6/100

                Glide.with(this)
                        .load(SpDefine.getUserInfo().avatar)
                        .apply(coverRequestOptions)
                        .error(R.mipmap.header)
                        .into(iv_header);
            } else {
                iv_header.setImageResource(R.mipmap.header);
            }
        } else {
            tv_name.setText("登录/注册");
            tv_end_date.setVisibility(View.GONE);
            iv_header.setImageResource(R.mipmap.header);
        }
    }

    private void refreshDatas() {
        //会员特权列表
        privilege_datas = new ArrayList<>();
        privilege_datas.add(new HomeItemEntity(this, "图标自由", R.mipmap.tubiao));
        privilege_datas.add(new HomeItemEntity(this, "海量壁纸", R.mipmap.bizhi));
        privilege_datas.add(new HomeItemEntity(this, "去广告", R.mipmap.clean_ad));
        privilege_datas.add(new HomeItemEntity(this, "专属客服", R.mipmap.my_help));
        privilege_adapter = new HomeMainAdapter(privilege_datas);
        rc_privilege.setAdapter(privilege_adapter);
    }

    @OnClick({R.id.iv_back,R.id.tv_name, R.id.tv_pay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://回退
                finish();
                break;
            case R.id.tv_pay:
                if (checkAli.isChecked()) {
                    getOrderInfo(1);
                } else {
                    getOrderInfo(2);
                }
                break;
            case R.id.tv_name://去登录
                startActivity(new Intent(MemberCenterActivity.this, LoginActivity.class));
                break;
        }
    }

    /**
     * 隐藏加载进度对话框
     */
    public void canceloadDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * 刷新用户信息
     */
    private void getUserInfo() {
        showLoadDialog("刷新会员信息...");
        HttpUtils.getInstance().getUserInfo(new BaseCallback<UserInfoBean>() {
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {
                canceloadDialog();
                ToastUtils.showLongToast("刷新用户信息失败");
            }

            @Override
            public void onSuccess(Response response, UserInfoBean resultBean) {
                canceloadDialog();
                //获取用户信息成功
                if (resultBean.data != null && resultBean.code == 0 || resultBean.code == 200) {
                    SpDefine.setUserInfo(resultBean.data);
                    SpDefine.setVipInfo(resultBean.data.vip);
                    if (resultBean.data.vip != null) {//
                        SpUtils.getInstance().putInt(Constants.VIP_STATUS, resultBean.data.vip.status);

                        tv_end_date.setText("会员到期日：" + SpDefine.getVipInfo().timeOutdate);
                        //充值成功
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.PAY_OK));
                    } else {
                        SpUtils.getInstance().putInt(Constants.VIP_STATUS, 3);
                    }
                    if (!TextUtils.isEmpty(resultBean.data.phone)) {
                        SpUtils.getInstance().putString(Constants.USER_PHONE, resultBean.data.phone);
                    }
                    if (SpDefine.getUserInfo() != null && !TextUtils.isEmpty(SpDefine.getUserInfo().nickname)) {

                        tv_name.setText(SpDefine.getUserInfo().nickname);

                    } else if (!TextUtils.isEmpty(SpUtils.getInstance().getString(Constants.USER_PHONE))) {
                        tv_name.setText(Utils.phoneNumber(SpUtils.getInstance().getString(Constants.USER_PHONE)));
                    } else {
                        tv_name.setText("游客");
                    }
                    SpUtils.getInstance().putBoolean(Constants.IS_LOGIN, true);
                    refreshLoginInfo();

                } else {
                    ToastUtils.showLongToast(resultBean.msg);
                }

            }

            @Override
            public void onError(Response response, int errorCode, Exception e, String erroMsg) {
                canceloadDialog();
                ToastUtils.showLongToast("刷新用户信息失败");
            }
        });
    }

    /**
     * 微信支付
     * et_order_info 由服务端生成
     */
    private void wechatPay(OrderInfoBean.OrderInfo o) {
        IWXAPI api = WXAPIFactory.createWXAPI(getApplicationContext(), o.app_id, false);
        api.registerApp(o.app_id);
        PayReq req = new PayReq();
        req.appId = o.app_id;
        req.partnerId = o.partner_id;//商户号
        req.prepayId = o.prepay_id;  //预订单id
        req.nonceStr = o.noncestr;//随机数
        req.timeStamp = o.timestamp;//时间戳
        req.packageValue = o.packageValue;//固定值Sign=WXPay
        req.sign = o.sign;//签名
        api.sendReq(req); //将订单信息对象发送给微信服务器，即发送支付请求
    }

    /**
     * 支付宝支付
     * et_order_info 由服务端生成
     */
    private void aliPay(OrderInfoBean.OrderInfo o) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.ONLINE);//支付宝  默认 线上
//        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);//支付宝  沙箱
        Runnable runnable = () -> {
            PayTask alipay = new PayTask(this);
            Map<String, String> map = alipay.payV2(o.sign, true);
            Message msg = new Message();
            msg.what = 1;
            msg.obj = map;
            mHandler.sendMessage(msg);
        };

        Thread payThread = new Thread(runnable);
        payThread.start();


    }

    /**
     * 支付宝支付回调
     */
    @SuppressLint("HandlerLeak")
    public static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    switch (payResult.getResultStatus()) {
                        case "9000":
                            ToastUtils.showShortToast("支付成功");
                            try {
                                Thread.sleep(3000);//延时3秒，等待后台状态刷新
                                EventBus.getDefault().post(new MessageEvent(MessageEvent.UPDATE_MEMBER_INFO));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            break;
                        case "8000":
                            ToastUtils.showShortToast("正在处理中");
                            break;
                        case "4000":
                            ToastUtils.showShortToast("订单支付失败");
                            break;
                        case "5000":
                            ToastUtils.showShortToast("重复请求");
                            break;
                        case "6001":
                            ToastUtils.showShortToast("已取消支付");
                            break;
                        case "6002":
                            ToastUtils.showShortToast("网络连接出错");
                            break;
                        case "6004":
                            ToastUtils.showShortToast("正在处理");
                            break;
                        default:
                            ToastUtils.showShortToast("支付失败");
                            break;
                    }
                    break;
            }
        }
    };

    /**
     * 获取订单信息
     * payType  支付类型    1:支付宝 2:微信
     */
    private void getOrderInfo(int payType) {

        if (TextUtils.isEmpty(idVip)) {
            ToastUtils.showLongToast("请先选择一项有效的VIP套餐信息！");
            return;
        }

        //        if (com.jtjsb.storycollect.Utils.Utils.isOpenBySwt("S2360001")) {//微信开关是否打开
        if (!SpUtils.getInstance().getBoolean(Constants.IS_LOGIN)) {
//            startActivity(new Intent(MemberCenterActivity.this, LoginActivity.class));
            ToastUtils.showLongToast("您还未登录，请先登录。");
            return;
        }
//        }

        if (!DeviceUtils.isNetworkConnected(this)) {
            ToastUtils.showLongToast("当前网络异常，请检查网络后重试");
            return;
        }

//        if (SpUtils.getInstance().getInt(Constants.VIP_STATUS, 3) == 1) {
//            ToastUtils.showLongToast("您已是会员，无需重复购买！");
//            return;
//        }

        //先判断是否安装微信APP,按照微信的说法，目前移动应用上微信登录只提供原生的登录方式，需要用户安装微信客户端才能配合使用。
        if (payType == 2 && !MyAppUtil.isWeChatInstalled(getApplicationContext())) {
            ToastUtils.showLongToast("请先安装微信客户端");
            return;
        }


        showLoadDialog("加载中...");
        HttpUtils.getInstance().getOrderInfo(payType, idVip,
                new BaseCallback<OrderInfoBean>() {
                    @Override
                    public void onRequestBefore() {

                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        canceloadDialog();
                        ToastUtils.showLongToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(Response response, OrderInfoBean o) {
                        canceloadDialog();
                        if (o != null && o.data != null) {
                            if (payType == 1) {//支付宝
                                aliPay(o.data);
                            } else {//微信
                                wechatPay(o.data);
                            }

                        } else {
                            if (!TextUtils.isEmpty(o.msg)) {
                                ToastUtils.showShortToast(o.msg);
                            }
                        }
                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e, String erroMsg) {
                        canceloadDialog();
                        if (!TextUtils.isEmpty(erroMsg)) {
                            ToastUtils.showLongToast(erroMsg);
                        }
                    }
                });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        refreshData(position);
        idVip = datas.get(position).id;
    }

    private void refreshData(int position) {
        for (int i = 0; i < datas.size(); i++
        ) {
            if (i == position) {
                datas.get(i).check = true;
            } else {
                datas.get(i).check = false;
            }
        }
        adapter.setNewData(datas);
        adapter.notifyDataSetChanged();
    }

    /**
     * 获取vip套餐信息
     */
    private void getVIPList() {

        showLoadDialog("加载中...");
        HttpUtils.getInstance().getVIPList(
                new BaseCallback<AppConfigInfoEntity.VipDataBean>() {
                    @Override
                    public void onRequestBefore() {

                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        canceloadDialog();
                        ToastUtils.showLongToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(Response response, AppConfigInfoEntity.VipDataBean o) {
                        canceloadDialog();
                       /* if (o != null && o.data != null && o.data.size() > 0) {
                            datas = o.data;
                            datas.get(datas.size() - 1).check = true;//默认选中最后一项
                            idVip = datas.get(datas.size() - 1).id;
                            adapter.setNewData(datas);
                            adapter.notifyDataSetChanged();

                        } else {
                            if (!TextUtils.isEmpty(o.msg)) {
                                ToastUtils.showShortToast(o.msg);
                            }
                        }*/
                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e, String erroMsg) {
                        canceloadDialog();
                        if (!TextUtils.isEmpty(erroMsg)) {
                            ToastUtils.showLongToast(erroMsg);
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}