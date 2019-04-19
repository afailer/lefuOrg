package com.lefuorgn.base;

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
 * RecyclerView基类
 * 其中SwipeRefreshLayout的id是srl_activity_recycler
 * RecyclerView的ID是rv_activity_recycler
 * 复写initView的时候一定要调用父类的方法
 *
 */

public abstract class BaseRecyclerViewActivity<T> extends BaseActivity  implements
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    /**
     * 页面每次请求最大的条数
     */
    private static final int LOAD_SIZE = 10;

    private RecyclerView mRecyclerView;
    protected BaseAdapter mBaseAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mCurrentPage;

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
    protected final void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_activity_recycler);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_activity_recycler);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryGov);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(hasItemDecoration()) {
            mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                    getResources().getColor(R.color.recycler_view_item_division_color)));
        }
        initChildView();
    }

    @Override
    protected final void initData() {
        initChildData();
        // 初始化页面
        onRefresh();
    }

    private void initAdapter(List<T> data) {
        mBaseAdapter = new BaseAdapter(data);
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

    protected class BaseAdapter extends BaseQuickAdapter<T> {

        BaseAdapter(List<T> data) {
            super(getItemLayoutId(), data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, T t) {
            BaseRecyclerViewActivity.this.convert(baseViewHolder, t);
        }
    }

    protected final void setResult(int pageNo, List<T> result) {
        // 记录当前请求数据的条目数, 如果小于LOAD_SIZE,则数据已经请求完毕
        isLoadComplete = result.size() < LOAD_SIZE;
        if(pageNo == 1) {
            // 下拉刷新
            if(mBaseAdapter == null) {
                initAdapter(result);
                mRecyclerView.setAdapter(mBaseAdapter);
            }else {
                mBaseAdapter.setNewData(result);
            }
            mBaseAdapter.openLoadMore(LOAD_SIZE, !isLoadComplete);
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
     * 获取空内容指示信息控件
     * @return 控件
     */
    private View getEmptyView() {
        View view = getLayoutInflater().inflate(R.layout.item_recyclerview_empty,
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
            mFooterView = getLayoutInflater().inflate(R.layout.item_recyclerview_footer_end,
                    (ViewGroup) mRecyclerView.getParent(), false);
        }
        return mFooterView;
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
    protected void initChildView() {

    }

    /**
     * 初始化数据
     */
    protected void initChildData() {
    }

    protected void initListener(BaseAdapter baseAdapter) {}

    protected abstract int getItemLayoutId();

    protected abstract void loadData(int pageNo);

    protected abstract void convert(BaseViewHolder baseViewHolder, T t);

}
