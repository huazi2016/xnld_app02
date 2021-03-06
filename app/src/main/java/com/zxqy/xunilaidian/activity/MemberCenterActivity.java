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
 * ????????????
 */
public class MemberCenterActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.rc_privilege)
    RecyclerView rc_privilege;//????????????
    @BindView(R.id.rc_list)
    RecyclerView rc_list;//????????????
    @BindView(R.id.tv_name)
    TextView tv_name;//?????????
    @BindView(R.id.tv_end_date)
    TextView tv_end_date;//????????????
    @BindView(R.id.iv_header)
    ImageView iv_header;//????????????

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
        roundedCorners = new RoundedCorners(200);//?????????????????????
        coverRequestOptions = new RequestOptions()
                .transforms(new CenterCrop(), roundedCorners)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//??????????????????
                .skipMemoryCache(true);//??????????????????
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
        if (event != null && event.data.equals((MessageEvent.UPDATE_LOG_INFO))) {//????????????????????????  ????????????
            refreshLoginInfo();
        } else if (event != null && event.data.equals((MessageEvent.UPDATE_MEMBER_INFO))) {//??????????????????  ????????????
            getUserInfo();
        }
    }

    private void setVipList() {
        appConfigInfoEntity = SpDefine.getAppVipConfigInfo();
        appConfigInfo = SpDefine.getAppConfigInfo();
        if (appConfigInfoEntity != null && appConfigInfoEntity.vips != null && appConfigInfoEntity.vips.size() > 0) {
            datas = appConfigInfoEntity.vips;
            datas.get(datas.size() - 1).check = true;//????????????????????????
            idVip = datas.get(datas.size() - 1).id;
            adapter = new MemberCenterListAdapter(datas);
            adapter.setOnItemClickListener(this);
            rc_list.setAdapter(adapter);
            //??????????????????
            if (appConfigInfo != null) {
                cardPay.setVisibility(View.VISIBLE);
            }
        } else {
            getVIPList();//????????????
//            ToastUtils.showLongToast("VIP????????????????????????????????????APP");
        }


    }

    /**
     * ?????????????????????????????????
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
                tv_name.setText("??????");
            }
            if (SpDefine.getVipInfo() != null) {
                if (SpDefine.getVipInfo().status == 1) {//???????????????
                    tv_end_date.setText("??????????????????" + SpDefine.getVipInfo().timeOutdate);
                } else {
                    tv_end_date.setText("???????????????");
                }
            } else {
                tv_end_date.setText("?????????");
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
            tv_name.setText("??????/??????");
            tv_end_date.setVisibility(View.GONE);
            iv_header.setImageResource(R.mipmap.header);
        }
    }

    private void refreshDatas() {
        //??????????????????
        privilege_datas = new ArrayList<>();
        privilege_datas.add(new HomeItemEntity(this, "????????????", R.mipmap.tubiao));
        privilege_datas.add(new HomeItemEntity(this, "????????????", R.mipmap.bizhi));
        privilege_datas.add(new HomeItemEntity(this, "?????????", R.mipmap.clean_ad));
        privilege_datas.add(new HomeItemEntity(this, "????????????", R.mipmap.my_help));
        privilege_adapter = new HomeMainAdapter(privilege_datas);
        rc_privilege.setAdapter(privilege_adapter);
    }

    @OnClick({R.id.iv_back,R.id.tv_name, R.id.tv_pay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://??????
                finish();
                break;
            case R.id.tv_pay:
                if (checkAli.isChecked()) {
                    getOrderInfo(1);
                } else {
                    getOrderInfo(2);
                }
                break;
            case R.id.tv_name://?????????
                startActivity(new Intent(MemberCenterActivity.this, LoginActivity.class));
                break;
        }
    }

    /**
     * ???????????????????????????
     */
    public void canceloadDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * ??????????????????
     */
    private void getUserInfo() {
        showLoadDialog("??????????????????...");
        HttpUtils.getInstance().getUserInfo(new BaseCallback<UserInfoBean>() {
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {
                canceloadDialog();
                ToastUtils.showLongToast("????????????????????????");
            }

            @Override
            public void onSuccess(Response response, UserInfoBean resultBean) {
                canceloadDialog();
                //????????????????????????
                if (resultBean.data != null && resultBean.code == 0 || resultBean.code == 200) {
                    SpDefine.setUserInfo(resultBean.data);
                    SpDefine.setVipInfo(resultBean.data.vip);
                    if (resultBean.data.vip != null) {//
                        SpUtils.getInstance().putInt(Constants.VIP_STATUS, resultBean.data.vip.status);

                        tv_end_date.setText("??????????????????" + SpDefine.getVipInfo().timeOutdate);
                        //????????????
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
                        tv_name.setText("??????");
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
                ToastUtils.showLongToast("????????????????????????");
            }
        });
    }

    /**
     * ????????????
     * et_order_info ??????????????????
     */
    private void wechatPay(OrderInfoBean.OrderInfo o) {
        IWXAPI api = WXAPIFactory.createWXAPI(getApplicationContext(), o.app_id, false);
        api.registerApp(o.app_id);
        PayReq req = new PayReq();
        req.appId = o.app_id;
        req.partnerId = o.partner_id;//?????????
        req.prepayId = o.prepay_id;  //?????????id
        req.nonceStr = o.noncestr;//?????????
        req.timeStamp = o.timestamp;//?????????
        req.packageValue = o.packageValue;//?????????Sign=WXPay
        req.sign = o.sign;//??????
        api.sendReq(req); //?????????????????????????????????????????????????????????????????????
    }

    /**
     * ???????????????
     * et_order_info ??????????????????
     */
    private void aliPay(OrderInfoBean.OrderInfo o) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.ONLINE);//?????????  ?????? ??????
//        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);//?????????  ??????
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
     * ?????????????????????
     */
    @SuppressLint("HandlerLeak")
    public static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    switch (payResult.getResultStatus()) {
                        case "9000":
                            ToastUtils.showShortToast("????????????");
                            try {
                                Thread.sleep(3000);//??????3??????????????????????????????
                                EventBus.getDefault().post(new MessageEvent(MessageEvent.UPDATE_MEMBER_INFO));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            break;
                        case "8000":
                            ToastUtils.showShortToast("???????????????");
                            break;
                        case "4000":
                            ToastUtils.showShortToast("??????????????????");
                            break;
                        case "5000":
                            ToastUtils.showShortToast("????????????");
                            break;
                        case "6001":
                            ToastUtils.showShortToast("???????????????");
                            break;
                        case "6002":
                            ToastUtils.showShortToast("??????????????????");
                            break;
                        case "6004":
                            ToastUtils.showShortToast("????????????");
                            break;
                        default:
                            ToastUtils.showShortToast("????????????");
                            break;
                    }
                    break;
            }
        }
    };

    /**
     * ??????????????????
     * payType  ????????????    1:????????? 2:??????
     */
    private void getOrderInfo(int payType) {

        if (TextUtils.isEmpty(idVip)) {
            ToastUtils.showLongToast("???????????????????????????VIP???????????????");
            return;
        }

        //        if (com.jtjsb.storycollect.Utils.Utils.isOpenBySwt("S2360001")) {//????????????????????????
        if (!SpUtils.getInstance().getBoolean(Constants.IS_LOGIN)) {
//            startActivity(new Intent(MemberCenterActivity.this, LoginActivity.class));
            ToastUtils.showLongToast("?????????????????????????????????");
            return;
        }
//        }

        if (!DeviceUtils.isNetworkConnected(this)) {
            ToastUtils.showLongToast("?????????????????????????????????????????????");
            return;
        }

//        if (SpUtils.getInstance().getInt(Constants.VIP_STATUS, 3) == 1) {
//            ToastUtils.showLongToast("???????????????????????????????????????");
//            return;
//        }

        //???????????????????????????APP,????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (payType == 2 && !MyAppUtil.isWeChatInstalled(getApplicationContext())) {
            ToastUtils.showLongToast("???????????????????????????");
            return;
        }


        showLoadDialog("?????????...");
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
                            if (payType == 1) {//?????????
                                aliPay(o.data);
                            } else {//??????
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
     * ??????vip????????????
     */
    private void getVIPList() {

        showLoadDialog("?????????...");
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
                            datas.get(datas.size() - 1).check = true;//????????????????????????
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