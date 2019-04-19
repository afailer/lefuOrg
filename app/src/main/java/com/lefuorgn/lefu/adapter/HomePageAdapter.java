package com.lefuorgn.lefu.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.gov.bean.IconItem;

import java.util.List;

/**
 * 机构页面首页机构信息展示信息
 */

public class HomePageAdapter extends BaseQuickAdapter<IconItem>{
    int width;
    boolean shouldSetWidth;
    public HomePageAdapter(List<IconItem> data,boolean shouldSetWidth) {
        super(R.layout.item_fragment_home_page, data);
        WindowManager wm = (WindowManager) AppContext.getInstance()
                .getSystemService(Context.WINDOW_SERVICE);
        width= wm.getDefaultDisplay().getWidth();
        this.shouldSetWidth=shouldSetWidth;
    }

    @Override
    protected void convert(BaseViewHolder holder,final IconItem iconItem) {
        TextView textView = holder.getView(R.id.vt_item_fragment_home_page);
        if(shouldSetWidth) {
            textView.setLayoutParams(new RecyclerView.LayoutParams(width / 4, RecyclerView.LayoutParams.WRAP_CONTENT));
        }
        textView.setText(iconItem.getName());
        Drawable top = AppContext.getInstance().getResources().getDrawable(iconItem.getIcon());
        textView.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
        holder.setOnClickListener(R.id.vt_item_fragment_home_page, new OnItemChildClickListener());
    }

}
