package com.zxqy.xunilaidian.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/3/2.
 */

public class DeviceUtils {

    // 保存文件的路径
    private static final String CACHE_IMAGE_DIR = "aray/cache/devices";

    // 保存的文件 采用隐藏文件的形式进行保存
    private static final String DEVICES_FILE_NAME = "GEETOL_DEVICES";

    // sp存储deviceId
    public static final String GEETOL_DEVICE_ID = "marcy_device_id";

    /**
     * dp 转化为 px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px 转化为 dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 验证手机号是否合法
     */
    public static boolean isMobileNO(String mobile) {
        //移动号段正则表达式
        String pat1 = "^1[0-9]{10}$";

        Pattern pattern1 = Pattern.compile(pat1);
        Matcher match1 = pattern1.matcher(mobile);
        return match1.matches();
    }

    /**
     * 获取设备宽度（px）
     *
     * @param context
     * @return
     */
    public static int deviceWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取设备高度（px）
     *
     * @param context
     * @return
     */
    public static int deviceHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * SD卡判断
     *
     * @return
     */
    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 是否有网
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    public static String getSimName(Context context) {
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String operator = telManager.getSimOperator();

        if (operator != null) {

            if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {

                return "中国移动";

            } else if (operator.equals("46001")) {

                return "中国联通";

            } else if (operator.equals("46003")) {

                return "中国电信";

            }
        }
        return "中国移动";
    }

    /**
     * VersionName
     * 对应build.gradle中的versionName
     * 返回版本名字
     * 对应build.gradle中的versionName
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * PackageName
     * 获取应用项目包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        String packageName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            packageName = packInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageName;
    }


    /**
     * VersionCode
     * 对应build.gradle中的versionCode
     * 返回版本号
     * 对应build.gradle中的versionCode
     *
     * @param context
     * @return
     */
    public static String getVersionCode(Context context) {
        String versionCode = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = String.valueOf(packInfo.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取设备的唯一标识，deviceId
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {
        String deviceId = null;

        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (deviceId == null) {
            return "10001020";
        } else {
            return deviceId;
        }
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getPhoneBrand() {
        return Build.BRAND;
    }
    //手机型号

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }
    //系统Android API等级

    /**
     * 获取手机Android API等级（22、23 ...）
     *
     * @return
     */
    public static int getBuildLevel() {
        return Build.VERSION.SDK_INT;
    }
    //系统android-版本">系统Android 版本

    /**
     * 获取手机Android 版本（4.4、5.0、5.1 ...）
     *
     * @return
     */
    public static String getBuildVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取当前App进程的id
     *
     * @return
     */
    public static int getAppProcessId() {
        return android.os.Process.myPid();
    }


    /**
     * 获取当前App进程的Name
     *
     * @param context
     * @param processId
     * @return
     */
    public static String getAppProcessName(Context context, int processId) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取所有运行App的进程集合
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = context.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == processId) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));

                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                Log.e("DeviceUtils", e.getMessage(), e);
            }
        }
        return processName;
    }

    /**
     * 获取当前网络连接类型
     *
     * @param context
     * @return
     */
    public static String getNetworkState(Context context) {
//获取系统的网络服务
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//如果当前没有网络
        if (null == connManager)
            return "没有网络";
//获取当前网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return "没有网络";
        }
// 判断是不是连接的是不是wifi
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return "WIFI";
                }
        }
// 如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null != networkInfo) {
            NetworkInfo.State state = networkInfo.getState();
            String strSubTypeName = networkInfo.getSubtypeName();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    switch (activeNetInfo.getSubtype()) {
//如果是2g类型
                        case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return "2G";
//如果是3g类型
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return "3G";
//如果是4g类型
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return "4G";
                        default:
//中国移动 联通 电信 三种3G制式
                            if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA") || strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                return "3G";
                            } else {
                                return "3G";
                            }
                    }
                }
        }
        return "没有网络";
    }

    /**
     * 获取app数据存储deviceId
     */
    public static String getSpDeviceId() {
        return SpUtils.getInstance().getString(Constants.DEVICE_ID);
    }

    /**
     * 存储deviceId到app数据
     */
    public static void putSpDeviceId(String deviceId) {
        SpUtils.getInstance().putString(Constants.DEVICE_ID, deviceId);
    }


    /**
     * 读取sd卡中保存的设备唯一标识符
     */
    public static String readDeviceID(Context context) {
        File file = getDevicesDir(context);
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            Reader in = new BufferedReader(isr);
            int i;
            while ((i = in.read()) > -1) {
                buffer.append((char) i);
            }
            in.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 统一处理设备唯一标识 保存的文件的地址
     */
    private static File getDevicesDir(Context context) {
        File cropFile;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cropDir = new File(Environment.getExternalStorageDirectory(), CACHE_IMAGE_DIR);
            if (!cropDir.exists()) {
                cropDir.mkdirs();
            }
            cropFile = new File(cropDir, DEVICES_FILE_NAME);
        } else {
            File cropDir = new File(context.getFilesDir(), CACHE_IMAGE_DIR);
            if (!cropDir.exists()) {
                cropDir.mkdirs();
            }
            cropFile = new File(cropDir, DEVICES_FILE_NAME);
        }
        return cropFile;
    }

    /**
     * 获取IMEI(Android版本28及28以下)
     */
    @SuppressLint("HardwareIds")
    public static String getIMEI(Context context) {
        if (Build.VERSION.SDK_INT <= 28) {
            try {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                        == PackageManager.PERMISSION_GRANTED) {
                    try {
                        // 适配双卡情况
                        if (tm != null) {
                            Method method = tm.getClass().getMethod("getImei", int.class);
                            if (!TextUtils.isEmpty((String) method.invoke(tm, 0))) {
                                return (String) method.invoke(tm, 0);
                            } else {
                                return getUUID();
                            }
                        } else {
                            return getUUID();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (tm != null) {
                            if (!TextUtils.isEmpty(tm.getDeviceId())) {
                                return tm.getDeviceId();
                            } else {
                                return getUUID();
                            }
                        } else {
                            return getUUID();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return getUUID();
            }
        }
        return getUUID();
    }
    /**
     * 获取UUID
     */
    public static String getUUID() {
        String uuid;
        UUID id = UUID.randomUUID();
        uuid = id.toString().replace("-", "");
        //为了统一格式对设备的唯一标识进行md5加密，最终生成32位字符串
        return getMD5(uuid, false);
    }
    /**
     * 对特定的内容进行 md5 加密
     * @param message  加密明文
     * @param upperCase  加密以后的字符串是是大写还是小写  true 大写  false 小写
     */
    private static String getMD5(String message, boolean upperCase) {
        String md5str = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] input = message.getBytes();
            byte[] buff = md.digest(input);
            md5str = bytesToHex(buff, upperCase);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5str;
    }

    private static String bytesToHex(byte[] bytes, boolean upperCase) {
        StringBuffer md5str = new StringBuffer();
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];
            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        if (upperCase) {
            return md5str.toString().toUpperCase();
        }
        return md5str.toString().toLowerCase();
    }

    /**
     * 保存内容到SD卡中
     */
    public static void saveDeviceID(String str, Context context) {
        if (TextUtils.isEmpty(str)) return;
        File file = getDevicesDir(context);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            Writer out = new OutputStreamWriter(fos, "UTF-8");
            out.write(str);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
