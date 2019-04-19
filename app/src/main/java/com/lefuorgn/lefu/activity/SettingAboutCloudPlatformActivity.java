package com.lefuorgn.lefu.activity;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.lefuorgn.base.BaseWebViewActivity;

/**
 * 关于云平台
 */

public class SettingAboutCloudPlatformActivity extends BaseWebViewActivity {

    @Override
    protected String getWebUrl() {
        return "lefuyun/appointmentCtr/toLefuInfo";
    }

    @Override
    protected void addJavaScript(WebView webView) {
        webView.addJavascriptInterface(new SettingAboutCloudPlatformActivity.WebBtn(), "webBtn");
    }

    protected class WebBtn {

        @JavascriptInterface
        public void close() {
            finish();
        }

    }
}
