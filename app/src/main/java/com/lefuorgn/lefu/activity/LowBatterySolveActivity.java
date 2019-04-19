package com.lefuorgn.lefu.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.lefu.adapter.AlarmSolverAdapter;
import com.lefuorgn.lefu.bean.AlarmEntryDetails;
import com.lefuorgn.lefu.bean.AlarmEntrySolver;
import com.lefuorgn.lefu.bean.AlarmEntryWarning;
import com.lefuorgn.lefu.fragment.AlarmInformationDetailsFragment;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 告警信息处理中详细内容展示页面
 */

public class LowBatterySolveActivity extends BaseActivity {

    private TextView mNameView, mDetailsView;
    private EditText mRemarkView;

    private long mId; // 告警信息ID
    private String mAgencyName; // 机构名称

    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_low_battery_solve;
    }

    @Override
    protected void initView() {
        mNameView = (TextView) findViewById(R.id.tv_activity_alarm_solve_name);
        mDetailsView = (TextView) findViewById(R.id.tv_activity_alarm_solve_details);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_activity_alarm_solve);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 根据状态初始化按钮控件以及处理结果控件
        int status = getIntent().getIntExtra("status", 0);
        mRemarkView = (EditText) findViewById(R.id.et_activity_alarm_solve);
        TextView btn = (TextView) findViewById(R.id.btn_activity_alarm_solve_processing);
        if(status == AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_UNRESOLVED) {
            // 未处理
            btn.setText("接收报警");
            btn.setOnClickListener(this);
            mRemarkView.setVisibility(View.VISIBLE);
        }else {
            btn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        mId = getIntent().getLongExtra("id", 0);
        long agencyId = getIntent().getLongExtra("agencyId", 0);
        mAgencyName = getIntent().getStringExtra("agencyName");
        String title = getIntent().getStringExtra("title");
        setToolBarTitle(title);
        showWaitDialog();
        LefuApi.getAlarmEntryDetails(mId, agencyId, new RequestCallback<AlarmEntryDetails>() {
            @Override
            public void onSuccess(AlarmEntryDetails result) {
                hideWaitDialog();
                if(result == null) {
                    showToast("警告详情获取失败");
                    return;
                }
                initPageInfo(result);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                hideWaitDialog();
            }
        });
    }

    /**
     * 设置页面内容详情
     */
    private void initPageInfo(AlarmEntryDetails details) {
        AlarmEntryWarning warning = details.getSosWarningForm();
        if(warning != null) {
            mNameView.setText(warning.getOlder_name());
            String date = StringUtils.getFormatData(warning.getTime(), "yyyy-MM-dd HH:mm");
            String status;
            if(warning.getStatus() == AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_UNRESOLVED) {
                status = "未处理";
            }else if(warning.getStatus() == AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_SOLVING) {
                status = "处理中";
            }else if(warning.getStatus() == AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_SOLVED) {
                status = "已处理";
            }else {
                status = "";
            }
            String sb = "身份证号: " + warning.getDocument_number() + "\n" +
                    "告警时间: " + date + "\n" +
                    "告警内容: " + "电量告警: " + warning.getRemaining_power() + "%" + "\n" +
                    "处理状态: " + status;
            if(!StringUtils.isEmpty(mAgencyName)) {
                sb = "所在机构: " + mAgencyName + "\n" + sb;
            }
            mDetailsView.setText(sb);
        }
        List<AlarmEntrySolver> alarmEntrySolvers = details.getSosWarningUserMaps();
        if(alarmEntrySolvers == null) {
            alarmEntrySolvers = new ArrayList<AlarmEntrySolver>();
        }
        AlarmSolverAdapter adapter = new AlarmSolverAdapter(alarmEntrySolvers);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_alarm_solve_processing:
                String remark = mRemarkView.getText().toString();
                processingAlarm(mId, remark);
                break;
        }
    }

    /**
     * 处理告警
     */
    private void processingAlarm(long id, String remark) {
        showLoadingDialog();
        LefuApi.processingAlarmDetails(id, remark, new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast("提交成功");
                hideLoadingDialog();
                setResult(200);
                finish();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                hideLoadingDialog();
            }
        });
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
