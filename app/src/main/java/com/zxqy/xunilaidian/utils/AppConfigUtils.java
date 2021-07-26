package com.zxqy.xunilaidian.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;


/**
 * 系统配置公共资源工具类
 */
public class AppConfigUtils {

    /**
     * 判断某个开关是否打开
     * @param code 开关代号   后台配置
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
     * 复制内容到剪切板
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
}