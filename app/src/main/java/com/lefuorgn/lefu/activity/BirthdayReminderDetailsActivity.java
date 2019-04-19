package com.lefuorgn.lefu.activity;

import android.content.Intent;

import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseRecyclerViewActivity;
import com.lefuorgn.lefu.bean.BirthdayDetails;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 生日提醒详情页面
 */

public class BirthdayReminderDetailsActivity extends BaseRecyclerViewActivity<BirthdayDetails> {

    private String mMonth;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_birthday_reminder_details;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_activity_birthday_reminder_details;
    }

    @Override
    protected void initChildData() {
        setToolBarTitle("生日提醒");
    }

    @Override
    protected void onBeforeSetContentLayout() {
        Intent intent = getIntent();
        mMonth = intent.getStringExtra("month");
    }

    @Override
    protected void loadData(final int pageNo) {
        LefuApi.getBirthdayDetails(pageNo, mMonth, new RequestCallback<List<BirthdayDetails>>() {
            @Override
            public void onSuccess(List<BirthdayDetails> result) {
                setResult(pageNo, result);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                setResult(pageNo, new ArrayList<BirthdayDetails>());
                showToast(e.getMessage());
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BirthdayDetails birthday) {
        baseViewHolder.setText(R.id.tv_item_activity_birthday_reminder_details_name, birthday.getElderly_name())
                .setText(R.id.tv_item_activity_birthday_reminder_details_data,
                        StringUtils.getFormatData(birthday.getBirthday_dt(), "yyyy-MM-dd"))
                .setText(R.id.tv_item_activity_birthday_reminder_details_type,
                        birthday.getBirthday_type() == 1 ? "公历" : "农历")
                .setText(R.id.tv_item_activity_birthday_reminder_details_num,
                        birthday.getBed_no());
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
