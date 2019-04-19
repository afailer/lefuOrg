package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.db.util.SignConfigManager;
import com.lefuorgn.interf.OnItemChildColumnClickListener;
import com.lefuorgn.lefu.adapter.SignInfoAdapter;
import com.lefuorgn.lefu.base.BaseGridActivity;
import com.lefuorgn.lefu.base.BaseGridAdapter;
import com.lefuorgn.lefu.bean.SearchConditionGrid;
import com.lefuorgn.lefu.bean.SignInfo;
import com.lefuorgn.lefu.bean.SignItemInfo;
import com.lefuorgn.lefu.dialog.SignInfoDialog;
import com.lefuorgn.lefu.dialog.SignInfoSpecialDialog;

import java.util.List;

/**
 * 体征信息页面
 */
public class SignInfoActivity extends BaseGridActivity<SignInfo> {

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
        return SignConfigManager.getSignOrNursingItem(true);
    }

    @Override
    protected BaseGridAdapter getBaseGridAdapter(List<SignInfo> result, List<DisplaySignOrNursingItem> items) {
        return new SignInfoAdapter(this, result, items);
    }

    @Override
    protected List<SignInfo> readList(SearchConditionGrid condition, List<DisplaySignOrNursingItem> items) {
        List<SignInfo> list;
        if(first) {
            // 首次加载数据
            list = OldPeopleManager.getSignInfoData(condition, items, true, attention);
            if(list.size() > 0) {
                SignInfo info = list.get(0);
                attention = info.iscAttention();
            }
            first = false;
        }else {
            list = OldPeopleManager.getSignInfoData(condition, items, false, attention);
        }
        return list;
    }

    @Override
    protected void initAdapter(final BaseGridAdapter mAdapter) {
        // 行名称点击事件
        mAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int i) {
                SignInfo sign = (SignInfo) adapter.getData().get(i);
                Intent intent = new Intent(SignInfoActivity.this, SignInfoDetailsActivity.class);
                intent.putExtra("id", sign.getOldPeopleId());
                intent.putExtra("name", sign.getOldPeopleName());
                startActivityForResult(intent, 100);
            }
        });
        // 模块点击事件
        mAdapter.setOnItemChildColumnClickListener(new OnItemChildColumnClickListener<SignInfo>() {

            @Override
            public void onClick(View v, final SignInfo item, final int position, int column) {
                if(!isToday()) {
                    showToast("只能添加当天数据");
                    return;
                }
                final SignItemInfo info = item.getSignItemInfos().get(column);
                final String type = info.getName();
                if("进食".equals(type) || "睡眠".equals(type)) {
                    new SignInfoSpecialDialog().setTitle(type)
                            .setClickCallBack(new SignInfoSpecialDialog.ClickCallBack() {
                                @Override
                                public void saveClick(String value, int color) {
                                    long time = OldPeopleManager.saveSignItemInfo(mUser, mAgencyId, item.getOldPeopleId(),
                                            item.getOldPeopleName(), value, "", info.getName());
                                    if(time == 0) {
                                        showToast("保存失败");
                                    }else {
                                        if("进食".equals(type)) {
                                            String[] str = value.split(";");
                                            String[] types = str[0].split(",");
                                            String content = types[types.length - 1];
                                            content = content + str[1];
                                            info.setContent(content);
                                        }else {
                                            info.setContent(value);
                                        }
                                        info.setColor(color);
                                        info.setTime(time);
                                        mAdapter.notifyItemChanged(position);
                                    }
                                }
                            }).show(getSupportFragmentManager(), "");
                }else {
                    new SignInfoDialog().setTitle(info.getName())
                            .setSignConfig(SignConfigManager.getSignConfig(info.getName()))
                            .setShowDevice(true)
                            .setClickCallBack(new SignInfoDialog.ClickCallBack() {
                                @Override
                                public void deviceClick() {
                                    redirectTo(info.getName(), item);
                                }

                                @Override
                                public void saveClick(String value, int color) {
                                    // 保存数据到数据库中
                                    long time = OldPeopleManager.saveSignItemInfo(mUser, mAgencyId, item.getOldPeopleId(),
                                            item.getOldPeopleName(), value, "", info.getName());
                                    if(time == 0) {
                                        showToast("保存失败");
                                    }else {
                                        info.setContent(value);
                                        info.setColor(color);
                                        info.setTime(time);
                                        mAdapter.notifyItemChanged(position);
                                        // 数据有更新
                                        update = true;
                                    }
                                }
                            }).show(SignInfoActivity.this.getSupportFragmentManager(), "");
                }
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

    /**
     * 跳转到硬件采集数据页面
     * @param type 当前数据类型
     */
    private void redirectTo(String type, SignInfo item) {
        if("血压".equals(type)) {
            Intent intent = new Intent(this, SignBloodPressureActivity.class);
            intent.putExtra("SignInfo", item);
            startActivityForResult(intent, 100);
        }else if("血糖".equals(type)) {
            Intent intent = new Intent(this, SignBloodSugarActivity.class);
            intent.putExtra("SignInfo", item);
            startActivityForResult(intent, 100);
        }
    }

}
