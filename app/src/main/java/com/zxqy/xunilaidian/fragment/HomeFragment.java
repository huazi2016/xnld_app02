package com.zxqy.xunilaidian.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.activity.CallPageActivity;
import com.zxqy.xunilaidian.activity.CallerInfoActivity;
import com.zxqy.xunilaidian.base.BaseFragment;
import com.zxqy.xunilaidian.entity.CallItemEntity;
import com.zxqy.xunilaidian.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class HomeFragment extends BaseFragment {

    @BindView(R.id.ivHelp)
    ImageView ivHelp;
    @BindView(R.id.rcHomeList)
    RecyclerView rcHomeList;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_icon;
    }

    @Override
    public void initUI() {
        ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showLongToast("帮助");
                //VirtualCallService.Companion.start(context, false);
                CallPageActivity.launchActivity(context);
            }
        });
        List<CallItemEntity> dataList = setDataInfo();
        rcHomeList.setLayoutManager(new LinearLayoutManager(context));
        rcHomeList.setAdapter(new HomeListAdapter(R.layout.item_home_list, dataList));
    }

    @Override
    public void initData() {

    }


    @Override
    protected boolean isLoadData() {

        return false;
    }


    @Override
    protected void initView() {

    }

    @Override
    protected void lazyLoad() {

    }

    @NotNull
    private List<CallItemEntity> setDataInfo() {
        List<CallItemEntity> dataList = new ArrayList();
        for (int i = 0; i < 5; i++) {
            CallItemEntity itemBo01 = new CallItemEntity();
            itemBo01.itemId = i;
            switch (i) {
                case 0: {
                    itemBo01.title = "时间";
                    itemBo01.imgId = R.drawable.icon_about;
                    itemBo01.desc = "15秒之后";
                    dataList.add(itemBo01);
                    break;
                }
                case 1: {
                    itemBo01.title = "来电者";
                    itemBo01.imgId = R.drawable.icon_about;
                    itemBo01.desc = "10086";
                    dataList.add(itemBo01);
                    break;
                }
                case 2: {
                    itemBo01.title = "铃声和振动";
                    itemBo01.imgId = R.drawable.icon_about;
                    itemBo01.desc = "can you feel my world";
                    dataList.add(itemBo01);
                    break;
                }
                case 3: {
                    itemBo01.title = "来电语音内容";
                    itemBo01.imgId = R.drawable.icon_about;
                    itemBo01.desc = "luyin001";
                    dataList.add(itemBo01);
                    break;
                }
                case 4: {
                    itemBo01.title = "壁纸";
                    itemBo01.imgId = R.drawable.icon_about;
                    itemBo01.desc = "华为";
                    dataList.add(itemBo01);
                    break;
                }
            }
        }
        return dataList;
    }

    class HomeListAdapter extends BaseQuickAdapter<CallItemEntity, BaseViewHolder> {

        public HomeListAdapter(int layoutResId, @Nullable List<CallItemEntity> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, CallItemEntity itemBo) {
            TextView tvItemName = holder.getView(R.id.tvItemName);
            TextView tvItemHint = holder.getView(R.id.tvItemHint);
            tvItemName.setText(itemBo.title);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (itemBo.itemId) {
                        case 0: {
                            //时间
                            new XPopup.Builder(getContext())
                                    //.maxHeight(800)
                                    .isDarkTheme(false)
                                    .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                                    .asCenterList("请选择", new String[]{"5秒", "30秒", "60秒", "2分钟", "5分钟", "10分钟", "20分钟", "30分钟", "60分钟"},
                                            new OnSelectListener() {
                                                @Override
                                                public void onSelect(int position, String text) {
                                                    tvItemHint.setText(text + "后");
                                                }
                                            })
                                    .show();
                            break;
                        }
                        case 1: {
                            //来电者
                            CallerInfoActivity.launchActivity(context);
                            break;
                        }
                        case 2: {
                            Toast.makeText(mContext, "03", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case 3: {
                            Toast.makeText(mContext, "04", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case 4: {
                            Toast.makeText(mContext, "05", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
            });
        }
    }
}
