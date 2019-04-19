package com.lefuorgn.lefu.activity;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.bean.User;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.model.download.DailyNursingDownload;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.db.util.SignConfigManager;
import com.lefuorgn.dialog.AlertDialog;
import com.lefuorgn.interf.OnItemChildColumnClickListener;
import com.lefuorgn.interf.OnScrollChangedListenerImp;
import com.lefuorgn.lefu.adapter.NursingInfoBatchEditingAdapter;
import com.lefuorgn.lefu.base.BaseGridActivity;
import com.lefuorgn.lefu.base.BaseGridItem;
import com.lefuorgn.lefu.bean.NursingInfo;
import com.lefuorgn.lefu.bean.NursingItemInfo;
import com.lefuorgn.lefu.bean.SearchConditionGrid;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.widget.MyHScrollView;

import java.util.ArrayList;
import java.util.List;


/**
 * 护理信息批量编辑页面
 */
public class NursingInfoBatchEditingActivity extends BaseActivity {

    private View mTitleAllView;

    private RecyclerView mRecyclerView;
    private TextView mErrorView;

    private LinearLayout mLinearLayout;

    private NursingInfoBatchEditingTask mTask;
    private SaveNursingInfoBatchEditingTask mSaveTask; // 保存数据任务
    private List<DisplaySignOrNursingItem> mItems;
    private NursingInfoBatchEditingAdapter mAdapter;

    private AlertDialog mDialog; // 列数据批量删除老人信息提示内容
    // 记录当前页面操作的数据是否有更新
    private boolean update;

    /**
     * 查询条件
     */
    private SearchConditionGrid mSearchConditionGrid;

    private User mUser;
    private long mAgencyId;
    private boolean firstBack = true; // 是否要继续推出

    @Override
    protected int getLayoutId() {
        return R.layout.activity_nursing_info_batch_editing;
    }

