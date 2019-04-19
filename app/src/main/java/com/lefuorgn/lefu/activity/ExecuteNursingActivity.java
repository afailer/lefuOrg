package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.Json;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseRecyclerViewActivity;
import com.lefuorgn.db.model.basic.AllocatingTypeTask;
import com.lefuorgn.db.util.AllocatingTypeTaskManager;
import com.lefuorgn.dialog.AlertDialog;
import com.lefuorgn.interactive.Interactive;
import com.lefuorgn.interactive.impl.AddAndModifyDataFilter;
import com.lefuorgn.lefu.adapter.ExecuteTakingNursingAdapter;
import com.lefuorgn.lefu.bean.AllocatingTaskExecute;
import com.lefuorgn.util.NetworkUtils;
import com.lefuorgn.util.StringUtils;

import java.util.List;

/**
 * 护理服务执行页面
 */

public class ExecuteNursingActivity extends BaseRecyclerViewActivity<AllocatingTaskExecute> {

    // 数据请求任务
    private AllocatingTaskExecuteTask mAllocatingTaskExecuteTask;
    private long mAgencyId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_execute_nursing;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_activity_execute_nursing;
    }

    @Override
    protected void initChildView() {
        findViewById(R.id.btn_activity_execute_nursing_upload).setOnClickListener(this);
        findViewById(R.id.btn_activity_execute_nursing_go_off_work).setOnClickListener(this);
    }

    @Override
    protected void initChildData() {
        setToolBarTitle("护理任务-执行");
        setMenuTextView("添加");
        mAgencyId = AppContext.getInstance().getAgencyId();
    }

    @Override
    protected void loadData(int pageNo) {
        mAllocatingTaskExecuteTask = new AllocatingTaskExecuteTask();
        mAllocatingTaskExecuteTask.execute(pageNo);
    }

    @Override
    protected void convert(BaseViewHolder holder, AllocatingTaskExecute task) {
        holder.setText(R.id.tv_item_activity_execute_nursing_name, task.getOld_people_name())
                .setText(R.id.tv_item_activity_execute_nursing_date
                        , StringUtils.getFormatData(task.getTask_time(), "yyyy-MM-dd"));
        RecyclerView recyclerView = holder.getView(R.id.rv_item_activity_execute_nursing);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new ExecuteTakingNursingAdapter(task.getOptions()));
    }

    @Override
    protected void initListener(final BaseAdapter baseAdapter) {
        baseAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(ExecuteNursingActivity.this, ExecuteAllocatingTaskActivity.class);
                intent.putExtra("AllocatingTaskExecute", baseAdapter.getItem(i));
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onMenuClick(View v) {
        // 添加
        Intent intent = new Intent(this, ExecuteNursingAddActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200) {
            // 刷新数据
            resetResult();
        }
    }

    @Override
    public void onClick(View v) {
        if(mBaseAdapter.getData().size() == 0) {
            showToast("没有配单任务");
            return;
        }
        if(v.getId() == R.id.btn_activity_execute_nursing_upload) {
            // 上传, 完成的配单上传完成后就调用了数据同步(并删除用户自己添加的配单)
           final  List<AllocatingTypeTask> tasks = AllocatingTypeTaskManager.getAllocatingTypeTask(false);
            if(tasks.size() > 0) {
                // 提价完成的配单
                LefuApi.submitAllocatingTypeTask(getJson(tasks), new RequestCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        syncData();
                        AllocatingTypeTaskManager.deleteAllocatingTypeTaskAndLocalTask(tasks);
                        resetResult();
                        showToast("上传成功");
                    }

                    @Override
                    public void onFailure(ApiHttpException e) {
                        showToast("上传失败");
                    }
                });
            }else {
                syncData();
                AllocatingTypeTaskManager.deleteAllocatingTypeTaskAndLocalTask(tasks);
                resetResult();
                showToast("上传成功");
            }

        }else if(v.getId() == R.id.btn_activity_execute_nursing_go_off_work) {
            // 下班 完成的配单上传, 未完成的配单置为未接单状态提交后就调用了数据同步(并删除用户自己添加的配单)
            final List<AllocatingTypeTask> tasks = AllocatingTypeTaskManager.getAllocatingTypeTask(true);
            for (AllocatingTypeTask task : tasks) {
                if(task.getNumber_nursing() > task.getNumber_current()) {
                    // 未完成置成未接单状态
                    task.setTask_state(1);
                }
            }
            if(tasks.size() > 0) {
                // 提价完成的配单
                LefuApi.submitAllocatingTypeTask(getJson(tasks), new RequestCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        syncData();
                        AllocatingTypeTaskManager.deleteAllocatingTypeTaskAndLocalTask(tasks);
                        resetResult();
                        showToast("下班成功");
                    }

                    @Override
                    public void onFailure(ApiHttpException e) {
                        new AlertDialog()
                                .setContent("任务失效,点击\"确定\"清除任务并联系客服!")
                                .setConfirmBtnText("取消")
                                .setCancelBtnText("确认")
                                .setClickCallBack(new AlertDialog.ClickCallBack() {
                                    @Override
                                    public void cancel() {
                                        // 确认
                                        AllocatingTypeTaskManager.deleteAllocatingTypeTaskAndLocalTask(tasks);
                                        resetResult();
                                    }

                                    @Override
                                    public void confirm() {}
                                }).show(getSupportFragmentManager(), "AlertDialog");
                    }
                });
            }else {
                syncData();
                AllocatingTypeTaskManager.deleteAllocatingTypeTaskAndLocalTask(tasks);
                resetResult();
                showToast("下班成功");
            }
        }
    }

    /**
     * 同步数据
     */
    private void syncData() {
        if(AppContext.getInstance().getNetState() == NetworkUtils.NETWORK_NONE) {
            showToast("网络不可用");
            return;
        }
        // 数据库首次加载数据
        Interactive interactive = new Interactive.Builder()
                .agencyId(AppContext.getInstance().getAgencyId())
                .uploadTable(true) // 上传数据
                .downloadTable(true) // 下载数据
                .runningService(true) // 运行在后台服务中
                .filter(new AddAndModifyDataFilter()) // 添加数据过滤器
                .build();
        interactive.newSync().enqueue(null);
    }

    /**
     * 本地获取数据请求任务类
     */
    private class AllocatingTaskExecuteTask extends AsyncTask<Integer, Void, List<AllocatingTaskExecute>> {

        private int pageNo;

        @Override
        protected List<AllocatingTaskExecute> doInBackground(Integer... params) {
            pageNo = params[0];
            // 加载开始时间
            long startTime = System.currentTimeMillis();
            List<AllocatingTaskExecute> data = AllocatingTypeTaskManager
                    .getAllocatingTaskExecute(mAgencyId, pageNo, true);
            // 获取数据加载完成后的时间
            long endTime = System.currentTimeMillis();
            long time = endTime - startTime;
            if(time < 100) {
                SystemClock.sleep(100 - time);
            }
            return data;
        }

        @Override
        protected void onPostExecute(List<AllocatingTaskExecute> data) {
            setResult(pageNo, data);
        }
    }

    /**
     * 取消当前正在执行的任务
     */
    private void cancelReadCacheTask() {
        if (mAllocatingTaskExecuteTask != null) {
            mAllocatingTaskExecuteTask.cancel(true);
            mAllocatingTaskExecuteTask = null;
        }
    }

    @Override
    protected boolean hasItemDecoration() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelReadCacheTask();
    }

    /**
     * 将配单任务列表转换成json数据
     * @param list 要转换的列表
     * @return json数据格式
     */
    protected String getJson(List<AllocatingTypeTask> list) {
        return Json.getGson().toJson(list);
    }

    @Override
    protected String getEmptyNote() {
        return "没有要执行的任务";
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
