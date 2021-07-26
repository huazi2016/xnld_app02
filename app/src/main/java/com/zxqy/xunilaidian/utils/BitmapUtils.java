package com.zxqy.xunilaidian.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.widget.RelativeLayout;

import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.bean.ImageBean;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BitmapUtils {
    public static Bitmap   drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }


    public static Bitmap loadBitmapFromView(RelativeLayout layout) {
        int w = layout.getWidth();
        int h = layout.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.parseColor("#f4f4f4"));
//        layout.layout(0, 0, w, h);
        layout.draw(c);
        return bmp;
    }
    public static Bitmap returnUrlBitMap(final String url) {
        Bitmap bitmap = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(url));
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static List<Integer> getTuijian(Context mContext){
        List<Integer> drawableList = new ArrayList<>();
        drawableList.add(R.drawable.icon1);
        drawableList.add(R.drawable.icon2);
        drawableList.add(R.drawable.icon3);
        drawableList.add(R.drawable.icon4);
        drawableList.add(R.drawable.icon5);
        drawableList.add(R.drawable.icon6);
        drawableList.add(R.drawable.icon7);
        drawableList.add(R.drawable.icon8);
        drawableList.add(R.drawable.icon9);
        drawableList.add(R.drawable.icon10);
        return drawableList;
    }
    public static List<Integer> getIconDongwu(Context mContext){
        List<Integer> drawableList = new ArrayList<>();
        drawableList.add(R.drawable.icondong1);
        drawableList.add(R.drawable.icondong2);
        drawableList.add(R.drawable.icondong3);
        drawableList.add(R.drawable.icondong4);
        drawableList.add(R.drawable.icondong5);
        drawableList.add(R.drawable.icondong6);
        drawableList.add(R.drawable.icondong7);
        drawableList.add(R.drawable.icondong8);
        drawableList.add(R.drawable.icondong9);
        drawableList.add(R.drawable.icondong10);
        return drawableList;
    }
    public static List<Integer> getMusicIcon(Context mContext){
        List<Integer> drawableList = new ArrayList<>();
        drawableList.add(R.drawable.music1);
        drawableList.add(R.drawable.music2);
        drawableList.add(R.drawable.music3);
        drawableList.add(R.drawable.music4);
        drawableList.add(R.drawable.music5);
        drawableList.add(R.drawable.music6);
        drawableList.add(R.drawable.music7);
        drawableList.add(R.drawable.music8);
        drawableList.add(R.drawable.music9);
        drawableList.add(R.drawable.music10);
        drawableList.add(R.drawable.music11);
        drawableList.add(R.drawable.music12);
        drawableList.add(R.drawable.music13);
        return drawableList;
    }
    public static List<Integer>getFengjing(Context mContext){
        List<Integer> drawableList = new ArrayList<>();
        drawableList.add(R.drawable.fengjing1);
        drawableList.add(R.drawable.fengjing2);
        drawableList.add(R.drawable.fengjing3);
        drawableList.add(R.drawable.fengjing4);
        drawableList.add(R.drawable.fengjing5);
        drawableList.add(R.drawable.fengjing6);
        drawableList.add(R.drawable.fengjing7);
        drawableList.add(R.drawable.fengjing8);
        drawableList.add(R.drawable.fengjing9);
        drawableList.add(R.drawable.fengjing10);
        drawableList.add(R.drawable.fengjing11);
        drawableList.add(R.drawable.fengjing12);
        drawableList.add(R.drawable.fengjing13);
        drawableList.add(R.drawable.fengjing14);
        return drawableList;
    }
    public static List<Integer>getBiaoqing(Context mContext){
        List<Integer> drawableList = new ArrayList<>();
        drawableList.add(R.drawable.biaoqing1);
        drawableList.add(R.drawable.biaoqing2);
        drawableList.add(R.drawable.biaoqing3);
        drawableList.add(R.drawable.biaoqing4);
        drawableList.add(R.drawable.biaoqing5);
        drawableList.add(R.drawable.biaoqing6);
        drawableList.add(R.drawable.biaoqing7);
        drawableList.add(R.drawable.biaoqing8);
        drawableList.add(R.drawable.biaoqing9);
        drawableList.add(R.drawable.biaoqing10);
        drawableList.add(R.drawable.biaoqing11);
        drawableList.add(R.drawable.biaoqing12);
        drawableList.add(R.drawable.biaoqing13);
        return drawableList;
    }
    public static List<Integer>getDoc(Context mContext){
        List<Integer> drawableList = new ArrayList<>();
        drawableList.add(R.drawable.doc1);
        drawableList.add(R.drawable.doc2);
        drawableList.add(R.drawable.doc3);
        drawableList.add(R.drawable.doc4);
        drawableList.add(R.drawable.doc5);
        drawableList.add(R.drawable.doc6);
        drawableList.add(R.drawable.doc7);
        drawableList.add(R.drawable.doc8);
        drawableList.add(R.drawable.doc9);
        drawableList.add(R.drawable.doc10);
        drawableList.add(R.drawable.doc11);
        drawableList.add(R.drawable.doc12);
        drawableList.add(R.drawable.doc13);
        drawableList.add(R.drawable.doc14);
        drawableList.add(R.drawable.doc15);
        return drawableList;
    }
    public static List<Integer>getFruitDrinks(Context mContext){
        List<Integer> drawableList = new ArrayList<>();
        drawableList.add(R.drawable.fruit_drinks1);
        drawableList.add(R.drawable.fruit_drinks2);
        drawableList.add(R.drawable.fruit_drinks3);
        drawableList.add(R.drawable.fruit_drinks4);
        drawableList.add(R.drawable.fruit_drinks5);
        drawableList.add(R.drawable.fruit_drinks6);
        drawableList.add(R.drawable.fruit_drinks7);
        drawableList.add(R.drawable.fruit_drinks8);
        drawableList.add(R.drawable.fruit_drinks9);
        drawableList.add(R.drawable.fruit_drinks10);
        drawableList.add(R.drawable.fruit_drinks11);
        drawableList.add(R.drawable.fruit_drinks12);
        drawableList.add(R.drawable.fruit_drinks13);
        drawableList.add(R.drawable.fruit_drinks14);
        return drawableList;
    }
    public static List<Integer>getWeather(Context mContext){
        List<Integer> drawableList = new ArrayList<>();
        drawableList.add(R.drawable.weathe1);
        drawableList.add(R.drawable.weathe2);
        drawableList.add(R.drawable.weathe3);
        drawableList.add(R.drawable.weathe4);
        drawableList.add(R.drawable.weathe5);
        drawableList.add(R.drawable.weathe6);
        drawableList.add(R.drawable.weathe7);
        drawableList.add(R.drawable.weathe8);
        drawableList.add(R.drawable.weathe9);
        drawableList.add(R.drawable.weathe10);
        drawableList.add(R.drawable.weathe11);
        return drawableList;
    }
    public static List<Integer>getWalldongman(Context mContext){
        List<Integer> drawableList = new ArrayList<>();
        drawableList.add(R.drawable.dongman1);
        drawableList.add(R.drawable.dongman2);
        drawableList.add(R.drawable.dongman3);
        drawableList.add(R.drawable.dongman4);
        drawableList.add(R.drawable.dongman5);
        drawableList.add(R.drawable.dongman6);
        drawableList.add(R.drawable.dongman7);
        return drawableList;
    }

    public static List<Integer> getWallFengjing(Context context) {
        List<Integer> drawableList = new ArrayList<>();
        drawableList.add(R.drawable.fengjingwall1);
        drawableList.add(R.drawable.fengjingwall2);
        drawableList.add(R.drawable.fengjingwall3);
        drawableList.add(R.drawable.fengjingwall4);
        drawableList.add(R.drawable.fengjingwall5);
        drawableList.add(R.drawable.fengjingwall6);
        drawableList.add(R.drawable.fengjingwall7);
        return drawableList;
    }
    public static List<Integer> getWallstart(Context context) {
        List<Integer> drawableList = new ArrayList<>();
        drawableList.add(R.drawable.start1);
        drawableList.add(R.drawable.start2);
        drawableList.add(R.drawable.start3);
        drawableList.add(R.drawable.start4);
        drawableList.add(R.drawable.start5);
        drawableList.add(R.drawable.start6);
        drawableList.add(R.drawable.start7);
        return drawableList;
    }
    public static List<Integer> getDongWu(Context context) {
        List<Integer> drawableList = new ArrayList<>();
        drawableList.add(R.drawable.dongwu1);
        drawableList.add(R.drawable.dongwu2);
        drawableList.add(R.drawable.dongwu3);
        drawableList.add(R.drawable.dongwu4);
        drawableList.add(R.drawable.dongwu5);
        drawableList.add(R.drawable.dongwu6);
        drawableList.add(R.drawable.dongwu7);
        return drawableList;
    }
    public static List<Integer> getScience(Context context) {
        List<Integer> drawableList = new ArrayList<>();
        drawableList.add(R.drawable.science1);
        drawableList.add(R.drawable.science2);
        drawableList.add(R.drawable.science3);
        drawableList.add(R.drawable.science4);
        drawableList.add(R.drawable.science5);
        drawableList.add(R.drawable.science6);
        drawableList.add(R.drawable.science7);
        return drawableList;
    }
    public static List<Integer> getSport(Context context) {
        List<Integer> drawableList = new ArrayList<>();
        drawableList.add(R.drawable.sprot1);
        drawableList.add(R.drawable.sport2);
        drawableList.add(R.drawable.sport3);
        drawableList.add(R.drawable.sport4);
        drawableList.add(R.drawable.sport5);
        drawableList.add(R.drawable.sport6);
        drawableList.add(R.drawable.sport7);
        return drawableList;
    }
    public static List<ImageBean>getWallBg(Context mContext){
        List<ImageBean> list = new ArrayList<>();
        list.add(new ImageBean("风景", R.drawable.fengjingbg));
        list.add(new ImageBean("动漫", R.drawable.dongbg));
        list.add(new ImageBean("明星", R.drawable.starbg));
        list.add(new ImageBean("动物", R.drawable.dongwubg));
        list.add(new ImageBean("科幻", R.drawable.sciencebg));
        list.add(new ImageBean("体育", R.drawable.sportbg));
        return list;
    }
    public static List<ImageBean>getIconOption(Context mContext){
        List<ImageBean> list = new ArrayList<>();
        list.add(new ImageBean("热门图标", R.drawable.tubiao));
        list.add(new ImageBean("手机相册", R.drawable.xiangce));
        list.add(new ImageBean("文字设置", R.drawable.wenzi));
        list.add(new ImageBean("手机应用", R.drawable.yingyong));
        list.add(new ImageBean("近期使用", R.drawable.later));
        return list;
    }
}
