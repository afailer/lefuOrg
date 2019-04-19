package com.lefuorgn.lefu.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseContactFragment;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.lefu.activity.SelectOldPeopleActivity;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 老人选择页面
 */

public class SelectOldPeopleFragment extends BaseContactFragment<OldPeople> {

    private SelectOldPeopleActivity mActivity;
    private DataAsyncTask mAsyncTask;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (SelectOldPeopleActivity) context;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.fragment_select_oldpeople;
    }

    @Override
    protected void initData() {
        mAsyncTask = new DataAsyncTask();
        mAsyncTask.execute();
    }

    /**
     * Activity中的全选按钮被点击
     * @param isChecked 当前的选择状态
     */
    public void onAllBtnClick(boolean isChecked) {
        List<OldPeople> list = mAdapter.getData();
        for (OldPeople oldPeople : list) {
            oldPeople.setChecked(isChecked);
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 获取选择到的老人ID集合,通过逗号链接
     * @return 老人ID集合字符串
     */
    public String getSelectIds() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (OldPeople oldPeople : mData) {
            if(oldPeople.isChecked()) {
                if(first) {
                    sb.append(oldPeople.getId()).append(",").append(oldPeople.getElderly_name());
                    first = false;
                }else {
                    sb.append(";");
                    sb.append(oldPeople.getId()).append(",").append(oldPeople.getElderly_name());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 数据获取处理任务类
     */
    private class DataAsyncTask extends AsyncTask<Void, Void, List<OldPeople>> {
        @Override
        protected void onPreExecute() {
           showWaitDialog();
        }

        @Override
        protected List<OldPeople> doInBackground(Void... params) {
            String idStr = ((SelectOldPeopleActivity)getActivity()).getIds();
            boolean attention = ((SelectOldPeopleActivity)getActivity()).isAttention();
            List<OldPeople> list = OldPeopleManager.getOldPeople(attention);
            String[] data = idStr.split(",");
            if(data.length > 0) {
                // 获取id集合
                List<String> ids = new ArrayList<String>();
                for (String id : data) {
                    if(!StringUtils.isEmpty(id)) {
                        ids.add(id);
                    }
                }
                // 记录当前已选老人的个数
                int num = ids.size();
                // 遍历老人信息
                for (OldPeople oldPeople : list) {
                    // 先将老人选择状态置为false
                    oldPeople.setChecked(false);
                    String str = "";
                    for (String s : ids) {
                        if(s.equals(oldPeople.getId() + "")) {
                            // 当前老人存在, 记住当前的内容,并置选中状态
                            str = s;
                            oldPeople.setChecked(true);
                            break;
                        }
                    }
                    if(!StringUtils.isEmpty(str)) {
                        // 移除已被选中的内容
                        ids.remove(str);
                    }
                }
                if(list.size() > 0 && list.size() == num) {
                    // 全选
                    mActivity.onAllBtnState(true);
                }
            }else {
                for (OldPeople oldPeople : list) {
                    // 先将老人选择状态置为false
                    oldPeople.setChecked(false);
                }
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<OldPeople> oldPeoples) {
            setData(oldPeoples);
            hideWaitDialog();
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, final OldPeople t, boolean flag) {
        holder.setText(R.id.tv_item_fragment_select_elderly_name, t.getElderly_name())
                .setText(R.id.tv_item_fragment_select_elderly_letter, t.getSortLetters())
                .setText(R.id.tv_item_fragment_select_elderly_sex, t.getGender() == 14 ? "男" : "女")
                .setText(R.id.tv_item_fragment_select_elderly_age, t.getAge() + "岁")
                .setVisible(R.id.tv_item_fragment_select_elderly_letter, flag)
                .setChecked(R.id.cb_item_fragment_select_elderly, t.isChecked())
                .setOnClickListener(R.id.cb_item_fragment_select_elderly, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        boolean isChecked = cb.isChecked();
                        t.setChecked(isChecked);
                        if(isChecked) {
                            // 当前条目被选中
                            boolean flag = true;
                            List<OldPeople> list = mAdapter.getData();
                            for (OldPeople oldPeople : list) {
                                if(!oldPeople.isChecked()) {
                                    // 注意: 未被选中
                                    flag = false;
                                    mActivity.onAllBtnState(false);
                                    break;
                                }
                            }
                            if(flag) {
                                // 全选
                                mActivity.onAllBtnState(true);
                            }
                        }else {
                            // 当前条目未被选中
                            mActivity.onAllBtnState(false);
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAsyncTask != null) {
            mAsyncTask.cancel(true);
            mAsyncTask = null;
        }
    }
}
