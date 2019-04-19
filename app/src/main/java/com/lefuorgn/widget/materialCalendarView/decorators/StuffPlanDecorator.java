package com.lefuorgn.widget.materialCalendarView.decorators;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import com.lefuorgn.oa.bean.StaffPlan;
import com.lefuorgn.util.TLog;
import com.lefuorgn.widget.materialCalendarView.CalendarDay;
import com.lefuorgn.widget.materialCalendarView.DayViewDecorator;
import com.lefuorgn.widget.materialCalendarView.DayViewFacade;
import com.lefuorgn.widget.materialCalendarView.spans.TextSpan;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuting on 2017/4/20.
 */

public class StuffPlanDecorator implements DayViewDecorator{
    Map<Long,StaffPlan> calendarDayMap=new HashMap<Long, StaffPlan>();
    String textVal;
    String bgColor;
    public StuffPlanDecorator(String textVal,String bgColor){

        this.textVal=textVal;
        TLog.error(textVal+"  "+bgColor);
        this.bgColor=bgColor;
    }

    public void addDataValue(StaffPlan data){
        long time=CalendarDay.from(new Date(data.getTime())).getDate().getTime();
        if(!calendarDayMap.containsKey(time)){
            calendarDayMap.put(time , data);
        }
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
       if(calendarDayMap.containsKey(day.getDate().getTime())){
           return true;
       }else {
           return false;
       }
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(new ColorDrawable(Color.parseColor(bgColor)));//generateCircleDrawable(Color.parseColor(bgColor))
        TextSpan textSpan = new TextSpan(textVal);
        view.addSpan(textSpan);
    }
    private static Drawable generateCircleDrawable(final int color) {
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(color);
        return drawable;
    }
}
