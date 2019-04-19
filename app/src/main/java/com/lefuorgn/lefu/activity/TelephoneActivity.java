package com.lefuorgn.lefu.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.R;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.util.DictionaryManager;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.lefu.adapter.TelephoneAdapter;
import com.lefuorgn.lefu.bean.FamilyTelephone;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.StringUtils;

import java.util.List;

/**
 * 老人电话号码以及家属电话信息展示页面
 */

public class TelephoneActivity extends BaseActivity {

    private ImageView mHeadView;
    private TextView mNameView, mSexView, mAgeView, mBedNoView;
    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_telephone;
    }

    @Override
    protected void initView() {
        mHeadView = (ImageView)findViewById(R.id.iv_activity_telephone_head);
        mNameView = (TextView)findViewById(R.id.tv_activity_telephone_name);
        mSexView = (TextView)findViewById(R.id.tv_activity_telephone_sex);
        mAgeView = (TextView)findViewById(R.id.tv_activity_telephone_age);
        mBedNoView = (TextView)findViewById(R.id.tv_activity_telephone_bed_no);
        mRecyclerView = (RecyclerView)findViewById(R.id.rv_activity_telephone);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        OldPeople oldPeople = (OldPeople) getIntent().getSerializableExtra("oldPeople");
        ImageLoader.loadCircleImg(oldPeople.getIcon(), mHeadView);
        mNameView.setText(oldPeople.getElderly_name());
        mSexView.setText(DictionaryManager.getContent(oldPeople.getGender()));
        mAgeView.setText(oldPeople.getAge() + "");
        mBedNoView.setText(oldPeople.getBed_no());
        List<FamilyTelephone> data = OldPeopleManager.getFamilyTelephone(oldPeople.getId());
        if(!StringUtils.isEmpty(oldPeople.getPhone())) {
            FamilyTelephone telephone = new FamilyTelephone();
            telephone.setName("本人座机");
            telephone.setTelephone(oldPeople.getPhone());
            data.add(0, telephone);
        }
        if(!StringUtils.isEmpty(oldPeople.getMobile())) {
            FamilyTelephone telephone = new FamilyTelephone();
            telephone.setName("本人手机");
            telephone.setTelephone(oldPeople.getMobile());
            data.add(0, telephone);
        }

        final TelephoneAdapter adapter = new TelephoneAdapter(data);
        adapter.setEmptyView(getEmptyView());
        mRecyclerView.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + adapter.getItem(i).getTelephone()));
                phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(phoneIntent);
            }
        });
    }

    /**
     * 获取空内容指示信息控件
     * @return 控件
     */
    private View getEmptyView() {
        View view = getLayoutInflater().inflate(R.layout.item_recyclerview_empty,
                (ViewGroup) mRecyclerView.getParent(), false);
        ((TextView) view.findViewById(R.id.item_recycler_view_item)).setText("家属信息未添加");
        return view;
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
