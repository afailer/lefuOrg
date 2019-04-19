package com.lefuorgn.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.util.DividerItemDecoration;

import java.util.List;

/**
 * 具有下拉刷新和上拉加载的方法的RecyclerView
 * 当复写loadData()方法加载数据成功后必须调用
 * setResult(),将你的结果添加进来
 *
 */

public abstract class BaseRecyclerViewFragment<T> extends BaseFragment implements
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{

    /**
     * 页面每次请求最大的条数
     */
    private static final int LOAD_SIZE = 10;

    private RecyclerView mRecyclerView;
    protected BaseRecyclerViewFragmentAdapter mBaseAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mCurrentPage;
    private int mLoadSize;
    private View mFooterView;

    /**
     * 记录当前数据是否加载完成
     */
    private boolean isLoadComplete;

    @Override
    protected int getLayoutId() {
        return R.layout.base_swipe_recycler_view;
    }

    @Override
    protected final void initView(View view, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_activity_recycler);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_activity_recycler);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryGov);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(hasItemDecoration()) {
            mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                    getResources().getColor(R.color.recycler_view_item_division_color)));
        }
        initChildView(view);
    }

    @Override
    protected final void initData() {
        mLoadSize = getLoadSize();
    }

    @Override
    protected final void lazyFetchData() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        initChildData();
        // 初始化页面
        onRefresh();
    }

    private void initAdapter(List<T> data) {
        mBaseAdapter = new BaseRecyclerViewFragmentAdapter(data);
        mBaseAdapter.openLoadAnimation();
        mBaseAdapter.setOnLoadMoreListener(this);
        mBaseAdapter.setEmptyView(getEmptyView());
        initListener(mBaseAdapter);
    }

    @Override
    public void onLoadMoreRequested() {
        if(isLoadComplete) {
            return;
        }
        mCurrentPage++;
        loadData(mCurrentPage);
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        loadData(mCurrentPage);
    }

    protected class BaseRecyclerViewFragmentAdapter extends BaseQuickAdapter<T> {

        BaseRecyclerViewFragmentAdapter(List<T> data) {
            super(getItemLayoutId(), data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, T t) {
            BaseRecyclerViewFragment.this.convert(baseViewHolder, t);
        }
    }

    protected final void setResult(int pageNo, List<T> result) {
        // 记录当前请求数据的条目数, 如果小于mLoadSize,则数据已经请求完毕
        isLoadComplete = result.size() < mLoadSize;
        if(pageNo == 1) {
            // 下拉刷新
            if(mBaseAdapter == null) {
                initAdapter(result);
                mRecyclerView.setAdapter(mBaseAdapter);
            }else {
                mBaseAdapter.setNewData(result);
            }
            mBaseAdapter.openLoadMore(mLoadSize, !isLoadComplete);
            mSwipeRefreshLayout.setRefreshing(false);
        }else {
            // 上拉加载
            mBaseAdapter.notifyDataChangedAfterLoadMore(result, !isLoadComplete);
        }
        if(isLoadComplete) {
            // 没有数据了
            mBaseAdapter.addFooterView(getFooterView());
        }
    }

    /**
     * 重置状态
     */
    protected void resetResult() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        onRefresh();
    }

    /**
     * 获取空内容指示信息控件
     * @return 控件
     */
    private View getEmptyView() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.item_recyclerview_empty,
                (ViewGroup) mRecyclerView.getParent(), false);
        ((TextView) view.findViewById(R.id.item_recycler_view_item)).setText(getEmptyNote());
        return view;
    }

    /**
     * 获取没有更多指示信息控件
     * @return 控件
     */
    private View getFooterView() {
        if(mFooterView == null) {
            mFooterView = getActivity().getLayoutInflater().inflate(
                    R.layout.item_recyclerview_footer_end,
                    (ViewGroup) mRecyclerView.getParent(), false);
        }
        return mFooterView;
    }

    @Override
    protected boolean hasWaitDialog() {
        return false;
    }

    /**
     * 是否拥有分割线
     * @return 默认拥有分割线
     */
    protected boolean hasItemDecoration() {
        return true;
    }

    /**
     * 当前数据为空的时候提示内容
     */
    protected String getEmptyNote() {
        return "这里还没有内容哦~";
    }

    /**
     * 初始化控件
     */
    protected void initChildView(View view) {}

    /**
     * 初始化数据
     */
    protected void initChildData() {}

    /**
     * 获取每页加载数据条数
     */
    protected int getLoadSize() {
        return LOAD_SIZE;
    }

    protected void initListener(BaseRecyclerViewFragmentAdapter adapter) {}

    protected abstract int getItemLayoutId();

    protected abstract void loadData(int pageNo);

    protected abstract void convert(BaseViewHolder holder, T t);

}
