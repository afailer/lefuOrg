package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.interf.OnItemChildColumnClickListener;
import com.lefuorgn.lefu.adapter.ReadilyShootAdapter;
import com.lefuorgn.lefu.base.BaseGridActivity;
import com.lefuorgn.lefu.base.BaseGridAdapter;
import com.lefuorgn.lefu.bean.MultiMedia;
import com.lefuorgn.lefu.bean.NursingInfo;
import com.lefuorgn.lefu.bean.NursingItemInfo;
import com.lefuorgn.lefu.bean.NursingOldPeople;
import com.lefuorgn.lefu.bean.SearchConditionGrid;
import com.lefuorgn.lefu.dialog.NursingInfoDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 随手拍页面
 */

public class ReadilyShootActivity extends BaseGridActivity<NursingInfo> {

    /**
     * 记录当前数据是否是第一次加载
     */
    private boolean first = true;
    /**
     * 记录当前加载老人数据的类型,根据第一次加载返回的数据进行确定;以后每次加载,根据这个记录进行数据请求
     */
    private boolean attention = false;

    @Override
    protected List<DisplaySignOrNursingItem> getItem() {
        List<DisplaySignOrNursingItem> list = new ArrayList<DisplaySignOrNursingItem>();
        DisplaySignOrNursingItem item = new DisplaySignOrNursingItem();
        item.setTitle("随手拍");
        list.add(item);
        return list;
    }

    @Override
    protected BaseGridAdapter getBaseGridAdapter(List<NursingInfo> result, List<DisplaySignOrNursingItem> items) {
        return new ReadilyShootAdapter(this, result, items);
    }

    @Override
    protected List<NursingInfo> readList(SearchConditionGrid condition, List<DisplaySignOrNursingItem> items) {
        List<NursingInfo> list;
        if(first) {
            // 首次加载数据
            list = OldPeopleManager.getReadilyShootData(condition, items, true, attention);
            if(list.size() > 0) {
                NursingInfo info = list.get(0);
                attention = info.iscAttention();
            }
            first = false;
        }else {
            list = OldPeopleManager.getReadilyShootData(condition, items, false, attention);
        }
        return list;
    }

    @Override
    protected void initAdapter(final BaseGridAdapter mAdapter) {
        // 行名称点击事件
        mAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int i) {
                NursingInfo sign = (NursingInfo) adapter.getData().get(i);
                Intent intent = new Intent(ReadilyShootActivity.this, ReadilyShootDetailsActivity.class);
                intent.putExtra("id", sign.getOldPeopleId());
                intent.putExtra("name", sign.getOldPeopleName());
                startActivityForResult(intent, 100);
            }
        });
        // 模块点击事件
        mAdapter.setOnItemChildColumnClickListener(new OnItemChildColumnClickListener<NursingInfo>() {

            @Override
            public void onClick(View v, final NursingInfo item, final int position, int column) {
                if(!isToday()) {
                    showToast("只能添加当天数据");
                    return;
                }
                final NursingItemInfo info = item.getNursingItemInfoList().get(column);
                final NursingOldPeople o = new NursingOldPeople();
                o.setOld_people_id(item.getOldPeopleId());
                o.setOld_people_name(item.getOldPeopleName());
                new NursingInfoDialog()
                        .setTitle("随手拍")
                        .setNursingOldPeople(o)
                        .setAttention(attention)
                        .setClickCallBack(new NursingInfoDialog.ClickCallBack() {

                            @Override
                            public void saveClick(String remarks, List<MultiMedia> multiMedia, List<NursingOldPeople> data) {
                                OldPeopleManager.saveReadilyShootItemInfo(mUser, mAgencyId, remarks, multiMedia, data);
                                // 刷新数据
                                if(data.size() == 1 && o.getOld_people_name().equals(data.get(0).getOld_people_name())) {
                                    // 当前老人
                                    info.setNurse_times(info.getNurse_times() + 1);
                                    mAdapter.notifyItemChanged(position);
                                }else {
                                    // 可能多个老人, 重新加载数据
                                    resetResult();
                                }
                                // 数据有更新
                                update = true;
                            }
                        }).show(getSupportFragmentManager(), "");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200) {
            resetResult();
            // 数据有更新
            update = true;
        }
    }

}
