package com.lefuorgn.lefu.base;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.bean.User;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.dialog.ProgressDialog;
import com.lefuorgn.interactive.Interactive;
import com.lefuorgn.interactive.impl.AddAndModifyDataFilter;
import com.lefuorgn.interactive.interf.SyncCallback;
import com.lefuorgn.interactive.util.InteractiveUtils;
import com.lefuorgn.interf.OnScrollChangedListenerImp;
import com.lefuorgn.lefu.bean.SearchConditionGrid;
import com.lefuorgn.lefu.fragment.GridMenuRightFragment;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.NetworkUtils;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.widget.MyHScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * 网格数据展示bean类
 */
public abstract class BaseGridActivity<T extends BaseGridItem> extends BaseActivity {

    /**
     * 默认排序方式
     */
    public static final int SORT_ORDER_DEFAULT = SearchConditionGrid.ROOM;

    private DrawerLayout mDrawerLayout;

    private TextView mTitleView;

    private RecyclerView mRecyclerView;
    private TextView mErrorView;
    private ImageView mSearchBtn;
    private ImageView mSyncBtn;

    private List<DisplaySignOrNursingItem> mItems;
    private BaseGridAdapter mAdapter;
    private LinearLayout mLinearLayout;

    // 数据请求任务
    private BaseGridItemTask mBaseGridItemTask;

    // 加载数据等待页面
    private View mLoadingView;

    // 记录当前页面展示的数据是否是当天
    private boolean isToday;
    // 记录当前页面操作的数据是否有更新
    protected boolean update;

    protected User mUser;
    protected long mAgencyId;

