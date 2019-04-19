package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.model.basic.OldPeopleFamily;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.lefu.adapter.FamilyInfoAdapter;
import com.lefuorgn.util.DividerItemDecoration;

import java.util.List;

/**
 * 老人家属信息展示页面
 */

public class FamilyInfoActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.base_recycler_view;
    }

    @Override
    protected void initView() {
        setToolBarTitle("家属信息");
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_base_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        OldPeople oldPeople = (OldPeople) intent.getSerializableExtra("oldPeople");
        List<OldPeopleFamily> data = OldPeopleManager.getOldPeopleFamily(oldPeople.getId());
        FamilyInfoAdapter adapter = new FamilyInfoAdapter(data);
        View view = getLayoutInflater().inflate(R.layout.item_recyclerview_empty,
                (ViewGroup) mRecyclerView.getParent(), false);
        ((TextView)view.findViewById(R.id.item_recycler_view_item)).setText("家属信息未添加");
        adapter.setEmptyView(view);
        mRecyclerView.setAdapter(adapter);
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
