package com.lefuorgn.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lefuorgn.R;
import com.lefuorgn.dialog.WaitDialog;
import com.lefuorgn.util.ToastUtils;

/**
 * 基类Fragment, 数据的初始化有俩种状态:
 * 1、实现initData()方法可实现非懒加载的方式获取数据
 * 2、实现lazyFetchData()方法可实现懒加载的方式获取数据
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener{

    private boolean isViewPrepared; // 标识fragment视图已经初始化完毕
    private boolean hasFetchData; // 标识已经触发过懒加载数据

    protected ViewGroup mRootView;
    private View mLoadingView; // 页面层级的Dialog(即在其加载时可以进行其他操作)
    private WaitDialog mWaitDialog; // 页面上层级别的Dialog(加载时不进行任何操作);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mRootView == null) {
            if(hasWaitDialog()) {
                mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_base, container, false);
                View view = inflater.inflate(getLayoutId(), mRootView, false);
                mLoadingView = inflater.inflate(R.layout.load_activity_fragment, mRootView, false);
                mLoadingView.setVisibility(View.GONE);
                mRootView.addView(view);
                mRootView.addView(mLoadingView);
            }else {
                mRootView = (ViewGroup) inflater.inflate(getLayoutId(), container, false);
            }
            // 初始化视图控件
            initView(mRootView, savedInstanceState);
            // 非懒加载数据
            initData();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewPrepared = true;
        // 首个页面,可能直接加载
        lazyFetchDataIfPrepared();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // 当前页面用户可见
            lazyFetchDataIfPrepared();
        }
    }

    /**
     * 懒加载数据
     */
    private void lazyFetchDataIfPrepared() {
        // 用户可见fragment && 没有加载过数据 && 视图已经准备完毕
        if (getUserVisibleHint() && !hasFetchData && isViewPrepared) {
            hasFetchData = true; //已加载过数据
            lazyFetchData();
        }
    }

    /**
     * 非懒加载的方式获取数据
     */
    protected void initData() {

    }

    /**
     * 懒加载的方式获取数据，仅在满足fragment可见和视图已经准备好的时候调用一次
     */
    protected void lazyFetchData() {

    }

    /**
     * 初始化视图控件
     * @param view 父控件
     * @param savedInstanceState 页面状态记录信息
     */
    protected void initView(View view, Bundle savedInstanceState) {

    }

    protected abstract int getLayoutId();

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // fragment视图销毁
        isViewPrepared = false;
//        hasFetchData = false;
    }

    protected void showToast(String msg) {
        if(msg == null) {
            msg = "";
        }
        ToastUtils.show(getActivity(), msg);
    }

    protected void showToast(int resId) {
        ToastUtils.show(getActivity(), resId);
    }

    protected void showToast(String msg, int duration) {
        if(msg == null) {
            msg = "";
        }
        ToastUtils.show(getActivity(), msg, duration);
    }

    protected boolean hasWaitDialog() {
        return true;
    }

    protected void hideWaitDialog() {
        if(mLoadingView != null && mLoadingView.getVisibility() != View.GONE) {
            mLoadingView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示等待框(显示后可以进行一些操作)
     */
    protected void showWaitDialog() {
        if(mLoadingView != null && mLoadingView.getVisibility() != View.VISIBLE) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示数据等待加载框(页面上层级别的)(显示后不可进行任何操作)
     * @param msg 要显示的内容
     */
    protected void showLoadingDialog(String msg) {
        if(mWaitDialog == null) {
            mWaitDialog = new WaitDialog();
        }
        mWaitDialog.setMessage(msg);
        mWaitDialog.show(getFragmentManager(), "BaseActivity");
    }

    /**
     * 显示数据等待加载框(页面上层级别的)(显示后不可进行任何操作)
     */
    protected void showLoadingDialog() {
        showLoadingDialog("");
    }

    /**
     * 显示数据等待加载框
     */
    protected void hideLoadingDialog() {
        if(mWaitDialog != null) {
            mWaitDialog.dismiss();
        }
    }

}
