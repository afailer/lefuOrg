package com.lefuorgn.oa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.interf.Pinyinable;
import com.lefuorgn.oa.base.BaseGridAdapter;
import com.lefuorgn.oa.bean.StaffPlan;
import com.lefuorgn.oa.bean.StaffPlanCollector;

import java.util.List;

/**
 * Created by liuting on 2017/4/22.
 */

public class AllStuffWorkAdapter extends BaseGridAdapter<StaffPlanCollector> {
    Drawable transitionDrawable;
    public AllStuffWorkAdapter(Context context, List<StaffPlanCollector> data, int size, int width, int height) {
        super(context, R.layout.work_arrange_item, data, size, width, height);
        transitionDrawable=generateCircleDrawable(Color.TRANSPARENT);
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, StaffPlanCollector staffPlanCollector) {
        LinearLayout ll = baseViewHolder.getView(R.id.month_work_content);
        baseViewHolder.setText(R.id.stuff_name,staffPlanCollector.getStaffName());
        for(int i=0;i<staffPlanCollector.getStaffPlan().size();i++){
            StaffPlan staffPlan = staffPlanCollector.getStaffPlan().get(i);
            View view= ll.getChildAt(i);
            TextView workType= (TextView) view.findViewById(R.id.work_type);
            if(staffPlan!=null){
                workType.setText(staffPlan.getOa_scheduling_short_name());
                Drawable drawable = generateCircleDrawable(Color.parseColor(staffPlan.getOa_scheduling_color()));
                workType.setBackgroundDrawable(drawable);
            }else{
                workType.setText("");
                workType.setBackgroundDrawable(transitionDrawable);
            }
        }
        boolean flag;
        int position = baseViewHolder.getLayoutPosition() - this.getHeaderViewsCount();
        if(position == 0) {
            // 第一个条目
            flag = true;
        }else {
            Pinyinable t1 = (Pinyinable) getData().get(position - 1);
            flag = !staffPlanCollector.getSortLetters().equals(t1.getSortLetters());
        }
        baseViewHolder.setText(R.id.tv_item_fragment_contact_letter,staffPlanCollector.getSortLetters())
                .setVisible(R.id.tv_item_fragment_contact_letter, flag);
    }
    private static Drawable generateCircleDrawable(final int color) {
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(color);
        return drawable;
    }
   /* Date dateTime;
    public AllStuffWorkAdapter(List<StuffPlanList> data,Date date) {
        super(R.layout.work_arrange_item,data);
        this.dateTime=date;
    }
    List<Long> stuffIdList=new ArrayList<Long>();
    @Override
    protected void convert(BaseViewHolder baseViewHolder, StuffPlanList stuffPlanList) {
        baseViewHolder.setText(R.id.stuff_name,stuffPlanList.getUser_name());
        MyHScrollView itemHorizontal= baseViewHolder.getView(R.id.item_work_horizontal);
        itemHorizontal.AddOnScrollChangedListener(new OnScrollChangedListenerImp(itemHorizontal));
        LinearLayout workContent=baseViewHolder.getView(R.id.month_work_content);
        if(!stuffIdList.contains(stuffPlanList.getStaff_id())) {
            List<View> views = getViews(workContent, stuffPlanList);
            for (View view : views) {
                workContent.addView(view);
            }
            stuffIdList.add(stuffPlanList.getStaff_id());
        }
    }

    private List<View> getViews(ViewGroup parent, StuffPlanList stuffPlanList){
        List<View> views=new ArrayList<View>();
        CalendarDay date=CalendarDay.from(dateTime);
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.YEAR,date.getYear());
        cal.set(Calendar.MONTH,date.getMonth());
        int num = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        TLog.error("--------------------------------------");
        for(int i=1;i<=num;i++){
            cal.set(Calendar.DAY_OF_MONTH,i);
            View container = mLayoutInflater.inflate(R.layout.textview, parent, false);
            TextView textView= (TextView) container.findViewById(R.id.work_type);
            if(stuffPlanList.getOaStaffPlans()!=null) {
                for (StaffPlan staffPlan : stuffPlanList.getOaStaffPlans()) {
                    if (CalendarDay.from(cal).getDay() == CalendarDay.from(new Date(staffPlan.getTime())).getDay()) {
                        TLog.error(CalendarDay.from(cal).getDay()+"");
                        textView.setText(staffPlan.getOa_scheduling_short_name().substring(0,1));
                        Drawable drawable = generateCircleDrawable(Color.parseColor(staffPlan.getOa_scheduling_color()));
                        textView.setBackgroundDrawable(drawable);
                    }
                }
            }
            views.add(container);
        }
        TLog.error("+++++++++++++++++++++++++++++++++++++++++++++");
        return views;
    }
    private static Drawable generateCircleDrawable(final int color) {
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(color);
        return drawable;
    }*/
}
