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
 * 老人信息页面
 */

public class ElderlyInfoActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.base_recycler_view;
    }

    @Override
    protected void initView() {
        setToolBarTitle("老人信息");
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
        list.add(new OldPeopleInfo("姓名", o.getElderly_name()));
        list.add(new OldPeopleInfo("性别", DictionaryManager.getContent(o.getGender())));
        list.add(new OldPeopleInfo("手机号码", o.getMobile()));
        list.add(new OldPeopleInfo("电话号码", o.getPhone()));
        list.add(new OldPeopleInfo("名族", DictionaryManager.getContent(o.getNation())));
        list.add(new OldPeopleInfo("户籍地址", o.getAccount_address()));
        list.add(new OldPeopleInfo("居民身份证", o.getDocument_number()));
        list.add(new OldPeopleInfo("通信地址", o.getAddress()));
        list.add(new OldPeopleInfo("生日",
                StringUtils.getFormatData(
                        o.getBirthday_dt(), "yyyy-MM-dd") + "   " + o.getAge() + "岁"));
        list.add(new OldPeopleInfo("户籍类型", o.getAccount()));
        list.add(new OldPeopleInfo("婚姻状况", DictionaryManager.getContent(o.getMarital_status())));
        list.add(new OldPeopleInfo("政治面貌", DictionaryManager.getContent(o.getPolitical_affiliation())));
        list.add(new OldPeopleInfo("文化程度", DictionaryManager.getContent(o.getEducation())));
        list.add(new OldPeopleInfo("宗教", o.getReligion()));
        list.add(new OldPeopleInfo("爱好", o.getHobbies()));
        list.add(new OldPeopleInfo("电子邮件", o.getMailbox()));
        list.add(new OldPeopleInfo("子女总数", o.getFamily_number() == 0 ? "" : o.getFamily_number() + ""));
        list.add(new OldPeopleInfo("月收入", DictionaryManager.getContent(o.getIncome_level_id())));
        list.add(new OldPeopleInfo("儿子(个数)", o.getSon_number()  == 0 ? "" : o.getSon_number() + ""));
        list.add(new OldPeopleInfo("女儿(个数)", o.getDaughter_number()  == 0 ? "" : o.getDaughter_number() + ""));
        return list;
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
