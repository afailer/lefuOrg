package com.lefuorgn.lefu.fragment;

import android.os.AsyncTask;
import android.os.SystemClock;

import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseRecyclerViewFragment;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.lefu.bean.NursingItemInfo;
import com.lefuorgn.util.StringUtils;

import java.util.List;

/**
 * 今日工作护理信息详情页面
 */

public class ExecuteAllocatingTaskDetailsFragment extends BaseRecyclerViewFragment<NursingItemInfo> {

    public static final String BUNDLE_NURSING_INFO_TYPE = "bundle_nursing_info_type";
    public static final String BUNDLE_NURSING_INFO_ID = "bundle_nursing_info_id";

    private long mOldPeopleId;
    private DisplaySignOrNursingItem mItem;

    private NursingInfoDetailsTask mNursingInfoTask;

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_fragment_nursing_info_details;
    }

    @Override
    protected void initChildData() {
        mItem = (DisplaySignOrNursingItem) getArguments().getSerializable(BUNDLE_NURSING_INFO_TYPE);
        mOldPeopleId = getArguments().getLong(BUNDLE_NURSING_INFO_ID);
    }

    @Override
    protected final void loadData(int pageNo) {
        cancelReadCacheTask();
        mNursingInfoTask = (NursingInfoDetailsTask) new NursingInfoDetailsTask().execute(pageNo);
    }

    /**
     * 本地获取数据请求任务类
     */
    private class NursingInfoDetailsTask extends AsyncTask<Integer, Void, List<NursingItemInfo>> {

        private int mPageNo;

        @Override
        protected List<NursingItemInfo> doInBackground(Integer... params) {
            mPageNo = params[0];
            // 加载开始时间
            long startTime = System.currentTimeMillis();
            List<NursingItemInfo> data = OldPeopleManager.getNursingInfoDetails(mItem, mPageNo, mOldPeopleId);
            // 获取数据加载完成后的时间
            long endTime = System.currentTimeMillis();
            long time = endTime - startTime;
            if(time < 100) {
                SystemClock.sleep(100 - time);
            }
            return data;
        }

        @Override
        protected void onPostExecute(List<NursingItemInfo> data) {
            setResult(mPageNo, data);
        }
    }

    @Override
    protected void initListener(BaseRecyclerViewFragmentAdapter baseAdapter) {

    }

    @Override
    protected void convert(BaseViewHolder holder, NursingItemInfo t) {
        holder.setText(R.id.tv_item_fragment_nursing_info_details_date,
                StringUtils.getFormatData(t.getNursing_dt(), "yyyy-MM-dd HH:mm:ss"))
                .setText(R.id.tv_item_fragment_nursing_info_details_num, "待上传")
                .setText(R.id.tv_item_fragment_nursing_info_details_remarks, "备注: " + t.getReserved())
                .setText(R.id.tv_item_fragment_nursing_info_details_name, t.getCaregiver_name())
                .setVisible(R.id.iv_item_fragment_nursing_info_details_remove, t.getId() <= 0);
    }

    /**
     * 取消当前正在执行的任务
     */
    private void cancelReadCacheTask() {
        if (mNursingInfoTask != null) {
            mNursingInfoTask.cancel(true);
            mNursingInfoTask = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelReadCacheTask();
    }

}
