package com.zxqy.xunilaidian.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 分享工具类
 */
public class ShareUtils {

    /**
     * 分享文字
     * @param shareText 分享的文字内容
     * @param shareUrl 分享的链接
     */
    public static void shareText(Context context, String shareText, String shareUrl) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareText + shareUrl);
        context.startActivity(Intent.createChooser(intent, "分享"));
    }

    // 调用系統方法分享文件
    public static void shareFile(Context context, String info) {
        /* if (null != file && file.exists()) {*/
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, /*Uri.fromFile(file)*/info);
        share.setType("text/plain"/*getMimeType(file.getAbsolutePath())*/);
        //此处可发送多种文件
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(share, "分享文件"));
    }

    /**
     * 分享图片
     *
     * @param file 图片地址
     * @param authority AndroidManifest配置里面provider的authority参数
     */
    public static void shareImage(Context context, File file, String authority) {
        Uri uri = Uri.parse(file.getAbsolutePath());
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, authority, file);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(intent, "分享"));
    }

    /**
     * 分享多张图片(微信7.0以上版本不支持多图分享)
     * @param files 图片列表
     * @param authority AndroidManifest配置里面provider的authority参数
     */
    public static void shareMultiImage(Context context, List<File> files, String authority) {
        ArrayList<Uri> uris = new ArrayList<>();
        Intent mulIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        mulIntent.setType("image/jpeg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            for (File file : files) {
                Uri uri = FileProvider.getUriForFile(context, authority, file);
                uris.add(uri);
            }
            mulIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            for (File file : files) {
                uris.add(Uri.parse(file.getAbsolutePath()));
            }
        }
        mulIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        context.startActivity(Intent.createChooser(mulIntent, "分享"));
    }
}
