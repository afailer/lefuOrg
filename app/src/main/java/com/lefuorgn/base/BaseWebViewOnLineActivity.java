package com.lefuorgn.base;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lefuorgn.R;
import com.lefuorgn.api.ApiOkHttp;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.api.remote.common.RemoteUtil;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;

import java.util.HashMap;
import java.util.Map;

/**
 * WebView基类, 此方法封装了WebView一些基本信息设置
 * 可以通过复写getWebUrl()方法设置url,此url要包含要传递的参数
 * 当你想添加与JavaScript交互的方法,你可以调用addJavaScript()方法
 * 当有前一个页面的信息传递过来,你可以在getIntentInfo()方法中获取你传递过来的信息
 */
public abstract class BaseWebViewOnLineActivity extends BaseActivity {

    private static final int TITLE_HEIGHT = 15;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        final WebView webView = (WebView) findViewById(R.id.wv_activity_web_view);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.pb_activity_web_view);
        // 获取Intent中的内容
        getIntentInfo();
        String uri = RemoteUtil.getAbsoluteApiUrl(getWebUrl());
        if(StringUtils.isEmpty(uri)) {
            throw new IllegalArgumentException(
                    "The url is null or \"\" you should check override getWebUrl()");
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            if(uri.contains("?")) {
                uri = uri + "&titlehight=" + TITLE_HEIGHT;
            }else {
                uri = uri + "?titlehight=" + TITLE_HEIGHT;
            }

        }
        final String url = uri;
        final WebSettings settings = webView.getSettings();
        String userAgentString = settings.getUserAgentString();
        if(!userAgentString.contains("lefuAppO")){
            userAgentString += RemoteUtil.getUserAgent();
        }
        settings.setUserAgentString(userAgentString);
        settings.setJavaScriptEnabled(true);
        addJavaScript(webView);
        // 防止打开系统默认浏览器
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if(newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        LefuApi.isLogin(new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                synCookies(url);
                Map<String, String> map = new HashMap<String, String>();
                map.put("User-Agent", settings.getUserAgentString());
                webView.loadUrl(url, map);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                TLog.log(e.getMessage());
            }
        });
    }

    /**
     * 添加JavaScript接口
     * @param webView 当前WebView对象
     */
    protected void addJavaScript(WebView webView) {
    }

    /**
     * 获取当前页面的url地址
     * @return 不包括host
     */
    protected abstract String getWebUrl();

    /**
     * 这个方法运行在WebView配置前面
     */
    protected void getIntentInfo() {}

    /**
     * 同步sessionID
     * @param url 当前请求url
     */
    private void synCookies(String url) {
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(this);
        cookieSyncManager.startSync();
        CookieManager cookieManager = CookieManager.getInstance();
        // 添加cookie
        cookieManager.setCookie(url, ApiOkHttp.getConfig().cookieJar.getCookie());
        cookieSyncManager.sync();
    }

    @Override
    protected boolean hasStatusBar() {
        return false;
    }

}
