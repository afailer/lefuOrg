package com.lefuorgn.viewloader.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.lefuorgn.util.StringUtils;


/**
 * 线控件
 */

public class LineView extends View {

    /**
     * 实线
     */
    public static final int SOLID_LINE = 0x00000000;
    /**
     * 虚线
     */
    public static final int DOTTED_LINE = 0x00000004;
    /**
     * 双实线
     */
    public static final int DOUBLE_SOLID_LINE = 0x00000008;

    private Context mContext;
    private int mLineColor; // 线条颜色
    private int mLineType; // 线条类型
    private int mLineWidth; // 线条宽度

    private int mWidth; // 当前控件的宽度
    private int mDoubleLineInterval; // 双实线间隔

    private Paint mPaint; // 线条画笔
    private Path path; // 路径

    public LineView(Context context) {
        super(context);
        init(context);
    }

    public LineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mLineColor = Color.GRAY;
        mLineType = SOLID_LINE;
        mLineWidth = StringUtils.dip2px(context, 1);
        mDoubleLineInterval = mLineWidth;
        path = new Path();
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width; // 实际宽度
        int height; // 实际高度
        // 获取实际宽度
        if(widthMode == MeasureSpec.EXACTLY) {
            // 明确值
            width = widthSize;
        }else {
            // 其他
            width = StringUtils.dip2px(mContext, 12) + getPaddingLeft() + getPaddingRight();
        }
        // 获取实际高度
        if(heightMode == MeasureSpec.EXACTLY) {
            // 明确值
            height = heightSize;
        }else {
            // 包裹内容
            if(mLineType == DOUBLE_SOLID_LINE) { // mDoubleLineInterval
                height = getPaddingTop() + getPaddingBottom() + mLineWidth * 2 + mDoubleLineInterval;
            }else {
                height = getPaddingTop() + getPaddingBottom() + mLineWidth;
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        int firstTop = getPaddingTop() + mLineWidth / 2;
        path.moveTo(getPaddingLeft(), firstTop);
        path.lineTo(mWidth - getPaddingRight(), firstTop);
        path.close();
        canvas.drawPath(path, mPaint);
        if(mLineType == DOUBLE_SOLID_LINE) {
            int top = firstTop + mLineWidth + mDoubleLineInterval;
            path.moveTo(getPaddingLeft(), top);
            path.lineTo(mWidth - getPaddingRight(), top);
            path.close();
            canvas.drawPath(path, mPaint);
        }
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = getMeasuredWidth();
    }

    /**
     * 设置线条类型
     */
    public void setLineType(int lineType) {
        if(mLineType == lineType) {
            return;
        }
        if(lineType == SOLID_LINE) {
            mLineType = SOLID_LINE;
            mPaint.setPathEffect(null);
        }else if(lineType == DOTTED_LINE) {
            mLineType = DOTTED_LINE;
            mPaint.setPathEffect(new DashPathEffect(new float[] {20, 20}, 1));
        }else if(lineType == DOUBLE_SOLID_LINE) {
            mLineType = DOUBLE_SOLID_LINE;
            mPaint.setPathEffect(null);
        }else {
            throw new IllegalArgumentException("the lineType must SOLID_LINE、DOTTED_LINE or DOUBLE_SOLID_LINE");
        }
        requestLayout();
        invalidate();
    }

    /**
     * 设置线条宽度
     */
    public void setLineWidth(int width) {
        mLineWidth = StringUtils.dip2px(mContext, width);
        mPaint.setStrokeWidth(mLineWidth);
        requestLayout();
        invalidate();
    }

    /**
     * 设置线条颜色
     */
    public void setLineColor(int lineColor) {
        this.mLineColor = lineColor;
        mPaint.setColor(mLineColor);
        invalidate();
    }

}
