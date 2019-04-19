package com.lefuorgn.viewloader.bean;

/**
 * 下拉菜单数据
 */

public class SpinnerData {

    private String label;
    private String value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SpinnerData{" +
                "label='" + label + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
