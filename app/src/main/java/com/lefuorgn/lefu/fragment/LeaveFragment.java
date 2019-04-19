package com.lefuorgn.lefu.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseRecyclerViewFragment;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.lefu.activity.LeaveActivity;
import com.lefuorgn.lefu.activity.LeaveDetailsActivity;
import com.lefuorgn.lefu.activity.LeaveOperationActivity;
import com.lefuorgn.lefu.adapter.PersonalDataSearchAdapter;
import com.lefuorgn.lefu.bean.Leave;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * 老人外出信息展示页面
 */

public class LeaveFragment extends BaseRecyclerViewFragment<Leave> {

    public static final String BUNDLE_LEAVE_TYPE = "bundle_leave_type";

    private int mLeaveState;

    private View mContentView; // 搜索内容视图

    private ClearEditText mSearchView;
    private List<OldPeople> mOldPeoples; // 所有老人列表

    /**
     * 当前页面被选择的老人
     */
    private OldPeople mOldPeople;

    /**
     * 搜索内容展示适配器
     */
    private PersonalDataSearchAdapter mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_leave;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_fragment_leave;
    }

    @Override
    protected void initChildView(View view) {
        mSearchView = (ClearEditText) view.findViewById(R.id.cet_fragment_leave);
        initContentPopWindow(view);
        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                if("".equals(s.toString())) {
                    if(mContentView.getVisibility() == View.VISIBLE) {
                        mContentView.setVisibility(View.GONE);
                    }
                    mOldPeople = null;
                    return;
                }
                if(mContentView.getVisibility() == View.GONE) {
                    mContentView.setVisibility(View.VISIBLE);
                }
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void initChildData() {
        mLeaveState = getArguments().getInt(BUNDLE_LEAVE_TYPE, 0);
        mOldPeoples = OldPeopleManager.getOldPeople(false);
    }

    /**
     * 初始化老人选择PopWindow控件
     */
    private void initContentPopWindow(View view) {
        mContentView = view.findViewById(R.id.ll_popup_window_fragment_leave);
        view.findViewById(R.id.v_popup_window_fragment_leave)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mAdapter.getData().size()==1){
                            if(((OldPeople)mAdapter.getData().get(0)).getElderly_name().equals(mSearchView.getText().toString().trim())){
                                mOldPeople = (OldPeople) mAdapter.getData().get(0);
                                resetResult();
                            }else{
                                mOldPeople=null;
                            }
                        }else{
                            mOldPeople=null;
                        }
                        mContentView.setVisibility(View.GONE);
                    }
                });
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv_popup_window_fragment_leave);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
        mAdapter = new PersonalDataSearchAdapter();
        rv.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                mOldPeople = (OldPeople) mAdapter.getData().get(i);
                mSearchView.setText(mOldPeople.getElderly_name());
                mSearchView.setSelection(mSearchView.getText().length());
                mContentView.setVisibility(View.GONE);
                hideKeyBoard(view);
                resetResult();
            }
        });
    }

    @Override
    protected void loadData(final int pageNo) {
        String name = "";
        boolean flag = false;
        if(mOldPeople != null) {
            name = mOldPeople.getElderly_name();
            flag = true;
        }
        LefuApi.getLeave(mLeaveState, pageNo, name, flag, new RequestCallback<List<Leave>>() {
            @Override
            public void onSuccess(List<Leave> result) {
                setResult(pageNo, result);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                setResult(pageNo, new ArrayList<Leave>());
                showToast(e.getMessage());
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder holder, Leave leave) {
        long backTime;
        if(leave.getLeave_state() == Leave.LEAVE_STATE_COMPLETE) {
            // 已完成状态
            backTime = leave.getReal_return_dt();
        }else {
            backTime = leave.getExpected_return_dt();
        }
        holder.setText(R.id.tv_item_fragment_leave_name, leave.getElderly_name())
                .setText(R.id.tv_item_fragment_leave_date
                        , StringUtils.getFormatData(leave.getLeave_hospital_dt(), StringUtils.FORMAT)
                        + " -- "
                        + (backTime == 0 ? "" : StringUtils.getFormatData(backTime, StringUtils.FORMAT)));
    }

    @Override
    protected void initListener(final BaseRecyclerViewFragmentAdapter adapter) {
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent;
                if(mLeaveState == 1) {
                    intent = new Intent(getActivity(), LeaveOperationActivity.class);

                }else {
                    intent = new Intent(getActivity(), LeaveDetailsActivity.class);
                }
                intent.putExtra("leave", adapter.getItem(i));
                startActivityForResult(intent, 100);
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 500) {
            // 修改成功
            resetResult();
        }else if(resultCode == 400) {
            // 销假
            resetResult();
            // 通知完成页面
            ((LeaveActivity)getActivity()).overFragmentRefresh();
        }
    }

    /**
     * 刷新数据
     */
    public void updateLeave() {
        resetResult();
    }

    /**
     * 通过字段过滤老人列表
     * @param filterStr 指定的字符串
     */
    private void filterData(String filterStr) {
        List<OldPeople> filterDateList = new ArrayList<OldPeople>();
        filterDateList.clear();
        filterStr = filterStr.toLowerCase();
        for (OldPeople o : mOldPeoples) {
            if (o.getCharacters().startsWith(filterStr) ||
                    o.getFullPinYin().startsWith(filterStr) ||
                    o.getInitial().startsWith(filterStr)) {
                filterDateList.add(o);
            }
        }
        mAdapter.setNewData(filterDateList);
    }

    /**
     * 隐藏软件盘
     * @param v 视图
     */
    private void hideKeyBoard(View v) {
        // 1.得到InputMethodManager对象
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // 2.调用hideSoftInputFromWindow方法隐藏软键盘
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0); // 强制隐藏键盘
    }

}
