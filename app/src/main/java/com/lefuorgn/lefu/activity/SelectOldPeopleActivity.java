package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.lefu.fragment.SelectOldPeopleFragment;
import com.lefuorgn.util.TLog;

/**
 * 护理月报和随手拍老人选择页面
 */

public class SelectOldPeopleActivity extends BaseActivity {

    public static final int RESULT_SELECT_OLDPEOPLE = 0x500;
    public static final String RESULT_OLDPEOPLE_NAME = "result_oldpeople_name";

    private SelectOldPeopleFragment mFragment;
    private CheckBox mCheckBox;
    private boolean attention;
    private String ids = "";

    @Override
    protected void onBeforeSetContentLayout() {
        ids = getIntent().getStringExtra("ids");
        attention = getIntent().getBooleanExtra("attention", false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_oldpeople;
    }

    @Override
    protected void initView() {
        setToolBarTitle("选择老人");
        mCheckBox = (CheckBox) findViewById(R.id.cb_activity_select_elderly);
        findViewById(R.id.btn_activity_select_elderly).setOnClickListener(this);

    }

    @Override
    protected void initData() {
        mFragment = (SelectOldPeopleFragment) getSupportFragmentManager()
                .findFragmentById(R.id.f_activity_select_elderly);
        mCheckBox.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cb_activity_select_elderly:
                mFragment.onAllBtnClick(mCheckBox.isChecked());
                break;
            case R.id.btn_activity_select_elderly:
                String ids = mFragment.getSelectIds();
                Intent intent = new Intent();
                intent.putExtra(RESULT_OLDPEOPLE_NAME, ids);
                setResult(RESULT_SELECT_OLDPEOPLE, intent);
                finish();
                break;
        }
    }

    /**
     * 设置全选按钮当前的额状态
     * @param isChecked 是否所有的选项被选中
     */
    public void onAllBtnState(boolean isChecked) {
        mCheckBox.setChecked(isChecked);
    }

    public boolean isAttention() {
        return attention;
    }

    public String getIds() {
        return ids;
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
