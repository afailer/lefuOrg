package com.lefuorgn.gov.activity;

import com.lefuorgn.base.BaseWebViewOnLineActivity;
import com.lefuorgn.gov.bean.MessageInfo;

/**
 * Created by liuting on 2016/12/26.
 */

public class MessageDetailActivity extends BaseWebViewOnLineActivity{

    MessageInfo messageInfo;

    @Override
    protected void getIntentInfo() {
        super.getIntentInfo();
        setToolBarTitle("公告通知");
        messageInfo = (MessageInfo) getIntent().getSerializableExtra("extra");
    }

    @Override
    protected String getWebUrl() {
        return "lefuyun/leaderNotificationCtr/toInfoPage?id="+messageInfo.getId();
    }

    @Override
    protected boolean hasStatusBar() {
        return true;
    }
    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected boolean hasToolBar() {
        return true;
    }
}
