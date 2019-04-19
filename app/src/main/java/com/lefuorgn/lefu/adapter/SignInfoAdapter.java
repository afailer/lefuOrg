package com.lefuorgn.lefu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.lefu.base.BaseGridAdapter;
import com.lefuorgn.lefu.bean.SignInfo;
import com.lefuorgn.util.StringUtils;

import java.util.List;

/**
 * 体征信息数据适配器
 */
public class SignInfoAdapter extends BaseGridAdapter<SignInfo> {

    public SignInfoAdapter(Context context, List<SignInfo> data, List<DisplaySignOrNursingItem> items) {
        super(context, R.layout.item_activity_base_grid, data, items);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final SignInfo signInfo) {
        baseViewHolder.setText(R.id.tv_item_activity_base_grid, signInfo.getOldPeopleName())
                .setOnClickListener(R.id.tv_item_activity_base_grid, new OnItemChildClickListener());
        LinearLayout ll = baseViewHolder.getView(R.id.ll_item_activity_base_grid);
        // 获取当前条目位置
        final int position = baseViewHolder.getLayoutPosition() - this.getHeaderViewsCount();
        for (int i = 0; i < signInfo.getSignItemInfos().size(); i++) {
            // 获取当前列位置
            final int column = i;
            // 获取方格布局
            final LinearLayout view = (LinearLayout) ll.getChildAt(column * 2);
            // 获取内容展示控件
            TextView content = (TextView)view.getChildAt(0);
            content.setText(signInfo.getSignItemInfos().get(column).getContent());
            content.setTextColor(signInfo.getSignItemInfos().get(column).getColor());
            long time = signInfo.getSignItemInfos().get(column).getTime();
            // 展示内容添加的时间
            ((TextView)view.getChildAt(1)).setText(time > 0 ? StringUtils.getFormatData(
                    signInfo.getSignItemInfos().get(column).getTime(), "HH:mm:ss") : "");
            if(mListener != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onClick(view, signInfo, position, column);
                    }
                });
            }
        }
    }

    @Override
    protected View getView(ViewGroup parent, int width, int height) {
        View view = mInflater.inflate(R.layout.item_column_sign_info, parent, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
        return view;
    }
}
