package com.zxqy.xunilaidian.utils;

import android.app.AppOpsManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * By:Solidaye
 * QQ:35367278
 */
public class CheckShortCutUtil {
    private static final String MARK = Build.MANUFACTURER.toLowerCase();
    public static final int PERMISSION_GRANTED = 0;
    public static final int PERMISSION_DENIED = -1;
    public static final int PERMISSION_ASK = 1;
    public static final int PERMISSION_UNKNOWN = 2;

    public static int check(Context context) {
        int result = PERMISSION_UNKNOWN;
        if (MARK.contains("huawei")) {
            result = checkOnEMUI(context);
        } else if (MARK.contains("xiaomi")) {
            result = checkOnMIUI(context);
        } else if (MARK.contains("oppo")) {
            result = checkOnOPPO(context);
        } else if (MARK.contains("vivo")) {
            result = checkOnVIVO(context);
        } else if (MARK.contains("samsung") || MARK.contains("meizu")) {
            result = PERMISSION_GRANTED;
        }
        return result;
    }


    private static final String TAG = "ShortcutPermissionCheck";

    public static int checkOnEMUI(@NonNull Context context) {
        Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        try {
            Class<?> PermissionManager = Class.forName("com.huawei.hsm.permission.PermissionManager");
            Method canSendBroadcast = PermissionManager.getDeclaredMethod("canSendBroadcast", Context.class, Intent.class);
            boolean invokeResult = (boolean) canSendBroadcast.invoke(PermissionManager, context, intent);
            if (invokeResult) {
                return PERMISSION_GRANTED;
            } else {
                return PERMISSION_DENIED;
            }
        } catch (ClassNotFoundException e) {//Mutil-catch require API level 19
            return PERMISSION_UNKNOWN;
        } catch (NoSuchMethodException e) {
            return PERMISSION_UNKNOWN;
        } catch (IllegalAccessException e) {
            return PERMISSION_UNKNOWN;
        } catch (InvocationTargetException e) {
            return PERMISSION_UNKNOWN;
        }
    }

    public static int checkOnVIVO(@NonNull Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null) {
            return PERMISSION_UNKNOWN;
        }
        Uri parse = Uri.parse("content://com.bbk.launcher2.settings/favorites");
        Cursor query = contentResolver.query(parse, null, null, null, null);
        if (query == null) {
            return PERMISSION_UNKNOWN;
        }
        try {
            while (query.moveToNext()) {
                String titleByQueryLauncher = query.getString(query.getColumnIndexOrThrow("title"));
                if (!TextUtils.isEmpty(titleByQueryLauncher) && titleByQueryLauncher.equals(getAppName(context))) {
                    int value = query.getInt(query.getColumnIndexOrThrow("shortcutPermission"));
                    if (value == 1 || value == 17) {
                        return PERMISSION_DENIED;
                    } else if (value == 16) {
                        return PERMISSION_GRANTED;
                    } else if (value == 18) {
                        return PERMISSION_ASK;
                    }
                }
            }
        } catch (Exception e) {
        } finally {
            query.close();
        }
        return PERMISSION_UNKNOWN;
    }

    public static int checkOnMIUI(@NonNull Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return  PERMISSION_UNKNOWN;
        }
        try {
            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            String pkg = context.getApplicationContext().getPackageName();
            int uid = context.getApplicationInfo().uid;
            Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getDeclaredMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class);
            Object invoke = checkOpNoThrowMethod.invoke(mAppOps, 10017, uid, pkg);//the ops of INSTALL_SHORTCUT is 10017
            if (invoke == null) {
                return PERMISSION_UNKNOWN;
            }
            String result = invoke.toString();
            switch (result) {
                case "0":
                    return PERMISSION_GRANTED;
                case "1":
                    return PERMISSION_DENIED;
                case "5":
                    return PERMISSION_ASK;
            }
        } catch (Exception e) {
            return PERMISSION_UNKNOWN;
        }
        return PERMISSION_UNKNOWN;
    }

    public static int checkOnOPPO(@NonNull Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null) {
            return PERMISSION_UNKNOWN;
        }
        Uri parse = Uri.parse("content://settings/secure/launcher_shortcut_permission_settings");
        Cursor query = contentResolver.query(parse, null, null, null, null);
        if (query == null) {
            return PERMISSION_UNKNOWN;
        }
        try {
            String pkg = context.getApplicationContext().getPackageName();
            while (query.moveToNext()) {
                String value = query.getString(query.getColumnIndex("value"));
                if (!TextUtils.isEmpty(value)) {
                    if (value.contains(pkg + ", 1")) {
                        return  PERMISSION_GRANTED;
                    }
                    if (value.contains(pkg + ", 0")) {
                        return  PERMISSION_DENIED;
                    }
                }
            }
            return  PERMISSION_UNKNOWN;
        } catch (Exception e) {
            return  PERMISSION_UNKNOWN;
        } finally {
            query.close();
        }
    }

    private static String getAppName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }
}
