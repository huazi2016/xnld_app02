package com.zxqy.xunilaidian.view;


import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

public class UnCheckDrawable extends Drawable {
    private Paint mPaint;
    private Paint mStrokePaint;//描边的画笔
    private int radius;//半径
    private Point mCenterPoint;
    private int unCheckedColor;//未选中时颜色
    private int strokeColor;//边框颜色
    private int strokeSize;//边框大小

    public UnCheckDrawable() {
        mStrokePaint = new Paint();
        mPaint = new Paint();

        mCenterPoint = new Point();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
//        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setColor(strokeColor);//在draw方法设置可以获取CircleCheckBox设置的颜色，而在构造函数中获取不到（下同理）
        mStrokePaint.setStrokeWidth(strokeSize);
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, radius, mStrokePaint);//绘制描边的圆环

        mPaint.setColor(unCheckedColor);
//        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, radius - strokeSize, mPaint);//绘制选中时的圆
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setCenterPoint(Point mCenterPoint) {
        this.mCenterPoint = mCenterPoint;
    }

    public void setUnCheckedColor(int unCheckedColor) {
        this.unCheckedColor = unCheckedColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public void setStrokeSize(int strokeSize) {
        this.strokeSize = strokeSize;
    }

    /**
     * 必须实现getIntrinsicWidth()和getIntrinsicHeight()两个方法，
     * 否则会出现控件的触摸背景波纹实现偏移，即控件不在触摸背景中间
     *
     * @return
     */
    @Override
    public int getIntrinsicWidth() {
        return mCenterPoint.x * 2;
    }

    @Override
    public int getIntrinsicHeight() {
        return mCenterPoint.y * 2;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
