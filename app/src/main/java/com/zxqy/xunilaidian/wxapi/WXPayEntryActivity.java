package com.zxqy.xunilaidian.wxapi;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zxqy.xunilaidian.utils.Constants;
import com.zxqy.xunilaidian.utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;

/********************
 *  Created by ：  lj
 *         Date：  2019\5\27 0027.
 * Introduction：  IWXAPIEventHandler
 *********************/
public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //这句没有写,是不能执行回调的方法的
//            api = WXAPIFactory.createWXAPI(this, DataSaveUtils.getInstance().getUpdate().getConfig().getWxid());//这里填入自己的微信APPID
            api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID,true);//这里填入自己的微信APPID
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            Toast.makeText(this, "微信支付失败", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    @Override
    public void onReq(BaseReq baseReq) {}

    @Override
    public void onResp(BaseResp baseResp) {
        Log.e("测试一下", "errCode:" + baseResp.errCode + "===errStr:" + baseResp.errStr);
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    //微信支付成功后的逻辑
                    Toast.makeText(this, "微信支付成功", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.UPDATE_MEMBER_INFO));
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    //取消了微信支付
                    Toast.makeText(this, "微信支付失败", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Toast.makeText(this, "微信支付取消", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    Toast.makeText(this, "微信支付失败", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    }

}
