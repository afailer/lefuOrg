package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.util.PermissionManager;
import com.lefuorgn.lefu.adapter.EstimateAdapter;
import com.lefuorgn.lefu.bean.Estimate;
import com.lefuorgn.lefu.bean.EstimateItem;
import com.lefuorgn.util.DividerItemDecoration;

import java.util.List;

/**
 * 老人评估页面
 */

public class EstimateActivity extends BaseActivity {

    private OldPeople mOldPeople;
    private RecyclerView mRecyclerView;
    private View mLoadView;
    // 记录用户当前是否有增加评估权限
    private boolean increasePermission;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_estimate;
    }

    @Override
    protected void initView() {
        // 设置当前dialog布局的宽高
        matchWindow();
        // 设置点击空白处不关闭当前dialog
        setFinishOnTouchOutside(false);
        ((TextView) findViewById(R.id.tv_activity_estimate_title)).setText("评估表");
        findViewById(R.id.iv_activity_estimate_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLoadView = findViewById(R.id.ll_load_activity_estimate);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_activity_estimate);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));

    }

    @Override
    protected void initData() {
        mOldPeople = (OldPeople) getIntent().getSerializableExtra("oldPeople");
        increasePermission = PermissionManager
                .hasPermission(PermissionManager.EXAM_ANSWER + PermissionManager.P_C);
        showWaitDialog();
        LefuApi.getEstimateItem(AppContext.getInstance().getAgencyId()
                , new RequestCallback<List<EstimateItem>>() {
            @Override
            public void onSuccess(List<EstimateItem> result) {
                EstimateItem item = new EstimateItem();
                item.setId(-1);
                item.setTitle("历史记录");
                result.add(item);
                final EstimateAdapter adapter = new EstimateAdapter(result, increasePermission);
                mRecyclerView.setAdapter(adapter);
                adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        // 跳转到指定的页面
                        redirectTo((EstimateItem) adapter.getData().get(i));
                    }
                });

                hideWaitDialog();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast("数据加载失败");
                hideWaitDialog();
            }
        });
    }

    private void redirectTo(EstimateItem estimateItem) {
        if(estimateItem.getId() == -1) {
            // 历史记录页面
            Intent intent = new Intent(this, EstimateDetailsActivity.class);
            intent.putExtra("oldPeople", mOldPeople);
            startActivity(intent);
            finish();
        }else {
            // 评估表页面
            if(!increasePermission) {
                showToast(R.string.permission_no);
                return;
            }
            Estimate estimate = new Estimate();
            estimate.setId(estimateItem.getId());
            estimate.setTitle(estimateItem.getTitle());
            estimate.setOld_people_id(mOldPeople.getId());
            estimate.setOld_people_name(mOldPeople.getElderly_name());
            estimate.setOld_people_card_number(mOldPeople.getDocument_number());
            estimate.setContent("");
            Intent intent = new Intent(this, EstimateSaveActivity.class);
            intent.putExtra("estimate", estimate);
            startActivity(intent);
            finish();
        }

    }

    /**
     * 设置当前dialog布局的宽高
     */
    @SuppressWarnings("deprecation")
    private void matchWindow() {
        // 获取窗体管理者
        WindowManager manager = getWindowManager();
        // 为获取屏幕宽、高
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        // 高度设置为屏幕的0.6
        params.height = (int) (display.getHeight() * 0.6);
        // 宽度设置为屏幕的0.7
        params.width = (int) (display.getWidth() * 0.7);
        // 设置当前dialog的宽和高
        getWindow().setAttributes(params);
    }

    @Override
    public void showWaitDialog() {
        mLoadView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void hideWaitDialog() {
        mLoadView.setVisibility(View.GONE);
    }

    @Override
    protected boolean hasStatusBar() {
        return false;
    }
}
