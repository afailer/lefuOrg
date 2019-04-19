package com.lefuorgn.interf;


import com.lefuorgn.widget.MyHScrollView;

public class OnScrollChangedListenerImp implements OnScrollChangedListener {

	private MyHScrollView mScrollViewArg;

	public OnScrollChangedListenerImp(MyHScrollView scrollViewar) {
		mScrollViewArg = scrollViewar;
	}

	@Override
	public void onOtherScrollChanged(MyHScrollView view, final int l, final int t, int oldl, int oldt) {
        if(mScrollViewArg != view) {
            mScrollViewArg.scrollToByFlag(false, l, t);
        }
	}

	@Override
	public MyHScrollView getMyHScrollView() {
		return mScrollViewArg;
	}
}
