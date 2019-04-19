package com.lefuorgn.lefu.fragment;

import android.os.Bundle;

import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseRecyclerViewFragment;
import com.lefuorgn.db.model.basic.AllocatingTypeTask;
import com.lefuorgn.util.TLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 配单任务条目详细信息页面
 */

public class AllocatingTaskProgressDetailsFragment extends BaseRecyclerViewFragment<AllocatingTypeTask> {

    /**
     * 当前页面类型
     */
    public static final String BUNDLE_PROGRESS_DETAILS_PAGE = "bundle_progress_details_page";
    /**
     * 当前页面护理类型ID
     */
    public static final String BUNDLE_PROGRESS_DETAILS_TYPE = "bundle_progress_details_type";
    /**
     * 当前配单指定时间
     */
    public static final String BUNDLE_PROGRESS_DETAILS_TIME = "bundle_progress_details_time";

    private int mPage; // 当前页面类型
    private long mType; // 当前配单护理项条目类型ID
    private long mTime; // 当前配单时间
    private long mUserId; // 当前用户ID

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_fragment_allocating_task_progress_details;
    }

    @Override
    protected void initChildData() {
        Bundle bundle = getArguments();
        mPage = bundle.getInt(AllocatingTaskProgressDetailsFragment.BUNDLE_PROGRESS_DETAILS_PAGE, 0);
        mType = bundle.getLong(AllocatingTaskProgressDetailsFragment.BUNDLE_PROGRESS_DETAILS_TYPE, 0);
        mTime = bundle.getLong(AllocatingTaskProgressDetailsFragment.BUNDLE_PROGRESS_DETAILS_TIME, 0);
        mUserId = AppContext.getInstance().getUser().getUser_id();
        TLog.log("mPage == " + mPage);
        TLog.log("mType == " + mType);
        TLog.log("mTime == " + mTime);
        TLog.log("mUserId == " + mUserId);
    }

    @Override
    protected void loadData(final int pageNo) {
        LefuApi.getAllocatingTypeTask(pageNo, mType, mTime, mPage, mUserId
                , new RequestCallback<List<AllocatingTypeTask>>() {
            @Override
            public void onSuccess(List<AllocatingTypeTask> result) {
                setResult(pageNo, result);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                setResult(pageNo, new ArrayList<AllocatingTypeTask>());
                showToast(e.getMessage());
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder holder, AllocatingTypeTask task) {
        String info = task.getNumber_current() + "/" + task.getNumber_nursing();
        holder.setText(R.id.tv_item_fragment_allocating_task_progress_details_name,
                task.getOld_people_name())
            .setText(R.id.tv_item_fragment_allocating_task_progress_details_operation,
                task.getWorker_name())
            .setText(R.id.tv_item_fragment_allocating_task_progress_details_num, info);
    }
}
