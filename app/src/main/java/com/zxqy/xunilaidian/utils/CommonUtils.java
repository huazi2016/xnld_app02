package com.zxqy.xunilaidian.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.ColorRes;

import com.blankj.utilcode.util.StringUtils;
import com.zxqy.xunilaidian.R;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: lixh
 * @CreateDate: 2019/4/3 10:30
 * @Version: 1.0
 */
public class CommonUtils {

    public static boolean checkEmaile(String emaile) {
        /**
         *   [^abc]取非 除abc以外的任意字符
         *   |  将两个匹配条件进行逻辑“或”（Or）运算
         *   [1-9] 1到9 省略123456789
         *    邮箱匹配 eg: ^[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\.){1,3}[a-zA-z\-]{1,}$
         *
         */
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(RULE_EMAIL);
        //正则表达式的匹配器
        Matcher m = p.matcher(emaile);
        //进行正则匹配\
        return m.matches();
    }

    public static boolean isEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(email);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }


    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        return min + "";
    }

    public static @ColorRes
    int getThemeColorId(Context context, String theme) {
        return context.getResources().getIdentifier(theme + "_backup", "color", context.getPackageName());
    }

    public static String getTheme(Context context) {
        if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_STORM) {
            return "blue";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_HOPE) {
            return "purple";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_WOOD) {
            return "green";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_LIGHT) {
            return "green_light";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_THUNDER) {
            return "yellow";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_SAND) {
            return "orange";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_FIREY) {
            return "red";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_SAKURA) {
            return "pink";
        }
        return null;
    }

    public static int getCId(Context context) {
        if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_STORM) {
            return R.color.blue;
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_HOPE) {
            return R.color.purple;
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_WOOD) {
            return R.color.green;
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_LIGHT) {
            return R.color.green_light;
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_THUNDER) {
            return R.color.yellow;
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_SAND) {
            return R.color.orange;
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_FIREY) {
            return R.color.red;
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_SAKURA) {
            return R.color.pink;
        }
        return R.color.blue;
    }

    /**
     * @Author lixh
     * @Date 2019/5/29 15:14
     * @Description: 根据颜色id 获取16进制颜色
     */
    public static String changeColor(int id, Context context) {
        StringBuffer stringBuffer = new StringBuffer();
        int color = context.getResources().getColor(id);
        stringBuffer.append("#");
        stringBuffer.append(Integer.toHexString(Color.alpha(color)));
        stringBuffer.append(Integer.toHexString(Color.red(color)));
        stringBuffer.append(Integer.toHexString(Color.green(color)));
        stringBuffer.append(Integer.toHexString(Color.blue(color)));
        return stringBuffer.toString();
    }

    //判断文件是否存在
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }



    public static String getDeviceIdByDate() {
        Calendar Cld = Calendar.getInstance();
        int YY = Cld.get(Calendar.YEAR);
        int MM = Cld.get(Calendar.MONTH) + 1;
        int DD = Cld.get(Calendar.DATE);
        int HH = Cld.get(Calendar.HOUR_OF_DAY);
        int mm = Cld.get(Calendar.MINUTE);
        int SS = Cld.get(Calendar.SECOND);
        int MI = Cld.get(Calendar.MILLISECOND);
        //由整型而来,因此格式不加0,如  2016/5/5-1:1:32:694
        String date = (YY + "").substring(2, 4) + "/" + MM + "/" + DD + "-" + HH + ":" + mm + ":" + SS + ":" + MI;
        return date;
    }



    /**
     * @Author lixh
     * @Date 2020/4/21 19:08
     * @Description: 生成概率
     */
    public static boolean generateRandom(String hprat) {
        Random random = new Random();
        int generateNum = random.nextInt(100) + 1;
        if (Integer.valueOf(hprat) < generateNum) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 隐藏键盘
     */
    public static void hideInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        View v = activity.getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
