package com.zxqy.xunilaidian.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CommonUtil {
    public static void getTextScreen(Handler handler,RelativeLayout layout, Context context) {
        Bitmap bitmap = BitmapUtils.loadBitmapFromView(layout);
        String path = FileUtil.getImageFolderPath(context);
        File fileList = new File(path);
        if (!fileList.exists() && !fileList.mkdir()) {
            Log.e("CJT", "创建缓存文件失败");
            return;
        }
        File file = new File(path + "/" + "textIcon" + ".png");
        try {
            if (!file.exists() ) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
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

    public static void openOtherApp(Context context,String name) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(name);
        if (intent != null) {
            intent.putExtra("type", "110");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

}

