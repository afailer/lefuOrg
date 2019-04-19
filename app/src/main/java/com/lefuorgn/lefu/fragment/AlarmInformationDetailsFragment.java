package com.lefuorgn.lefu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseRecyclerViewFragment;
import com.lefuorgn.lefu.activity.AlarmInformationDetailsActivity;
import com.lefuorgn.lefu.activity.AlarmSolveActivity;
import com.lefuorgn.lefu.activity.LowBatterySolveActivity;
import com.lefuorgn.lefu.bean.AlarmEntry;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 告警信息类别详情
 */

public class AlarmInformationDetailsFragment extends BaseRecyclerViewFragment<AlarmEntry> {

    /**
     * 页面所属类型
     */
    public static final String BUNDLE_ALARM_INFO_TYPE = "bundle_alarm_info_type";
    /**
     * 页面所属类型名称
     */
    public static final String BUNDLE_ALARM_INFO_TITLE = "bundle_alarm_info_title";
    /**
     * 当前页面加载数据的状态
     */
    public static final String BUNDLE_ALARM_INFO_STATUS = "bundle_alarm_info_status";
    /**
     * 未处理状态
     */
    public static final int BUNDLE_ALARM_INFO_STATUS_UNRESOLVED = 0;
    /**
     * 处理中状态
     */
    public static final int BUNDLE_ALARM_INFO_STATUS_SOLVING = 1;
    /**
     * 已处理状态
     */
    public static final int BUNDLE_ALARM_INFO_STATUS_SOLVED = 2;

    private int mType; // 当前报警数据类型
    private String mTypeName; // 当前报警数据类型名称
    private int mStatus; // 当前报警数据类型的状态

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_fragment_alarm_info_details;
    }

    @Override
    protected void initChildData() {
        Bundle bundle = getArguments();
        mType = bundle.getInt(BUNDLE_ALARM_INFO_TYPE);
        mStatus = bundle.getInt(BUNDLE_ALARM_INFO_STATUS);
        mTypeName = bundle.getString(BUNDLE_ALARM_INFO_TITLE);
    }

    @Override
    protected void loadData(final int pageNo) {
        LefuApi.getAlarmDetailsInfo(pageNo, mType, mStatus, new RequestCallback<List<AlarmEntry>>() {
            @Override
            public void onSuccess(List<AlarmEntry> result) {
                setResult(pageNo, result);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                setResult(pageNo, new ArrayList<AlarmEntry>());
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder holder, AlarmEntry entry) {
        holder.setText(R.id.tv_item_fragment_alarm_info_details_name, entry.getOlder_name())
                .setText(R.id.tv_item_fragment_alarm_info_details_place,
                        mType != AlarmInformationDetailsActivity.INTENT_ALARM_TYPE_LOW_BATTERY
                                ? entry.getAddress() : "低电量: " + entry.getRemaining_power() + "%")
                .setText(R.id.tv_item_fragment_alarm_info_details_time,
                        StringUtils.getFormatData(entry.getTime(), "MM-dd HH:mm"))
                .setText(R.id.btn_item_fragment_alarm_info_details_refresh,
                        mStatus == AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_SOLVED ? "查看" : "处理");
    }

    @Override
    protected void initListener(final BaseRecyclerViewFragmentAdapter adapter) {
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                AlarmEntry entry = adapter.getItem(i);
                Intent intent;
                if(mType == AlarmInformationDetailsActivity.INTENT_ALARM_TYPE_LOW_BATTERY) {
                    intent = new Intent(getActivity(), LowBatterySolveActivity.class);
                }else {
                    intent = new Intent(getActivity(), AlarmSolveActivity.class);
                }
                intent.putExtra("id", entry.getId());
                intent.putExtra("status", mStatus);
                intent.putExtra("title", mTypeName);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200) {
            ((AlarmInformationDetailsActivity)getActivity()).refreshPage(mStatus);
        }
    }

    /**
     * 刷新页面数据
     */
    public void refresh() {
        resetResult();
    }

    public int getStatus() {
        return mStatus;
    }
}
