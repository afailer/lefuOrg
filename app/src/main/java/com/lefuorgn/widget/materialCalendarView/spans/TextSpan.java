package com.lefuorgn.widget.materialCalendarView.spans;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

/**
 * Created by liuting on 2017/4/20.
 */

public class TextSpan implements LineBackgroundSpan {
    private String value="";
    public TextSpan(String str){
        this.value=str;
    }
    @Override
    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
        int oldColor = paint.getColor();
        float oldTextSize = paint.getTextSize();
        paint.setTextSize(30);
        paint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawText(value,(left+right)/2-value.length()*15, bottom +25,paint);
        paint.setTextSize(oldTextSize);
        paint.setColor(oldColor);
    }
}
