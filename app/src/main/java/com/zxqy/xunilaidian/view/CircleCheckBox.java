package com.zxqy.xunilaidian.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.utils.DensityUtil;


public class CircleCheckBox extends AppCompatCheckBox {
    private CheckDrawable checkDrawable;
    private UnCheckDrawable unCheckDrawable;
    private Point point;
    private int mRadiusSize;//半径大小
    private int mPaddingSize;//padding大小
    private OnCircleCheckedChangeListener listener;
    private boolean isChecked;//是否选中的标记
    private AlertDialog.Builder builder;
    public CircleCheckBox(Context context) {
        this(context, null);
    }

    public CircleCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.checkboxStyle);//第三个参数是AppCompatCheckBox的默认Style参数，可以达到触摸反馈的波纹效果
    }

    public CircleCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取APP主题样式中的colorAccent颜色
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        int mDefaultCheckedColor = typedValue.data;

        @SuppressLint("Recycle") TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CircleCheckBox);
        //圆的实际半径
        mRadiusSize = ta.getDimensionPixelSize(R.styleable.CircleCheckBox_radius_size, DensityUtil.dip2px(getContext(), 10));
        //圆的实际padding
        mPaddingSize = ta.getDimensionPixelSize(R.styleable.CircleCheckBox_padding_size, DensityUtil.dip2px(getContext(), 5));
        //对号大小
        int tickSize = ta.getDimensionPixelSize(R.styleable.CircleCheckBox_tick_size, DensityUtil.dip2px(getContext(), 1));
        //未选中时，边框大小
        int strokeSize = ta.getDimensionPixelSize(R.styleable.CircleCheckBox_stroke_size, DensityUtil.dip2px(getContext(), 1));
        //对号的实际颜色
        int mTickColor = ta.getColor(R.styleable.CircleCheckBox_tickColor, Color.WHITE);
        int checkedColor = ta.getColor(R.styleable.CircleCheckBox_checkedColor, mDefaultCheckedColor);
        int unCheckedColor = ta.getColor(R.styleable.CircleCheckBox_uncheckedColor, Color.WHITE);
        int strokeColor = ta.getColor(R.styleable.CircleCheckBox_strokeColor, Color.parseColor("#EDEDED"));

        point = new Point();

        checkDrawable = new CheckDrawable();
        unCheckDrawable = new UnCheckDrawable();

        checkDrawable.setTickSize(tickSize);
        checkDrawable.setTickColor(mTickColor);
        checkDrawable.setCheckedColor(checkedColor);

        unCheckDrawable.setUnCheckedColor(unCheckedColor);
        unCheckDrawable.setStrokeColor(strokeColor);
        unCheckDrawable.setStrokeSize(strokeSize);

        isChecked = isChecked();
        if (isChecked()) {
            setButtonDrawable(checkDrawable);
        } else {
            setButtonDrawable(unCheckDrawable);
        }
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = !isChecked;
                if (listener != null) {
                    listener.onCircleCheckedChange(isChecked);
                }
            }
        });

    }


    public void setCircleChecked(boolean checked) {
            setChecked(checked);
        if (checked) {
            setButtonDrawable(checkDrawable);
        } else {
            setButtonDrawable(unCheckDrawable);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 获取宽-测量规则的模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        // 获取高-测量规则的模式和大小
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 设置wrap_content的默认宽 / 高值
        // 默认宽/高的设定并无固定依据,根据需要灵活设置
        // 类似TextView,ImageView等针对wrap_content均在onMeasure()对设置默认宽 / 高值有特殊处理,具体读者可以自行查看
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {//当布局参数设置都为wrap_content时，设置默认值
            setMeasuredDimension((mRadiusSize + mPaddingSize) * 2, (mRadiusSize + mPaddingSize) * 2);
            point.x = mRadiusSize + mPaddingSize;
            point.y = mRadiusSize + mPaddingSize;
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {//宽布局参数为wrap_content时，设置宽为默认值
            setMeasuredDimension((mRadiusSize + mPaddingSize) * 2, heightSize);
            point.x = mRadiusSize + mPaddingSize;
            point.y = heightSize / 2;
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {//高布局参数为wrap_content时，设置高为默认值
            setMeasuredDimension(widthSize, (mRadiusSize + mPaddingSize) * 2);
            point.x = widthSize / 2;
            point.y = mRadiusSize + mPaddingSize;
        } else {
            point.x = widthSize / 2;
            point.y = heightSize / 2;
        }
        checkDrawable.setRadius(mRadiusSize);
        checkDrawable.setCenterPoint(point);

        unCheckDrawable.setRadius(mRadiusSize);
        unCheckDrawable.setCenterPoint(point);
    }

    public void setOnCircleCheckedChangeListener(OnCircleCheckedChangeListener listener) {
        this.listener = listener;
    }

    public interface OnCircleCheckedChangeListener {
        void onCircleCheckedChange(boolean isChecked);
    }
}
