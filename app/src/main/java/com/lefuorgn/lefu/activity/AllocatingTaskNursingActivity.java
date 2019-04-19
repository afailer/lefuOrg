package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseRecyclerViewActivity;
import com.lefuorgn.dialog.AlertDialog;
import com.lefuorgn.dialog.DatePickerDialog;
import com.lefuorgn.lefu.bean.AllocatingTask;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 护理服务派单页面
 */

public class  AllocatingTaskNursingActivity extends BaseRecyclerViewActivity<AllocatingTask> {

    private String format = "yyyy-MM-dd";
    private long mUserId;
    private long mTodayState;
    private AllocatingTask mAllocatingTask;

    private TextView mStateView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_allocating_task_nursing;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_activity_allocating_task_nursing;
    }

    @Override
    protected void initChildView() {
        mStateView = (TextView) findViewById(R.id.tv_activity_allocating_task_nursing_state);
        TextView dateView = (TextView) findViewById(R.id.tv_activity_allocating_task_nursing_date);
        dateView.setText(StringUtils.getFormatData(System.currentTimeMillis(), format));
        findViewById(R.id.ll_activity_allocating_task_nursing).setOnClickListener(this);
        findViewById(R.id.btn_activity_allocating_task_nursing).setOnClickListener(this);
    }

    @Override
    protected void initChildData() {
        setToolBarTitle("护理任务-派单");
        mUserId = AppContext.getInstance().getUser().getUser_id();
        // 获取当天的配单详情
        getSameDayInfo(System.currentTimeMillis(), false);
    }

    @Override
    protected void loadData(final int pageNo) {
        LefuApi.getAllocatingTask(pageNo, mUserId, new RequestCallback<List<AllocatingTask>>() {
            @Override
            public void onSuccess(List<AllocatingTask> result) {
                setResult(pageNo, result);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                setResult(pageNo, new ArrayList<AllocatingTask>());
                showToast(e.getMessage());
            }
        });
    }

    /**
     * 获取当天配单的情况
     * @param time 指定时间
     * @param check 当前请求是否是校验请求
     */
    private void getSameDayInfo(final long time, final boolean check) {
        LefuApi.getSameDayAllocatingTask(time, mUserId, new RequestCallback<AllocatingTask>() {

            @Override
            public void onSuccess(final AllocatingTask result) {
                if(check) {
                    if(result != null) {
                        // 已经配单, 操作配单
                        handleAllocatingTask(result);
                    }else {
                        // 未配单, 创建配单
                        establishAllocatingTask(time);
                    }
                }else {
                    if(result != null) {
                        mAllocatingTask = result;
                        updateTodayInfo(result.getTask_state());
                    }else {
                        mAllocatingTask = null;
                        updateTodayInfo(AllocatingTask.TASK_STATE_NO_ALLOT);
                    }
                }
            }

            @Override
            public void onFailure(ApiHttpException e) {
                if(check) {
                    showToast("当前配单创建失败");
                }else {
                    updateTodayInfo(AllocatingTask.TASK_STATE_NO_ALLOT);
                }
            }
        });
    }

    /**
     * 根据配单任务处理
     * @param task 配单任务
     */
    private void handleAllocatingTask(final AllocatingTask task) {
        if(task.getTask_state() == AllocatingTask.TASK_STATE_NO_RELEASE) {
            // 已配单, 但是未配单
            new AlertDialog().setContent("当日任务已经生成")
                    .setConfirmBtnText("编辑")
                    .setCancelBtnText("重置任务")
                    .setClickCallBack(new AlertDialog.ClickCallBack() {
                        @Override
                        public void cancel() {
                            // 重置任务, 将任务内容清空
                            establishAllocatingTask(task.getTask_time());
                        }

                        @Override
                        public void confirm() {
                            // 编辑, 跳转到编辑页面
                            Intent intent = new Intent(AllocatingTaskNursingActivity.this,
                                    AllocatingTaskNursingDetailsActivity.class);
                            intent.putExtra("time", task.getTask_time());
                            startActivityForResult(intent, 100);
                        }
                    })
                    .show(getSupportFragmentManager(), "AlertDialog");
        }else {
            // 已发布, 跳转进度页面
            Intent intent = new Intent(this, AllocatingTaskProgressActivity.class);
            intent.putExtra("id", task.getId());
            intent.putExtra("time", task.getTask_time());
            startActivityForResult(intent, 100);
        }
    }

    /**
     * 创建任务, 或者重置任务
     * @param time 时间戳
     */
    private void establishAllocatingTask(final long time) {
        LefuApi.createAllocatingTask(time, mUserId, new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast("生成配单成功");
                // 跳转到编辑页面
                Intent intent = new Intent(AllocatingTaskNursingActivity.this,
                        AllocatingTaskNursingDetailsActivity.class);
                intent.putExtra("time", time);
                startActivityForResult(intent, 100);
                resetResult();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder holder, AllocatingTask d) {
        holder.setText(R.id.tv_item_activity_allocating_task_nursing_date,
                        StringUtils.getFormatData(d.getTask_time(), format))
                .setText(R.id.tv_item_activity_allocating_task_nursing_state,
                        getState(d.getTask_state()))
                .setTextColor(R.id.tv_item_activity_allocating_task_nursing_state,
                        getColor(d.getTask_state()))
                .setText(R.id.tv_item_activity_allocating_task_nursing_num,
                        d.getTask_number() + "单");
    }

    @Override
    protected void initListener(final BaseAdapter adapter) {
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent;
                AllocatingTask d = adapter.getItem(i);
                if(d.getTask_state() == AllocatingTask.TASK_STATE_NO_RELEASE) {
                    // 跳转到派单详情页面
                    intent = new Intent(AllocatingTaskNursingActivity.this, AllocatingTaskNursingDetailsActivity.class);
                    intent.putExtra("time", d.getTask_time());
                    startActivityForResult(intent, 100);
                }else {
                    // 跳转到任务进度页面
                    intent = new Intent(AllocatingTaskNursingActivity.this, AllocatingTaskProgressActivity.class);
                    intent.putExtra("id", d.getId());
                    intent.putExtra("time", d.getTask_time());
                    startActivityForResult(intent, 100);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 校验当天配单修改情况
        if(resultCode == 200) {
            // 获取当天的配单详情
            getSameDayInfo(System.currentTimeMillis(), false);
            resetResult();
        }else if(resultCode == 300) {
            resetResult();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_activity_allocating_task_nursing:
                // 查看当天配单情况
                Intent intent;
                if(mTodayState == AllocatingTask.TASK_STATE_NO_RELEASE) {
                    // 未发布
                    intent = new Intent(this, AllocatingTaskNursingDetailsActivity.class);
                    intent.putExtra("time", mAllocatingTask.getTask_time());
                    startActivityForResult(intent, 100);
                }else if(mTodayState == AllocatingTask.TASK_STATE_EXECUTING ||
                        mTodayState == AllocatingTask.TASK_STATE_COMPLETE) {
                    // 执行中或者已完成, 跳转到任务进度页面
                    intent = new Intent(this, AllocatingTaskProgressActivity.class);
                    intent.putExtra("id", mAllocatingTask.getId());
                    intent.putExtra("time", mAllocatingTask.getTask_time());
                    startActivityForResult(intent, 100);
                }else {
                    // 未派单
                    pickDate();
                }
                break;
            case R.id.btn_activity_allocating_task_nursing:
                // 生成配单
                pickDate();
                break;
        }
    }

    /**
     * 更新今天的配单情况
     * @param state 当前配单状态
     */
    private void updateTodayInfo(long state) {
        mTodayState = state;
        mStateView.setText(getState(state));
        mStateView.setTextColor(getColor(state));
    }

    public void pickDate() {
        final DatePickerDialog dialog = new DatePickerDialog();
        dialog.isCancelOutside(false)
            .setDisplay(DatePickerDialog.DATE)
            .isShowRightBtn(true)
            .isShowLeftBtn(true)
            .setTitle("设置日期")
            .setLeftBtnText("取消")
            .setRightBtnText("生成")
            .setMinDate(System.currentTimeMillis() - 1000)
            .setClickCallBack(new DatePickerDialog.ClickCallBack() {
                @Override
                public void leftClick() {
                    dialog.dismiss();
                }

                @Override
                public void rightClick(long time) {
                    getSameDayInfo(time, true);
                    dialog.dismiss();
                }
            });
        dialog.show(getSupportFragmentManager(), "");
    }

    /**
     * 获取当前状态
     * @param state 当前条目的状态
     * @return 状态
     */
    private String getState(long state) {
        String str;
        if(state == AllocatingTask.TASK_STATE_NO_RELEASE) {
            str = "未发布";
        }else if(state == AllocatingTask.TASK_STATE_EXECUTING) {
            str = "执行中";
        }else if(state == AllocatingTask.TASK_STATE_COMPLETE) {
            str = "已完成";
        }else {
            str = "未派单";
        }
        return str;
    }

    /**
     * 获取当前的颜色值  0 未发布, #FF343F; 1 执行中 #9FAFB6; 2 已完成, 主色; 其他 主色;
     * @param state 当前条目的状态
     * @return 状态颜色
     */
    private int getColor(long state) {
        int color;
        if(state == AllocatingTask.TASK_STATE_NO_RELEASE) {
            color = Color.parseColor("#FF343F");
        }else if(state == AllocatingTask.TASK_STATE_EXECUTING) {
            color = Color.parseColor("#9FAFB6");
        }else if(state == AllocatingTask.TASK_STATE_COMPLETE) {
            color = getResources().getColor(R.color.colorPrimary);
        }else {
            color = getResources().getColor(R.color.colorPrimary);
        }
        return color;
    }

    @Override
    protected boolean hasItemDecoration() {
        return false;
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
