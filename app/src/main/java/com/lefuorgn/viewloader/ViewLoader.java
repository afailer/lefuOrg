package com.lefuorgn.viewloader;

import android.content.Context;
import android.graphics.Color;

import com.lefuorgn.util.StringUtils;
import com.lefuorgn.viewloader.interf.PageBuilder;

/**
 * 视图加载器
 */

public class ViewLoader {

    private final Context context;
    private final String content; // 带解析的字符串内容
    private final int paddingWidth; // 当前控件左右内边距(单位DP)
    private final int paddingHeight; // 当前控件上下内边距(单位DP)
    private final float labelSize; // 标签文字大小(单位SP)
    private final int labelColor; // 标签文字颜色
    private final float valueSize; // 值文字大小(单位SP)
    private final int valueColor; // 值文字颜色
    private final float describeSize; // 描述文字大小(单位SP)
    private final int describeColor; // 描述文字颜色
    private final int describeBackground; // 描述
    private final float buttonSize; // 按钮文字大小(单位SP)
    private final float unitSize; // 单位文字大小(单位SP)
    private final int unitColor; // 单位文字颜色
    private final boolean copy; // 是否添加抄送控件
    private final boolean dynamic; // 是否显示动态添加按钮

    private ViewLoader(Builder builder) {
        if(builder.context == null) {
            throw new NullPointerException("the context is null. you must set the context value");
        }
        context = builder.context;
        content = builder.content;
        paddingWidth = StringUtils.dip2px(context, builder.paddingWidth);
        paddingHeight = StringUtils.dip2px(context, builder.paddingHeight);
        labelSize = builder.labelSize;
        labelColor = builder.labelColor;
        valueSize = builder.valueSize;
        valueColor = builder.valueColor;
        describeSize = builder.describeSize;
        describeColor = builder.describeColor;
        describeBackground = builder.describeBackground;
        buttonSize = builder.buttonSize;
        unitSize = builder.unitSize;
        unitColor = builder.unitColor;
        copy = builder.copy;
        dynamic = builder.dynamic;
    }

    public Context context() {
        return context;
    }

    public String content() {
        return content;
    }

    public int paddingWidth() {
        return paddingWidth;
    }

    public int paddingHeight() {
        return paddingHeight;
    }

    public float labelSize() {
        return labelSize;
    }

    public int labelColor() {
        return labelColor;
    }

    public float valueSize() {
        return valueSize;
    }

    public int valueColor() {
        return valueColor;
    }

    public float describeSize() {
        return describeSize;
    }

    public int describeColor() {
        return describeColor;
    }

    public int describeBackground() {
        return describeBackground;
    }

    public float buttonSize() {
        return buttonSize;
    }

    public float unitSize() {
        return unitSize;
    }

    public int unitColor() {
        return unitColor;
    }

    public boolean copy() {
        return copy;
    }

    public boolean dynamic() {
        return dynamic;
    }

    /**
     * 构建指定的页面构建类
     * @param pageBuilder 页面构建类
     */
    public void build(PageBuilder pageBuilder) {
        pageBuilder.setParams(this);
        pageBuilder.buildView();
    }

    public static final class Builder {

        Context context; // 环境上下文
        String content; // 带解析的字符串内容
        int paddingWidth; // 当前控件左右内边距(单位DP)
        int paddingHeight; // 当前控件上下内边距(单位DP)
        float labelSize; // 标签文字大小(单位SP)
        int labelColor; // 标签文字颜色
        float valueSize; // 值文字大小(单位SP)
        int valueColor; // 值文字颜色
        float describeSize; // 描述文字大小(单位SP)
        int describeColor; // 描述文字颜色
        int describeBackground; // 描述
        float buttonSize; // 按钮文字大小(单位SP)
        float unitSize; // 单位文字大小(单位SP)
        int unitColor; // 单位文字颜色
        boolean copy; // 是否存在抄送人控件
        boolean dynamic; // 是否显示动态添加按钮

        public Builder() {
            context = null;
            content = "[]";
            paddingWidth = 12;
            paddingHeight = 8;
            labelSize = 18;
            labelColor = Color.parseColor("#646D75");
            valueSize = 18;
            valueColor = Color.parseColor("#2C2C2C");
            describeSize = 14;
            describeColor = Color.parseColor("#808080");
            describeBackground = Color.parseColor("#EFEFEF");
            buttonSize = 14;
            unitSize = 14;
            unitColor = Color.parseColor("#E8E9EB");
            copy = false;
            dynamic = true;
        }

