package com.lefuorgn.lefu.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.model.basic.SignConfig;
import com.lefuorgn.db.util.SignConfigManager;
import com.lefuorgn.lefu.adapter.CordonInfoAdapter;
import com.lefuorgn.util.DividerItemDecoration;

import java.util.List;

/**
 * 老人体征数据警戒值, 信息页面
 */

public class CordonInfoActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.base_recycler_view;
    }

    @Override
    protected void initView() {
        setToolBarTitle("体征数据警戒值");
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_base_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
    }

    @Override
    protected void initData() {
//        OldPeople oldPeople = (OldPeople) getIntent().getSerializableExtra("oldPeople");
//        String config = oldPeople.getConfig();
        // 暂时不支持老人个人配置文件显示
        List<SignConfig> data= SignConfigManager.getAllSignConfig();
        CordonInfoAdapter adapter = new CordonInfoAdapter(data);
        View view = getLayoutInflater().inflate(R.layout.item_recyclerview_empty,
                (ViewGroup) mRecyclerView.getParent(), false);
        ((TextView)view.findViewById(R.id.item_recycler_view_item)).setText("无此信息");
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
