package com.lefuorgn.widget.materialCalendarView.decorators;

import android.graphics.Color;

import com.lefuorgn.util.TLog;
import com.lefuorgn.widget.materialCalendarView.CalendarDay;
import com.lefuorgn.widget.materialCalendarView.DayViewDecorator;
import com.lefuorgn.widget.materialCalendarView.DayViewFacade;
import com.lefuorgn.widget.materialCalendarView.spans.DotSpan;

import java.util.Date;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

    private CalendarDay date;

    public EventDecorator(CalendarDay calendarDay) {
        this.date = calendarDay;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        TLog.error("EventDecorator shouldDecorate");
        if(date==null){return false;}
        if(date.getDate().getTime()==day.getDate().getTime()) {
            return true;
        }else{
            return false;
        }
    }
    public void setDate(Date date) {
        this.date = CalendarDay.from(date);
    }
    public void clearData(){
        this.date=null;
    };
    @Override
    public void decorate(DayViewFacade view) {
        TLog.error("EventDecorator decorate");
        DotSpan dotSpan = new DotSpan(5, Color.parseColor("#FF0000"));
        view.addSpan(dotSpan);
    }
}
