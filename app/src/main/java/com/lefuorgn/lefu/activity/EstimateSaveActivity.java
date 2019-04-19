package com.lefuorgn.lefu.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.lefu.base.BaseEstimateDetailsActivity;
import com.lefuorgn.dialog.AlertDialog;
import com.lefuorgn.lefu.bean.Estimate;

/**
 * 评估表测试页面
 */

public class EstimateSaveActivity extends BaseEstimateDetailsActivity {

    private Estimate mEstimate;

    @Override
    protected void initData() {
        final Estimate estimate = (Estimate) getIntent().getSerializableExtra("estimate");
        showWaitDialog();
        LefuApi.getEstimate(AppContext.getInstance().getAgencyId(), estimate.getId()
                , new RequestCallback<Estimate>() {
            @Override
            public void onSuccess(Estimate result) {
                if(result == null) {
                    result = new Estimate();
                    result.setId(estimate.getId());
                    result.setTitle(estimate.getTitle());
                    result.setReserved("");
                }else {
                    result.setQuestions(getQuestion(result.getContent()));
                }
                result.setOld_people_id(estimate.getOld_people_id());
                result.setOld_people_name(estimate.getOld_people_name());
                result.setOld_people_card_number(estimate.getOld_people_card_number());
                mEstimate = result;
                setData(result);
                hideWaitDialog();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                setData(new Estimate());
                hideWaitDialog();
                showToast(e.getMessage());
            }
        });
    }

    @Override
    protected View getFooterView(ViewGroup parent) {
        View view = getLayoutInflater().inflate(R.layout.item_estimate_footer, parent, false);
        Button tv = (Button) view.findViewById(R.id.btn_item_estimate_footer);
        tv.setText("保存");
        tv.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(!isComplete()) {
            new AlertDialog().setContent(note.toString())
                    .isShowCancelBtn(false)
                    .show(getSupportFragmentManager(), "");
            return;
        }
        showLoadingDialog();
        // 提交数据
        String json = getQuestionJson(mEstimate.getQuestions());
        LefuApi.saveEstimate(mEstimate, json, sum, new RequestCallback<String>(){
            @Override
            public void onSuccess(String result) {
                hideLoadingDialog();
                showToast("数据保存成功");
                finish();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                hideLoadingDialog();
                showToast("数据保存失败");
            }
        });
    }

}
