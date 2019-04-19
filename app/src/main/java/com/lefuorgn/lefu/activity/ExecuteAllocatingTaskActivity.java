package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.bean.User;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.util.AllocatingTypeTaskManager;
import com.lefuorgn.db.util.SignConfigManager;
import com.lefuorgn.lefu.adapter.ExecuteAllocatingTaskAdapter;
import com.lefuorgn.lefu.bean.AllocatingTaskExecute;
import com.lefuorgn.lefu.bean.AllocatingTaskExecuteOption;
import com.lefuorgn.lefu.bean.MultiMedia;
import com.lefuorgn.lefu.dialog.ExceptionInfoDialog;
import com.lefuorgn.lefu.dialog.TodayWorkExecuteDialog;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.TLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 执行配单执行操作页面
 */

public class ExecuteAllocatingTaskActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    // 配置条目信息
    private List<DisplaySignOrNursingItem> mNursingItem;

    private AllocatingTaskExecute mTaskExecute; // 某一老人配单的信息

    private ExecuteAllocatingTaskAdapter mAdapter;

    private long mAgencyId; // 当前机构ID
    private User mUser; // 当前用户
    private boolean update; // 当前数据是否进行了刷新

    @Override
    protected int getLayoutId() {
        return R.layout.base_recycler_view;
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_base_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
    }

    @Override
    protected void initData() {
        mAgencyId = AppContext.getInstance().getAgencyId();
        mUser = AppContext.getInstance().getUser();
        mTaskExecute = (AllocatingTaskExecute) getIntent().getSerializableExtra("AllocatingTaskExecute");
        if(mTaskExecute != null) {
            setToolBarTitle(mTaskExecute.getOld_people_name());
            setMenuTextView("历史数据");
        }
        mNursingItem = SignConfigManager.getSignOrNursingItem(false);
        mAdapter = new ExecuteAllocatingTaskAdapter(getData());
        mRecyclerView.setAdapter(mAdapter);
        initListener();
    }

    private void initListener() {
        // 添加条目模块点击事件
        mAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int i) {
                AllocatingTaskExecuteOption option = (AllocatingTaskExecuteOption) adapter.getItem(i);
                switch (view.getId()) {
                    case R.id.tv_item_activity_execute_allocating_task_time:
                        // 数据录入
                        dataEntry(i, option);
                        break;
                    case R.id.tv_item_activity_execute_allocating_task_total:
                        // 添加备注
                        addRemark(i, option);
                        break;
                }
            }
        });
    }

    @Override
    protected void onMenuClick(View v) {
        // 历史数据点击事件处理
        Intent intent = new Intent(this, ExecuteAllocatingTaskDetailsActivity.class);
        intent.putExtra("id", mTaskExecute.getOld_people_id());
        intent.putExtra("name", mTaskExecute.getOld_people_name());
        startActivity(intent);
    }

    /**
     * 获取当前页面要展示的数据
     */
    private List<AllocatingTaskExecuteOption> getData() {
        // 存放要保存的数据
        List<AllocatingTaskExecuteOption> resultData = new ArrayList<AllocatingTaskExecuteOption>();
        // 根据本地数据库护理项条目创建类型集合
        List<DisplaySignOrNursingItem> itemList = new ArrayList<DisplaySignOrNursingItem>(mNursingItem);
        // 遍历服务器过滤掉服务器配置的类型
        for (AllocatingTaskExecuteOption option : mTaskExecute.getOptions()) {
            DisplaySignOrNursingItem removeItem = null;
            for (DisplaySignOrNursingItem item : itemList) {
                if(option.getNursing_item_id() == item.getType()) {
                    removeItem = item;
                    break;
                }
            }
            if(removeItem != null) {
                itemList.remove(removeItem);
            }
        }
        // 添加服务器配置的条目信息
        resultData.addAll(mTaskExecute.getOptions());
        long time = System.currentTimeMillis();
        // 添加服务器没有配置的条目信息
        for (DisplaySignOrNursingItem item : itemList) {
            AllocatingTaskExecuteOption option = new AllocatingTaskExecuteOption();
            // 本地数据库中不存在本条数据
            option.set_id(0);
            // 服务器数据库中不存在本条数据
            option.setId(0);
            option.setAgency_id(mAgencyId);
            option.setCare_workers("," + mUser.getUser_id() + ",");
            option.setCare_worker(mUser.getUser_id());
            option.setWorker_name(mUser.getUser_name());
            option.setNursing_item_id(item.getType());
            option.setNursing_item_name(item.getTitle());
            option.setPercentage("0/0");
            option.setComplete(0);
            option.setTotal(0);
            option.setCreate_time(time);
            option.setUpdate_time(time);
            option.setTask_time(time);
            option.setRemark("");
            // 记录当前信息条目为本地数据添加
            option.setSave_type(AllocatingTaskExecuteOption.SAVE_TYPE_LOCAL);
            resultData.add(option);
        }
        TLog.error("resultData == " + resultData.toString());
        return resultData;
    }

    /**
     * 数据录入
     * @param option 当前配单项目信息
     */
    private void dataEntry(final int position, final AllocatingTaskExecuteOption option) {
        new TodayWorkExecuteDialog()
                .setTitle(option.getNursing_item_name()
                        + " "
                        + mTaskExecute.getOld_people_name())
                .setClickCallBack(new TodayWorkExecuteDialog.ClickCallBack() {
                    @Override
                    public void saveClick(String remarks, List<MultiMedia> multiMedia) {
                        // 保存当前信息条目数据
                        update = true;
                        option.setRemark(remarks);
                        option.setComplete(option.getComplete() + 1);
                        option.setPercentage(option.getComplete() + "/" + option.getTotal());
                        AllocatingTypeTaskManager.updateAllocatingTaskExecute(
                                mUser, mAgencyId,
                                mTaskExecute.getOld_people_id(),
                                mTaskExecute.getOld_people_name(), option, multiMedia);
                        mAdapter.notifyItemChanged(position);
                    }
                })
                .show(getSupportFragmentManager(), "TodayWorkExecuteDialog");
    }

    /**
     * 添加异常备注
     * @param option 当前配单项目信息
     */
    private void addRemark(final int position, final AllocatingTaskExecuteOption option) {
        new ExceptionInfoDialog()
                .setTitle("异常情况说明")
                .isCancelOutside(false)
                .setContent(option.getRemark())
                .isShowCancelBtn(true)
                .setCancelBtnText("取消")
                .isShowConfirmBtn(true)
                .setConfirmBtnText("确认")
                .setClickCallBack(new ExceptionInfoDialog.ClickCallBack() {
            @Override
            public void cancel() {

            }

            @Override
            public void confirm(String value) {

            }
        }).show(getSupportFragmentManager(), "ExceptionInfoDialog");
    }

    @Override
    public void finish() {
        if(update) {
            setResult(200);
        }
        super.finish();
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
