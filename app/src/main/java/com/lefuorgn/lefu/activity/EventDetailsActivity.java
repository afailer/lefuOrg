package com.lefuorgn.lefu.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.lefuorgn.api.remote.common.RemoteUtil;
import com.lefuorgn.base.BaseWebViewActivity;
import com.lefuorgn.lefu.bean.Event;
import com.lefuorgn.util.ShareUtils;

/**
 * 院方活动详情页
 */

public class EventDetailsActivity extends BaseWebViewActivity {

    private Event mEvent;

    @Override
    protected void getIntentInfo() {
        Intent intent = getIntent();
        mEvent = (Event) intent.getSerializableExtra("event");
    }

    @Override
    protected String getWebUrl() {
        return "lefuyun/agencyActivites/toInfoPage?id=" + mEvent.getId();
    }

    @SuppressLint("AddJavascriptInterface")
    @Override
    protected void addJavaScript(WebView webView) {
        webView.addJavascriptInterface(new WebBtn(), "webBtn");
    }

    protected class WebBtn {

        // 分享
        @JavascriptInterface
        public void share() {
            String url = RemoteUtil.getAbsoluteApiUrl(getWebUrl());
            String imgUrl = RemoteUtil.IMG_URL + mEvent.getPic();
            ShareUtils.share(EventDetailsActivity.this, mEvent.getTheme(), mEvent.getReserved(), imgUrl, url,false);
        }
        @JavascriptInterface
        public void close() {
            finish();
        }

    }
}
