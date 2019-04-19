package com.lefuorgn.gov.activity;

import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseWebViewActivity;
import com.lefuorgn.gov.Utils.GovUtils;
import com.lefuorgn.gov.bean.OrgActive;
import com.lefuorgn.util.StringUtils;

public class OrgActiveActivity extends BaseWebViewActivity {
    String url;
    TextView webTheme,webTime;
    @Override
    protected String getWebUrl() {
        return url;
    }

    @Override
    protected void getIntentInfo() {
        super.getIntentInfo();
        setToolBarTitle("活动详情");
        url = getIntent().getStringExtra("url");
        OrgActive orgActive= (OrgActive) getIntent().getSerializableExtra("extra");
        webTheme= (TextView) findViewById(R.id.web_theme);
        webTime= (TextView) findViewById(R.id.web_time);
        webTheme.setText(orgActive.getTheme());
        webTime.setText(StringUtils.getFormatData(orgActive.getHoldTime(),"yyyy年MM月dd日"));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.gov_web_view;
    }

    @Override
    protected boolean hasToolBar() {
        return true;
    }

    @Override
    protected boolean hasStatusBar() {
        return true;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }
}
