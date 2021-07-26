package com.zxqy.xunilaidian.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.zxqy.xunilaidian.utils.HttpUrl;


import java.util.List;

/**
 * 大图查看适配器
 */
public class FeedBigImgAdapter extends PagerAdapter {
    private Context context;
    private List<String> datas;
    private OnItemClickListener listener;

    public FeedBigImgAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(context);
        if (listener != null) {
            photoView.setOnClickListener(view -> listener.onItemClick());
        }
        String path = datas.get(position);
        if (!TextUtils.isEmpty(path)) {
            if (path.contains("http")) {
                // 阿里云图片，直接展示
                Glide.with(context).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).into(photoView);
            } else {
                // 原来的图片，拼接展示
//                String host = SpUtils.getInstance().getString(Contants.COMMON_URL, API.COMMON_URL);
//                String imgUrl = host.replace("app/", "") + path;
                Glide.with(context).load(HttpUrl.FILE_BASE_URI+path).diskCacheStrategy(DiskCacheStrategy.ALL).into(photoView);
            }
        }
        container.addView(photoView);
        return photoView;
    }

    /**
     * 获取当前数据的总页数
     */
    public int getSize() {
        return datas != null ? datas.size() : 0;
    }

    public interface OnItemClickListener {
        void onItemClick();
    }
}
