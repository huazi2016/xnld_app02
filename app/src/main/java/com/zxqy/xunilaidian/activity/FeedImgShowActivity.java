package com.zxqy.xunilaidian.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.gyf.immersionbar.ImmersionBar;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.adapter.FeedBigImgAdapter;
import com.zxqy.xunilaidian.base.BaseGTActivity;
import com.zxqy.xunilaidian.view.MultiViewpager;

import java.util.Arrays;
import java.util.List;

public class FeedImgShowActivity extends BaseGTActivity {
    public static final String IMG_DATAS = "datas";
    public static final String IMG_POS = "pos";
    private TextView mTitleText;
    private MultiViewpager mViewPager;
    private FeedBigImgAdapter mAdapter;
    private int mPosition = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gt_activity_image_show);

        initView();
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).init();
    }

    /**
     * 初始化
     */
    private void initView() {

        ImageView back = findViewById(R.id.iv_back);
        mTitleText = findViewById(R.id.tv_title);
        mViewPager = findViewById(R.id.viewPager);
        back.setOnClickListener(v -> onBackPressed());
        initViewPager();
    }

    /**
     * 初始化viewPager
     */
    private void initViewPager() {
        mPosition = getIntent().getIntExtra(IMG_POS, 0);
//        List<ImageBean> mPics = GsonUtils.getFromList(getIntent().getStringExtra(IMG_DATAS), ImageBean.class);
        List<String> mPics = Arrays.asList(getIntent().getStringExtra(IMG_DATAS).split(","));
        int index = mPosition + 1;
        mTitleText.setText("图片展示(" + index + "/" + mPics.size() + ")");
        mAdapter = new FeedBigImgAdapter(this, mPics);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPosition < mPics.size() ? mPosition : 0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                int index = position + 1;
                mTitleText.setText("图片展示(" + index + "/" + mAdapter.getSize() + ")");
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }
}
