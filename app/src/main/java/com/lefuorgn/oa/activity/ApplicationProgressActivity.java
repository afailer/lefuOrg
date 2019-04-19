package com.lefuorgn.oa.activity;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.oa.adapter.ApplicationProgressAdapter;
import com.lefuorgn.oa.bean.ApprovalProcess;
import com.lefuorgn.oa.bean.AttendanceApplyDetails;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 申请进度查询
 */

public class ApplicationProgressActivity extends BaseActivity {

    private CircleImageView mHeadImg;
    private TextView mNameView;
    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_application_progress;
    }

    @Override
    protected void initView() {
        mHeadImg = (CircleImageView) findViewById(R.id.iv_activity_application_progress);
        mHeadImg.setBorderColor(Color.parseColor("#56D5B3"));
        mHeadImg.setBorderWidth(StringUtils.dip2px(this, 4f));
        mNameView = (TextView) findViewById(R.id.tv_activity_application_progress_name);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_activity_application_progress);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {
        setToolBarTitle("进度查询");
        AttendanceApplyDetails details = (AttendanceApplyDetails) getIntent().getSerializableExtra("details");
        ImageLoader.loadImgForUserDefinedView(details.getUser_icon(), mHeadImg);
        mNameView.setText(details.getUser_name());
        ApplicationProgressAdapter adapter = new ApplicationProgressAdapter(getApprovalProcesses(details));
        adapter.addFooterView(getFooter(details.getStatus()));
        mRecyclerView.setAdapter(adapter);
    }

    private View getFooter(int status) {
        View mFooterView = getLayoutInflater().inflate(R.layout.footer_application_progress,
                (ViewGroup) mRecyclerView.getParent(), false);
        TextView tv = (TextView) mFooterView.findViewById(R.id.tv_footer_application_progress);
        tv.setText(getStatus(status));
        return mFooterView;
    }

    /**
     * 获取当前审批信息的状态
     */
    private String getStatus(int status) {
        String result;
        switch (status) {
            case 1:
                result = "待审核";
                break;
            case 2:
                result = "已驳回";
                break;
            case 3:
                result = "已同意";
                break;
            case 4:
                result = "已撤销";
                break;
            case 5:
                result = "已退回";
                break;
            default:
                result = "审核完成";
        }
        return result;
    }

    /**
     * 获取审批流程信息列表
     */
    private List<ApprovalProcess> getApprovalProcesses(AttendanceApplyDetails details) {
        List<ApprovalProcess> result = details.getVerifyAskLines();
        if(result == null) {
            result = new ArrayList<ApprovalProcess>();
        }
        ApprovalProcess process = new ApprovalProcess();
        process.setVerify_user_name(details.getUser_name() + "--" + details.getOa_verify_from_name());
        process.setCreate_time(details.getCreate_time());
        result.add(0, process);
        return result;
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
