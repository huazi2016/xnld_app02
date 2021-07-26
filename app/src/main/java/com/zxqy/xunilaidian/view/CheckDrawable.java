package com.zxqy.xunilaidian.view;


import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;


public class CheckDrawable extends Drawable {
    private Paint mPaint;//圆形的画笔
    private Paint mTickPaint;//对勾的画笔
    private int radius;//圆的半径
    private Point mCenterPoint;
    private int tickColor;
    private int checkedColor;//选中时的颜色
    private int tickSize;//对号大小

    public CheckDrawable() {
        mPaint = new Paint();

        mTickPaint = new Paint();
        mTickPaint.setAntiAlias(true);

        mCenterPoint = new Point();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        mPaint.setColor(checkedColor);
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, radius, mPaint);//绘制选中时的圆
        //绘制对勾
        mTickPaint.setColor(tickColor);//在draw方法设置可以获取CircleCheckBox设置的颜色，而在构造函数中获取不到（下同理）
        mTickPaint.setStrokeWidth(tickSize);
        canvas.drawLine(mCenterPoint.x - radius * 3 / 5, mCenterPoint.y, mCenterPoint.x, mCenterPoint.y + radius / 2, mTickPaint);
        canvas.drawLine(mCenterPoint.x, mCenterPoint.y + radius / 2, mCenterPoint.x + radius * 3 / 5, mCenterPoint.y - radius * 2 / 5, mTickPaint);
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setCenterPoint(Point mCenterPoint) {
        this.mCenterPoint = mCenterPoint;
    }

    public void setTickColor(int tickColor) {
        this.tickColor = tickColor;
    }

    public void setCheckedColor(int checkedColor) {
        this.checkedColor = checkedColor;
    }

    public void setTickSize(int tickSize) {
        this.tickSize = tickSize;
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
