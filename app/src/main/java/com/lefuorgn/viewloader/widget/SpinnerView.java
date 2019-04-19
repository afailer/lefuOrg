package com.lefuorgn.viewloader.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.viewloader.base.BaseSpinnerAdapter;

/**
 * 下拉菜单控件
 */

public class SpinnerView extends TextView {

    private static final String INSTANCE_STATE = "instance_state";
    private static final String SELECTED_INDEX = "selected_index";
    private static final String IS_POPUP_SHOWING = "is_popup_showing";

    private int selectedIndex;
    private BaseSpinnerAdapter mAdapter;
    private ListPopupWindow mPopup;
    private String defaultValue;

    public SpinnerView(Context context) {
        this(context, null);
    }

    public SpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SpinnerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    private void init(Context context) {
        setClickable(true);
        setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.tree_ex, 0);
        setCompoundDrawablePadding(StringUtils.dip2px(context, 4));
        setGravity(Gravity.END | Gravity.CENTER_VERTICAL);

        selectedIndex = -1;

        mPopup = new ListPopupWindow (context);
        mPopup.setAnchorView(this);
        mPopup.setModal(true);
        mPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedIndex = position;
                setText(mAdapter == null ? "" : mAdapter.getSelectText(position));
                dismissDropDown();
            }
        });

    }

    public void setAdapter(BaseSpinnerAdapter adapter) {
        mAdapter = adapter;
        mPopup.setAdapter(adapter);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mPopup.setWidth(View.MeasureSpec.getSize(widthMeasureSpec));
        mPopup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!mPopup.isShowing()) {
                showDropDown();
            } else {
                dismissDropDown();
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            // 按返回退出
            if (mPopup != null && mPopup.isShowing()) {
                mPopup.dismiss();
                return true;
            }else {
                return super.dispatchKeyEvent(event);
            }
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mPopup != null && mPopup.isShowing()) {
            mPopup.dismiss();
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(SELECTED_INDEX, selectedIndex);
        if (mPopup != null) {
            bundle.putBoolean(IS_POPUP_SHOWING, mPopup.isShowing());
            dismissDropDown();
        }
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable savedState) {
        if (savedState instanceof Bundle) {
            Bundle bundle = (Bundle) savedState;
            selectedIndex = bundle.getInt(SELECTED_INDEX);

            if (mAdapter != null && selectedIndex >= 0) {
                setText(mAdapter.getSelectText(selectedIndex));
            }

            if (bundle.getBoolean(IS_POPUP_SHOWING)) {
                if (mPopup != null) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            showDropDown();
                        }
                    });
                }
            }
            savedState = bundle.getParcelable(INSTANCE_STATE);
        }
        super.onRestoreInstanceState(savedState);
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setYOffset(int yOffset) {
        mPopup.setVerticalOffset(yOffset);
    }

    public void dismissDropDown() {
        mPopup.dismiss();
    }

    public void showDropDown() {
        mPopup.show();
    }

    public void setSelectData(int position, String label) {
        selectedIndex = position;
        setText(label);
    }
}
