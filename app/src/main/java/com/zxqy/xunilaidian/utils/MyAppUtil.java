package com.zxqy.xunilaidian.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class MyAppUtil {


    public static String listToString1(List<String> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + " ");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 验证手机号是否合法
     *
     * @return
     */
    public static boolean isMobileNO(String mobile) {
        String pat1 = "1[0-9]{10}";
        Pattern pattern1 = Pattern.compile(pat1);
        Matcher match1 = pattern1.matcher(mobile);
        boolean isMatch1 = match1.matches();
        if (isMatch1) {
            return true;
        }
        return false;
    }

    /**
     * 复制内容到剪切板
     *
     * @param content 要复制的内容
     */
    public static void setPrimaryClip(Context context, String content, String toast) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData clipData = ClipData.newPlainText("content", content);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(clipData);
        if (!TextUtils.isEmpty(toast)) {
            ToastUtils.showLongToast(toast);
        }
    }

    /**
     * 显示居中的toast
     */

  /*  public static void showCenterToast(String text) {
        Toast toast = new Toast(MyApplication.getInstance());
        View view = LayoutInflater.from(MyApplication.getInstance()).inflate(R.layout.custom_toast, null);
        TextView textView = view.findViewById(R.id.tv_toast);
        textView.setText(text);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }*/


    /**
     * 判断是否是今日首次启动APP
     *
     * @param context
     * @return
     */
    public static boolean isTodayFirstStartApp(Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences("NB_TODAY_FIRST_START_APP", context.MODE_PRIVATE);
            String svaeTime = preferences.getString("startAppTime", "2020-01-01");
            String todayTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            Log.e("weather", "svaeTime：" + svaeTime);
            Log.e("weather", "todayTime：" + todayTime);

            if (!TextUtils.isEmpty(todayTime) && !TextUtils.isEmpty(svaeTime)) {
                if (!svaeTime.equals(todayTime)) {
                    preferences.edit().putString("startAppTime", todayTime).commit();
                    return true;
                }
            }

        } catch (Exception e) {
            Log.e("weather", "是否为今日首次启动APP,获取异常：" + e.toString());
            return true;
        }
        return false;

    }

    /**
     * 判断今日是否签到
     *
     * @param context
     * @return
     */
    public static boolean isTodayReport(Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences("NB_TODAY_REPORT", context.MODE_PRIVATE);
            String svaeTime = preferences.getString("reportTime", "2020-01-01");
            String todayTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            Log.e("weather", "svaeTime：" + svaeTime);
            Log.e("weather", "todayTime：" + todayTime);

            if (!TextUtils.isEmpty(todayTime) && !TextUtils.isEmpty(svaeTime)) {
                if (svaeTime.equals(todayTime)) {
                    return true;
                }
            }

        } catch (Exception e) {
            Log.e("weather", "今日是否签到,获取异常：" + e.toString());
            return true;
        }
        return false;

    }

    public static String getTime(Date date) {//可根据需要自行截取数据显示
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * list去掉重复数据
     */
    public static List<String> removeDuplicate(List<String> list) {
        Set set = new LinkedHashSet<String>();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Bitmap类型的图片转化成file类型
     *
     * @param bm
     * @param fileName
     * @return
     */
    public static File saveFile(Bitmap bm, String fileName) throws IOException {
        String path = Environment.getExternalStorageDirectory() + "/Ask";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;

    }

    /**
     * 将控件的内容保存为Bitmap
     *
     * @param v
     * @return
     */
    public static Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    /**
     * 检测是否安装微信
     */
    public static boolean isWeChatInstalled(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> packageInfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (packageInfo != null) {
            for (int i = 0; i < packageInfo.size(); i++) {
                String pn = packageInfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 判断某个开关是否打开
     *
     * @param code 开关代号
     */
    public static boolean isSwtOpen(String code) {
       /* if (DataSaveUtils.getInstance().getSwt() != null && DataSaveUtils.getInstance().getSwt().size() > 0) {
            for (Swt swt : DataSaveUtils.getInstance().getSwt()) {
                if (swt.getCode().equals(code) && swt.getVal1() == 1) {
                    return true;
                }
            }
        }*/
        return false;
    }

    /**
     * 判断是否是合格的手机号码
     */
    public static boolean judgePhoneQual(String number) {
        return number
                .matches("^(\\+86-|\\+86|86-|86){0,1}1[2|3|4|5|6|7|8|9]{1}\\d{9}$");
    }

    /**
     * 判断是否是合格的ip
     */
    public static boolean isIP(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(addr);

        boolean ipAddress = mat.find();

        //============对之前的ip判断的bug在进行判断
        if (ipAddress == true) {
            String ips[] = addr.split("\\.");

            if (ips.length == 4) {
                try {
                    for (String ip : ips) {
                        if (Integer.parseInt(ip) < 0 || Integer.parseInt(ip) > 255) {
                            return false;
                        }

                    }
                } catch (Exception e) {
                    return false;
                }

                return true;
            } else {
                return false;
            }
        }

        return ipAddress;
    }

    /**
     * 是否合格的身份证号码
     *
     * @param IDStr
     * @return
     */
    public static String IDCardValidate(String IDStr) {
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            return errorInfo;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。";
                return errorInfo;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return errorInfo;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return errorInfo;
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        Hashtable<String, String> h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return errorInfo;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return errorInfo;
            }
        } else {
            return "";
        }
        // =====================(end)=====================
        return "";
    }

    // 1、正则表达式
    public static boolean isNumeric(String str) {
        if (TextUtils.isEmpty(str))
            return false;

        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 功能：判断字符串是否为日期格式
     *
     * @param strDate
     * @return
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：设置地区编码
     *
     * @return HashTable 对象
     */
    private static Hashtable<String, String> GetAreaCode() {
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideSoftInputFromWindow(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(INPUT_METHOD_SERVICE);
        try {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 将px值转换为dp值
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获得屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 保留小数点后2位
     */
    public static String keepTwo(double b) {
        DecimalFormat format = new DecimalFormat("#0.00");
        return format.format(b);
    }
}
