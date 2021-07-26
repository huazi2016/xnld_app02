package com.zxqy.xunilaidian.utils.httputil.callback;

import android.content.Context;

import com.zxqy.xunilaidian.utils.Constants;
import com.zxqy.xunilaidian.utils.DeviceUtils;
import com.zxqy.xunilaidian.utils.MyLog;

/**
 * Created by Administrator on 2017/10/11.
 */

public class CPResourceUtils {
    private static Context mContext;

    public static void init(Context context){
        if (context != null){
            mContext = context;
        }else {
            MyLog.e(Constants.PUB_LOG_TAG,"未初始化lib");
            return;
        }
    }

    /**
     *          通过反射获取layout
     * @param paramString
     * @return
     */
    public static int getLayoutId(String paramString){
        return mContext.getResources().getIdentifier(paramString,"layout",mContext.getPackageName());
    }

    /**
     *          通过反射获取string
     * @param paramString
     * @return
     */
    public static int getStringId(String paramString){
        return mContext.getResources().getIdentifier(paramString,"string",mContext.getPackageName());
    }

    /**
     *          通过反射获取drawable
     * @param paramString
     * @return
     */
    public static int getDrableId(String paramString){
        return mContext.getResources().getIdentifier(paramString,"drawable",mContext.getPackageName());
    }

    /**
     *          通过反射获取style
     * @param paramString
     * @return
     */
    public static int getStyleId(String paramString){
        return mContext.getResources().getIdentifier(paramString,"style",mContext.getPackageName());
    }

    /**
     *          通过反射获取id
     * @param paramString
     * @return
     */
    public static int getId(String paramString){
        return mContext.getResources().getIdentifier(paramString,"id",mContext.getPackageName());
    }

    /**
     *          通过反射获取color
     * @param paramString
     * @return
     */
    public static int getColor(String paramString){
        return mContext.getResources().getIdentifier(paramString,"color",mContext.getPackageName());
    }

    /**
     *          通过反射获取array
     * @param paramString
     * @return
     */
    public static int getArrayId(String paramString){
        return mContext.getResources().getIdentifier(paramString,"array",mContext.getPackageName());
    }

    /**
     *          获取资源目录值
     * @param key
     * @return
     */
    public static String getString(String key){
        return mContext.getResources().getString(getStringId(key));
    }

    /**
     *          获取设备标识码
     * @return
     */
    public static String getDevice(){
        return DeviceUtils.getSpDeviceId();
    }

    /**
     *          根据assets资源拿取id和key
     * @return
     */
//    public static String getFromAssetsForIdKey(){
//        try {
//            String encoding = "GBK";
//            InputStreamReader inputStreamReader = new InputStreamReader(mContext.getResources().getAssets().open("app.xml"),encoding);
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            String line = "";
//            String result = "";
//            while ((line = bufferedReader.readLine())!=null){
//                result += line.toString().trim();
//            }
//            return result.trim();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }



}
