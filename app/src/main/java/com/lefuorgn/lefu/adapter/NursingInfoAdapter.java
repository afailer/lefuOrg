package com.lefuorgn.lefu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.lefu.base.BaseGridAdapter;
import com.lefuorgn.lefu.bean.NursingInfo;

import java.util.List;

/**
 * 护理信息数据适配器
 */
public class NursingInfoAdapter extends BaseGridAdapter<NursingInfo> {

    // 数据控件背景色
    private int normalColor; // 默认背景色
    private int oneColor; // 值为1的背景颜色
    private int twoColor; // 值为2的背景颜色
    private int threeColor; // 值大于等于3的颜色

    public NursingInfoAdapter(Context context, List<NursingInfo> data, List<DisplaySignOrNursingItem> items) {
        super(context, R.layout.item_activity_base_grid, data, items);
        normalColor = Color.WHITE;
        oneColor = context.getResources().getColor(R.color.nursing_info_color_one);
        twoColor = context.getResources().getColor(R.color.nursing_info_color_two);
        threeColor = context.getResources().getColor(R.color.nursing_info_color_three);
    }

    @Override
    protected void convert(BaseViewHolder holder, final NursingInfo nursingInfo) {
        holder.setText(R.id.tv_item_activity_base_grid, nursingInfo.getOldPeopleName())
                .setOnClickListener(R.id.tv_item_activity_base_grid, new OnItemChildClickListener());
        LinearLayout ll = holder.getView(R.id.ll_item_activity_base_grid);
        final int position = holder.getLayoutPosition() - this.getHeaderViewsCount();
        for (int i = 0; i < nursingInfo.getNursingItemInfoList().size(); i++) {
            // 获取当前列位置
            final int column = i;
            final TextView view = (TextView) ll.getChildAt(i * 2);
            // 当前护理次数
            int times = nursingInfo.getNursingItemInfoList().get(i).getNurse_times();
            if(times == 0) {
                view.setText("");
                view.setBackgroundColor(normalColor);
            }else if(times == 1) {
                view.setText("1");
                view.setBackgroundColor(oneColor);
            }else if(times == 2) {
                view.setText("2");
                view.setBackgroundColor(twoColor);
            }else {
                String timeStr = times + "";
                view.setText(timeStr);
                view.setBackgroundColor(threeColor);
            }
            if(mListener != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onClick(view, nursingInfo, position, column);
                    }
                });
            }
        }
    }

}
