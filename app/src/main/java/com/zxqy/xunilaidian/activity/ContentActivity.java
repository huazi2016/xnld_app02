package com.zxqy.xunilaidian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.zxqy.xunilaidian.Constants;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.fragment.WebFragment;


public class ContentActivity extends AppCompatActivity {
    private OnBackPressLinstener mOnBackPressLinstener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        //   setStatusBarDarkMode();
        swichFragment(getIntent());
    }

    public void setStatusBarDarkMode() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public void swichFragment(Intent intent) {
        int fragmentKey = intent.getIntExtra(Constants.KEY_FRAGMENT, 0);
        switch (fragmentKey) {
//            case Constants.ABOUT_FRAGMENT:
////                replaceFragment(AboutFragment.newInstance());
//                break;
//            case Constants.WEB_FRAGMENT:
//                replaceFragment(WebFragment.newInstance(intent.getStringExtra(Constants.KEY_URL), intent.getStringExtra(Constants.KEY_TITLE)));
//                break;
//            case Constants.FAQ_FRAGMENT:
//                break;
//            case Constants.FEEDBACK_FRAGMENT:
//                replaceFragment(FeedBackFragment.newInstance());
//                break;
//            case Constants.FREE_FRAGMENT:
//                replaceFragment(FreeFragment.newInstance());
//                break;
//            case Constants.DYNAMIC_FRAGMENT:
//                replaceFragment(DynamicFragment.newInstance());
//                break;
//            case Constants.MAGIC_FRAGMENT:
//                replaceFragment(AnimationFragment.newInstance());
//                break;
//            case Constants.TEACHE_FRAGMENT:
//                replaceFragment(TeacherFragment.newInstance());
//                break;
//            case Constants.TEACHE_DRAW_FRAGMENT:
//                replaceFragment(TeacherDrawFragment.newInstance(getIntent().getIntExtra(Constants.KEY_IMAGEID,0),getIntent().getIntExtra(Constants.KEY_STUDYID,0)));
//                break;
//            case Constants.HISTORY_FRAGMENT:
//                replaceFragment(HistoryFragment.newInstance());
//                break;
//            case Constants.FILL_FRAGMENT:
//                replaceFragment(FillFragment.newInstance(getIntent().getIntExtra(Constants.KEY_IMAGEID,0)));
//                break;
            case Constants.WEB_FRAGMENT:
                String title = intent.getStringExtra("协议名称");
                replaceFragment(WebFragment.newInstance(intent.getStringExtra(Constants.KEY_URL), title));
                break;
            case Constants.TEXT_FRAGMENT:
                break;
            case Constants.LATER_FRAGMENT:
                break;
            case Constants.TUBIAO_FRAGMENT:
                break;
            case Constants.VOICE_FRAGMENT:
                break;
            default:
//                LogUtils.d("Not found fragment:" + Integer.toHexString(fragmentKey));
                break;
        }
    }

    public void setOnBackPressLinstener(OnBackPressLinstener mOnBackPressLinstener) {
        this.mOnBackPressLinstener = mOnBackPressLinstener;
    }

    public void replaceFragment(Fragment fragmnet) {
        replaceFragment(R.id.fragmentContent, fragmnet);
    }

    public void replaceFragment(@IdRes int id, Fragment fragment) {

        getSupportFragmentManager().beginTransaction().replace(id, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (mOnBackPressLinstener != null) {
            mOnBackPressLinstener.onBackPress();
        } else {
            finish();
        }
    }

    public void OnBack(View view) {

        finish();
    }


    public interface OnBackPressLinstener {
        void onBackPress();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
