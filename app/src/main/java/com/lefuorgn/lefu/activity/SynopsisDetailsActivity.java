package com.lefuorgn.lefu.activity;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.lefuorgn.AppContext;
import com.lefuorgn.api.remote.common.RemoteUtil;
import com.lefuorgn.base.BaseWebViewActivity;
import com.lefuorgn.util.ShareUtils;

/**
 * 机构概况详情页面
 */

public class SynopsisDetailsActivity extends BaseWebViewActivity {

    @Override
    protected String getWebUrl() {
        return "lefuyun/agencyIntroCtr/toInfoPage?id=" + AppContext.getInstance().getAgencyId();
    }

    @Override
    protected void addJavaScript(WebView webView) {
        webView.addJavascriptInterface(new SynopsisDetailsActivity.WebBtn(), "webBtn");
    }

    protected class WebBtn {

        // 分享
        @JavascriptInterface
        public void share() {
            String name = AppContext.getInstance().getAgencyName();
            String url = RemoteUtil.getAbsoluteApiUrl(getWebUrl());
            ShareUtils.share(SynopsisDetailsActivity.this, name, name, "", url, true);
        }
        @JavascriptInterface
        public void close() {
            finish();
        }

    }

}
