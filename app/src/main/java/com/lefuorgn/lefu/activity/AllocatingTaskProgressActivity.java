package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.lefu.adapter.AllocatingTaskProgressAdapter;
import com.lefuorgn.lefu.bean.AllocatingTaskProgress;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 配单执行进度展示页面
 */

public class AllocatingTaskProgressActivity extends BaseActivity {

    private static final String format = "yyyy-MM-dd";

    private TextView mTotalView, mCompleteView;
    private TextView mNoCompleteView, mNoAllotView;
    private RecyclerView mRecyclerView;

    private long mTime;
    private long mUserId;
    private boolean error;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_allocating_task_progress;
    }

    @Override
    protected void initView() {
        mTotalView = (TextView) findViewById(R.id.tv_activity_allocating_task_progress_total);
        mCompleteView = (TextView) findViewById(R.id.tv_activity_allocating_task_progress_complete);
        mNoCompleteView = (TextView) findViewById(R.id.tv_activity_allocating_task_progress_no_complete);
        mNoAllotView = (TextView) findViewById(R.id.tv_activity_allocating_task_progress_no_allot);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_activity_allocating_task_progress);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
    }

    @Override
    protected void initData() {
        mUserId = AppContext.getInstance().getUser().getUser_id();
        Intent intent = getIntent();
        long id = intent.getLongExtra("id", 0);
        mTime = intent.getLongExtra("time", 0);
        setToolBarTitle(StringUtils.getFormatData(mTime, "yyyy-MM-dd") + "进度");
        setMenuTextView("编辑");
        showWaitDialog();
        LefuApi.getAllocatingTaskProgress(id, new RequestCallback<List<AllocatingTaskProgress>>() {
            @Override
            public void onSuccess(List<AllocatingTaskProgress> result) {
                int total = 0;
                int complete = 0;
                int noComplete = 0;
                int noAllot = 0;
                if(result != null) {
                    for (AllocatingTaskProgress a : result) {
                        total += a.getTotal();
                        complete += a.getFinish();
                        noComplete += a.getUnfinished();
                        noAllot += a.getUnreceived();
                    }
                }else {
                    result = new ArrayList<AllocatingTaskProgress>();
                }
                String sTotal = total + "";
                String sComplete = complete + "";
                String sNoComplete = noComplete + "";
                String sNoAllot = noAllot + "";
                mTotalView.setText(sTotal);
                mCompleteView.setText(sComplete);
                mNoCompleteView.setText(sNoComplete);
                mNoAllotView.setText(sNoAllot);
                initAdapter(result, "无护理进度信息");
                hideWaitDialog();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                error = true;
                initAdapter(new ArrayList<AllocatingTaskProgress>(), "数据加载失败");
                hideWaitDialog();
            }
        });
    }

    /**
     * 初始化适配器
     * @param result 适配器中要添加的数据
     * @param note 适配器无数据显示提示内容
     */
    private void initAdapter(List<AllocatingTaskProgress> result, String note) {
        final AllocatingTaskProgressAdapter adapter = new AllocatingTaskProgressAdapter(result);
        View view = getLayoutInflater().inflate(R.layout.item_recyclerview_empty,
                (ViewGroup) mRecyclerView.getParent(), false);
        ((TextView)view.findViewById(R.id.item_recycler_view_item)).setText(note);
        adapter.setEmptyView(view);
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(AllocatingTaskProgressActivity.this,
                        AllocatingTaskProgressDetailsActivity.class);
                intent.putExtra("item_id", adapter.getItem(i).getNursing_item_id());
                intent.putExtra("time", mTime);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onMenuClick(View v) {
        LefuApi.editAllocatingTaskProgress(mTime, mUserId, new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast("编辑任务记录成功");
                if(StringUtils.getFormatData(mTime, format).equals(
                        StringUtils.getFormatData(System.currentTimeMillis(), format))) {
                    // 当前任务
                    setResult(200);
                }else {
                    setResult(300);
                }
                finish();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
            }
        });
    }

    @Override
    public void finish() {
        if(error) {
            setResult(300);
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
