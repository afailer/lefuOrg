package com.lefuorgn.lefu.fragment;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseRecyclerViewFragment;
import com.lefuorgn.bean.User;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.db.util.PermissionManager;
import com.lefuorgn.db.util.SignConfigManager;
import com.lefuorgn.lefu.activity.SignInfoDetailsActivity;
import com.lefuorgn.lefu.bean.SignItemInfo;
import com.lefuorgn.lefu.dialog.SignInfoDialog;
import com.lefuorgn.lefu.dialog.SignInfoSpecialDialog;
import com.lefuorgn.util.StringUtils;

import java.util.List;

/**
 * 体征信息详情页面
 */

public class SignInfoDetailsFragment extends BaseRecyclerViewFragment<SignItemInfo> {

    public static final String BUNDLE_SIGN_INFO_TYPE = "bundle_sign_info_type";
    public static final String BUNDLE_SIGN_INFO_ID = "bundle_sign_info_id";

    private long mOldPeopleId;
    private DisplaySignOrNursingItem mItem;
    private User mUser;
    private boolean updatePermission; // 修改权限

    private SignInfoDetailsTask mSignInfoTask;

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_fragment_sign_info_details;
    }

    @Override
    protected void initChildData() {
        mItem = (DisplaySignOrNursingItem) getArguments().getSerializable(BUNDLE_SIGN_INFO_TYPE);
        mOldPeopleId = getArguments().getLong(BUNDLE_SIGN_INFO_ID);
        mUser = AppContext.getInstance().getUser();
        updatePermission = PermissionManager.hasSignUpdatePermission(mItem.getTitle());
    }

    @Override
    protected final void loadData(int pageNo) {
        cancelReadCacheTask();
        mSignInfoTask = (SignInfoDetailsTask) new SignInfoDetailsTask().execute(pageNo);
    }

    /**
     * 本地获取数据请求任务类
     */
    private class SignInfoDetailsTask extends AsyncTask<Integer, Void, List<SignItemInfo>> {

        private int mPageNo;

        @Override
        protected List<SignItemInfo> doInBackground(Integer... params) {
            mPageNo = params[0];
            // 加载开始时间
            long startTime = System.currentTimeMillis();
            List<SignItemInfo> data = OldPeopleManager.getSignInfoDetails(mItem, mPageNo, mOldPeopleId);
            // 获取数据加载完成后的时间
            long endTime = System.currentTimeMillis();
            long time = endTime - startTime;
            if(time < 100) {
                SystemClock.sleep(100 - time);
            }
            return data;
        }

        @Override
        protected void onPostExecute(List<SignItemInfo> data) {
            setResult(mPageNo, data);
        }
    }

    @Override
    protected void initListener(BaseRecyclerViewFragmentAdapter baseAdapter) {

    }

    @Override
    protected void convert(final BaseViewHolder holder, final SignItemInfo t) {

        holder.setText(R.id.tv_item_fragment_sign_info_details_name, t.getInspect_user_name())
                .setText(R.id.tv_item_fragment_sign_info_details_date,
                        StringUtils.getFormatData(t.getTime(), "yyyy-MM-dd"))
                .setText(R.id.tv_item_fragment_sign_info_details_time,
                        StringUtils.getFormatData(t.getTime(), "HH:mm:ss"))
                .setText(R.id.tv_item_fragment_sign_info_details_value, t.getContent())
                .setTextColor(R.id.tv_item_fragment_sign_info_details_value, t.getColor());
        ImageView update = holder.getView(R.id.iv_item_fragment_sign_info_details);
        // 修改按钮处理
        if(updatePermission) {
            update.setVisibility(View.VISIBLE);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showUpdateDialog(mBaseAdapter, t
                            , holder.getLayoutPosition() - mBaseAdapter.getHeaderViewsCount());
                }
            });
        }else {
            update.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 取消当前正在执行的任务
     */
    private void cancelReadCacheTask() {
        if (mSignInfoTask != null) {
            mSignInfoTask.cancel(true);
            mSignInfoTask = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelReadCacheTask();
    }

    /**
     * 显示数据修改dialog
     * @param adapter 当前数据适配器
     * @param info 当前数据信息
     * @param position 当前数据条目位置
     */
    private void showUpdateDialog(final BaseQuickAdapter<SignItemInfo> adapter,
                                  final SignItemInfo info, final int position) {
        final String type = mItem.getTitle();

        if("进食".equals(type) || "睡眠".equals(type)) {
            new SignInfoSpecialDialog().setTitle(type)
                    .setContent(info.getContent())
                    .setReserved(info.getReserved())
                    .setClickCallBack(new SignInfoSpecialDialog.ClickCallBack() {
                        @Override
                        public void saveClick(String value, int color) {
                            if("进食".equals(type)) {
                                String[] str = value.split(";");
                                String[] types = str[0].split(",");
                                String content = types[types.length - 1];
                                content = content + str[1];
                                info.setContent(content);
                            }else {
                                info.setContent(value);
                            }
                            info.setReserved(value);
                            info.setColor(color);
                            info.setInspect_user_id(mUser.getUser_id());
                            info.setInspect_user_name(mUser.getUser_name());
                            info.setEntry_user_id(mUser.getUser_id());
                            info.setEntry_user_name(mUser.getUser_name());
                            info.setApprovalStatus(AppContext.getApprovalStatus());
                            boolean success = OldPeopleManager.updateSignItemInfo(info, type);
                            if(!success) {
                                showToast("保存失败");
                            }else {
                                ((SignInfoDetailsActivity) getActivity()).setModify(true);
                                adapter.notifyItemChanged(position);
                            }
                        }
                    }).show(getFragmentManager(), "");
        }else {
            new SignInfoDialog().setTitle(type)
                    .setSignConfig(SignConfigManager.getSignConfig(type))
                    .setShowDevice(false)
                    .setContent(info.getContent())
                    .setClickCallBack(new SignInfoDialog.ClickCallBack() {
                        @Override
                        public void deviceClick() {

                        }

                        @Override
                        public void saveClick(String value, int color) {
                            info.setContent(value);
                            info.setColor(color);
                            info.setInspect_user_id(mUser.getUser_id());
                            info.setInspect_user_name(mUser.getUser_name());
                            info.setEntry_user_id(mUser.getUser_id());
                            info.setEntry_user_name(mUser.getUser_name());
                            info.setApprovalStatus(AppContext.getApprovalStatus());
                            // 保存数据到数据库中
                            boolean success = OldPeopleManager.updateSignItemInfo(info, type);
                            if(!success) {
                                showToast("保存失败");
                            }else {
                                ((SignInfoDetailsActivity) getActivity()).setModify(true);
                                adapter.notifyItemChanged(position);
                            }
                        }
                    }).show(getFragmentManager(), "");
        }
    }

}
