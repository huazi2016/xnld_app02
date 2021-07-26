package com.zxqy.xunilaidian.utils;

import android.app.Activity;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;


/**
 * 文件名：GTMatisseUtil
 * 创建者： Luochunlin
 * 创建日期： 2020/9/27 17:00
 * 邮箱： 1008611@qq.com
 * 描述： 暂时不写
 */
public class ZTMatisseUtil {
    //权限
    public static String[] PICTURE_PERMISSION = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 选择指定数量的照片
     */
    public static void getPhoto(Activity context, int num, int permission_code, int image_code) {
        PermissionUtils.checkAndRequestMorePermissions(context, PICTURE_PERMISSION, permission_code, () ->
                Matisse.from(context)
                        //选择视频和图片
//                        .choose(MimeType.ofAll())
                        //选择图片
                        .choose(MimeType.ofImage())
                        //选择视频
//                        .choose(MimeType.ofVideo())
                        .countable(true)
//                        .capture(true)//使用相机，和 captureStrategy 一起使用
//                        .captureStrategy(new CaptureStrategy(true,"PhotoPicker"))
                        .maxSelectable(num)
                        .spanCount(4)
                        .imageEngine(new GlideEngine()).forResult(image_code));
    }
}
