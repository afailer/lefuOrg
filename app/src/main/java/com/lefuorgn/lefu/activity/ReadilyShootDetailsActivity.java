package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseRecyclerViewActivity;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.dialog.AlertDialog;
import com.lefuorgn.lefu.bean.MultiMedia;
import com.lefuorgn.lefu.bean.NursingItemInfo;
import com.lefuorgn.lefu.dialog.NursingInfoUpdateDialog;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 护理信息详情页
 */

public class ReadilyShootDetailsActivity extends BaseRecyclerViewActivity<NursingItemInfo> {

    private long mOldPeopleId;
    private String mOldPeopleName;

    private ReadilyShootDetailsTask mReadilyShootTask;

    private boolean modify; // 有数据删除了

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_fragment_nursing_info_details;
    }

    @Override
    protected void initChildData() {
        Intent intent = getIntent();
        mOldPeopleId = intent.getLongExtra("id", 0);
        mOldPeopleName = intent.getStringExtra("name");
        setToolBarTitle(mOldPeopleName + " 随手拍");
    }

    @Override
    protected void loadData(int pageNo) {
        cancelReadCacheTask();
        mReadilyShootTask = (ReadilyShootDetailsTask) new ReadilyShootDetailsTask().execute(pageNo);
    }

    /**
     * 本地获数据请求任务类
     */
    private class ReadilyShootDetailsTask extends AsyncTask<Integer, Void, List<NursingItemInfo>> {

        private int mPageNo;

        @Override
        protected List<NursingItemInfo> doInBackground(Integer... params) {
            mPageNo = params[0];
            // 加载开始时间
            long startTime = System.currentTimeMillis();
            List<NursingItemInfo> data = OldPeopleManager.getReadilyShootDetails(mPageNo, mOldPeopleId);
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
    protected void initListener(final BaseAdapter adapter) {
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, final int i) {
                final NursingItemInfo info = adapter.getItem(i);
                if(info.getId() <= 0) {
                    // 本地数据, 进行修改
                    new NursingInfoUpdateDialog()
                            .setTitle(mOldPeopleName)
                            .setMultiMedias(new ArrayList<MultiMedia>(info.getMedia()))
                            .setRemarks(info.getReserved())
                            .setClickCallBack(new NursingInfoUpdateDialog.ClickCallBack() {

                                @Override
                                public void saveClick(String remarks, List<MultiMedia> multiMedia) {
                                    boolean flag = OldPeopleManager.updateReadilyShootItemInfo(info.get_id(), remarks, multiMedia);
                                    if(flag) {
                                        showToast("修改成功");
                                        info.setReserved(remarks);
                                        info.setMedia(multiMedia);
                                        adapter.notifyItemChanged(i);
                                    }else {
                                        showToast("修改失败");
                                    }
                                }
                            }).show(getSupportFragmentManager(), "NursingInfoUpdateDialog");
                }else {
                    // 查看多媒体信息
                    if(info.getMedia().size() == 0) {
                        showToast("未添加媒体信息");
                    }else {
                        Intent intent = new Intent(ReadilyShootDetailsActivity.this, DisplayMultiMediaActivity.class);
                        intent.putExtra("nursingItemInfo", info);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder holder, final NursingItemInfo info) {
        // true 未上传数据, 本地数据; false 服务器数据
        boolean localData = (info.getId() <= 0);
        holder.setText(R.id.tv_item_fragment_nursing_info_details_date,
                StringUtils.getFormatData(info.getNursing_dt(), "yyyy-MM-dd HH:mm:ss"))
                .setText(R.id.tv_item_fragment_nursing_info_details_num,
                        localData ? "待上传" : info.getMedia().size() + "个文件")
                .setText(R.id.tv_item_fragment_nursing_info_details_remarks,
                        getReserved(info.getReserved()))
                .setText(R.id.tv_item_fragment_nursing_info_details_name, info.getCaregiver_name())
                .setVisible(R.id.iv_item_fragment_nursing_info_details_remove, localData)
                .setOnClickListener(R.id.iv_item_fragment_nursing_info_details_remove,
                        !localData ? null : new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAlertDialog(info);
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
                boolean flag = OldPeopleManager.deleteReadilyShootItemInfo(info);
                if(flag) {
                    showToast("删除成功...");
                    modify = true;
                    mBaseAdapter.getData().remove(info);
                    mBaseAdapter.notifyDataSetChanged();
                }else {
                    showToast("删除失败...");
                }
            }

            @Override
            public void confirm() {
            }
        }).show(getSupportFragmentManager(), "AlertDialog");
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
        if (mReadilyShootTask != null) {
            mReadilyShootTask.cancel(true);
            mReadilyShootTask = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelReadCacheTask();
    }

    @Override
    public void finish() {
        if(modify) {
            setResult(200);
        }
        super.finish();
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
