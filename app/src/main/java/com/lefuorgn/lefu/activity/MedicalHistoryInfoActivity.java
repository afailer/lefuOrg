package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.lefu.adapter.MedicalHistoryInfoAdapter;
import com.lefuorgn.lefu.bean.MedicalHistory;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 老人病史信息页面
 */

public class MedicalHistoryInfoActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.base_recycler_view;
    }

    @Override
    protected void initView() {
        setToolBarTitle("病史");
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_base_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        OldPeople oldPeople = (OldPeople) intent.getSerializableExtra("oldPeople");
        showWaitDialog();
        LefuApi.getMedicalHistoryInfo(oldPeople.getId(), new RequestCallback<List<MedicalHistory>>() {
            @Override
            public void onSuccess(List<MedicalHistory> result) {
                String info;
                List<String> data = new ArrayList<String>();
                // 获取数据中是否包含条目
                if(result.size() == 0) {
                    info = "";
                }else {
                    info = result.get(0).getItems();
                }
                // 过滤包含条目的信息
                if(!StringUtils.isEmpty(info)) {
                    String[] split = info.split(",");
                    Collections.addAll(data, split);
                }
                // 展示数据
                MedicalHistoryInfoAdapter adapter = new MedicalHistoryInfoAdapter(data);
                View view = getLayoutInflater().inflate(R.layout.item_recyclerview_empty,
                        (ViewGroup) mRecyclerView.getParent(), false);
                ((TextView)view.findViewById(R.id.item_recycler_view_item)).setText("无病史信息");
                adapter.setEmptyView(view);
                mRecyclerView.setAdapter(adapter);
                hideWaitDialog();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                MedicalHistoryInfoAdapter adapter = new MedicalHistoryInfoAdapter(new ArrayList<String>());
                View view = getLayoutInflater().inflate(R.layout.item_recyclerview_empty,
                        (ViewGroup) mRecyclerView.getParent(), false);
                ((TextView)view.findViewById(R.id.item_recycler_view_item)).setText("数据加载失败");
                adapter.setEmptyView(view);
                mRecyclerView.setAdapter(adapter);
                hideWaitDialog();
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
