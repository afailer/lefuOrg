package com.lefuorgn.oa.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.api.remote.OaApi;
import com.lefuorgn.base.BaseRecyclerViewFragment;
import com.lefuorgn.oa.activity.AttendanceApplyHandleActivity;
import com.lefuorgn.oa.activity.MyAttendanceApplyActivity;
import com.lefuorgn.oa.bean.AttendanceApply;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 审批模块中的审批页面
 */

public class MyAttendanceApplyFragment extends BaseRecyclerViewFragment<AttendanceApply> {

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
     * 进行中的状态
     */
    public static final int BUNDLE_ATTENDANCE_STATUS_SOLVING = 1;
    /**
     * 已处理状态
     */
    public static final int BUNDLE_ATTENDANCE_STATUS_SOLVED = 2;
    /**
     * 驳回
     */
    public static final int BUNDLE_ATTENDANCE_STATUS_REJECT = 2;
    /**
     * 抄送我的状态
     */
    public static final int BUNDLE_ATTENDANCE_STATUS_COPY_FOR_ME = 3;
    /**
     * 同意
     */
    public static final int BUNDLE_ATTENDANCE_STATUS_AGREE = 3;
    /**
     * 已撤销状态
     */
    public static final int BUNDLE_ATTENDANCE_STATUS_REVOKE = 4;

    private int mStatus; // 当前报警数据类型的状态
    private boolean approval; // true: 我审批的; false: 我申请的

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_fragment_my_attendance_apply;
    }

    @Override
    protected void initChildData() {
        Bundle bundle = getArguments();
        mStatus = bundle.getInt(BUNDLE_ATTENDANCE_STATUS);
        approval = bundle.getBoolean(BUNDLE_ATTENDANCE_APPROVAL);
    }

    @Override
    protected void loadData(final int pageNo) {
        OaApi.getMyAttendanceApply(mStatus, pageNo, approval, new RequestCallback<List<AttendanceApply>>() {
            @Override
            public void onSuccess(List<AttendanceApply> result) {
                setResult(pageNo, result);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                setResult(pageNo, new ArrayList<AttendanceApply>());
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder holder, AttendanceApply apply) {
        ImageView imageView = holder.getView(R.id.iv_item_fragment_my_attendance_apply);
        ImageLoader.loadOAImage(apply.getOa_verify_from_logo(), imageView);
        holder.setText(R.id.tv_item_fragment_my_attendance_apply_content,
                        apply.getOa_verify_from_name() + "  " + apply.getUser_name())
                .setText(R.id.tv_item_fragment_my_attendance_apply_date,
                        StringUtils.getFormatData(apply.getCreate_time(), "yyyy年MM月dd日 HH:mm:ss"))
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获取当前审批信息状态
     */
    private CharSequence getStatus(int status) {
        String result;
        switch (status) {
            case 1:
                result = "进行中";
                break;
            case 2:
                result = "驳回";
                break;
            case 3:
                result = "同意";
                break;
            case 4:
                result = "撤销";
                break;
            case 5:
                result = "退回";
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
                AttendanceApply apply = adapter.getItem(i);
                Intent intent = new Intent(getActivity(), AttendanceApplyHandleActivity.class);
                intent.putExtra("id", apply.getId());
                intent.putExtra("approval", approval);
                intent.putExtra("status", mStatus);
                startActivityForResult(intent, 100);
            }
        });
    }

    public boolean getApproval(){
        return approval;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 2: 驳回 3: 同意 4: 撤销
        if(resultCode == 3) {
            // 同意
            refresh();
            // 刷新已处理和抄送我的页面
            ((MyAttendanceApplyActivity)getActivity()).refreshPage(3);
        }else if(resultCode == 2) {
            // 驳回
            refresh();
            // 刷新抄送我的页面
            ((MyAttendanceApplyActivity)getActivity()).refreshPage(2);
        }else if(resultCode == 4) {
            // 撤销
            refresh();
            // 刷新撤销页面
            ((MyAttendanceApplyActivity)getActivity()).refreshPage(4);
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
