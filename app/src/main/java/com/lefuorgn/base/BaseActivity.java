package com.lefuorgn.base;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lefuorgn.AppManager;
import com.lefuorgn.R;
import com.lefuorgn.dialog.WaitDialog;
import com.lefuorgn.util.ToastUtils;

/**
 * 基类Activity
 * <p>必须实现方法{@link #getLayoutId()} 设置你的布局</p>
 * <p>实现方法{@link #initView()}初始化布局中的控件</p>
 * <p>实现方法{@link #initData()}初始化当前页面所需要的数据</p>
 * <p>实现方法{@link #init(Bundle)}根据用户状态记录进行初始化</p>
 * <p>如果要在添加布局文件时候进行操作, 可以通过复写{@link #onBeforeSetContentLayout()}方法来实现</p>
 *
 * <p>当前页面如果不进行状态栏操作,那么你就不需要关注此条信息; 如果你需要操作状态栏
 *    那么你就实现{@link #hasStatusBar()}方法, 并返回false; 但是以下可能需要的
 *    方法都需要你来实现;
 * </p>
 *
 * <p>如果当前页面不需要ActionBar, 那么你不需要任何操作;
 *    如果当前页面需要ActionBar,可以通过{@link #hasToolBar()}方法并返回true来实现;
 *    如果需要返回的需求那么就可以通过{@link #hasBackButton()}方法并返回true来实现;
 *    如果还需要实现菜单,有3种情况:
 *    <ul>
 *        <li>只是一个文本: 通过调用{@link #setMenuTextView(String str)}方法来实现</li>
 *        <li>只是一个图片: 通过调用{@link #setMenuImageView(int resId)}方法来实现</li>
 *        <li>复杂的布局: 通过调用{@link #setMenuLayout(int layoutId)}方法来实现,
 *              具体布局要求看此方法的注释
 *        </li>
 *    </ul>
 * </p>
 * <p>菜单的点击事件只需实现{@link #onMenuClick(View v)}方法即可;</p>
 * <p>如果存在标题的情况可以通过{@link #setToolBarTitle(String name)}方法设置;</p>
 *
 * <p>加载数据等待页面可以调用{@link #showWaitDialog()}方法; 隐藏则调用{@link #hideWaitDialog()}方法。
 *    如果已近复写了{@link #hasStatusBar()}方法,那么这俩个方法不会起任何作用
 * <p/>
 *
 */

