package com.lefuorgn.lefu.activity;

import com.lefuorgn.R;
import com.lefuorgn.lefu.base.BaseEstimateDetailsActivity;
import com.lefuorgn.lefu.bean.Estimate;

/**
 * 评估表测试页面
 */

public class EstimateUpdateActivity extends BaseEstimateDetailsActivity {

    private Estimate mEstimate;

    // 记录用户当前是否有修改评估权限
//    private boolean updatePermission;

    @Override
    protected void initData() {
//        updatePermission = PermissionManager
//                .hasPermission(PermissionManager.EXAM_ANSWER + PermissionManager.P_U);
        mEstimate = (Estimate) getIntent().getSerializableExtra("estimate");
        mEstimate.setQuestions(getQuestion(mEstimate.getContent()));
        setData(mEstimate);
    }

//    @Override
//    protected View getFooterView(ViewGroup parent) {
//        View view = getLayoutInflater().inflate(R.layout.item_estimate_footer, parent, false);
//        Button tv = (Button) view.findViewById(R.id.btn_item_estimate_footer);
//        tv.setText("修改");
//        tv.setOnClickListener(this);
//        if(!updatePermission) {
//            tv.setVisibility(View.GONE);
//        }
//        return view;
//    }

//    @Override
//    public void onClick(View v) {
//        if(!isComplete()) {
//            new AlertDialog().setContent(note.toString())
//                    .isShowCancelBtn(false)
//                    .show(getSupportFragmentManager(), "");
//            return;
//        }
//        showWaitDialog();
////        // 提交数据
//        String json = getQuestionJson(mEstimate.getQuestions());
//        LefuApi.updateEstimate(mEstimate, json, sum, new RequestCallback<String>(){
//            @Override
//            public void onSuccess(String result) {
//                hideWaitDialog();
//                showToast("数据修改成功");
//                setResult(200);
//                finish();
//            }
//
//            @Override
//            public void onFailure(ApiHttpException e) {
//                hideWaitDialog();
//                showToast("数据修改失败");
//            }
//        });
//    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
