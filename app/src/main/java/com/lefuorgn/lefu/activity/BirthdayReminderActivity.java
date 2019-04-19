package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseRecyclerViewActivity;
import com.lefuorgn.lefu.bean.Birthday;

import java.util.ArrayList;
import java.util.List;

/**
 * 生日提醒详情页面
 */

public class BirthdayReminderActivity extends BaseRecyclerViewActivity<Birthday> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_birthday_reminder;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_activity_birthday_reminder;
    }

    @Override
    protected void initChildData() {
        setToolBarTitle("生日提醒");
    }

    @Override
    protected void loadData(final int pageNo) {
        LefuApi.getBirthday(pageNo, new RequestCallback<List<Birthday>>() {
            @Override
            public void onSuccess(List<Birthday> result) {
                setResult(pageNo, result);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                setResult(pageNo, new ArrayList<Birthday>());
                showToast(e.getMessage());
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final Birthday birthday) {
        baseViewHolder.setText(R.id.tv_item_activity_birthday_reminder_data, birthday.getMONTH())
                .setText(R.id.tv_item_activity_birthday_reminder_num, birthday.getCOUNT() + "");
        baseViewHolder.getView(R.id.tv_item_activity_birthday_reminder_operation)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BirthdayReminderActivity.this, BirthdayReminderDetailsActivity.class);
                intent.putExtra("month", birthday.getMONTH());
                startActivity(intent);
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
