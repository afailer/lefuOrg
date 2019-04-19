package com.lefuorgn.oa.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.api.remote.OaApi;
import com.lefuorgn.base.BaseRecyclerViewFragment;
import com.lefuorgn.oa.activity.ClockAttendanceApplyActivity;
import com.lefuorgn.oa.activity.ClockAttendanceApplyHandleActivity;
import com.lefuorgn.oa.bean.ClockAttendanceApply;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 告警信息类别详情
 */

public class ClockAttendanceApplyFragment extends BaseRecyclerViewFragment<ClockAttendanceApply> {

    /**
     * 当前页面加载数据的状态
     */
    public static final String BUNDLE_ATTENDANCE_STATUS = "bundle_attendance_status";
    /**
     * true: 我审批的; false: 我申请的
     */
    public static final String BUNDLE_ATTENDANCE_APPROVAL = "bundle_attendance_approval";
    /**
     * 待处理状态
     */
    public static final int BUNDLE_ATTENDANCE_STATUS_UNRESOLVED = 1;
    /**
     * 审批中状态
     */
    public static final int BUNDLE_ATTENDANCE_STATUS_SOLVING = 1;
    /**
     * 已驳回
     */
    public static final int BUNDLE_ATTENDANCE_STATUS_REJECT = 2;
    /**
     * 已同意
     */
    public static final int BUNDLE_ATTENDANCE_STATUS_AGREE = 3;

    private int mStatus; // 当前报警数据类型的状态
    private boolean approval; // true: 我审批的; false: 我申请的
    private long mUserId;

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_fragment_my_attendance_apply;
    }

    @Override
    protected void initChildData() {
        Bundle bundle = getArguments();
        mStatus = bundle.getInt(BUNDLE_ATTENDANCE_STATUS);
        approval = bundle.getBoolean(BUNDLE_ATTENDANCE_APPROVAL);
        mUserId = AppContext.getInstance().getUser().getUser_id();
    }

    @Override
    protected void loadData(final int pageNo) {
        OaApi.getClockAttendanceApply(mUserId, mStatus, pageNo, approval, new RequestCallback<List<ClockAttendanceApply>>() {
            @Override
            public void onSuccess(List<ClockAttendanceApply> result) {
                setResult(pageNo, result);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                setResult(pageNo, new ArrayList<ClockAttendanceApply>());
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder holder, ClockAttendanceApply apply) {
        ImageView imageView = holder.getView(R.id.iv_item_fragment_my_attendance_apply);
        ImageLoader.loadOAImageByType(apply.getOa_vacation_type(), imageView);
        holder.setText(R.id.tv_item_fragment_my_attendance_apply_content,
                        apply.getOa_vacation_name() + "  " + apply.getUser_name())
                .setText(R.id.tv_item_fragment_my_attendance_apply_date,
                        StringUtils.getFormatData(apply.getVerify_start_time(), "yyyy年MM月dd日 HH:mm:ss"))
                .setText(R.id.btn_item_fragment_my_attendance_apply, getStatus(apply.getStatus()))
                .setBackgroundColor(R.id.btn_item_fragment_my_attendance_apply, getStatusColor(apply.getStatus()));
    }

    /**
     * 根据状态获取颜色
     */
    private int getStatusColor(int status) {
        String color;
        switch (status) {
            case 1:
                color = "#67AFF9";
                break;
            case 2:
                color = "#FC8795";
                break;
            default:
                color = "#87DC7D";
        }
        return Color.parseColor(color);
    }

    /**
     * 获取当前审批信息状态
     */
    private CharSequence getStatus(int status) {
        String result;
        switch (status) {
            case 1:
                result = approval ? "待处理" : "进行中";
                break;
            case 2:
                result = "已驳回";
                break;
            case 3:
                result = "已同意";
                break;
            default:
                result = "";
        }
        return result;
    }

    @Override
    protected void initListener(final BaseRecyclerViewFragmentAdapter adapter) {
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                ClockAttendanceApply apply = adapter.getItem(i);
                Intent intent = new Intent(getActivity(), ClockAttendanceApplyHandleActivity.class);
                intent.putExtra("id", apply.getId());
                intent.putExtra("approval", approval);
                intent.putExtra("status", mStatus);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200) {
            // 同意
            ((ClockAttendanceApplyActivity)getActivity()).refreshPage(true);
        }else if(resultCode == 300) {
            // 驳回
            ((ClockAttendanceApplyActivity)getActivity()).refreshPage(false);
        }else if(resultCode == 400) {
            // 撤销, 只刷新当前页面
            refresh();
            ((ClockAttendanceApplyActivity)getActivity()).refreshPage();
        }
    }

    /**
     * 刷新页面数据
     */
    public void refresh() {
        resetResult();
    }

    /**
     * 获取当前页面的状态
     */
    public int getStatus() {
        return mStatus;
    }

}
