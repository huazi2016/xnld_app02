package com.zxqy.xunilaidian.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.zxqy.xunilaidian.R;


/********************
 *  @创建者 ：  lj
 *  @时间   ：  2019\7\18 0018.
 *  @描述   ：  隐私政策服务协议
 *********************/
public class BrowserUsActivity extends AppCompatActivity {

    private ProgressBar bProgressBarLoading;
    private WebView bWebView;
    private TextView tx_title;
    private String URL = "file:///android_asset/conceal_new.html";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_browser);
//        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        ImmersionBar.with(this).statusBarDarkFont(true).transparentStatusBar().fullScreen(false).init();
        bProgressBarLoading = findViewById(R.id.b_progressBarLoading);
        bWebView = findViewById(R.id.b_webView);
        tx_title = findViewById(R.id.tv_title);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bWebView.canGoBack()) {
                    // 返回键退回
                    bWebView.goBack();
                }else {
                    finish();
                }
            }
        });


        try {
            String url = getIntent().getStringExtra("URL");
            if (!TextUtils.isEmpty(url)) {
                URL = url;
            }
            String name = getIntent().getStringExtra("name");
            if (!TextUtils.isEmpty(name)) {
                tx_title.setText(name);
            }
        } catch (Exception e) {
            URL = "file:///android_asset/conceal_new.html";
        }

        bProgressBarLoading.setMax(100);
        bWebView.getSettings().setUseWideViewPort(true);
        bWebView.getSettings().setJavaScriptEnabled(true);
        //设置可以支持缩放
        bWebView.getSettings().setSupportZoom(true);
        bWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        Log.i("测试一下", "URL=" + URL);
        bWebView.loadUrl(URL);
        bWebView.setWebChromeClient(new WebChromeClient());
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        bWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //设置加载进度条
                view.setWebChromeClient(new WebChromeClientProgress());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //添加需要监听的WebView内部的图片
                super.onPageFinished(view, url);
            }
        });
    }

    /**
     * 按键响应，在WebView中查看网页时，检查是否有可以前进的历史记录。
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && bWebView.canGoBack()) {
            // 返回键退回
            bWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebChromeClientProgress extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int progress) {
            if (bProgressBarLoading != null) {
                bProgressBarLoading.setProgress(progress);
                if (progress == 100) bProgressBarLoading.setVisibility(View.GONE);
                Log.i("测试一下", "=2=" + System.currentTimeMillis() + ";;;;" + progress);
            }
            super.onProgressChanged(view, progress);
        }
    }
}
