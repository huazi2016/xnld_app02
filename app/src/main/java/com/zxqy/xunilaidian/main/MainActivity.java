package com.zxqy.xunilaidian.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bilibili.magicasakura.utils.ThemeUtils;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.base.BaseActivity;
import com.zxqy.xunilaidian.bean.EventType;
import com.zxqy.xunilaidian.fragment.HomeFragment;
import com.zxqy.xunilaidian.fragment.MineFragment;
import com.zxqy.xunilaidian.fragment.WallpaperFragment;
import com.zxqy.xunilaidian.utils.UserEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

//import androidx.core.content.pm.ShortcutInfoCompatV2;
//import com.muugi.shortcut.core.ShortcutV2;
//import com.muugi.shortcut.setting.ShortcutPermission;
//import com.muugi.shortcut.utils.Logger;

/**
 * 系统自动添加应用Icon在快捷方式图标的右下角
 * <p>
 * 8.0以上系统自动，并且无法设置不添加
 * 8.0以前可以代码设置是否添加，@see ShortcutInfoCompat.Builder#setAlwaysBadged()
 * https://blog.csdn.net/scau_zhangpeng/article/details/88259464?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522161879608816780366579523%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fall.%2522%257D&request_id=161879608816780366579523&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~first_rank_v2~rank_v29-22-88259464.first_rank_v2_pc_rank_v29&utm_term=%E5%AE%89%E5%8D%93%E5%88%9B%E5%BB%BA%E6%A1%8C%E9%9D%A2%E5%BF%AB%E6%8D%B7%E6%96%B9%E5%BC%8F&spm=1018.2226.3001.4187
 */

@UserEvent
public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    HomeFragment homeFragment;
    MineFragment mineFragment;
    WallpaperFragment wallpaperFragment;
    @BindView(R.id.fragmentContent)
    FrameLayout fragmentContent;
    @BindView(R.id.mBottomNavigationView)
    BottomNavigationBar mBottomNavigationView;
    @BindView(R.id.ll_all)
    LinearLayout llAll;

    private static void launchActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void setRootView() {
//        PermissionMgr.grantPermission(this, permissions);
        setShowStatusBar(false);
        setContentView(R.layout.activity_main);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(int type) {
//        if (!event.equals("")) {
//            llAll.setVisibility(View.GONE);
//        }
        switch (type) {
            case EventType.SYSEIXT:
                finish();
                break;
        }
    }

    @Override
    protected void initView() {

        showIconFragment();
        int mainColor = ThemeUtils.getColorById(this, R.color.colorPrimary);
        initBottomNavigation(mainColor);

//        ShortcutV2.get().addPinShortcutListener(mCallback);
    }

    public void showIconFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(fragmentTransaction);
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
            fragmentTransaction.add(R.id.fragmentContent, homeFragment, "homeFragment");
        }
        commitShowFragment(fragmentTransaction, homeFragment);
    }


    public void showMoreFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(fragmentTransaction);
        if (mineFragment == null) {
            mineFragment = MineFragment.newInstance();
            fragmentTransaction.add(R.id.fragmentContent, mineFragment, "mineFragment");
        }
        commitShowFragment(fragmentTransaction, mineFragment);
    }

    public void showWallFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(fragmentTransaction);
        if (wallpaperFragment == null) {
            wallpaperFragment = WallpaperFragment.newInstance();
            fragmentTransaction.add(R.id.fragmentContent, wallpaperFragment, "wallpaperFragment");
        }
        commitShowFragment(fragmentTransaction, wallpaperFragment);
    }

    private void initBottomNavigation(int color) {
        mBottomNavigationView.setTabSelectedListener(this);
        mBottomNavigationView.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationView.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationView
                .addItem(
                        new BottomNavigationItem(R.drawable.icon_phone, "模拟来电")
                                .setActiveColor(color)
                                .setInActiveColor(Color.parseColor("#A5A5A5"))
                )
                .addItem(
                        new BottomNavigationItem(R.drawable.icon_lingsheng, "铃声")
                                .setActiveColor(color)
                                .setInActiveColor(Color.parseColor("#A5A5A5"))
                ).addItem(
                new BottomNavigationItem(R.drawable.icon_mine, "个人中心")
                        .setActiveColor(color)
                        .setInActiveColor(Color.parseColor("#A5A5A5"))
        );
        mBottomNavigationView.setFirstSelectedPosition(0);
        mBottomNavigationView.initialise();
    }


    public void hideAllFragment(FragmentTransaction fragmentTransaction) {
        hideFragment(fragmentTransaction, homeFragment);
        hideFragment(fragmentTransaction, mineFragment);
        hideFragment(fragmentTransaction, wallpaperFragment);
    }

    private void hideFragment(FragmentTransaction fragmentTransaction, Fragment fragment) {
        if (fragment != null) {
            fragmentTransaction.hide(fragment);
        }
    }

    public void commitShowFragment(FragmentTransaction fragmentTransaction, Fragment fragment) {
        fragmentTransaction.show(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initData() {

    }

    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case 0:
                showIconFragment();
                break;
            case 1:
                showWallFragment();
                break;
            case 2:
                showMoreFragment();
                break;
        }

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}