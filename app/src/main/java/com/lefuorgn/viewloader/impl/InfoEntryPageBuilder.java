package com.lefuorgn.viewloader.impl;

import android.content.Intent;
import android.support.v4.widget.Space;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.lefuorgn.lefu.multiMedia.MultiMediaActivity;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;
import com.lefuorgn.viewloader.ViewLoader;
import com.lefuorgn.viewloader.base.LabelViewBuilder;
import com.lefuorgn.viewloader.base.ViewBuilder;
import com.lefuorgn.viewloader.builder.ImageViewBuilder;
import com.lefuorgn.viewloader.config.Config;
import com.lefuorgn.viewloader.config.ViewBuilderFactory;
import com.lefuorgn.viewloader.interf.PageBuilder;
import com.lefuorgn.viewloader.util.BuilderUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.lefuorgn.viewloader.util.BuilderUtils.getArray;


/**
 * 页面条目构造器实现类
 */

public class InfoEntryPageBuilder implements PageBuilder {

    private ViewLoader mViewLoader;
    private Map<String, String> mResult; // 存放填写内容控件
    private Map<String, String> mPictureResult; // 存放图片
    private ScrollView mPageView; //
    private LinearLayout mParentView;

    private int mItemCounter; // 条目计数器
    private SparseArray<LabelViewBuilder> mBaseBuilders;

    private OnJumpPageListener mListener; // 页面跳转请求

    @Override
    public void setParams(ViewLoader viewLoader) {
        mViewLoader = viewLoader;
        mResult = new HashMap<String, String>();
        mPictureResult = new HashMap<String, String>();
    }

    @Override
    public void buildView() {
        mPageView = new ScrollView(mViewLoader.context());
        mParentView = new LinearLayout(mViewLoader.context());
        mParentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mParentView.setOrientation(LinearLayout.VERTICAL);
        mPageView.addView(mParentView);
        ViewBuilderFactory factory = new ViewBuilderFactory(mViewLoader);
        JSONArray jsonArray = getArray(mViewLoader.content());
        mBaseBuilders = new SparseArray<LabelViewBuilder>();
        mItemCounter = 0;
        buildItemView(jsonArray, factory);
        if(mParentView.getChildCount() > 0) {
            View view = mParentView.getChildAt(mParentView.getChildCount() - 1);
            if(view instanceof Space) {
                mParentView.removeView(view);
            }
        }
    }

    /**
     * 创建条目控件
     * @param jsonArray 条目JSON集合
     * @param factory View构建工厂类
     */
    private void buildItemView(JSONArray jsonArray, ViewBuilderFactory factory) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = BuilderUtils.getObject(jsonArray, i);
            // 判断当前控件类型
            String type = BuilderUtils.getString(jsonObject, "comType");
            if(Config.GRID_BUILDER.equals(type)) {
                // 网格控件
                JSONArray childJSONArray = BuilderUtils.getArray(jsonObject, "childrens");
                buildItemView(childJSONArray, factory);
            }else {
                final int index = mItemCounter;
                mItemCounter++;
                final ViewBuilder builder = factory.makeViewBuilder(type, index);
                if(builder == null) {
                    continue;
                }
                if(builder instanceof LabelViewBuilder) {
                    mBaseBuilders.append(index, (LabelViewBuilder) builder);
                }
                mParentView.addView(builder.buildView(jsonObject, mParentView));
                // 如果是图片加载器则添加点击事件
                if(builder instanceof ImageViewBuilder) {
                    ((ImageViewBuilder) builder).setOnItemClickListener(new ImageViewBuilder.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view) {
                            if(mListener != null) {
                                mListener.jumpPage(index, ((ImageViewBuilder) builder).getMaxNum());
                            }
                        }
                    });
                }
                mParentView.addView(getBlankLine());
            }
        }
    }

    /**
     * 获取分割线
     */
    private View getBlankLine() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, StringUtils.dip2px(mViewLoader.context(), 8));
        Space space = new Space(mViewLoader.context());
        space.setLayoutParams(params);
        return space;
    }

    @Override
    public View getPageView() {
        return mPageView;
    }

    /**
     * 获取填充条目的父控件
     */
    public ViewGroup getParentView() {
        return mParentView;
    }

    /**
     * 检验当前条目完成状态
     * @return true: 全部完成; false: 有未完成状态
     */
    public boolean isComplete() {
        boolean complete = true;
        boolean scroll = true;
        mResult.clear();
        mPictureResult.clear();
        for (int i = 0; i < mBaseBuilders.size(); i++) {
            TLog.error("KEY == " + mBaseBuilders.keyAt(i));
            TLog.error("VALUE == " + mBaseBuilders.valueAt(i));
            LabelViewBuilder builder = mBaseBuilders.valueAt(i);
            if(builder.isCompleted() && complete) {
                String value = builder.getValue();
                if(!StringUtils.isEmpty(value)) {
                    mResult.put(builder.getId(), builder.getValue());
                    if(builder instanceof ImageViewBuilder) {
                        mPictureResult.put(builder.getId(), builder.getValue());
                    }
                }
            }else {
                complete = false;
                if(scroll) {
                    scroll = false;
                    int[] location = new int[2];
                    mPageView.getLocationInWindow(location);
                    mPageView.smoothScrollBy(0, builder.getPosition() - location[1]);
                }

            }
        }
        return complete;
    }

    /**
     * 获取结果集合
     * @return 集合 key: ID; value: 内容
     */
    public Map<String, String> getResult() {
        return mResult;
    }

    /**
     * 获取图片控件
     */
    public Map<String, String> getPictureResult() {
        return mPictureResult;
    }

    /**
     * 设置页面跳转监听器
     * @param listener 监听器
     */
    public void setOnJumpPageListener(OnJumpPageListener listener) {
        this.mListener = listener;
    }

    /**
     * 页面操作成功回调函数
     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data 返回的数据
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LabelViewBuilder builder = mBaseBuilders.get(requestCode);
        if(builder == null) {
            return;
        }
        if(builder instanceof ImageViewBuilder) {

            if(resultCode == MultiMediaActivity.RESULT_IMAGE_CAPTURE && data != null) {
                // 从相机中获取的照片, 数量为1
                ((ImageViewBuilder) builder).addPicture(data.getStringExtra(MultiMediaActivity.RESULT_NAME), true);
            }else if(resultCode == MultiMediaActivity.RESULT_IMAGE_ALBUM && data != null) {
                // 从相册中选中的照片,数量为1~9张
                String images = data.getStringExtra(MultiMediaActivity.RESULT_NAME);
                String[] split = images.split(",");
                // 获取返回的结果
                for (String s : split) {
                    if(!StringUtils.isEmpty(s)) {
                        ((ImageViewBuilder) builder).addPicture(s, true);
                    }
                }
            }
        }
    }

    /**
     * 页面跳转请求监听
     */
    public interface OnJumpPageListener {

        /**
         * 跳转
         * @param requestCode 请求码
         */
        void jumpPage(int requestCode, int maxNum);

    }

}
