package com.zxqy.xunilaidian.utils;

import android.content.Context;
import android.telephony.TelephonyManager;


import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/10/11.
 */

public class HttpUtil {

    public static String PID = "120003";
    public static String Sign = "uid=tyzhzxl123&upwd=killhand007&timestamp=";
//    public static String Sign = "uid=test123&upwd=testpwd123&timestamp=";
//    public static String SignLocal = "uid=test123&upwd=testpwd123&timestamp=";
    public static String SignLocal = "uid=tyzhzxl123&upwd=killhand007&timestamp=";
    public static String t = "android";
    public static String Category_Id="10038";

    static final String HttpF = "http://app.weiapp8.cn";
    public static final String HttpMain = HttpF + "/multiopen/api/";
    public static final String HttpTemp="http://192.168.10.252/multiopen/api/";
    /*链接*/

    //客户端信息汇报接口
    public static final String HttpReport = HttpMain + "ClientReport.aspx";
    //推荐接口
    public static final String httpRecommend = HttpMain + "TjList.aspx";
//    public static final String httpRecommend = HttpTemp + "TjList.aspx";
    //版本升级接口
    public static final String httpVersionUpdate = HttpMain + "Version.aspx";
    //信息汇报接口
    public static final String httpClientReport = HttpMain + "ClientReport.aspx";
    //短信余额查询接口
    public static final String httpSmsAcountQuery = HttpMain + "ldb/SmsAcountQuery.aspx";
    //106网关短信发送接口
    public static final String httpSendSms = HttpMain + "ldb/SendSms.aspx";
    //订单生成接口
//    public static final String httpOrder = HttpMain + "Order.aspx";
    public static final String httpOrder = HttpMain + "Order.aspx";
    //订单查询接口
//    public static final String HttpOrderCheck = HttpMain + "OrderQuery.aspx";
    public static final String HttpOrderCheck = HttpMain + "OrderQuery.aspx";

    //分享信息查询接口
    public static final String HttpShare=HttpMain+"share.html";

    //列表获取接口
//    public static final String httpStoryGet=HttpTemp+"Edu/GuShiList.aspx";
//    public static final String httpStoryGet=HttpMain+"Edu/GuShiList.aspx";
//    VIP查询接口
//    public static final String httpVIPCheck=HttpMain+"Edu/GuShiVip.aspx";
    //收听状态更新接口
//    public static final String httpUpdateStatus=HttpMain+"Edu/GuShiListenUpdate.aspx";

    public static HttpUtil httpUtil;
    private static Context mContext;
    public static synchronized HttpUtil getHttpUtil(Context context){
        if (httpUtil == null){
            httpUtil = new HttpUtil(context.getApplicationContext());
        }
        return httpUtil;
    }
    private HttpUtil(Context context){
        mContext = context;
    }

    //时间戳
    String timestamp;
    //签名
    String sign;
    //客户端ID
    String client_id;
    //用户ID
    String user_id;

    //获取时间
    public String getnewtime(int type) {
        SimpleDateFormat formatter;
        if (type == 1){
            formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        }else{
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    public static String getnewtime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    //获取设备ID
    public  String getClientId(){
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        if(deviceId.equals("") || deviceId == null){
            deviceId = getSerialNum();
        }
        //Log.i("zz",deviceId);
        return deviceId;
    }

    // 加密成MD5
    public static String stringToMD5(String string) {
        return MD5Util.MD5(string);
    }

    private String getSerialNum(){
        try {
            Class<?> classZ = Class.forName("android.os.SystemProperties");
            Method get = classZ.getMethod("get", String.class);
            String serialNum = (String) get.invoke(classZ, "ro.serialno");
            return serialNum;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