        public Builder(ViewLoader viewLoader) {
            this.context = viewLoader.context;
            this.content = viewLoader.content;
            this.paddingWidth = viewLoader.paddingWidth;
            this.paddingHeight = viewLoader.paddingHeight;
            this.labelSize = viewLoader.labelSize;
            this.labelColor = viewLoader.labelColor;
            this.valueSize = viewLoader.valueSize;
            this.valueColor = viewLoader.valueColor;
            this.describeSize = viewLoader.describeSize;
            this.describeColor = viewLoader.describeColor;
            this.describeBackground = viewLoader.describeBackground;
            this.buttonSize = viewLoader.buttonSize;
            this.unitSize = viewLoader.unitSize;
            this.unitColor = viewLoader.unitColor;
            this.copy = viewLoader.copy;
            this.dynamic = viewLoader.dynamic;
        }

        /**
         * 设置环境上下文
         */
        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        /**
         * 设置要展示的内容
         * @param content 格式是: {{[...],[...],[...]}
         */
        public Builder content(String content) {
            this.content = content;
            return this;
        }

        /**
         * 设置控件的左右内边距
         * @param paddingWidth 控件的左右内边距(单位是DP); 如果你设置的是15,内边距就是15DP
         */
        public Builder paddingWidth(int paddingWidth) {
            this.paddingWidth = paddingWidth;
            return this;
        }

        /**
         * 设置控件的上下内边距
         * @param paddingHeight 控件的上下内边距(单位是DP); 如果你设置的是15,内边距就是15DP
         */
        public Builder paddingHeight(int paddingHeight) {
            this.paddingHeight = paddingHeight;
            return this;
        }

        /**
         * 设置标签的大小
         * @param labelSize 标签文字大小(单位SP)
         */
        public Builder labelSize(float labelSize) {
            this.labelSize = labelSize;
            return this;
        }

        /**
         * 设置标签的颜色
         * @param labelColor 标签的颜色
         */
        public Builder labelColor(int labelColor) {
            this.labelColor = labelColor;
            return this;
        }

        /**
         * 设置值文字大小
         * @param valueSize 值文字大小(单位SP)
         */
        public Builder valueSize(float valueSize) {
            this.valueSize = valueSize;
            return this;
        }

        /**
         * 设置值文字颜色
         * @param valueColor 值文字颜色
         */
        public Builder valueColor(int valueColor) {
            this.valueColor = valueColor;
            return this;
        }

        /**
         * 设置描述文字大小
         * @param describeSize 描述文字大小(单位SP)
         */
        public Builder describeSize(float describeSize) {
            this.describeSize = describeSize;
            return this;
        }

        /**
         * 设置描述文字颜色
         * @param describeColor 描述文字颜色
         */
        public Builder describeColor(int describeColor) {
            this.describeColor = describeColor;
            return this;
        }

        /**
         * 设置描述文字背景颜色
         * @param describeBackground 描述文字背景颜色
         */
        public Builder describeBackground(int describeBackground) {
            this.describeBackground = describeBackground;
            return this;
        }

        /**
         * 设置按钮文字大小
         * @param buttonSize 按钮文字大小
         */
        public Builder buttonSize(float buttonSize) {
            this.buttonSize = buttonSize;
            return this;
        }

        /**
         * 设置单位文字大小
         * @param unitSize 单位文字大小(单位SP)
         */
        public Builder unitSize(float unitSize) {
            this.unitSize = unitSize;
            return this;
        }

        /**
         * 设置单位文字颜色
         * @param unitColor 单位文字颜色
         */
        public Builder unitColor(int unitColor) {
            this.unitColor = unitColor;
            return this;
        }

        /**
         * 是否添加抄送人控件
         */
        public Builder copy(boolean copy) {
            this.copy = copy;
            return this;
        }

        /**
         * 是否显示动态添加按钮
         */
        public Builder dynamic(boolean dynamic) {
            this.dynamic = dynamic;
            return this;
        }

        public ViewLoader build() {
            return new ViewLoader(this);
        }

    }

}
