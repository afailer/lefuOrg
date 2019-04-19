package com.lefuorgn.oa.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.oa.bean.ApprovalProcess;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.widget.CircleImageView;

import java.util.List;

/**
 * 审批或者申请进度查询数据适配器
 */

public class ApplicationProgressAdapter extends BaseQuickAdapter<ApprovalProcess> {

    public ApplicationProgressAdapter(List<ApprovalProcess> data) {
        super(R.layout.item_activity_application_progress, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ApprovalProcess process) {
        int position = holder.getLayoutPosition() - this.getHeaderViewsCount();
        if(position == 0) {
            // 第一条目显示审批信息
            holder.setVisible(R.id.iv_item_activity_application_progress, false)
                    .setVisible(R.id.tv_item_activity_application_progress_name, false)
                    .setText(R.id.tv_item_activity_application_progress_status, process.getVerify_user_name());
        }else {
            holder.setVisible(R.id.iv_item_activity_application_progress, true)
                    .setVisible(R.id.tv_item_activity_application_progress_name, true)
                    .setText(R.id.tv_item_activity_application_progress_name, process.getVerify_user_name())
                    .setText(R.id.tv_item_activity_application_progress_status, getStatus(process.getStatus()));
            // 设置照片内容
            CircleImageView image = holder.getView(R.id.iv_item_activity_application_progress);
            image.setBorderColor(Color.parseColor("#56D5B3"));
            image.setBorderWidth(StringUtils.dip2px(mContext, 2f));
            ImageLoader.loadImgForUserDefinedView(process.getVerify_user_icon(), image);
        }
        long time = process.getUpdate_time() == 0 ? process.getCreate_time() : process.getUpdate_time();
        holder.setText(R.id.tv_item_activity_application_progress_date,
                StringUtils.getFormatData(time, "yyyy-MM-dd HH:mm"))
                .setText(R.id.tv_item_activity_application_progress_reason, process.getRemark())
                .setBackgroundColor(R.id.v_item_activity_application_progress, getStatusPointColor(process.getStatus()))
                .setBackgroundColor(R.id.tv_item_activity_application_progress_status, getStatusBackgroundColor(process.getStatus()));
    }

    /**
     * 获取当前审批人的审批状态
     */
    private String getStatus(int status) {
        String result;
        switch (status) {
            case 1:
                result = "待审核";
                break;
            case 2:
                result = "已驳回";
                break;
            case 3:
                result = "已同意";
                break;
            case 4:
                result = "已退回";
                break;
            default:
                result = "";
        }
        return result;
    }

    /**
     * 获取审批人状态的背景颜色
     */
    private int getStatusBackgroundColor(int status) {
        String result;
        switch (status) {
            case 1:
                result = "#D0EEFF";
                break;
            case 2:
                result = "#FC8794";
                break;
            case 3:
                result = "#FFE7CD";
                break;
            case 4:
                result = "#EB3F2F";
                break;
            default:
                result = "#DFF9D7";
        }
        return Color.parseColor(result);
    }

    /**
     * 获取审批人状态的指示颜色
     */
    private int getStatusPointColor(int status) {
        String result;
        switch (status) {
            case 1:
                result = "#53C2F7";
                break;
            case 2:
                result = "#FC8794";
                break;
            case 3:
                result = "#FFC179";
                break;
            case 4:
                result = "#B31800";
                break;
            default:
                result = "#B7E080";
        }
        return Color.parseColor(result);
    }

}
