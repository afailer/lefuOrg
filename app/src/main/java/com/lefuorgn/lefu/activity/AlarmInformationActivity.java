package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.lefu.adapter.AlarmInformationAdapter;
import com.lefuorgn.lefu.bean.AlarmEntry;
import com.lefuorgn.lefu.bean.AlarmInformation;

import java.util.ArrayList;
import java.util.List;

/**
 * 告警信息查询
 */

public class AlarmInformationActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private int mSOSNum; // SOS报警信息未完成个数
    private int mFenceNum; // 围栏报警信息未完成个数
    private int mLowBatteryNum; // 低电量报警信息未完成个数

    private AlarmInformationAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm_information;
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_activity_alarm_information);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {
        setToolBarTitle("告警信息");
        showWaitDialog();
        getSOSNum();
    }

    /**
     * 获取SOS未处理告警信息的条目数;
     * 注意: sos和电子围栏告警信息未处理条目的获取是同一个接口,因此不可同时去请求数据,否则可能会报错
     */
    private void getSOSNum() {
        LefuApi.getAlarmDetailsInfo(0, 1, 0, new RequestCallback<List<AlarmEntry>>() {
            @Override
            public void onSuccess(List<AlarmEntry> result) {
                mSOSNum = result == null ? 0 : result.size();
                getFenceNum();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                mSOSNum = 0;
                getFenceNum();
            }
        });
    }

    /**
     * 获取电子围栏未处理告警信息条目数
     */
    private void getFenceNum() {
        LefuApi.getAlarmDetailsInfo(0, 2, 0, new RequestCallback<List<AlarmEntry>>() {
            @Override
            public void onSuccess(List<AlarmEntry> result) {
                mFenceNum = result == null ? 0 : result.size();
                getLowBatteryNum();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                mFenceNum = 0;
                getLowBatteryNum();
            }
        });
    }

    /**
     * 获取低电量未处理告警信息条目数
     */
    private void getLowBatteryNum() {
        LefuApi.getAlarmDetailsInfo(0, 3, 0, new RequestCallback<List<AlarmEntry>>() {
            @Override
            public void onSuccess(List<AlarmEntry> result) {
                hideWaitDialog();
                mLowBatteryNum = result == null ? 0 : result.size();
                initPageInfo();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                hideWaitDialog();
                showToast(e.getMessage());
                mLowBatteryNum = 0;
                initPageInfo();
            }
        });
    }

    /**
     * 设置页面信息值
     */
    private void initPageInfo() {
        if(mAdapter == null) {
            mAdapter = new AlarmInformationAdapter(getAlarmInformation());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    AlarmInformation info = mAdapter.getItem(i);
                    if(info.getCls() == null) {
                        showToast(R.string.permission_no);
                        return;
                    }
                    Intent intent = new Intent(AlarmInformationActivity.this, info.getCls());
                    intent.putExtra("title", info.getName());
                    intent.putExtra("type", info.getType());
                    startActivityForResult(intent, 100);
                }
            });
        }else {
            mAdapter.setNewData(getAlarmInformation());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200) {
            showWaitDialog();
            getSOSNum();
        }
    }

    /**
     * 获取告警信息列表
     */
    private List<AlarmInformation> getAlarmInformation() {
        List<AlarmInformation> result = new ArrayList<AlarmInformation>();
        // SOS告警
        result.add(getAlarmInfo("SOS告警", R.mipmap.sos_alarm,
                AlarmInformationDetailsActivity.INTENT_ALARM_TYPE_SOS, mSOSNum));
        // 电子围栏告警
        result.add(getAlarmInfo("电子围栏告警", R.mipmap.electronic_fence_alarm,
                AlarmInformationDetailsActivity.INTENT_ALARM_TYPE_ELECTRONIC_FENCE, mFenceNum));
        // 电量告警
        result.add(getAlarmInfo("电量告警", R.mipmap.low_battery,
                AlarmInformationDetailsActivity.INTENT_ALARM_TYPE_LOW_BATTERY, mLowBatteryNum));
        return result;
    }

    /**
     * 获取指定的信息
     * @param name 告警名称
     * @param drawableRes 资源ID
     * @param type 告警类别
     * @param num 未处理告警信息条数
     */
    private AlarmInformation getAlarmInfo(String name, int drawableRes, int type, int num) {
        AlarmInformation info = new AlarmInformation();
        info.setName(name);
        info.setDrawableRes(drawableRes);
        info.setCls(AlarmInformationDetailsActivity.class);
        info.setType(type);
        info.setInfoNum(num);
        info.setSkipInfo("详细信息 >");
        return info;
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
