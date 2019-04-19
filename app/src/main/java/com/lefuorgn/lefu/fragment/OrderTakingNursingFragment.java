package com.lefuorgn.lefu.fragment;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseRecyclerViewFragment;
import com.lefuorgn.db.util.AllocatingTypeTaskManager;
import com.lefuorgn.lefu.adapter.OrderTakingNursingAdapter;
import com.lefuorgn.lefu.bean.AllocatingTaskExecute;
import com.lefuorgn.lefu.util.OrderTakingNursingUtils;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 护理服务,接单和退单布局
 */

public class OrderTakingNursingFragment extends BaseRecyclerViewFragment<AllocatingTaskExecute> {

    public static final String BUNDLE_ORDER_TASK_TYPE = "bundle_order_task_type";

    private long mUserId;
    private long mAgencyId;
    private int mType;
    // 数据请求任务
    private AllocatingTaskExecuteTask mAllocatingTaskExecuteTask;

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_fragment_order_taking_nursing;
    }

    @Override
    protected void initChildData() {
        mUserId = AppContext.getInstance().getUser().getUser_id();
        mAgencyId = AppContext.getInstance().getAgencyId();
        mType = getArguments().getInt(BUNDLE_ORDER_TASK_TYPE, 0);
    }

    @Override
    protected void loadData(final int pageNo) {
        if(mType == 1) {
            // 接单
            LefuApi.getOrderTakingNursing(mUserId, System.currentTimeMillis(), mType, pageNo,
                    new RequestCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            List<AllocatingTaskExecute> list = OrderTakingNursingUtils.getOrderTakingNursing(result);
                            setResult(pageNo, list);
                        }

                        @Override
                        public void onFailure(ApiHttpException e) {
                            setResult(pageNo, new ArrayList<AllocatingTaskExecute>());
                            showToast(e.getMessage());
                        }
                    });
        }else {
            // 退还配单
            mAllocatingTaskExecuteTask = new AllocatingTaskExecuteTask();
            mAllocatingTaskExecuteTask.execute(pageNo);
        }

    }

    @Override
    protected void convert(BaseViewHolder holder, final AllocatingTaskExecute o) {
        holder.setText(R.id.tv_item_fragment_order_taking_nursing_name, o.getOld_people_name())
                .setText(R.id.tv_item_fragment_order_taking_nursing_date,
                        StringUtils.getFormatData(o.getTask_time(), "yyyy-MM-dd"))
                .setOnCheckedChangeListener(R.id.cb_item_fragment_order_taking_nursing, null)
                .setChecked(R.id.cb_item_fragment_order_taking_nursing, o.isChecked())
                .setOnCheckedChangeListener(R.id.cb_item_fragment_order_taking_nursing,
                        new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                o.setChecked(isChecked);
            }
        });
        RecyclerView recyclerView = holder.getView(R.id.rv_item_fragment_order_taking_nursing);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(new OrderTakingNursingAdapter(o.getOptions()));
    }

    /**
     * 本地获取数据请求任务类
     */
    private class AllocatingTaskExecuteTask extends AsyncTask<Integer, Void, List<AllocatingTaskExecute>> {

        private int pageNo;

        @Override
        protected List<AllocatingTaskExecute> doInBackground(Integer... params) {
            pageNo = params[0];
            // 加载开始时间
            long startTime = System.currentTimeMillis();
            List<AllocatingTaskExecute> data = AllocatingTypeTaskManager
                    .getAllocatingTaskExecute(mAgencyId, pageNo, false);
            // 获取数据加载完成后的时间
            long endTime = System.currentTimeMillis();
            long time = endTime - startTime;
            if(time < 100) {
                SystemClock.sleep(100 - time);
            }
            return data;
        }

        @Override
        protected void onPostExecute(List<AllocatingTaskExecute> data) {
            setResult(pageNo, data);
        }
    }

    /**
     * 获取此方法要保存数据的集合
     */
    public List<AllocatingTaskExecute> getOrderTakingNursing() {
        return mBaseAdapter.getData();
    }

    /**
     * 取消当前正在执行的任务
     */
    private void cancelReadCacheTask() {
        if (mAllocatingTaskExecuteTask != null) {
            mAllocatingTaskExecuteTask.cancel(true);
            mAllocatingTaskExecuteTask = null;
        }
    }

    /**
     * 数据进行刷新
     */
    public void updateData() {
        resetResult();
    }

    @Override
    protected int getLoadSize() {
        return 9;
    }

    @Override
    protected String getEmptyNote() {
        if(mType == 1) {
            return "没有您的配单";
        }else {
            return "没有退还配单";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelReadCacheTask();
    }

    @Override
    protected boolean hasItemDecoration() {
        return false;
    }
}