    /**
     * 查询条件
     */
    private SearchConditionGrid mSearchConditionGrid;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_grid;
    }

    @Override
    protected final void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_activity_base_grid);
        // 关闭DrawerLayout手势滑动监听
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        // 初始化ToolBar
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 当前版本大于4.4
            RelativeLayout view = (RelativeLayout) findViewById(R.id.rl_activity_base_grid_tool_bar);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = params.height + getStatusBarHeight();
            view.setLayoutParams(params);
            view.setPadding(0, getStatusBarHeight(), 0, 0);
        }
        mTitleView = (TextView) findViewById(R.id.tv_activity_base_grid_title);
        findViewById(R.id.iv_activity_base_grid_back).setOnClickListener(this);
        mSearchBtn = (ImageView) findViewById(R.id.iv_activity_base_grid_search);
        mSearchBtn.setOnClickListener(this);
        mSyncBtn = (ImageView) findViewById(R.id.iv_activity_base_grid_sync);
        mSyncBtn.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_activity_base_grid);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));

        mErrorView = (TextView) findViewById(R.id.tv_activity_base_grid_error);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_activity_base_grid);

        mLoadingView = findViewById(R.id.ll_load_activity_fragment);
        mLoadingView.setVisibility(View.GONE);

        View footerView = getFooterView();
        if(footerView != null) {
            ((LinearLayout) findViewById(R.id.ll_activity_base_grid_content)).addView(footerView);
        }
    }

    @Override
    protected final void initData() {
        // 初始化查询条件
        initSearchConditionGrid();
        mItems = getItem();
        if(mItems == null || mItems.size() == 0) {
            // 没有配置项目
            mErrorView.setText("服务器没有配置指定项");
            mSearchBtn.setVisibility(View.GONE);
            mSyncBtn.setVisibility(View.GONE);
            return;
        }
        mUser = AppContext.getInstance().getUser();
        mAgencyId = AppContext.getInstance().getAgencyId();
        // 初始化头部控件
        initLinearLayout();
        // 开启初始化数据请求任务
        mBaseGridItemTask = (BaseGridItemTask) new BaseGridItemTask().execute(mSearchConditionGrid);
    }

    /**
     * 处理返回的结果
     * @param result 结果集合
     */
    private void setResult(List<T> result) {
        if(result == null) {
            result = new ArrayList<T>();
        }
        if(result.size() == 0) {
            // 提示信息
            mErrorView.setVisibility(View.VISIBLE);
            mErrorView.setText("没有搜索到相关内容");
        }else {
            // 隐藏提示控件
            mErrorView.setVisibility(View.GONE);
            mTitleView.setText(mSearchConditionGrid.getDate());
        }
        if(mAdapter != null) {
            // 重置当前位置
            MyHScrollView.resetLocation();
            // 添加新数据
            mAdapter.setNewData(result);
        }else {
            mAdapter = getBaseGridAdapter(result, mItems);
            mRecyclerView.setAdapter(mAdapter);
            initAdapter(mAdapter);
        }
    }

    /**
     * 初始化条目头布局
     */
    private void initLinearLayout() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                StringUtils.dip2px(this, 1), ViewGroup.LayoutParams.MATCH_PARENT);
        MyHScrollView hsv = (MyHScrollView) findViewById(R.id.hsv_activity_base_grid);
        hsv.AddOnScrollChangedListener(new OnScrollChangedListenerImp(hsv));
        for (int i = 0; i < mItems.size(); i++) {
            mLinearLayout.addView(getTitleView(mItems.get(i).getTitle()));
            if(i < mItems.size() - 1) {
                mLinearLayout.addView(getLine(), params);
            }
        }
    }

    /**
     * 初始化查询条件
     */
    private void initSearchConditionGrid() {
        isToday = true;
        mSearchConditionGrid = new SearchConditionGrid();
        mSearchConditionGrid.setDate(StringUtils.getFormatData(System.currentTimeMillis(), "yyyy-MM-dd"));
        mSearchConditionGrid.setBuildingNo("-1");
        mSearchConditionGrid.setUnitNo("-1");
        mSearchConditionGrid.setRoomNo("-1");
        mSearchConditionGrid.setSort(SORT_ORDER_DEFAULT);
    }

    /**
     * 获取分割线
     * @return 分割线对象
     */
    protected View getLine() {
        View view = new View(this);
        view.setBackgroundResource(R.color.recycler_view_item_division_color);
        return view;
    }

    /**
     * 如果要改变列的名称,可以复写这个方法
     * @param title 当前条目名称
     * @return 当前列对象控件
     */
    protected View getTitleView(String title) {
        TextView tv = new TextView(this);
        tv.setWidth(getResources().getDimensionPixelSize(R.dimen.base_grid_width));
        tv.setHeight(getResources().getDimensionPixelSize(R.dimen.base_grid_height));
        tv.setGravity(Gravity.CENTER);
        tv.setMaxLines(2);
        tv.setText(title);
        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
        return tv;
    }

    /**
     * 获取页面最下面的控件
     * @return 控件
     */
    protected View getFooterView() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 页面退出的时候要清空缓存
        MyHScrollView.clearViewCache();
        // 如果任务正在执行, 则取消
        cancelReadCacheTask();
    }

    /**
     * 本地获取数据请求任务类
     */
    private class BaseGridItemTask extends AsyncTask<SearchConditionGrid, Void, List<T>> {

        @Override
        protected void onPreExecute() {
            mLoadingView.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<T> doInBackground(SearchConditionGrid... params) {
            return readList(params[0], mItems);
        }

        @Override
        protected void onPostExecute(List<T> data) {
            setResult(data);
            mLoadingView.setVisibility(View.GONE);
        }
    }

    /**
     * 取消当前正在执行的任务
     */
    private void cancelReadCacheTask() {
        if (mBaseGridItemTask != null) {
            mBaseGridItemTask.cancel(true);
            mBaseGridItemTask = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_activity_base_grid_back :
                finish();
                break;
            case R.id.iv_activity_base_grid_search:
                // 打开搜索侧拉菜单
                mDrawerLayout.openDrawer(GravityCompat.END);
                break;
            case R.id.iv_activity_base_grid_sync:
                InteractiveUtils interactiveUtils = new InteractiveUtils(this);
                interactiveUtils.setClickCallBack(new InteractiveUtils.ClickCallBack() {
                    @Override
                    public void sync() {
                        syncData();
                    }
                });
                interactiveUtils.start();
                break;
        }
    }

    /**
     * 同步数据
     */
    private void syncData() {
        if(AppContext.getInstance().getNetState() == NetworkUtils.NETWORK_NONE) {
            showToast("网络不可用");
            return;
        }
        update = false;
        final ProgressDialog bf = new ProgressDialog();
        // 数据库首次加载数据
        Interactive interactive = new Interactive.Builder()
                .agencyId(AppContext.getInstance().getAgencyId())
                .uploadTable(true)
                .downloadTable(true)
                .elderlyRelatedTable(true)
                .runningService(false)
                .filter(new AddAndModifyDataFilter())
                .build();
        interactive.newSync().enqueue(new SyncCallback() {
            @Override
            public void onStart() {
                bf.show(getSupportFragmentManager(), "");
            }

            @Override
            public void onLoading(final long count, final long current, final String info) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bf.setProgressBar((int) current, (int) count);
                        bf.setNote(info);
                    }
                });
            }

            @Override
            public void onStop() {
                bf.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 同步完成刷新数据
                        mBaseGridItemTask = (BaseGridItemTask) new BaseGridItemTask().execute(mSearchConditionGrid);
                    }
                });
            }

            @Override
            public void syncing() {
                showToast("数据正在同步中...");
            }
        });
    }

    /**
     * 初始化adapter
     * @param mAdapter 当前适配器对象
     */
    protected void initAdapter(BaseGridAdapter mAdapter) {
    }

    /**
     * 初始化数据
     * @param condition 查询条件
     * @param items 当前列展示条目
     */
    protected abstract List<T> readList(SearchConditionGrid condition, List<DisplaySignOrNursingItem> items);

    /**
     * 此方法在数据第一次请求成功后进行调用
     * @param result 请求的结果
     * @param items 当前护理项条目
     * @return 返回指定的适配器
     */
    protected abstract BaseGridAdapter getBaseGridAdapter(List<T> result, List<DisplaySignOrNursingItem> items);

    /**
     * 获取当前要展示的条目
     * @return 条目列表
     */
    protected abstract List<DisplaySignOrNursingItem> getItem();

    /**
     * 获取当前展示数据日期是否是当天的
     * @return true: 是当天; false: 不是当天
     */
    protected boolean isToday() {
        return isToday;
    }

    /**
     * 获取查询条件bean类
     * @return SearchConditionGrid对象
     */
    public SearchConditionGrid getSearchCondition() {
        return mSearchConditionGrid;
    }

    /**
     * 设置查询条件bean类
     * @param condition 新的查询条件
     */
    public void setInputDataOptions(SearchConditionGrid condition) {
        if(mSearchConditionGrid.equals(condition)) {
            // 条件相同不进行任何操作
            return;
        }
        isToday = StringUtils.getFormatData(System.currentTimeMillis(), "yyyy-MM-dd")
                .equals(condition.getDate());
        cancelReadCacheTask();
        mSearchConditionGrid.copySearchCondition(condition);
        mBaseGridItemTask = (BaseGridItemTask) new BaseGridItemTask().execute(mSearchConditionGrid);
    }

    /**
     * 重置状态
     */
    protected void resetResult() {
        mBaseGridItemTask = (BaseGridItemTask) new BaseGridItemTask().execute(mSearchConditionGrid);
    }

    /**
     * 关闭侧拉界面
     */
    public void closeDrawerLayout() {
        mDrawerLayout.closeDrawer(GravityCompat.END);
    }

    /**
     * 获取DrawerLayout对象
     * @return DrawerLayout对象
     */
    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            // 返回键点击完成,处理相应的操作
            if(mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                GridMenuRightFragment fragment = (GridMenuRightFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.menu_activity_base_grid);
                boolean flag = fragment.closePage();
                if(!flag) {
                    closeDrawerLayout();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        if(update) {
            setResult(200);
        }
        super.finish();
    }

    @Override
    protected boolean hasStatusBar() {
        return false;
    }
}
