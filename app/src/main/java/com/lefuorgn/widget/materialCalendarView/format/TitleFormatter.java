package com.lefuorgn.widget.materialCalendarView.format;


import com.lefuorgn.widget.materialCalendarView.CalendarDay;

/**
 */
public interface TitleFormatter {

    /**
     * Converts the supplied day to a suitable month/year title
     *
     * @param day the day containing relevant month and year information
     * @return a label to display for the given month/year
     */
    CharSequence format(CalendarDay day);
}
