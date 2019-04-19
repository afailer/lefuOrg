package com.lefuorgn.lefu.activity;

import android.view.View;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.api.remote.common.RemoteUtil;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.util.ShareUtils;
import com.lefuorgn.util.StringUtils;

/**
 * 生成邀请码页面
 */

public class BuildInviteCodeActivity extends BaseActivity {

    private TextView mCodeView;
    private OldPeople mOldPeople;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_build_invite_code;
    }

    @Override
    protected void initView() {
        mCodeView = (TextView) findViewById(R.id.tv_activity_build_invite_code_code);
        findViewById(R.id.tv_activity_build_invite_code_copy).setOnClickListener(this);
        findViewById(R.id.tv_activity_build_invite_code_btn).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setToolBarTitle("邀请码");
        mOldPeople = (OldPeople) getIntent().getSerializableExtra("oldPeople");
        String number = StringUtils.randomBuildNumber(mOldPeople.getId());
        mCodeView.setText(number);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_activity_build_invite_code_copy:
                // 复制到剪切板上
                StringUtils.copyToShearPlate(this, mCodeView.getText().toString());
                showToast("复制成功");
                break;
            case R.id.tv_activity_build_invite_code_btn:
                // 前往邀请,即分享
                String msg = "输入邀请码:" + mCodeView.getText().toString() + "关注您的家人" + mOldPeople.getElderly_name();
                String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/socialPeopleCtr/toShareOldPeople");
                url = url + "?oid=" + mOldPeople.getId() + "&type=2&invite=" + mCodeView.getText().toString();
                ShareUtils.share(this, "孝心无界·关爱随行", msg, "", url, true);
                break;
        }
    }

    @Override
    protected boolean hasToolBar() {
        return true;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }
}
