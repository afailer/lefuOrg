package com.lefuorgn.lefu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseFragment;
import com.lefuorgn.lefu.activity.ContactDepartmentsDetailsActivity;
import com.lefuorgn.lefu.adapter.ContactDepartmentsAdapter;
import com.lefuorgn.lefu.bean.Department;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.ToastUtils;
import com.lefuorgn.widget.tree.adapter.TreeListViewAdapter;
import com.lefuorgn.widget.tree.bean.Node;

import java.util.List;

/**
 * 通讯录部门页面
 */

public class ContactDepartmentsFragment extends BaseFragment {

    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.base_recycler_view;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_base_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
    }

    @Override
    protected void initData() {
        showWaitDialog();
    }

    /**
     * 设置员工结果
     * @param result 员工数据
     */
    public void setResult(List<Department> result) {
        hideWaitDialog();
        if(result == null) {
            return;
        }
        try {
            ContactDepartmentsAdapter adapter = new ContactDepartmentsAdapter(mRecyclerView, result);
            mRecyclerView.setAdapter(adapter);
            adapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(Node node, View view, int position) {
                    if (node.isLeaf()) {
                        if(node.getNum() > 0) {
                            Intent intent = new Intent(getActivity(), ContactDepartmentsDetailsActivity.class);
                            intent.putExtra("node", node);
                            getActivity().startActivity(intent);
                        }else {
                            ToastUtils.show(getActivity(), "当前部门无员工");
                        }

                    }
                }
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
