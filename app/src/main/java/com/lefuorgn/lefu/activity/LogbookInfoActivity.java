package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseRecyclerViewActivity;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.util.PermissionManager;
import com.lefuorgn.lefu.bean.Logbook;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 老人交班记录信息
 */

public class LogbookInfoActivity extends BaseRecyclerViewActivity<Logbook> {

    private OldPeople mOldPeople;

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_activity_logbook_info;
    }

    @Override
    protected void initChildData() {
        setToolBarTitle("交班记录");
        // 获取用户是否拥有添加交班信息的权限
        boolean insert = PermissionManager
                .hasPermission(PermissionManager.LOGBOOK + PermissionManager.P_C);
        if(insert) {
            setMenuTextView("添加");
        }
        Intent intent = getIntent();
        mOldPeople = (OldPeople) intent.getSerializableExtra("oldPeople");
    }

    @Override
    protected void loadData(final int pageNo) {

        LefuApi.getLogbookInfo(mOldPeople.getId(), pageNo, new RequestCallback<List<Logbook>>() {
            @Override
            public void onSuccess(List<Logbook> result) {
                setResult(pageNo, result);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                setResult(pageNo, new ArrayList<Logbook>());
                showToast(e.getMessage());
            }
        });
    }

    @Override
    protected void initListener(final BaseAdapter baseAdapter) {
        baseAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Logbook logbook = (Logbook) baseAdapter.getData().get(position);
                Intent intent = new Intent(LogbookInfoActivity.this, LogbookInfoDetailsActivity.class);
                intent.putExtra("name", "记录详情");
                intent.putExtra("logbook", logbook);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onMenuClick(View v) {
        Intent intent = new Intent(LogbookInfoActivity.this, LogbookInfoDetailsActivity.class);
        intent.putExtra("name", "添加交班记录");
        intent.putExtra("oldPeople", mOldPeople);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void convert(BaseViewHolder holder, Logbook logbook) {
        // 获取当前条目的位置
        int position = holder.getLayoutPosition() - mBaseAdapter.getHeaderViewsCount();
        boolean display;
        if(position == 0) {
            display = true;
        }else {
            // 拿到前一个数据
            Logbook preLogbook = mBaseAdapter.getItem(position - 1);
            display = isDisplay(logbook.getInspect_time(), preLogbook.getInspect_time());
        }
        holder.setText(R.id.logbook_title_time, getStringNote(logbook.getInspect_time()))
                .setVisible(R.id.logbook_title_time, display)
                .setText(R.id.tv_item_activity_logbook_info_name, logbook.getOld_people_name())
                .setText(R.id.tv_item_activity_logbook_info_date,
                        StringUtils.getFormatData(logbook.getInspect_time(), "yyyy.MM.dd HH:mm"))
                .setText(R.id.tv_item_activity_logbook_info_type,
                        logbook.getCare_name())
                .setText(R.id.tv_item_activity_logbook_info_content, logbook.getContent());
    }
    /**
     * 判断当前条目是否显示指示字段
     * @param time1 当期条目显示的时间
     * @param time2 前一个条目显示的时间
     * @return true： 显示； false： 不显示
     */
    private boolean isDisplay(long time1, long time2) {
        // 前天凌晨12点
        long time = StringUtils.getFirstNDays(2);
        if(time1 < time) {
            return time2 >= time;
        }
        String timeStr1 = StringUtils.getFormatData(time1, "yyyy-MM-dd");
        String timeStr2 = StringUtils.getFormatData(time2, "yyyy-MM-dd");
        return !timeStr1.equals(timeStr2);
    }

    private String getStringNote(long time) {
        if(time >= StringUtils.getFirstNDays(0)) {
            return "今天";
        }else if(time >= StringUtils.getFirstNDays(1)) {
            return "昨天";
        }else if(time >= StringUtils.getFirstNDays(2)) {
            return "前天";
        }else {
            return "三天前";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200) {
            // 数据添加成功, 刷新数据
            resetResult();
        }
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
