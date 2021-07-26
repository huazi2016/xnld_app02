package com.zxqy.xunilaidian.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.os.Environment.DIRECTORY_PICTURES;

public class FileUtil {
    public static String getImageFolderPath(Context mContext) {
        String path;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File externalFilesDir = mContext.getExternalFilesDir(DIRECTORY_PICTURES);
            if (externalFilesDir == null) {
                path = Environment.getExternalStorageDirectory().getPath() + "/Android/data/"
                        + getPackageName(mContext) + "/files";
            } else {
                path = externalFilesDir.getPath() + "/files";
            }
        } else {
            path = mContext.getFilesDir().getPath() + "/files";
        }
        Log.i("dizhi", "externalFileDir = " + path);
        return path;
    }
    public static void saveImageForFile (Context context, Bitmap bitmap, Handler handler){
        String path = FileUtil.getImageFolderPath(context);
        File fileList = new File(path);
        if (!fileList.exists() && !fileList.mkdir()) {
            Log.e("CJT", "创建缓存文件失败");
            return;
        }
        File file = new File(path + "/" + System.currentTimeMillis() + ".png");
        try {
            if (!file.exists() && !file.createNewFile()) {
                file.createNewFile();
            } else {

            }
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Message message = new Message();
            message.what = 0;
            message.obj = file.getAbsolutePath();
            handler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
