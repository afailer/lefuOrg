package com.lefuorgn.bean;

import com.lefuorgn.R;
import com.lefuorgn.lefu.fragment.ElderlyFragment;
import com.lefuorgn.lefu.fragment.HomePageFragment;
import com.lefuorgn.lefu.fragment.SettingFragment;

/**
 * 主页页面枚举类(一级页面)
 */

public enum MainFirstPageTab {

    HOMEPAGE(0, R.string.main_tab_name_home_page, R.drawable.tab_icon_home_page,
            HomePageFragment.class),
    ALLELDERLY(1, R.string.main_tab_name_all_elderly, R.drawable.tab_icon_all_elderly,
            ElderlyFragment.class),
    SETTING(2, R.string.main_tab_name_setting, R.drawable.tab_icon_setting,
            SettingFragment.class);

    private int idx;
    private int resName;
    private int resIcon;
    private Class<?> clz;

    MainFirstPageTab(int idx, int resName, int resIcon, Class<?> clz) {
        this.idx = idx;
        this.resName = resName;
        this.resIcon = resIcon;
        this.clz = clz;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getResName() {
        return resName;
    }

    public void setResName(int resName) {
        this.resName = resName;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }
}
