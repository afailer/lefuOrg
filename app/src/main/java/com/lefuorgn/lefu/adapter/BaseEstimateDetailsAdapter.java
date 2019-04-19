package com.lefuorgn.lefu.adapter;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.lefu.bean.Option;
import com.lefuorgn.lefu.bean.Question;
import com.lefuorgn.lefu.util.EstimateUtils;

import java.util.List;

/**
 * 评估表信息适配器
 */

public class BaseEstimateDetailsAdapter extends BaseQuickAdapter<Question> {

    private EstimateUtils mEstimateUtils;

    public BaseEstimateDetailsAdapter(Context context, List<Question> data) {
        super(R.layout.item_base_activity_estimate_details, data);
        mEstimateUtils = new EstimateUtils(context);
        initListener();
    }

    @Override
    protected void convert(BaseViewHolder holder, Question question) {
        // 当前条目位置
        int position = holder.getLayoutPosition() - this.getHeaderViewsCount();
        holder.setText(R.id.tv_item_base_activity_estimate_details_no, (position + 1) + ".")
                .setText(R.id.tv_item_base_activity_estimate_details_title, question.getQuestion());
        // 获取选项布局
        FrameLayout option = holder.getView(R.id.fl_item_base_activity_estimate_details_options);
        // 清空布局
        option.removeAllViews();
        // 判断当前选项的类型
        if(question.getType() == EstimateUtils.SINGLE_SELECTION) {
            // 单选
            option.addView(mEstimateUtils.getRadioView(position + 1, question.getOptions(), position));
        }else if(question.getType() == EstimateUtils.MULTISELECT) {
            // 多项选择
            option.addView(mEstimateUtils.getMultiChoiceView(position + 1, question.getOptions(), position));
        }
    }

    private void initListener() {
        // 添加选项的选择事件
        mEstimateUtils.setOnCheckedListener(new EstimateUtils.OnCheckedListener() {
            @Override
            public void onCheckedChanged(int position, RadioGroup group, int checkedId) {
                // 单选按钮的选择事件
                List<Option> options = ((Question) getData().get(position)).getOptions();
                for (int i = 1; i <= options.size(); i++) {
                    if (i == checkedId) {
                        options.get(i - 1).setStatus(1);
                    } else {
                        options.get(i - 1).setStatus(0);
                    }
                }
            }

            @Override
            public void onCheckedChanged(int position, int checkBoxId, CompoundButton view, boolean isChecked) {
                // 多选框的选择事件
            }
        });
    }

}
