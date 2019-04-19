package com.lefuorgn.lefu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.interf.OnItemChildColumnClickListener;
import com.lefuorgn.interf.OnScrollChangedListenerImp;
import com.lefuorgn.lefu.bean.NursingInfo;
import com.lefuorgn.lefu.bean.NursingItemInfo;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.widget.MyHScrollView;

import java.util.List;

/**
 * 护理信息批量编辑数据适配器
 */
public class NursingInfoBatchEditingAdapter extends BaseQuickAdapter<NursingInfo> {

    private Context mContext;
    private List<DisplaySignOrNursingItem> mItems;
    private ViewGroup.LayoutParams mParams;
    private LayoutInflater mInflater;

    // 数据控件背景色
    private int normalColor; // 默认背景色
    private int oneColor; // 值为1的背景颜色
    private int twoColor; // 值为2的背景颜色
    private int threeColor; // 值大于等于3的颜色

    private int noSelectColor; // 条目未选中状态

    /**
     * 方格点击事件
     */
    protected OnItemChildColumnClickListener mListener;

    public NursingInfoBatchEditingAdapter(Context context, List<NursingInfo> data, List<DisplaySignOrNursingItem> items) {
        super(R.layout.item_activity_nursing_info_batch_editing, data);
        this.mContext = context;
        this.mItems = items;
        mParams = new ViewGroup.LayoutParams(StringUtils.dip2px(context, 1), ViewGroup.LayoutParams.MATCH_PARENT);
        mInflater = LayoutInflater.from(context);
        normalColor = Color.WHITE;
        oneColor = context.getResources().getColor(R.color.nursing_info_color_one);
        twoColor = context.getResources().getColor(R.color.nursing_info_color_two);
        threeColor = context.getResources().getColor(R.color.nursing_info_color_three);
        noSelectColor = Color.parseColor("#EDECF1");
    }

    @Override
    protected void convert(BaseViewHolder holder, final NursingInfo nursingInfo) {
        // 设置每条数据的老人信息
        CheckedTextView ctv = holder.getView(R.id.ctv_item_activity_nursing_info_batch_editing);
        ctv.setText(nursingInfo.getOldPeopleName());
        holder.setOnClickListener(R.id.ctv_item_activity_nursing_info_batch_editing, new OnItemChildClickListener())
                .setChecked(R.id.ctv_item_activity_nursing_info_batch_editing, nursingInfo.isSelect());
        if(nursingInfo.isSelect()) {
            ctv.setBackgroundColor(normalColor);
        }else {
            ctv.setBackgroundColor(noSelectColor);
        }
        LinearLayout ll = holder.getView(R.id.ll_item_activity_nursing_info_batch_editing);
        // 设置每条数据的老人类型条目信息
        final int position = holder.getLayoutPosition() - this.getHeaderViewsCount();
        for (int i = 0; i < nursingInfo.getNursingItemInfoList().size(); i++) {
            // 获取当前列位置
            final int column = i;
            final LinearLayout view = (LinearLayout) ll.getChildAt(i * 2);
            // 当前护理次数
            NursingItemInfo info = nursingInfo.getNursingItemInfoList().get(i);
            // 获取当前类型数据总的护理次数
            int times = info.getNurse_times() + info.getCurrent_times();
            // 内容显示控件
            TextView name = (TextView) view
                    .findViewById(R.id.tv_item_item_activity_nursing_info_batch_editing_name);
            // 增加按钮控件
            final TextView plus = (TextView) view
                    .findViewById(R.id.tv_item_item_activity_nursing_info_batch_editing_plus);
            // 减少按钮控件
            final TextView reduce = (TextView) view
                    .findViewById(R.id.tv_item_item_activity_nursing_info_batch_editing_reduce);
            if(nursingInfo.isSelect()) {
                // 老人被选中
                if(info.isSelect()) {
                    // 当前条目在配置中被选中
                    name.setVisibility(View.VISIBLE);
                    view.findViewById(R.id.tv_item_item_activity_nursing_info_batch_editing_operation)
                            .setVisibility(View.VISIBLE);
                    if(times == 0) {
                        name.setText("");
                        view.setBackgroundColor(normalColor);
                    }else if(times == 1) {
                        name.setText("1");
                        view.setBackgroundColor(oneColor);
                    }else if(times == 2) {
                        name.setText("2");
                        view.setBackgroundColor(twoColor);
                    }else {
                        String timeStr = times + "";
                        name.setText(timeStr);
                        view.setBackgroundColor(threeColor);
                    }
                    if(mListener != null) {
                        plus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 点击数据增加
                                mListener.onClick(plus, nursingInfo, position, column);
                            }
                        });
                        reduce.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 点击数据减少
                                mListener.onClick(reduce, nursingInfo, position, column);
                            }
                        });
                    }
                }else {
                    // 当前条目在配置中未被选中
                    name.setVisibility(View.GONE);
                    view.findViewById(R.id.tv_item_item_activity_nursing_info_batch_editing_operation)
                            .setVisibility(View.GONE);
                    view.setBackgroundColor(noSelectColor);
                }
            }else {
                // 老人未被选中
                name.setVisibility(View.GONE);
                view.findViewById(R.id.tv_item_item_activity_nursing_info_batch_editing_operation)
                        .setVisibility(View.GONE);
                view.setBackgroundColor(noSelectColor);
            }
        }
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        View view = getItemView(layoutResId, parent);
        MyHScrollView hsvView = (MyHScrollView) view.findViewById(R.id.hsv_item_activity_nursing_info_batch_editing);
        if(hsvView != null) {
            hsvView.AddOnScrollChangedListener(new OnScrollChangedListenerImp(hsvView));
            LinearLayout hsv = (LinearLayout) view.findViewById(R.id.ll_item_activity_nursing_info_batch_editing);
            for (int i = 0; i < mItems.size(); i++) {
                hsv.addView(getView(hsv));
                if(i < mItems.size() - 1) {
                    hsv.addView(getLine(), mParams);
                }
            }
            return new MyBaseViewHolder(view);
        }else {
            return super.createBaseViewHolder(parent, layoutResId);
        }

    }

    public void notifyItemColumnChanged(int position, int column) {

    }

    /**
     * 获取分割线
     * @return 分割线控件
     */
    private View getLine() {
        View view = new View(mContext);
        view.setBackgroundResource(R.color.recycler_view_item_division_color);
        return view;
    }

    /**
     * 获取空格内的布局
     * @param parent 父控件
     * @return 内容布局控件
     */
    private View getView(ViewGroup parent) {
        return mInflater.inflate(R.layout.item_item_activity_nursing_info_batch_editing, parent, false);
    }

    private class MyBaseViewHolder extends BaseViewHolder {
        MyBaseViewHolder(View view) {
            super(view);
        }
    }

    /**
     * 设置网格点击事件
     * @param l 事件对象
     */
    public void setOnItemChildColumnClickListener(OnItemChildColumnClickListener l) {
        mListener = l;
    }

}
