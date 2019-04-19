package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.lefu.adapter.ContactDepartmentsDetailsAdapter;
import com.lefuorgn.lefu.bean.Staff;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.widget.tree.bean.Node;

import java.util.List;

/**
 * 部门员工详情页面
 */
public class ContactDepartmentsDetailsActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

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
        Intent intent = getIntent();
        Node node = (Node) intent.getSerializableExtra("node");
        setToolBarTitle(node.getName());
        LefuApi.getStaffOnDepartment(node.getId(), new RequestCallback<List<Staff>>() {
            @Override
            public void onSuccess(List<Staff> result) {
                final ContactDepartmentsDetailsAdapter adapter = new ContactDepartmentsDetailsAdapter(result);
                mRecyclerView.setAdapter(adapter);
                adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        Intent intent = new Intent(ContactDepartmentsDetailsActivity.this, ContactDetailsActivity.class);
                        intent.putExtra("staff", (Staff) adapter.getData().get(i));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
            }
        });
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
