package com.zxqy.xunilaidian.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zxqy.xunilaidian.R;


import java.util.ArrayList;
import java.util.List;

/**
 * 反馈图片
 */
public class FeedImgAddAdapter extends RecyclerView.Adapter<FeedImgAddAdapter.MyViewHolder> {
    private int image_num;
    private List<String> datas;
    private Context context;
    private OnItemClickListener listener;

    public FeedImgAddAdapter(Context context, int num, List<String> datas, OnItemClickListener listener) {
        this.image_num = num;
        this.datas = datas;
        this.context = context;
        this.listener = listener;
        if (this.datas == null) {
            this.datas = new ArrayList<>();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gt_item_suggest_image, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position >= datas.size()) {
            holder.iv_del.setVisibility(View.GONE);
            holder.iv_pic.setImageResource(R.mipmap.gt_suggest_add);
            holder.iv_pic.setOnClickListener(v -> {
                listener.OnAddItemClick(image_num - datas.size());
            });
        } else {
            try {
                Glide.with(context).load(datas.get(position)).into(holder.iv_pic);
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.iv_pic.setOnClickListener(v -> {
                listener.OnItemClick(position);
            });
            holder.iv_del.setVisibility(View.VISIBLE);
            holder.iv_del.setOnClickListener(v -> {
                datas.remove(position);
                this.notifyDataSetChanged();
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size() < image_num ? datas.size() + 1 : datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_pic;
        private ImageView iv_del;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_pic = itemView.findViewById(R.id.iv_pic);
            iv_del = itemView.findViewById(R.id.iv_del);
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);

        void OnAddItemClick(int count);
    }

    /**
     * 添加数据
     */
    public void AddDatas(List<String> data) {
        if (data != null) {
            this.datas.addAll(data);
            this.notifyDataSetChanged();
        }
    }

    /**
     * 添加单条数据
     */
    public void AddAloneData(String string) {
        if (!TextUtils.isEmpty(string)) {
            this.datas.add(string);
            this.notifyItemInserted(this.datas.size() - 1);
        }
    }

    /**
     * 替换单条数据
     */
    public void repeleceData(String string, int position) {
        if (!TextUtils.isEmpty(string) && position < this.datas.size()) {
            this.datas.remove(position);
            this.datas.add(position, string);
            this.notifyItemChanged(position);
        }
    }

    public List<String> getDatas() {
        return datas;
    }
}
