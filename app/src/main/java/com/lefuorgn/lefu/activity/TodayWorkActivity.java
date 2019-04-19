package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.view.View;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.util.AllocatingTypeTaskManager;
import com.lefuorgn.db.util.PermissionManager;
import com.lefuorgn.dialog.AlertDialog;

/**
 * 今日工作页面
 */

public class TodayWorkActivity extends BaseActivity {

    // 记录当前用户是否有配单权限
    private boolean addTaskPermission;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_today_work;
    }

    @Override
    protected void initView() {
        findViewById(R.id.tv_activity_today_work_distribute_leaflets_nursing).setOnClickListener(this);
        findViewById(R.id.tv_activity_today_work_order_taking_nursing).setOnClickListener(this);
        findViewById(R.id.tv_activity_today_work_execute_nursing).setOnClickListener(this);
        findViewById(R.id.tv_activity_today_work_distribute_leaflets_sign).setOnClickListener(this);
        findViewById(R.id.tv_activity_today_work_order_taking_sign).setOnClickListener(this);
        findViewById(R.id.tv_activity_today_work_execute_sign).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setToolBarTitle("今日工作");
        // 获取当前用户是否拥有发布配单的权限
        addTaskPermission = PermissionManager
                .hasPermission(PermissionManager.NURSING_TASK + PermissionManager.P_C);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_activity_today_work_distribute_leaflets_nursing:
                // 护理服务派单
                if(!addTaskPermission) {
                    showToast(R.string.permission_no);
                    return;
                }
                intent = new Intent(this, AllocatingTaskNursingActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_activity_today_work_order_taking_nursing:
                // 护理服务接单
                if(AllocatingTypeTaskManager.isExistExpiredData()) {
                    // 存在过期配单
                    new AlertDialog()
                            .setContent("您有未完成任务,请点击\"下班\"!")
                            .setConfirmBtnText("取消")
                            .setCancelBtnText("确认")
                            .setClickCallBack(new AlertDialog.ClickCallBack() {
                        @Override
                        public void cancel() {
                            // 确认
                            Intent intent = new Intent(TodayWorkActivity.this, ExecuteNursingActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void confirm() {}
                    }).show(getSupportFragmentManager(), "AlertDialog");
                }else {
                    intent = new Intent(this, OrderTakingNursingActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_activity_today_work_execute_nursing:
                // 护理服务执行
                intent = new Intent(this, ExecuteNursingActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_activity_today_work_distribute_leaflets_sign:
            case R.id.tv_activity_today_work_order_taking_sign:
            case R.id.tv_activity_today_work_execute_sign:
                // 以后实现模块
                showToast("敬请期待");
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
