package com.lefuorgn.viewloader.config;

import com.lefuorgn.viewloader.ViewLoader;
import com.lefuorgn.viewloader.base.ViewBuilder;
import com.lefuorgn.viewloader.builder.CheckBoxBuilder;
import com.lefuorgn.viewloader.builder.CommentViewBuilder;
import com.lefuorgn.viewloader.builder.DateRangeViewBuilder;
import com.lefuorgn.viewloader.builder.DateViewBuilder;
import com.lefuorgn.viewloader.builder.ImageViewBuilder;
import com.lefuorgn.viewloader.builder.LineViewBuilder;
import com.lefuorgn.viewloader.builder.MoneyViewBuilder;
import com.lefuorgn.viewloader.builder.NumberViewBuilder;
import com.lefuorgn.viewloader.builder.RadioViewBuilder;
import com.lefuorgn.viewloader.builder.SelectViewBuilder;
import com.lefuorgn.viewloader.builder.TextAreaViewBuilder;
import com.lefuorgn.viewloader.builder.TextViewBuilder;

/**
 * 视图构建器工厂类
 */

public class ViewBuilderFactory {

    private ViewLoader mLoader;

    public ViewBuilderFactory(ViewLoader viewLoader) {
        mLoader = viewLoader;
    }

    public ViewBuilder makeViewBuilder(String type, int position) {
        if(Config.TEXT_BUILDER.equals(type)) {
            // 单行文本视图构建器
            return new TextViewBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(),
                    mLoader.labelSize(), mLoader.labelColor(),
                    mLoader.valueSize(), mLoader.valueColor(),
                    mLoader.describeSize(), mLoader.describeColor(), mLoader.describeBackground());
        }else if(Config.DATE_BUILDER.equals(type)) {
            // 日期视图构建器
            return new DateViewBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(),
                    mLoader.labelSize(), mLoader.labelColor(),
                    mLoader.valueSize(), mLoader.valueColor(),
                    mLoader.describeSize(), mLoader.describeColor(), mLoader.describeBackground(),
                    mLoader.buttonSize());
        }else if(Config.TEXT_AREA_BUILDER.equals(type)) {
            // 多行文本视图构建器
            return new TextAreaViewBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(),
                    mLoader.labelSize(), mLoader.labelColor(),
                    mLoader.valueSize(), mLoader.valueColor(),
                    mLoader.describeSize(), mLoader.describeColor(), mLoader.describeBackground());
        }else if(Config.SELECT_BUILDER.equals(type)) {
            // 下拉菜单视图构建器
            return new SelectViewBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(),
                    mLoader.labelSize(), mLoader.labelColor(),
                    mLoader.valueSize(), mLoader.valueColor(),
                    mLoader.describeSize(), mLoader.describeColor(), mLoader.describeBackground());
        }else if(Config.RADIO_BUILDER.equals(type)) {
            // 单选框视图构建器
           return new RadioViewBuilder(mLoader.context(),
                   mLoader.paddingWidth(), mLoader.paddingHeight(),
                   mLoader.labelSize(), mLoader.labelColor(),
                   mLoader.valueSize(), mLoader.valueColor(),
                   mLoader.describeSize(), mLoader.describeColor(), mLoader.describeBackground(),
                   position);
        }else if(Config.CHECKBOX_BUILDER.equals(type)) {
            // 复选框视图构建器
            return new CheckBoxBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(),
                    mLoader.labelSize(), mLoader.labelColor(),
                    mLoader.valueSize(), mLoader.valueColor(),
                    mLoader.describeSize(), mLoader.describeColor(), mLoader.describeBackground());
        }else if(Config.MONEY_BUILDER.equals(type)) {
            // 金额视图构建器
            return new MoneyViewBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(),
                    mLoader.labelSize(), mLoader.labelColor(),
                    mLoader.valueSize(), mLoader.valueColor(),
                    mLoader.describeSize(), mLoader.describeColor(), mLoader.describeBackground(),
                    mLoader.unitSize(), mLoader.unitColor());
        }else if(Config.NUMBER_BUILDER.equals(type)) {
            // 数字输入框视图构建器
            return new NumberViewBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(),
                    mLoader.labelSize(), mLoader.labelColor(),
                    mLoader.valueSize(), mLoader.valueColor(),
                    mLoader.describeSize(), mLoader.describeColor(), mLoader.describeBackground());
        }else if(Config.COMMENT_BUILDER.equals(type)) {
            // 描述文字视图构建器
            return new CommentViewBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(), mLoader.valueSize());
        }else if(Config.DATE_RANGE_BUILDER.equals(type)) {
            // 日期区间视图构建器
            return new DateRangeViewBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(),
                    mLoader.labelSize(), mLoader.labelColor(),
                    mLoader.valueSize(), mLoader.valueColor(),
                    mLoader.describeSize(), mLoader.describeColor(), mLoader.describeBackground(),
                    mLoader.buttonSize());
        }else if(Config.IMAGE_BUILDER.equals(type)) {
            // 图片视图构建器
            return new ImageViewBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(),
                    mLoader.labelSize(), mLoader.labelColor(),
                    mLoader.valueSize(), mLoader.valueColor(),
                    mLoader.describeSize(), mLoader.describeColor(), mLoader.describeBackground());
        }else if(Config.LINE_BUILDER.equals(type)) {
            // 分割线视图构建器
            return new LineViewBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight());
        }
        return null;
    }

}
