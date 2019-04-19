package com.lefuorgn.widget.materialCalendarView.decorators;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.lefuorgn.R;
import com.lefuorgn.oa.bean.OaAttendanceRecord;
import com.lefuorgn.util.TLog;
import com.lefuorgn.widget.materialCalendarView.CalendarDay;
import com.lefuorgn.widget.materialCalendarView.DayViewDecorator;
import com.lefuorgn.widget.materialCalendarView.DayViewFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Use a custom selector
 */
public class MySelectorDecorator implements DayViewDecorator {

    private final Drawable drawable;
    Map<Long,CalendarDay> calendarDayMap=new HashMap<Long, CalendarDay>();
    public MySelectorDecorator(Activity context) {
        drawable = context.getResources().getDrawable(R.mipmap.dayview_bg);
    }
    public void addCalendarDay(CalendarDay calendarDay){
        if(!calendarDayMap.containsKey(calendarDay.getDate().getTime())){
            calendarDayMap.put(calendarDay.getDate().getTime(),calendarDay);
        }
    }
    public void addCalendarDays(List<CalendarDay> calendarDays){
        for(CalendarDay c: calendarDays){
            if(!calendarDayMap.containsKey(c.getDate().getTime())){
                calendarDayMap.put(c.getDate().getTime(),c);
            }
        }
    }
    public OaAttendanceRecord getOaAttendanceRecord(CalendarDay day){
        return (OaAttendanceRecord) calendarDayMap.get(day.getDate().getTime()).getExtraData();
    }
    public void clearData(){
        calendarDayMap.clear();
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        if(calendarDayMap.containsKey(day.getDate().getTime())){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void decorate(DayViewFacade view) {
        TLog.error("decorate");
        view.setSelectionDrawable(drawable);
    }
}
