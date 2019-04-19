package com.lefuorgn.lefu.base;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lefuorgn.R;
import com.lefuorgn.api.common.Json;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.lefu.adapter.BaseEstimateDetailsAdapter;
import com.lefuorgn.lefu.bean.Estimate;
import com.lefuorgn.lefu.bean.Option;
import com.lefuorgn.lefu.bean.Question;
import com.lefuorgn.util.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 评估表详情页面
 */

public abstract class BaseEstimateDetailsActivity extends BaseActivity {

    private TextView mNameView, mIDView;
    private RecyclerView mRecyclerView;
    private BaseEstimateDetailsAdapter mAdapter;

    // 数据统计的时候进行的校验信息
    protected StringBuilder note = new StringBuilder();
    // 在提交数据时进行的分值统计
    protected int sum;

    @Override
    protected final int getLayoutId() {
        return R.layout.activity_base_estimate_details;
    }

    @Override
    protected final void initView() {
        mNameView = (TextView) findViewById(R.id.tv_activity_base_estimate_details_name);
        mIDView = (TextView) findViewById(R.id.tv_activity_base_estimate_details_id);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_activity_base_estimate_details);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 设置数据数据
     * @param estimate 评估信息
     */
    protected void setData(Estimate estimate) {
        if(estimate == null) {
            return;
        }
        TextView tv = setToolBarTitle(estimate.getTitle());
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        display.getMetrics(outMetrics);
        tv.setMaxWidth(outMetrics.widthPixels * 2 / 3);
        mNameView.setText(estimate.getOld_people_name());
        mIDView.setText(estimate.getOld_people_card_number());
        if(mAdapter == null) {
            mAdapter = new BaseEstimateDetailsAdapter(this, estimate.getQuestions());
            mAdapter.setEmptyView(getEmptyView());
            mRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.setNewData(estimate.getQuestions());
        }
        if(estimate.getQuestions().size() > 0) {
            mAdapter.addFooterView(getFooterView(mRecyclerView));
        }
    }

    /**
     * 校验当前评估表是否完成
     *
     * @return true: 完成; false: 未完成
     */
    protected boolean isComplete() {
        // 将当前的分值置为0
        sum = 0;
        // 记录是否所有的题目完成
        boolean complete = true;
        // 记录每个问题中的选项是否有一个被选中
        boolean isSelect;
        // 清空note
        note.delete(0, note.length());
        note.append("你的第");
        List<Question> questions = mAdapter.getData();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            // 判断选项是否被选择
            // 是否有一个选项被选中
            isSelect = false;
            List<Option> options = question.getOptions();
            for (Option option : options) {
                if (option.getStatus() != 0) {
                    isSelect = true;
                    sum += option.getScore();
                    break;
                }
            }
            if (!isSelect) {
                complete = false;
                note.append((i + 1)).append("、");
            }
        }
        if (!complete) {
            // 未完成
            // 删除最后一个顿号
            note.deleteCharAt(note.length() - 1);
            note.append("题未完成");
        }
        return complete;
    }

    /**
     * 获取空内容指示信息控件
     * @return 控件
     */
    private View getEmptyView() {
        return getLayoutInflater().inflate(R.layout.item_recyclerview_empty,
                (ViewGroup) mRecyclerView.getParent(), false);
    }

    /**
     * 添加footerView; 如果不添加, 不复写本方法, 或者返回null
     * @param parent 父View
     * @return footer控件
     */
    protected View getFooterView(ViewGroup parent) {
        return null;
    }

    /**
     * 解析json
     * @param json json字符串
     * @return 问题集合
     */
    protected List<Question> getQuestion(String json) {
        if(StringUtils.isEmpty(json)) {
            return new ArrayList<Question>();
        }
        Type type = new TypeToken<List<Question>>(){}.getType();
        return Json.getGson().fromJson(json, type);
    }

    /**
     * 将问题列表转换成json数据
     * @param list 要转换的列表
     * @return json数据格式
     */
    protected String getQuestionJson(List<Question> list) {
        return Json.getGson().toJson(list);
    }

    @Override
    protected final boolean hasToolBar() {
        return true;
    }

    @Override
    protected final boolean hasBackButton() {
        return true;
    }
}
