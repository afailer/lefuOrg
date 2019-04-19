package com.lefuorgn.viewloader.util;

import android.widget.TextView;

import com.lefuorgn.dialog.DatePickerDialog;
import com.lefuorgn.util.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 构建器工具类
 */

public class BuilderUtils {

    /**
     * 从JSONObject中获取列表数据
     */
    public static JSONArray getArray(String name) {
        try {
            return new JSONArray(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    /**
     * 从JSONObject中获取列表数据
     */
    public static JSONArray getArray(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getJSONArray(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    /**
     * 从JSONArray中获取JSONObject对象
     */
    public static JSONObject getObject(JSONArray jsonArray, int index) {
        try {
            return jsonArray.getJSONObject(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    /**
     * 从JSONObject中获取字符串类型数据
     */
    public static String getString(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getString(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 从JSONObject中获取布尔类型数据
     */
    public static boolean getBoolean(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getBoolean(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取开始时间和结束时间时间差
     */
    public static String getCalculation(TextView startView, TextView endView, String format) {
        String start = startView.getText().toString();
        String end = endView.getText().toString();
        if(StringUtils.isEmpty(start) || StringUtils.isEmpty(end)) {
            return "";
        }
        long result = StringUtils.getFormatData(end, format)
                - StringUtils.getFormatData(start, format);
        if(result <= 0) {
            return "";
        }
        long min = result / 60000;
        long hour = min / 60;
        long day = hour / 24;
        StringBuilder sb = new StringBuilder();
        if(day != 0) {
            sb.append(day).append("天");
        }
        if(hour % 24 != 0) {
            sb.append(hour % 24).append("小时");
        }
        if(min % 60 != 0) {
            sb.append(min % 60).append("分钟");
        }
        return sb.toString();
    }

    /**
     * 获取时间控件的显示方式
     */
    public static int getDisplay(String format) {
        if("date".equals(format)) {
            return DatePickerDialog.DATE;
        }else if("time".equals(format)) {
            return DatePickerDialog.TIME;
        }else {
            return DatePickerDialog.DATE_TIME;
        }
    }

    /**
     * 获取日期显示格式
     */
    public static String getFormat(String format) {
        if("date".equals(format)) {
            return StringUtils.FORMAT_DATE;
        }else if("time".equals(format)) {
            return StringUtils.FORMAT_TIME;
        }else {
            return StringUtils.FORMAT_DATE_TIME;
        }
    }

}