public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener{

    private TextView mToolBarTitle;
    private FrameLayout mMenuLayout;
    private View mLoadingView; // 页面层级的Dialog(即在其加载时可以进行其他操作)
    private WaitDialog mWaitDialog; // 页面上层级别的Dialog(加载时不进行任何操作);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(!isOrientation()) {
            // 设置屏幕为竖屏,不可旋转
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 去掉状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        onBeforeSetContentLayout();
        if(getLayoutId() == 0) {
            throw new IllegalArgumentException("You must Override the method getLayoutId()");
        }
        if(hasStatusBar()) {
            // 填充自己的状态栏
            @SuppressLint("InflateParams")
            LinearLayout content = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
            View statusBar = content.findViewById(R.id.v_activity_base_status_bar);
            // 放实际内容的容器
            FrameLayout container = (FrameLayout) content.findViewById(R.id.fl_activity_base_status_bar);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 初始化状态栏
                ViewGroup.LayoutParams params = statusBar.getLayoutParams();
                params.height = getStatusBarHeight();
                statusBar.setLayoutParams(params);
            }
            // actionBar条目
            RelativeLayout title = (RelativeLayout) content.findViewById(R.id.rl_activity_base_status);
            // 初始化子类提供的布局文件
            View view = getLayoutInflater().inflate(getLayoutId(), container, false);
            // 初始化加载页面
            mLoadingView = getLayoutInflater().inflate(R.layout.load_activity_fragment, container, false);
            mLoadingView.setVisibility(View.GONE);
            if(hasToolBar()) {
                // 初始化ToolBar
                title.setVisibility(View.VISIBLE);
                initToolBar(content);

            }
            container.addView(view);
            container.addView(mLoadingView);
            setContentView(content);
        }else {
            // 不存在状态栏,以及标题栏
            setContentView(getLayoutId());
        }
        init(savedInstanceState);
        initView();
        initData();

    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar(ViewGroup parent) {
        mToolBarTitle = (TextView) parent.findViewById(R.id.tv_activity_base_title);
        mMenuLayout = (FrameLayout) parent.findViewById(R.id.fl_activity_base);
        if(hasBackButton()) {
            //
            ImageView back = (ImageView) parent.findViewById(R.id.iv_activity_base_back);
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    /**
     * 设置ToolBar标题名称
     * @param name 标题
     */
    protected TextView setToolBarTitle(String name) {
        if(hasToolBar()) {
            mToolBarTitle.setText(name);
        }
        return mToolBarTitle;
    }

    /**
     * 设置ToolBar标题名称
     * @param resId 资源ID
     */
   protected TextView setToolBarTitle(int resId) {
        if(hasToolBar()) {
            mToolBarTitle.setText(getResources().getText(resId));
        }
        return mToolBarTitle;
    }

    /**
     * 设置菜单选项, 菜单选项为文字
     * 控件的点击事件直接复写onMenuClick(View v)即可
     *
     * @param str 选项名称
     * @return TextView对象; 或者null
     */
    protected TextView setMenuTextView(String str) {
        if(hasToolBar()) {
            int padding = getResources().getDimensionPixelSize(R.dimen.tool_bar_margin_padding_left_right);
            int size = getResources().getDimensionPixelSize(R.dimen.tool_bar_menu_text_size);
            TextView tv = new TextView(this);
            tv.setText(str);
            tv.setPadding(padding, 0, padding, 0);
            tv.setTextColor(Color.WHITE);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMenuClick(v);
                }
            });
            mMenuLayout.addView(tv, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            return tv;
        }
        return null;
    }

    /**
     * 设置菜单选项, 菜单选项为图片
     * 控件的点击事件直接复写onMenuClick(View v)即可
     *
     * @param resId 图片资源ID
     * @return TextView对象; 或者null
     */
    protected ImageView setMenuImageView(int resId) {
        if(hasToolBar() && resId != 0) {
            int padding = getResources().getDimensionPixelSize(R.dimen.tool_bar_margin_padding_left_right);
            ImageView iv = new ImageView(this);
            iv.setImageResource(resId);
            iv.setPadding(padding, 0, padding, 0);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMenuClick(v);
                }
            });
            mMenuLayout.addView(iv, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            return iv;
        }
        return null;
    }

    /**
     * 设置菜单选项, 菜单选项为布局
     * 菜单布局的最外层必须是LinearLayout;
     * 控件的点击事件直接复写onMenuClick(View v)即可;
     * 多个控件点击事件需要通过ID进行区分;
     * 菜单布局距离右边界可以参考R.dimen.tool_bar_margin_padding_left_right;
     * 建议: 设置最右边的控件的内边距为R.dimen.tool_bar_margin_padding_left_right
     *      这样可以扩大控件的触摸点击范围
     *
     * @param layoutId 布局资源ID
     */
    protected void setMenuLayout(int layoutId) {
        if(hasToolBar() && layoutId != 0) {
            LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(layoutId, null);
            for (int i = 0; i < layout.getChildCount(); i++) {
                View view = layout.getChildAt(i);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onMenuClick(v);
                    }
                });
            }
            mMenuLayout.addView(layout, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
        }

    }

    /**
     * 在添加视图之前执行的方法
     */
    protected void onBeforeSetContentLayout() {}

    /**
     * 根据用户状态记录进行初始化
     * @param savedInstanceState 页面状态记录信息
     */
    protected void init(Bundle savedInstanceState) {}

    /**
     * 获取布局文件的ID值
     * @return 布局文件ID
     */
    protected abstract int getLayoutId();

    /**
     * 初始化页面视图
     */
    protected void initView() {}

    protected boolean hasStatusBar() {
        return true;
    }

    /**
     * 是否要添加ToolBar, 默认不添加
     * @return true : 添加; false : 不添加
     */
    protected boolean hasToolBar() {
        return false;
    }

    /**
     * 是否要添加返回按钮
     * @return true : 添加; false : 不添加
     */
    protected boolean hasBackButton() {
        return false;
    }

    /**
     * 是否可以旋转屏幕
     * @return true: 可以旋转; false: 不可以旋转
     */
    protected boolean isOrientation() {
        return false;
    }

    /**
     * 初始化页面数据
     */
    protected void initData() {}

    protected void showToast(String msg) {
        if(msg == null) {
            msg = "";
        }
        ToastUtils.show(this, msg);
    }

    protected void showToast(int resId) {
        ToastUtils.show(this, resId);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 菜单选项控件点击事件
     * @param v 被点击的视图控件
     */
    protected void onMenuClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }

    /**
     * 获取状态栏的高度
     * @return 状态栏的高度
     */
    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 隐藏等待框
     */
    protected void hideWaitDialog() {
        if(mLoadingView != null && mLoadingView.getVisibility() != View.GONE) {
            mLoadingView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示等待框(显示后可以进行一些操作)
     */
    public void showWaitDialog() {
        if(mLoadingView != null && mLoadingView.getVisibility() != View.VISIBLE) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示数据等待加载框(页面上层级别的)(显示后不可进行任何操作)
     * @param msg 要显示的内容
     */
    public void showLoadingDialog(String msg) {
        if(mWaitDialog == null) {
            mWaitDialog = new WaitDialog();
        }
        mWaitDialog.setMessage(msg);
        mWaitDialog.show(getSupportFragmentManager(), "BaseActivity");
    }

    /**
     * 显示数据等待加载框(页面上层级别的)(显示后不可进行任何操作)
     */
    public void showLoadingDialog() {
        showLoadingDialog("");
    }

    /**
     * 显示数据等待加载框
     */
    public void hideLoadingDialog() {
        if(mWaitDialog != null) {
            mWaitDialog.dismiss();
        }
    }

}
