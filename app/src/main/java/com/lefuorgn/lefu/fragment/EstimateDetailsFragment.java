package com.lefuorgn.lefu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseFragment;
import com.lefuorgn.lefu.activity.EstimateUpdateActivity;
import com.lefuorgn.lefu.adapter.EstimateDetailsAdapter;
import com.lefuorgn.lefu.bean.Estimate;
import com.lefuorgn.util.DividerItemDecoration;

import java.util.List;

/**
 * 评估表详情条目展示列表
 */

public class EstimateDetailsFragment extends BaseFragment {

    public static final String BUNDLE_ESTIMATE_DETAILS_ID = "bundle_estimate_details_id";
    public static final String BUNDLE_ESTIMATE_DETAILS_OLDPEOPLE_ID = "bundle_estimate_details_oldPeople_id";
    public static final String BUNDLE_ESTIMATE_DETAILS_AGENCY_ID = "bundle_estimate_details_agency_id";

    private RecyclerView mRecyclerView;
    private EstimateDetailsAdapter mAdapter;

    private long mId;
    private long mOldPeopleId;
    private long mAgencyId;

    @Override
    protected int getLayoutId() {
        return R.layout.base_recycler_view;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_base_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mId = bundle.getLong(BUNDLE_ESTIMATE_DETAILS_ID);
        mOldPeopleId = bundle.getLong(BUNDLE_ESTIMATE_DETAILS_OLDPEOPLE_ID);
        mAgencyId = bundle.getLong(BUNDLE_ESTIMATE_DETAILS_AGENCY_ID);
    }

    @Override
    protected void lazyFetchData() {
        showWaitDialog();
        LefuApi.getEstimateList(mAgencyId, mId, mOldPeopleId, new RequestCallback<List<Estimate>>() {
            @Override
            public void onSuccess(List<Estimate> result) {
                if(mAdapter == null) {
                    mAdapter = new EstimateDetailsAdapter(result);
                    View view = LayoutInflater.from(getActivity())
                            .inflate(R.layout.item_recyclerview_empty, mRecyclerView, false);
                    mAdapter.setEmptyView(view);
                    mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, int i) {
                            Intent intent = new Intent(getActivity(), EstimateUpdateActivity.class);
                            intent.putExtra("estimate", (Estimate) mAdapter.getData().get(i));
                            startActivityForResult(intent, 100);
                            getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                        }
                    });
                    mRecyclerView.setAdapter(mAdapter);
                }else {
                    mAdapter.setNewData(result);
                }
                hideWaitDialog();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                hideWaitDialog();
                showToast(e.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200) {
            lazyFetchData();
        }
    }
}
