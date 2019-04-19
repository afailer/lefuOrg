package com.lefuorgn.oa.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.OaApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.util.PermissionManager;
import com.lefuorgn.oa.adapter.AttendanceApprovalAdapter;
import com.lefuorgn.oa.bean.AttendanceApply;
import com.lefuorgn.oa.bean.AttendanceApprovalType;
import com.lefuorgn.util.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 审批页面
 */

public class AttendanceApprovalActivity extends BaseActivity {

    private TextView mPointView;
    private AttendanceApprovalAdapter mAdapter;
    private int mStatisticsNum; // 网络请求数
    private boolean addPermission; // 添加请假权限

    @Override
    protected int getLayoutId() {
        return R.layout.activity_attendance_approval;
    }

    @Override
    protected void initView() {
        findViewById(R.id.rl_activity_attendance_approval).setOnClickListener(this);
        findViewById(R.id.ll_activity_attendance_approval).setOnClickListener(this);
        mPointView = (TextView) findViewById(R.id.tv_activity_attendance_approval_point);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_activity_attendance_approval);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(Color.parseColor("#D3D3D3"),
                DividerGridItemDecoration.TYPE_SP, 1));
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        mAdapter = new AttendanceApprovalAdapter(new ArrayList<AttendanceApprovalType>());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                if(!addPermission) {
                    // 无权限
                    showToast(R.string.permission_no);
                    return;
                }
                AttendanceApprovalType type = mAdapter.getItem(i);
                Intent intent = new Intent(AttendanceApprovalActivity.this, ClockAttendanceApprovalActivity.class);
                intent.putExtra("id", type.getId());
                intent.putExtra("name", type.getName());
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void initData() {
        setToolBarTitle("审批");
        addPermission = PermissionManager.hasPermission(PermissionManager.OA_SAVEASK + PermissionManager.P_C);
        showWaitDialog();
        mStatisticsNum = 0;
        OaApi.getAttendanceApprovalType(new RequestCallback<List<AttendanceApprovalType>>() {
            @Override
            public void onSuccess(List<AttendanceApprovalType> result) {
                mAdapter.setNewData(result);
                statisticsNetWork();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                statisticsNetWork();
                showToast(e.getMessage());
            }
        });
        getUnreadNum(true);
    }

    /**
     * 获取未读信息条目数
     */
    private void getUnreadNum(final boolean isShow) {
        OaApi.getMyAttendanceApply(1, 0, true, new RequestCallback<List<AttendanceApply>>() {
            @Override
            public void onSuccess(List<AttendanceApply> result) {
                String num = result.size() + "";
                mPointView.setText(num);
                mPointView.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);
                if(isShow) {
                    statisticsNetWork();
                }
            }

            @Override
            public void onFailure(ApiHttpException e) {
                mPointView.setVisibility(View.GONE);
                showToast(e.getMessage());
                if(isShow) {
                    statisticsNetWork();
                }
            }
        });
    }

    /**
     * 统计网络请求个数
     */
    private void statisticsNetWork() {
        mStatisticsNum++;
        if(mStatisticsNum == 2) {
            hideWaitDialog();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_activity_attendance_approval:
                // 我审批的
                intent = new Intent(this, MyAttendanceApplyActivity.class);
                intent.putExtra("approval", true);
                startActivityForResult(intent, 100);
                break;
            case R.id.ll_activity_attendance_approval:
                // 我申请的
                intent = new Intent(this, MyAttendanceApplyActivity.class);
                intent.putExtra("approval", false);
                startActivityForResult(intent, 100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200) {
            getUnreadNum(false);
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
