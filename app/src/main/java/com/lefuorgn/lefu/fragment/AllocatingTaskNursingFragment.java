package com.lefuorgn.lefu.fragment;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.Json;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseRecyclerViewFragment;
import com.lefuorgn.db.model.basic.AllocatingTypeTask;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.dialog.AlertDialog;
import com.lefuorgn.lefu.activity.AllocatingTaskNursingDetailsActivity;
import com.lefuorgn.util.TLog;
import com.lefuorgn.widget.CounterView;

import java.util.ArrayList;
import java.util.List;

/**
 * 发布任务, 护理条目显示页面
 */

public class AllocatingTaskNursingFragment extends BaseRecyclerViewFragment<AllocatingTypeTask> {

    public static final String BUNDLE_DATA_ALLOCATING_TYPE = "bundle_data_allocating_type";
    public static final String BUNDLE_DATA_ALLOCATING_TIME = "bundle_data_allocating_time";

    private long mUserId; // 当前用户ID
    private DisplaySignOrNursingItem mItem; // 当前护理项条目类型
    private long mTime; // 配单日期

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_fragment_allocating_task_nursing;
    }

    @Override
    protected void initChildData() {
        mUserId = AppContext.getInstance().getUser().getUser_id();
        mItem = (DisplaySignOrNursingItem) getArguments().getSerializable(BUNDLE_DATA_ALLOCATING_TYPE);
        mTime = getArguments().getLong(BUNDLE_DATA_ALLOCATING_TIME);
        ((AllocatingTaskNursingDetailsActivity) getActivity()).resetRefreshBit(mItem.getType());
    }

    @Override
    protected void loadData(final int pageNo) {
        LefuApi.getAllocatingTypeTask(pageNo, mUserId, mItem.getType(), mTime,
                new RequestCallback<List<AllocatingTypeTask>>() {
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
    protected void convert(BaseViewHolder holder, final AllocatingTypeTask task) {
        holder.setText(R.id.tv_item_fragment_allocating_task_nursing_name, task.getOld_people_name())
                .setText(R.id.tv_item_fragment_allocating_task_nursing_type, "测量" + mItem.getTitle())
                .setOnClickListener(R.id.tv_item_fragment_allocating_task_nursing_delete,
                        new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAlertDialog(task);
                    }
                });
        CounterView cv = holder.getView(R.id.cv_item_fragment_allocating_task_nursing);
        cv.setValue(task.getNumber_nursing());
        cv.setOnCounterChangeListener(new CounterView.OnCounterChangeListener() {
            @Override
            public void onCounterChange(View view, int amount) {
                task.setNumber_nursing(amount);
                LefuApi.editAllocatingTypeTask(getJson(task), new RequestCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        showToast("操作成功");
                    }

                    @Override
                    public void onFailure(ApiHttpException e) {
                        showToast(e.getMessage());
                    }
                });
            }
        });
    }

    /**
     * 显示删除按钮确认提示框
     * @param task 当前要删除的任务
     */
    private void showAlertDialog(final AllocatingTypeTask task) {
        new AlertDialog().setContent("确定删除吗?")
                .setCancelBtnText("确定")
                .setConfirmBtnText("取消")
                .setClickCallBack(new AlertDialog.ClickCallBack() {
                    @Override
                    public void cancel() {
                        // 确认按钮点击
                        deleteTask(task);
                    }

                    @Override
                    public void confirm() {
                        // 取消按钮点击
                    }
                }).show(getFragmentManager(), "AlertDialog");
    }

    /**
     * 删除某一个配单任务
     * @param task 当前要删除的任务
     */
    private void deleteTask(final AllocatingTypeTask task) {
        LefuApi.deleteAllocatingTypeTask(task.getId(), new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast("删除成功");
                mBaseAdapter.getData().remove(task);
                mBaseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
            }
        });
    }

    /**
     * 将配单任务列表转换成json数据
     * @param task 要转换的配单任务
     * @return json数据格式
     */
    protected String getJson(AllocatingTypeTask task) {
        List<AllocatingTypeTask> list = new ArrayList<AllocatingTypeTask>();
        list.add(task);
        return Json.getGson().toJson(list);
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        AllocatingTaskNursingDetailsActivity activity = (AllocatingTaskNursingDetailsActivity) getActivity();
        if(activity != null && mItem != null) {
            if(activity.isRefresh(mItem.getType())) {
                activity.resetRefreshBit(mItem.getType());
                // 进行数据刷新
                resetResult();
                TLog.log("数据进行刷新中...");
            }
        }
    }

}
