package com.lefuorgn.viewloader.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lefuorgn.viewloader.util.BuilderUtils;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 视图构建器基类
 */

public abstract class ViewBuilder {

    protected Context mContext;
    protected LayoutInflater mInflater;

    protected int mPaddingWidth; // 宽内边距
    protected int mPaddingHeight; // 高内边距

    /**
     * 构造方法
     * @param context 环境上下文
     * @param paddingWidth 控件的左右内边距(单位DP)
     * @param paddingHeight 控件的上下内边距(单位DP)
     */
    public ViewBuilder(Context context, int paddingWidth, int paddingHeight) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mPaddingWidth = paddingWidth;
        mPaddingHeight = paddingHeight;
    }


    public final View buildView(JSONObject jsonObject, ViewGroup parent) {
        if(getLayoutId() == 0) {
            throw new IllegalArgumentException("You must Override the method getLayoutId()");
        }
        View view = mInflater.inflate(getLayoutId(), parent, false);
        initView(view);
        initData(jsonObject);
        return view;
    }

    /**
     * 从JSONObject中获取字符串类型数据
     */
    protected String getString(JSONObject jsonObject, String name) {
        return BuilderUtils.getString(jsonObject, name);
    }

    /**
     * 从JSONObject中获取布尔类型数据
     */
    protected boolean getBoolean(JSONObject jsonObject, String name) {
        return BuilderUtils.getBoolean(jsonObject, name);
    }

    /**
     * 从JSONObject中获取列表数据
     */
    protected JSONArray getArray(JSONObject jsonObject, String name) {
        return BuilderUtils.getArray(jsonObject, name);
    }

    /**
     * 从JSONArray中获取JSONObject对象
     */
    protected JSONObject getObject(JSONArray jsonArray, int index) {
        return BuilderUtils.getObject(jsonArray, index);
    }

    /**
     * 获取布局文件ID
     */
    protected abstract int getLayoutId();

    /**
     * 初始化文件
     */
    protected abstract void initView(View parent);

    /**
     * 初始化内容
     */
    protected abstract void initData(JSONObject jsonObject);

}
