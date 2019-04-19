package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.util.DictionaryManager;
import com.lefuorgn.lefu.adapter.ElderlyInfoAdapter;
import com.lefuorgn.lefu.bean.OldPeopleInfo;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 老人住院信息页面
 */

public class HospitalizationInfoActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.base_recycler_view;
    }

    @Override
    protected void initView() {
        setToolBarTitle("住院信息");
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_base_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        OldPeople oldPeople = (OldPeople) intent.getSerializableExtra("oldPeople");
        if(oldPeople != null) {
            List<OldPeopleInfo> data = initOldPeopleInfo(oldPeople);
            ElderlyInfoAdapter adapter = new ElderlyInfoAdapter(data);
            mRecyclerView.setAdapter(adapter);
        }
    }

    private List<OldPeopleInfo> initOldPeopleInfo(OldPeople o) {
        List<OldPeopleInfo> list = new ArrayList<OldPeopleInfo>();
        list.add(new OldPeopleInfo("入住时间", StringUtils.getFormatData(
                o.getCheck_in_dt(), "yyyy-MM-dd")));
        list.add(new OldPeopleInfo("机构床位", o.getBed_no()));
        list.add(new OldPeopleInfo("护理级别", o.getNursing_level_id() + ""));
        list.add(new OldPeopleInfo("老人状态", o.getCheck_in_statusStr()));
        list.add(new OldPeopleInfo("人员类别", DictionaryManager.getContent(o.getPerson_type())));
        String bedInfo = o.getFloor_no() + "号楼  " + o.getFloor_layer()
                + "层  " + o.getRoom_no() + "号房  " + o.getFace();
        list.add(new OldPeopleInfo("床位详情", getBedInfo(o)));
        list.add(new OldPeopleInfo("社   保", DictionaryManager.getContent(o.getSocial_security())));
        list.add(new OldPeopleInfo("费用支付方", o.getCheck_in_payStr()));
        list.add(new OldPeopleInfo("协议名称", ""));
        list.add(new OldPeopleInfo("协议扫描件", ""));
        return list;
    }

    private String getBedInfo(OldPeople o) {
        StringBuilder sb = new StringBuilder();
        if(!StringUtils.isEmpty(o.getFloor_no())) {
            sb.append(o.getFloor_no() + "号楼  ");
        }
        if(!StringUtils.isEmpty(o.getFloor_layer())) {
            sb.append(o.getFloor_layer() + "层  ");
        }
        if(!StringUtils.isEmpty(o.getRoom_no())) {
            sb.append(o.getRoom_no() + "号房  ");
        }
        sb.append(o.getFace());
        return sb.toString();
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
