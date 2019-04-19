package com.lefuorgn.lefu.util;

import com.lefuorgn.interf.OnScrollChangedListener;
import com.lefuorgn.widget.MyHScrollView;

import java.util.ArrayList;
import java.util.List;

public class ScrollViewObserver {
	
	private List<OnScrollChangedListener> mList;

	public ScrollViewObserver() {
		mList = new ArrayList<OnScrollChangedListener>();
	}

	public void AddOnScrollChangedListener(OnScrollChangedListener listener) {
		if(mList.size() > 0) {
			final MyHScrollView scrollView = mList.get(0).getMyHScrollView();
            final MyHScrollView newScrollView = listener.getMyHScrollView();
            newScrollView.post(new Runnable() {
                @Override
                public void run() {
                    newScrollView.scrollToByFlag(false, scrollView.getScrollX(), scrollView.getScrollY());
                }
            });

		}
		mList.add(listener);
	}

	public void NotifyOnScrollChanged(MyHScrollView view, int l, int t, int oldl, int oldt) {
		for (int i = 0; i < mList.size(); i++) {
			if (mList.get(i) != null) {
				mList.get(i).onOtherScrollChanged(view, l, t, oldl, oldt);
			}
		}
	}

    /**
     * 清除当前页面的条目缓存
     */
    public void clear() {
        if(mList != null) {
            mList.clear();
        }
    }

    /**
     * 重置当前位置
     */
	public void resetLocation() {
		if(mList != null && mList.size() > 0) {
			OnScrollChangedListener l = mList.get(0);
            l.getMyHScrollView().scrollToByFlag(true, 0, 0);
		}
	}

}
