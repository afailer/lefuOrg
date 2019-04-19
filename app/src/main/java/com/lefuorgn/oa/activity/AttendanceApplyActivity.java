package com.lefuorgn.oa.activity;

import android.content.Intent;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.OaApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.util.PermissionManager;
import com.lefuorgn.oa.bean.ClockAttendanceApply;
import com.lefuorgn.oa.bean.ClockType;
import com.lefuorgn.oa.bean.OaConfig;
import com.lefuorgn.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 申请页面
 */

public class AttendanceApplyActivity extends BaseActivity {

    private static final long DIVISOR_DEFAULT = 8;
    private SparseArray<ClockType> mClockTypeMap;
    private ClockType mOtherClockType; // 存放请假申请模块的类型
    private long mDivisor; // 获取剩余时间计算基数
    private int mStatisticsNum;

    private TextView mPointView;

    private boolean addPermission; // 添加请假权限

    @Override
    protected int getLayoutId() {
        return R.layout.activity_attendance_apply;
    }

    @Override
    protected void initView() {
        findViewById(R.id.rl_activity_apply_attendance).setOnClickListener(this);
        findViewById(R.id.rl_activity_attendance_apply).setOnClickListener(this);
        mPointView = (TextView) findViewById(R.id.tv_activity_attendance_apply_point);
        findViewById(R.id.btn_activity_attendance_apply_abnormal).setOnClickListener(this);
        findViewById(R.id.btn_activity_attendance_apply_off).setOnClickListener(this);
        findViewById(R.id.btn_activity_attendance_apply_overtime).setOnClickListener(this);
        findViewById(R.id.btn_activity_attendance_apply_business_travel).setOnClickListener(this);
        findViewById(R.id.btn_activity_attendance_apply_go_out).setOnClickListener(this);
        findViewById(R.id.btn_activity_attendance_apply_leave).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setToolBarTitle("申请");
        addPermission = PermissionManager.hasPermission(PermissionManager.OA_ASKFORLEAVE + PermissionManager.P_C);
        mClockTypeMap = new SparseArray<ClockType>();
        showWaitDialog();
        mStatisticsNum = 0;
        OaApi.getClockType(new RequestCallback<List<ClockType>>() {
            @Override
            public void onSuccess(List<ClockType> result) {
                mOtherClockType = new ClockType();
                mOtherClockType.setType(-1);
                mOtherClockType.setName("请假");
                mOtherClockType.setClockTypes(new ArrayList<ClockType>());
                for (ClockType clockType : result) {
                    if (clockType.getType() > 1 && clockType.getType() < 6) {
                        mClockTypeMap.append(clockType.getType(), clockType);
                    } else {
                        mOtherClockType.getClockTypes().add(clockType);
                    }
                }
                statisticsNetWork();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                statisticsNetWork();
            }
        });
        getClockAttendanceApply(true);
        getOaConfig();
    }

    /**
     * 获取未读信息条目数
     */
    private void getClockAttendanceApply(final boolean isShow) {
        long userId = AppContext.getInstance().getUser().getUser_id();
        OaApi.getClockAttendanceApply(userId, 1, 0, true, new RequestCallback<List<ClockAttendanceApply>>() {
            @Override
            public void onSuccess(List<ClockAttendanceApply> result) {
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
     * 获取OA配置文件
     */
    private void getOaConfig() {
        OaApi.getOaConfig(new RequestCallback<OaConfig>() {
            @Override
            public void onSuccess(OaConfig result) {
                if(result == null || StringUtils.isEmpty(result.getContent())) {
                    mDivisor = DIVISOR_DEFAULT;
                    statisticsNetWork();
                    return;
                }
                try {
                    JSONObject json = new JSONObject(result.getContent());
                    long divisor = json.getLong("daily_work_hours");
                    mDivisor = divisor == 0 ? DIVISOR_DEFAULT : divisor;
                }catch (JSONException e) {
                    mDivisor = DIVISOR_DEFAULT;
                }
                statisticsNetWork();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                mDivisor = DIVISOR_DEFAULT;
                statisticsNetWork();
            }
        });
    }

    /**
     * 统计网络请求个数
     */
    private void statisticsNetWork() {
        mStatisticsNum++;
        if(mStatisticsNum == 3) {
            hideWaitDialog();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_activity_apply_attendance:
                // 我审批的
                intent = new Intent(this, ClockAttendanceApplyActivity.class);
                intent.putExtra("approval", true);
                startActivityForResult(intent, 100);
                break;
            case R.id.rl_activity_attendance_apply:
                // 我申请的
                intent = new Intent(this, ClockAttendanceApplyActivity.class);
                intent.putExtra("approval", false);
                startActivityForResult(intent, 100);
                break;
            case R.id.btn_activity_attendance_apply_abnormal:
                showToast("暂未开通此功能");
                break;
            case R.id.btn_activity_attendance_apply_off:
                // 调休
                skipClockApplyActivity(2);
                break;
            case R.id.btn_activity_attendance_apply_overtime:
                // 加班
                skipClockApplyActivity(3);
                break;
            case R.id.btn_activity_attendance_apply_business_travel:
                // 出差
                skipClockApplyActivity(4);
                break;
            case R.id.btn_activity_attendance_apply_go_out:
                // 外出
                skipClockApplyActivity(5);
                break;
            case R.id.btn_activity_attendance_apply_leave:
                // 请假申请
                if(!addPermission) {
                    // 无添加权限
                    showToast(R.string.permission_no);
                    return;
                }
                if(mOtherClockType != null && mOtherClockType.getClockTypes().size() > 0) {
                    intent = new Intent(this, ClockApplyActivity.class);
                    intent.putExtra("clockType", mOtherClockType);
                    intent.putExtra("divisor", mDivisor);
                    startActivityForResult(intent, 100);
                }else {
                    showToast("无权限");
                }
                break;
        }
    }

    /**
     * 跳转到申请操作页面
     * @param type 请假类型
     */
    private void skipClockApplyActivity(int type) {
        if(!addPermission) {
            // 无添加权限
            showToast(R.string.permission_no);
            return;
        }
        ClockType clockType = mClockTypeMap.get(type);
        if(clockType == null) {
            showToast("无权限");
            return;
        }
        Intent intent = new Intent(this, ClockApplyActivity.class);
        intent.putExtra("clockType", clockType);
        intent.putExtra("divisor", mDivisor);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200) {
            getClockAttendanceApply(false);
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
