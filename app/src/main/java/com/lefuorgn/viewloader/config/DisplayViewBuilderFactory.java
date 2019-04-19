package com.lefuorgn.viewloader.config;

import com.lefuorgn.viewloader.ViewLoader;
import com.lefuorgn.viewloader.base.ViewBuilder;
import com.lefuorgn.viewloader.builder.CommentViewBuilder;
import com.lefuorgn.viewloader.builder.LineViewBuilder;
import com.lefuorgn.viewloader.display.CheckBoxDisplayBuilder;
import com.lefuorgn.viewloader.display.DateRangeDisplayBuilder;
import com.lefuorgn.viewloader.display.ImageDisplayBuilder;
import com.lefuorgn.viewloader.display.MoneyDisplayBuilder;
import com.lefuorgn.viewloader.display.RadioDisplayBuilder;
import com.lefuorgn.viewloader.display.TextAreaDisplayBuilder;
import com.lefuorgn.viewloader.display.TextDisplayBuilder;

/**
 * 展示视图构建器工厂类
 */

public class DisplayViewBuilderFactory {

    private ViewLoader mLoader;

    public DisplayViewBuilderFactory(ViewLoader viewLoader) {
        mLoader = viewLoader;
    }

    public ViewBuilder makeViewBuilder(String type) {
        if(Config.TEXT_BUILDER.equals(type)) {
            // 单行文本视图构建器
            return new TextDisplayBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(),
                    mLoader.labelSize(), mLoader.labelColor(),
                    mLoader.valueSize(), mLoader.valueColor());
        }else if(Config.DATE_BUILDER.equals(type)) {
            // 日期视图构建器
            return new TextDisplayBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(),
                    mLoader.labelSize(), mLoader.labelColor(),
                    mLoader.valueSize(), mLoader.valueColor());
        }else if(Config.TEXT_AREA_BUILDER.equals(type)) {
            // 多行文本视图构建器
            return new TextAreaDisplayBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(),
                    mLoader.labelSize(), mLoader.labelColor(),
                    mLoader.valueSize(), mLoader.valueColor());
        }else if(Config.SELECT_BUILDER.equals(type)) {
            // 下拉菜单视图构建器
            return new RadioDisplayBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(),
                    mLoader.labelSize(), mLoader.labelColor(),
                    mLoader.valueSize(), mLoader.valueColor());
        }else if(Config.RADIO_BUILDER.equals(type)) {
            // 单选框视图构建器
           return new RadioDisplayBuilder(mLoader.context(),
                   mLoader.paddingWidth(), mLoader.paddingHeight(),
                   mLoader.labelSize(), mLoader.labelColor(),
                   mLoader.valueSize(), mLoader.valueColor());
        }else if(Config.CHECKBOX_BUILDER.equals(type)) {
            // 复选框视图构建器
            return new CheckBoxDisplayBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(),
                    mLoader.labelSize(), mLoader.labelColor(),
                    mLoader.valueSize(), mLoader.valueColor());
        }else if(Config.MONEY_BUILDER.equals(type)) {
            // 金额视图构建器
            return new MoneyDisplayBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(),
                    mLoader.labelSize(), mLoader.labelColor(),
                    mLoader.valueSize(), mLoader.valueColor(),
                    mLoader.unitSize(), mLoader.unitColor());
        }else if(Config.NUMBER_BUILDER.equals(type)) {
            // 数字输入框视图构建器
            return new TextDisplayBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(),
                    mLoader.labelSize(), mLoader.labelColor(),
                    mLoader.valueSize(), mLoader.valueColor());
        }else if(Config.COMMENT_BUILDER.equals(type)) {
            // 描述文字视图构建器
            return new CommentViewBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(), mLoader.valueSize());
        }else if(Config.DATE_RANGE_BUILDER.equals(type)) {
            // 日期区间视图构建器
            return new DateRangeDisplayBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(),
                    mLoader.labelSize(), mLoader.labelColor(),
                    mLoader.valueSize(), mLoader.valueColor());
        }else if(Config.IMAGE_BUILDER.equals(type)) {
            // 图片视图构建器
            return new ImageDisplayBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight(),
                    mLoader.labelSize(), mLoader.labelColor(),
                    mLoader.valueSize(), mLoader.valueColor());
        }else if(Config.LINE_BUILDER.equals(type)) {
            // 分割线视图构建器
            return new LineViewBuilder(mLoader.context(),
                    mLoader.paddingWidth(), mLoader.paddingHeight());
        }
        return null;
    }

}
