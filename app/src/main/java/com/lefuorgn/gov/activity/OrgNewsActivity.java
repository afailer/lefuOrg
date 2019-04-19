package com.lefuorgn.gov.activity;

import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseWebViewActivity;
import com.lefuorgn.gov.bean.LeaderNews;
import com.lefuorgn.util.StringUtils;

public class OrgNewsActivity extends BaseWebViewActivity {

    String url;
    TextView webTheme,webTime;
    @Override
    protected String getWebUrl() {
        return url;
    }

    @Override
    protected void getIntentInfo() {
        super.getIntentInfo();
        url=getIntent().getStringExtra("url");
        LeaderNews leaderNews= (LeaderNews) getIntent().getSerializableExtra("extra");
        webTheme= (TextView) findViewById(R.id.web_theme);
        webTime= (TextView) findViewById(R.id.web_time);
        webTheme.setText(leaderNews.getTheme());
        webTime.setText(StringUtils.getFormatData(leaderNews.getCreate_dt(),"yyyy年MM月dd日"));
    }

    @Override
    protected void initData() {
        setToolBarTitle("民生政话");
    }

    @Override
    protected boolean hasBackButton() {
        return true;
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
    protected int getLayoutId() {
        return R.layout.gov_web_view;
    }
}