    @Override
    protected void initView() {
        mTitleAllView = setMenuTextView("日常全选");
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_activity_nursing_info_batch_editing);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));

        mErrorView = (TextView) findViewById(R.id.tv_activity_nursing_info_batch_editing_error);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_activity_nursing_info_batch_editing);
        findViewById(R.id.btn_activity_nursing_info_batch_editing_save).setOnClickListener(this);

    }

    @Override
    protected void initData() {
        // 初始化护理项信息
        mItems = SignConfigManager.getSignOrNursingItem(false);
        if(mItems == null || mItems.size() == 0) {
            // 没有配置项目
            mErrorView.setText("服务器没有配置指定项");
            mTitleAllView.setVisibility(View.INVISIBLE);
            return;
        }
        mUser = AppContext.getInstance().getUser();
        mAgencyId = AppContext.getInstance().getAgencyId();
        // 初始化查询条件
        initSearchConditionGrid();
        // 初始化头部控件
        initLinearLayout();
        // 开启初始化数据请求任务
        mTask = (NursingInfoBatchEditingTask) new NursingInfoBatchEditingTask().execute();
    }

    /**
     * 处理返回的结果
     * @param result 结果集合
     */
    private void setResult(List<NursingInfo> result) {
        if(result == null) {
            result = new ArrayList<NursingInfo>();
        }
        if(result.size() == 0) {
            // 提示信息
            mErrorView.setVisibility(View.VISIBLE);
            mTitleAllView.setVisibility(View.INVISIBLE);
            mErrorView.setText("请先关注老人");
        }else {
            // 隐藏提示控件
            mErrorView.setVisibility(View.GONE);
            mTitleAllView.setVisibility(View.VISIBLE);
        }
        if(mAdapter != null) {
            // 重置当前位置
            MyHScrollView.resetLocation();
            // 添加新数据
            mAdapter.setNewData(result);
        }else {
            mAdapter = new NursingInfoBatchEditingAdapter(this, result, mItems);
            mRecyclerView.setAdapter(mAdapter);
            initAdapter();
        }
    }

    /**
     * 初始化查询条件
     */
    private void initSearchConditionGrid() {
        mSearchConditionGrid = new SearchConditionGrid();
        mSearchConditionGrid.setDate(StringUtils.getFormatData(System.currentTimeMillis(), "yyyy-MM-dd"));
        mSearchConditionGrid.setBuildingNo("-1");
        mSearchConditionGrid.setUnitNo("-1");
        mSearchConditionGrid.setRoomNo("-1");
        mSearchConditionGrid.setSort(BaseGridActivity.SORT_ORDER_DEFAULT);
    }

    /**
     * 初始化条目头布局
     */
    private void initLinearLayout() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                StringUtils.dip2px(this, 1), ViewGroup.LayoutParams.MATCH_PARENT);
        MyHScrollView hsv = (MyHScrollView) findViewById(R.id.hsv_activity_nursing_info_batch_editing);
        hsv.AddOnScrollChangedListener(new OnScrollChangedListenerImp(hsv));
        for (int i = 0; i < mItems.size(); i++) {
            mLinearLayout.addView(getTitleView(mItems.get(i).getTitle(), i));
            if(i < mItems.size() - 1) {
                mLinearLayout.addView(getLine(), params);
            }
        }
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
    protected View getTitleView(String title, final int position) {
        LinearLayout view = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.item_item_activity_nursing_info_batch_editing, mRecyclerView, false);
        TextView tv = (TextView) view.findViewById(R.id.tv_item_item_activity_nursing_info_batch_editing_name);
        tv.setVisibility(View.VISIBLE);
        tv.setText(title);
        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
        view.findViewById(R.id.tv_item_item_activity_nursing_info_batch_editing_operation).
                setVisibility(View.VISIBLE);
        final TextView plus = (TextView) view
                .findViewById(R.id.tv_item_item_activity_nursing_info_batch_editing_plus);
        final TextView reduce = (TextView) view
                .findViewById(R.id.tv_item_item_activity_nursing_info_batch_editing_reduce);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemOnClickListener(plus, position);
            }
        });
        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemOnClickListener(reduce, position);
            }
        });
        return view;
    }

    private void initAdapter() {
        // 老人条目信息点击事件
        mAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int i) {
                NursingInfo nursingInfo = mAdapter.getItem(i);
                nursingInfo.setSelect(!nursingInfo.isSelect());
                mAdapter.notifyItemChanged(i);
            }
        });
        // 老人类型数据条目点击事件
        mAdapter.setOnItemChildColumnClickListener(new OnItemChildColumnClickListener() {
            @Override
            public void onClick(View v, BaseGridItem item, int position, int column) {
                NursingItemInfo info = mAdapter.getItem(position).getNursingItemInfoList().get(column);
                if(v.getId() == R.id.tv_item_item_activity_nursing_info_batch_editing_plus) {
                    // 增加
                    info.setCurrent_times(info.getCurrent_times() + 1);
                    mAdapter.notifyItemChanged(position);
                }else if(v.getId() == R.id.tv_item_item_activity_nursing_info_batch_editing_reduce) {
                    // 减少
                    if(info.getCurrent_times() > 0) {
                        info.setCurrent_times(info.getCurrent_times() - 1);
                        mAdapter.notifyItemChanged(position);
                        return;
                    }
                    if(info.getNurse_times() > 0) {
                        List<DailyNursingDownload> notUploaded = info.getNotUploaded();
                        if(notUploaded.size() > 0) {
                            if(notUploaded.size() + info.getCurrent_times() > 0) {
                                info.setCurrent_times(info.getCurrent_times() - 1);
                                mAdapter.notifyItemChanged(position);
                            }else if(info.getNurse_times() - notUploaded.size() > 0) {
                                showToast("已同步的数据,需要在服务器中删除");
                            }else {
                                showToast("已无数据");
                            }
                        }else {
                            showToast("已同步的数据,需要在服务器中删除");
                        }
                    }else {
                        showToast("已无数据");
                    }
                }

            }
        });
    }

    /**
     * 类型条目点击事件
     * @param v 被点击View对象
     * @param position 当前位置
     */
    private void itemOnClickListener(View v, int position) {
        List<NursingInfo> data = mAdapter.getData();
        if(v.getId() == R.id.tv_item_item_activity_nursing_info_batch_editing_plus) {
            // 增加按钮触发
            for (NursingInfo nursingInfo : data) {
                if(nursingInfo.isSelect()) {
                    NursingItemInfo info = nursingInfo.getNursingItemInfoList().get(position);
                    if(info.isSelect()) {
                        info.setCurrent_times(info.getCurrent_times() + 1);
                    }
                }
            }
        }else if(v.getId() == R.id.tv_item_item_activity_nursing_info_batch_editing_reduce) {
            StringBuilder sb = new StringBuilder(); // 存放已经上传的老人信息
            // 减少按钮触发
            for (NursingInfo nursingInfo : data) {
                if (nursingInfo.isSelect()) {
                    NursingItemInfo info = nursingInfo.getNursingItemInfoList().get(position);
                    if (info.isSelect()) {
                        if (info.getCurrent_times() > 0) {
                            // 当前操作存在未提交数据,直接减1
                            info.setCurrent_times(info.getCurrent_times() - 1);
                        } else if (info.getNurse_times() > 0) {
                            // 存在本地数据库是否有数据
                            // 获取未上传数据
                            List<DailyNursingDownload> notUploaded = info.getNotUploaded();
                            if (notUploaded.size() > 0 && notUploaded.size() + info.getCurrent_times() > 0) {
                                // 有未上传数据可以删除
                                info.setCurrent_times(info.getCurrent_times() - 1);
                            } else {
                                if (info.getNurse_times() + info.getCurrent_times() > 0) {
                                    sb.append(nursingInfo.getOldPeopleName()).append(",");
                                }
                            }
                        }
                    }
                }
            }
            // 提示数据已经上传的数据
            String message = sb.toString();
            if(!StringUtils.isEmpty(message)) {
                if(mDialog == null) {
                    mDialog = new AlertDialog()
                            .setConfirmBtnText("确定")
                            .isShowCancelBtn(false);
                }
                mDialog.setContent(message.substring(0, message.length() - 1) + "的数据需要在服务器中删除")
                        .show(getSupportFragmentManager(), "AlertDialog");
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onMenuClick(View v) {
        // 日常全选按钮触发事件
        List<NursingInfo> data = mAdapter.getData();
        int total = 0;
        for (NursingInfo nursingInfo : data) {
            if(nursingInfo.isSelect()) {
                // 当前老人被选中,可以根据配置进行数据设置
                for (NursingItemInfo info : nursingInfo.getNursingItemInfoList()) {
                    // 只有设置成每日配置数据才可以执行
                    if(info.getPeriod_type() == 1 && info.getNurse_times() + info.getCurrent_times() < info.getMax_times()) {
                        info.setCurrent_times(info.getMax_times() - info.getNurse_times());
                        total++;
                    }
                }
            }
        }
        if(total > 0) {
            mAdapter.notifyDataSetChanged();
            showToast("日常全选完成，点击\"保存\"生效！");
        }
    }

    @Override
    public void onClick(View v) {
        // 保存
        if(isExistNotSubmit()) {
            mSaveTask = (SaveNursingInfoBatchEditingTask) new SaveNursingInfoBatchEditingTask().execute(true);
        }else {
            showToast("请录入数据");
        }
    }

    /**
     * 判断当前页面是否存在未提交数据
     */
    private boolean isExistNotSubmit() {
        List<NursingInfo> data = mAdapter.getData();
        for (NursingInfo nursingInfo : data) {
            for (NursingItemInfo info : nursingInfo.getNursingItemInfoList()) {
                if(info.isSelect() && info.getCurrent_times() != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 数据保存类
     */
    private class SaveNursingInfoBatchEditingTask extends AsyncTask<Boolean, Void, Boolean> {

        private boolean refresh;

        @Override
        protected void onPreExecute() {
            showLoadingDialog("提交中");
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {
            refresh = params[0];
            return OldPeopleManager.saveBatchEditingNursingData(mUser, mAgencyId, mAdapter.getData());
        }

        @Override
        protected void onPostExecute(Boolean data) {
            // 刷新数据
            showToast("保存成功");
            hideLoadingDialog();
            // 数据有更新
            update = true;
            if(refresh) {
                // 刷新数据
                mTask = (NursingInfoBatchEditingTask) new NursingInfoBatchEditingTask().execute();
            }else {
                // 推出当前页面
                finish();
            }
        }
    }

    /**
     * 本地获取数据请求任务类
     */
    private class NursingInfoBatchEditingTask extends AsyncTask<Void, Void, List<NursingInfo>> {

        @Override
        protected void onPreExecute() {
            showWaitDialog();
        }

        @Override
        protected List<NursingInfo> doInBackground(Void... params) {
            return OldPeopleManager.getBatchEditingNursingData(mSearchConditionGrid, mItems);
        }

        @Override
        protected void onPostExecute(List<NursingInfo> data) {
            setResult(data);
            hideWaitDialog();
        }
    }

    @Override
    public void finish() {
        if(firstBack && isExistNotSubmit()) {
            firstBack = false;
            new AlertDialog()
                    .setContent("您有尚未保存的数据,是否保存")
                    .setConfirmBtnText("保存")
                    .setCancelBtnText("放弃")
            .setClickCallBack(new AlertDialog.ClickCallBack() {
                @Override
                public void cancel() {
                    finish();
                }

                @Override
                public void confirm() {
                    mSaveTask = (SaveNursingInfoBatchEditingTask) new SaveNursingInfoBatchEditingTask().execute(false);
                }
            }).show(getSupportFragmentManager(), "AlertDialog");
        }else {
            if(update) {
                setResult(200);
            }
            super.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 页面退出的时候要清空缓存
        MyHScrollView.clearViewCache();
        // 如果任务正在执行, 则取消
        if (mTask != null) {
            mTask.cancel(true);
            mTask = null;
        }
        if(mSaveTask != null) {
            mSaveTask.cancel(true);
            mSaveTask = null;
        }
    }

    @Override
    protected boolean hasToolBar() {
        return true;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }
}
