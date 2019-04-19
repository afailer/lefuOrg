package com.lefuorgn.bean;

import com.lefuorgn.R;
import com.lefuorgn.gov.fragment.AnnouncementFragment;
import com.lefuorgn.gov.fragment.LefuFragment;
import com.lefuorgn.gov.fragment.MapFragment;

/**
 * Created by liuting on 2016/12/19.
 */

public enum GovMainTab {

    GovHomePage(0,R.string.gov_lefu_page, R.drawable.tab_gov_lefu_page,
                LefuFragment.class),
    GovMapPage(1,R.string.gov_map_page,R.drawable.tab_gov_map_page
                , MapFragment.class),
    GovCircularPage(2,R.string.gov_circular_page,R.drawable.tab_gov_circular_page, AnnouncementFragment.class),
    GovSettingPage(3,R.string.gov_setting_page,R.drawable.tab_gov_setting_page, com.lefuorgn.gov.fragment.SettingFragment.class);


    private int idx;
    private int resName;
    private int resIcon;
    private Class<?> clz;

    private GovMainTab(int idx, int resName, int resIcon, Class<?> clz) {
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
