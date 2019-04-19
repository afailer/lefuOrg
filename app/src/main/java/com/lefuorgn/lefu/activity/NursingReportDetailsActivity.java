package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.lefuorgn.api.ApiOkHttp;
import com.lefuorgn.base.BaseWebViewOnLineActivity;

/**
 * 护理月报详情页面
 */

public class NursingReportDetailsActivity extends BaseWebViewOnLineActivity {

    private long mId;

    @Override
    protected void getIntentInfo() {
        Intent intent = getIntent();
        mId = intent.getLongExtra("id", 0);
    }

    @Override
    protected String getWebUrl() {
        return "lefuyun/dailyNursingRecordCtr/DailyNursingView?nurs_items_id=" + mId;
    }

    @Override
    protected void addJavaScript(WebView webView) {
        webView.addJavascriptInterface(new NursingReportDetailsActivity.WebBtn(), "webBtn");
        ApiOkHttp.getConfig().cookieJar.getCookie();
    }

    protected class WebBtn {

        @JavascriptInterface
        public void close() {
            finish();
        }

    }

}
