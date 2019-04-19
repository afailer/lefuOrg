package com.lefuorgn.lefu.fragment;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.api.common.Json;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseRecyclerViewFragment;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.util.DataProcessUtils;
import com.lefuorgn.lefu.activity.DataAuditActivity;
import com.lefuorgn.lefu.bean.SignDataAudit;
import com.lefuorgn.lefu.dialog.DataAuditDialog;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据审核; 每个体征项详情展示页面
 */

public class DataAuditFragment extends BaseRecyclerViewFragment<SignDataAudit> {

    public static final String BUNDLE_DATA_AUDIT_TYPE = "bundle_data_audit_type";

    private DataAuditActivity mActivity;
    private BaseRecyclerViewFragmentAdapter mAdapter;

    /**
     * 当前页面信息
     */
    private DisplaySignOrNursingItem mItem;

    private boolean mSelect;
    private int mSelectNum;

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_fragment_data_audit;
    }

    @Override
    protected void initChildData() {
        mItem = (DisplaySignOrNursingItem) getArguments().getSerializable(BUNDLE_DATA_AUDIT_TYPE);
    }

    @Override
    protected void loadData(final int pageNo) {
        LefuApi.getDataAudit(pageNo, getUri(), new RequestCallback<List<SignDataAudit>>() {
            @Override
            public void onSuccess(List<SignDataAudit> result) {
                checkData(pageNo, result);
                setResult(pageNo, result);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                checkData(pageNo, new ArrayList<SignDataAudit>());
                setResult(pageNo, new ArrayList<SignDataAudit>());
                showToast(e.getMessage());
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder holder, SignDataAudit d) {
        holder.setText(R.id.tv_item_fragment_data_audit_date,
                        StringUtils.getFormatData(d.getInspect_dt(), "yyyy-MM-dd"))
                .setText(R.id.tv_item_fragment_data_audit_time,
                        StringUtils.getFormatData(d.getInspect_dt(), "HH:mm:ss"))
                .setText(R.id.tv_item_fragment_data_audit_value, getValue(d))
                .setText(R.id.tv_item_fragment_data_audit_name, d.getOld_people_name())
                .setText(R.id.tv_item_fragment_data_audit_inspect_name, d.getInspect_user_name())
                .setChecked(R.id.cb_item_fragment_data_audit, d.isSelect());
    }

    @Override
    protected void initListener(BaseRecyclerViewFragmentAdapter adapter) {
        mAdapter = adapter;
        mAdapter.setOnRecyclerViewItemClickListener(
                new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                SignDataAudit s = mAdapter.getItem(i);
                s.setSelect(!s.isSelect());
                mSelectNum = s.isSelect() ? mSelectNum + 1 : mSelectNum - 1;
                if(mSelectNum == mAdapter.getData().size()) {
                    mSelect = true;
                    mActivity.setSelectAll(true);
                }else {
                    mSelect = false;
                    mActivity.setSelectAll(false);
                }
                mAdapter.notifyItemChanged(i);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (DataAuditActivity) getActivity();
    }

    private void checkData(int pageNo, List<SignDataAudit> list) {
        if(pageNo == 1) {
            // 加载首页的时候, 选择数置0
            mSelectNum = 0;
            mSelect = false;
        }else if(list.size() > 0) {
            mSelect = false;
        }
        mActivity.setSelectAll(mSelect);
    }

    /**
     * 全选按钮事件触发
     */
    public void selectAll() {
        List<SignDataAudit> list = mAdapter.getData();
        if(mSelectNum == list.size()) {
            // 已经是全选了, 现在要置成全不选状态
            mSelect = false;
            for (SignDataAudit s : list) {
                s.setSelect(false);
            }
            mSelectNum = 0;
        }else {
            // 已经是全不选状态了, 要置成全选状态
            mSelect = true;
            for (SignDataAudit s : list) {
                s.setSelect(true);
            }
            mSelectNum = list.size();
        }
        mAdapter.notifyDataSetChanged();
        TLog.log("DataAuditFragment当前的状态是" + mSelect);
    }

    /**
     * 不通过按钮事件触发
     */
    public void notApproved() {
        if(mSelectNum == 0) {
            showToast("请至少选择一个待审核数据");
            return;
        }
        submitDataAudit(2, "");
    }

    /**
     * 备注按钮事件触发
     */
    public void remark() {
        if(mSelectNum == 0) {
            showToast("请至少选择一个待审核数据");
            return;
        }
        new DataAuditDialog().setClickCallBack(new DataAuditDialog.ClickCallBack() {
            @Override
            public void noPassClick(String value) {
                submitDataAudit(2, value);
            }

            @Override
            public void passClick(String value) {
                submitDataAudit(1, value);
            }
        }).show(getFragmentManager(), "DataAuditDialog");
    }

    /**
     * 通过按钮事件触发
     */
    public void approved() {
        if(mSelectNum == 0) {
            showToast("请至少选择一个待审核数据");
            return;
        }
        submitDataAudit(1, "");
    }

    private void submitDataAudit(int status, String remark) {
        showLoadingDialog();
        List<SignDataAudit> list = mAdapter.getData();
        List<Long> ids = new ArrayList<Long>();
        for (SignDataAudit s : list) {
            if(s.isSelect()) {
                if("血糖".equals(mItem.getTitle())) {
                    ids.add(s.getBlood_sugar_id());
                }else if("血压".equals(mItem.getTitle())) {
                    ids.add(s.getBlood_pressure_id());
                }else if("心率".equals(mItem.getTitle())) {
                    ids.add(s.getPulse_id());
                }else {
                    ids.add(s.getId());
                }
            }
        }
        String jsonId = Json.getGson().toJson(ids);
        LefuApi.submitDataAudit(getSubmitUri(), jsonId, status, remark, new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast("审核成功");
                hideLoadingDialog();
                resetResult();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast("审核失败");
                hideLoadingDialog();
                resetResult();
            }
        });
    }

    /**
     * 获取当前内容值
     * @param d 当前信息条目
     * @return 指定的内容值
     */
    private String getValue(SignDataAudit d) {
        String value;
        if("血糖".equals(mItem.getTitle())) {
            value = d.getBlood_sugar() + "";
        }else if("体温".equals(mItem.getTitle())) {
            value = d.getTemperature() + "";
        }else if("呼吸".equals(mItem.getTitle())) {
            value = d.getBreathing_times() + "";
        }else if("血压".equals(mItem.getTitle())) {
            value = d.getHigh_blood_pressure() + "/" + d.getLow_blood_pressure();
        }else if("心率".equals(mItem.getTitle())) {
            value = d.getPulse_number() + "";
        }else if("排便".equals(mItem.getTitle())) {
            value = d.getDefecation_times() + "";
        }else if("饮水".equals(mItem.getTitle())) {
            value = d.getWater_amount() + "";
        }else if("睡眠".equals(mItem.getTitle())) {
            value = d.getSleep_quality();
        }else if("进食".equals(mItem.getTitle())) {
            value = DataProcessUtils.getDrinkInfo(d.getMeal_type(), d.getMeal_amount());
        }else {
            value = "";
        }
        return value;
    }

    /**
     * 血糖Bsr,体温Tr,呼吸Br,血压Bpr,心率Pr,
     * 排便Dr,饮水dwrs,睡眠Sleep,进食meallist
     * @return 当前审核接口的小尾巴
     */
    private String getSubmitUri() {
        String uri;
        if("血糖".equals(mItem.getTitle())) {
            uri = "Bsr";
        }else if("体温".equals(mItem.getTitle())) {
            uri = "Tr";
        }else if("呼吸".equals(mItem.getTitle())) {
            uri = "Br";
        }else if("血压".equals(mItem.getTitle())) {
            uri = "Bpr";
        }else if("心率".equals(mItem.getTitle())) {
            uri = "Pr";
        }else if("排便".equals(mItem.getTitle())) {
            uri = "Dr";
        }else if("饮水".equals(mItem.getTitle())) {
            uri = "Dwr";
        }else if("睡眠".equals(mItem.getTitle())) {
            uri = "Sleep";
        }else if("进食".equals(mItem.getTitle())) {
            uri = "Meal";
        }else {
            uri = "";
        }
        return uri;
    }

    /**
     * 血糖bsrs,体温trs,呼吸brs,血压bprs,心率prs,
     * 排便drs,饮水dwrs,睡眠sleeplist,进食meallist
     * @return 当前请求接口的小尾巴
     */
    private String getUri() {
        String uri;
        if("血糖".equals(mItem.getTitle())) {
            uri = "bsrs";
        }else if("体温".equals(mItem.getTitle())) {
            uri = "trs";
        }else if("呼吸".equals(mItem.getTitle())) {
            uri = "brs";
        }else if("血压".equals(mItem.getTitle())) {
            uri = "bprs";
        }else if("心率".equals(mItem.getTitle())) {
            uri = "prs";
        }else if("排便".equals(mItem.getTitle())) {
            uri = "drs";
        }else if("饮水".equals(mItem.getTitle())) {
            uri = "dwrs";
        }else if("睡眠".equals(mItem.getTitle())) {
            uri = "sleeplist";
        }else if("进食".equals(mItem.getTitle())) {
            uri = "meallist";
        }else {
            uri = "";
        }
        return uri;
    }

    /**
     * 判断当前页面是否是否为全选
     * @return true: 全选; false: 没有全选
     */
    public boolean isSelect() {
        return mSelect;
    }

    @Override
    protected String getEmptyNote() {
        return "没有要审核的数据";
    }
}
