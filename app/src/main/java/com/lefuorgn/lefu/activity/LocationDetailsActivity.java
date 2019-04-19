package com.lefuorgn.lefu.activity;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.lefuorgn.AppContext;
import com.lefuorgn.base.BaseWebViewActivity;

/**
 * 机构概况详情页面
 */

public class LocationDetailsActivity extends BaseWebViewActivity {

    @Override
    protected String getWebUrl() {
        return "lefuyun/agencyMapCtr/queryAgencyMapById?agency_id=" + AppContext.getInstance().getAgencyId();
    }

    @Override
    protected void addJavaScript(WebView webView) {
        webView.addJavascriptInterface(new LocationDetailsActivity.WebBtn(), "webBtn");
    }

    protected class WebBtn {

        @JavascriptInterface
        public void close() {
            finish();
        }

    }

}
