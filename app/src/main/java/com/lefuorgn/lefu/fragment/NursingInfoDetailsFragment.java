package com.lefuorgn.lefu.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseRecyclerViewFragment;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.dialog.AlertDialog;
import com.lefuorgn.lefu.activity.DisplayMultiMediaActivity;
import com.lefuorgn.lefu.activity.NursingInfoDetailsActivity;
import com.lefuorgn.lefu.bean.MultiMedia;
import com.lefuorgn.lefu.bean.NursingItemInfo;
import com.lefuorgn.lefu.dialog.NursingInfoUpdateDialog;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 护理信息详情页面
 */

public class NursingInfoDetailsFragment extends BaseRecyclerViewFragment<NursingItemInfo> {

    /**
     * 当前护理信息类型
     */
    public static final String BUNDLE_NURSING_INFO_TYPE = "bundle_nursing_info_type";
    /**
     * 当前护理老人ID
     */
    public static final String BUNDLE_NURSING_INFO_ID = "bundle_nursing_info_id";
    /**
     * 当前护理老人名称
     */
    public static final String BUNDLE_NURSING_INFO_NAME = "bundle_nursing_info_name";

    private long mOldPeopleId;
    private String mOldPeopleName;
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
        mOldPeopleName = getArguments().getString(BUNDLE_NURSING_INFO_NAME);
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
                // 警告: 不可删除...
                // 当前进程进行睡眠, 防止刷新控件无效
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
    protected void initListener(final BaseRecyclerViewFragmentAdapter adapter) {
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, final int i) {
                final NursingItemInfo info = adapter.getItem(i);
                if(info.getId() <= 0) {
                    // 本地数据, 进行修改
                    new NursingInfoUpdateDialog()
                            .setTitle(info.getName() + " " + mOldPeopleName)
                            .setMultiMedias(new ArrayList<MultiMedia>(info.getMedia()))
                            .setRemarks(info.getReserved())
                            .setClickCallBack(new NursingInfoUpdateDialog.ClickCallBack() {

                                @Override
                                public void saveClick(String remarks, List<MultiMedia> multiMedia) {
                                    boolean flag = OldPeopleManager.updateNursingItemInfo(info.get_id(), remarks, multiMedia);
                                    if(flag) {
                                        showToast("修改成功");
                                        info.setReserved(remarks);
                                        info.setMedia(multiMedia);
                                        adapter.notifyItemChanged(i);
                                    }else {
                                        showToast("修改失败");
                                    }

                                }
                            }).show(getFragmentManager(), "NursingInfoUpdateDialog");
                }else {
                    // 查看多媒体信息
                    if(info.getMedia().size() == 0) {
                        showToast("未添加媒体信息");
                    }else {
                        Intent intent = new Intent(getActivity(), DisplayMultiMediaActivity.class);
                        intent.putExtra("nursingItemInfo", info);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder holder, final NursingItemInfo t) {
        // true 未上传数据, 本地数据; false 服务器数据
        boolean localData = (t.getId() <= 0);
        holder.setText(R.id.tv_item_fragment_nursing_info_details_date,
                        StringUtils.getFormatData(t.getNursing_dt(), "yyyy-MM-dd HH:mm:ss"))
                .setText(R.id.tv_item_fragment_nursing_info_details_num,
                        localData ? "待上传" : t.getMedia().size() + "个文件")
                .setText(R.id.tv_item_fragment_nursing_info_details_remarks,
                        getReserved(t.getReserved()))
                .setText(R.id.tv_item_fragment_nursing_info_details_name, t.getCaregiver_name())
                .setVisible(R.id.iv_item_fragment_nursing_info_details_remove, localData)
                .setOnClickListener(R.id.iv_item_fragment_nursing_info_details_remove,
                        !localData ? null : new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showAlertDialog(t);
                            }
                        });
    }

    /**
     * 删除信息提示
     */
    private void showAlertDialog(final NursingItemInfo info) {
        new AlertDialog()
                .setCancelBtnText("删除")
                .setConfirmBtnText("取消")
                .setContent("删除后将不可恢复,需要重新添加!")
                .isCancelOutside(false)
                .setClickCallBack(new AlertDialog.ClickCallBack() {
                    @Override
                    public void cancel() {
                        // 删除
                        boolean flag = OldPeopleManager.deleteNursingItemInfo(info);
                        if(flag) {
                            showToast("删除成功...");
                            ((NursingInfoDetailsActivity) getActivity()).setModify(true);
                            mBaseAdapter.getData().remove(info);
                            mBaseAdapter.notifyDataSetChanged();
                        }else {
                            showToast("删除失败...");
                        }
                    }

                    @Override
                    public void confirm() {
                        // 取消
                    }
                }).show(getFragmentManager(), "AlertDialog");
    }

    /**
     * 处理备注信息
     */
    private String getReserved(String reserved) {
        if(StringUtils.isEmpty(reserved)) {
            return "备注: 无";
        }else {
            return "备注: " + reserved;
        }
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
