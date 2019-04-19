package com.lefuorgn.util;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.baidu.mapapi.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 字符串工具类
 */

public class StringUtils {

    /**
     * 日期默认显示格式
     */
    public static final String FORMAT = "yyyy-MM-dd HH:mm";
    /**
     * OA审批 日期显示格式
     */
    public static final String FORMAT_DATE = "yyyy/MM/dd";
    /**
     * OA审批 时间显示格式
     */
    public static final String FORMAT_TIME = "HH:mm:ss";
    /**
     * OA审批 日期和时间显示格式
     */
    public static final String FORMAT_DATE_TIME = "yyyy/MM/dd HH:mm:ss";

    private final static String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 字符串转长整数
     *
     * @param str 当前字符串
     * @param defValue 默认值
     * @return 转换后长整型
     */
    public static long toLong(String str, long defValue) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            TLog.error(e.toString());
        }
        return defValue;
    }

    /**
     * 字符串转浮点类型
     *
     * @param str 当前字符串
     * @param defValue 默认值
     * @return 转换后长整型
     */
    public static float toFloat(String str, float defValue) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            TLog.error(e.toString());
        }
        return defValue;
    }

    /**
     * 字符串转整数
     *
     * @param str 当前字符串
     * @param defValue 默认值
     * @return 转换后整型
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            TLog.error(e.toString());
        }
        return defValue;
    }

    /**
     * 字符串转布尔类型
     *
     * @param str 当前字符串
     * @param defValue 默认值
     * @return 转换后布尔类型值
     */
    public static boolean toBoolean(String str, boolean defValue) {
        try {
            return Boolean.parseBoolean(str);
        } catch (Exception e) {
            TLog.error(e.toString());
        }
        return defValue;
    }

    /**
     * 转换字符串, 如果str不为空则返回str, 如果为空则返回空字符串"";
     * @param str 当前字符串
     * @return 返回原字符串或者""
     */
    public static String toString(String str) {
        if(str == null) {
            str = "";
        }
        return str;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串
     *
     * @param input 待校验的字符串
     * @return boolean 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 将long类型的数据转换成指定格式的时间
     * @param time 时间
     * @param format 格式
     * @return 时间指定格式的字符串类型
     */
    @SuppressLint("SimpleDateFormat")
    public static String getFormatData(long time, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date(time));
    }

    /**
     * 将long类型的数据转换成指定格式的时间
     * @param time 时间
     * @param format 格式
     * @return 时间指定格式的字符串类型
     */
    @SuppressLint("SimpleDateFormat")
    public static long getFormatData(String time, String format) {
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(new SimpleDateFormat(format).parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.getTimeInMillis();
    }

    /**
     * 获取指定时间所属的星期
     * @param time 指定的时间
     * @return 指定时间所属的星期; 星期日
     */
    public static String getWeekOfTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if(w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 已友好的方式显示时间
     * 如: 1分钟前...
     *     2小时前...
     *     昨天
     *     前天
     *     1个月前...等
     *     超过三个月的直接显示日期
     * @param time 需转换的时间
     * @return 友好的时间字符串
     */
    public static String getFriendlyTime(long time) {
        if(time == 0) {
            return "";
        }
        String result;
        Calendar calendar = Calendar.getInstance();
        long lt = time / 86400000;
        long ct = calendar.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((calendar.getTimeInMillis() - time) / 3600000);
            if (hour == 0) {
                result = Math.max((calendar.getTimeInMillis() - time) / 60000, 1) + "分钟前";
            }
            else {
                result = hour + "小时前";
            }
        } else if (days == 1) {
            result = "昨天";
        } else if (days == 2) {
            result = "前天 ";
        } else if (days > 2 && days < 31) {
            result = days + "天前";
        } else if (days >= 31 && days <= 2 * 31) {
            result = "一个月前";
        } else if (days > 2 * 31 && days <= 3 * 31) {
            result = "2个月前";
        } else if (days > 3 * 31 && days <= 4 * 31) {
            result = "3个月前";
        } else {
            result = getFormatData(time, "yyyy-MM-dd hh:mm");
        }
        return result;
    }

    /**
     * 获取当前日期前N天的时间
     * @param n 前N天; n > 0
     * @return 返回前N天的long类型时间(前N天最小日期); 如果n<=0; 则返回当天最小日期
     */
    public static long getFirstNDays(int n) {
        if(n <= 0) {
            n = 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - n);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 获取当前日期前N个月的时间
     * @param n 前N个月; n > 0
     * @return 返回前N个月的long类型时间(前N天最小日期); 如果n<=0; 则返回当天最小日期
     */
    public static long getFirstNMonths(int n) {
        if(n <= 0) {
            n = 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 1 - n);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 获取当前日期的前一天
     * @param time 指定的时间
     * @return 当前时间的前一天时间
     */
    public static long getTimeBeforeTheCurrentDate(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
        return calendar.getTime().getTime();
    }

    /**
     * 获取当前日期的后一天
     * @param time 指定的时间
     * @return 当前时间的后一天时间
     */
    public static long getTimeAfterTheCurrentDate(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
        return calendar.getTime().getTime();
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param time 指定的时间
     * @return boolean true: 为今日时间; false: 不是今日时间
     */
    public static boolean isToday(long time) {
        Calendar calendar = Calendar.getInstance();
        String timeStr = getFormatData(time, "yyyy-MM-dd");
        String currentTimeStr = getFormatData(calendar.getTime().getTime(), "yyyy-MM-dd");
        return currentTimeStr.equals(timeStr);
    }

    /**
     * 获取当前时间所在某天中的最小值和最大值
     * @param time 当前时间
     * @return 数组, 0 : 当天最小值; 1: 当天最大值
     */
    public static long[] getSectionTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date start = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);

        Date end = calendar.getTime();
        return new long[]{start.getTime(), end.getTime()};
    }

    /**
     * 获取当前时间所在某天中的最小值和最大值
     * @param time 当前时间
     * @param format 当前时间格式
     * @return 数组, 0 : 当天最小值; 1: 当天最大值
     */
    @SuppressLint("SimpleDateFormat")
    public static long[] getSectionTime(String time, String format) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat(format).parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date start = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);

        Date end = calendar.getTime();
        return new long[]{start.getTime(), end.getTime()};
    }

    /**
     * 获取指定数据的格式化数据
     */
    public static String numberFormat(int num, double value) {
        // 获取要格式化数据小数位的位数
        return String.format("%." + num + "f", value);
    }

    /**
     * 将文本复制到剪切板上
     * @param context 环境上下文
     * @param text 复制到剪切板上的内容
     */
    public static void copyToShearPlate(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text", text);
        clipboard.setPrimaryClip(clip);
    }

    /**
     * 根据id随机生成邀请码
     * @param id 老人ID
     * @return 一组数据
     */
    public static String randomBuildNumber(long id) {
        // num为一个基数
        long num = 6953769;
        // 用id与基数相乘,加11213尽量避免连续0的出现
        String s = id * num + 11213 + "" ;
        return id + s.substring(s.length() - 5, s.length());
    }

    /**
     * 将坐标字符串转换成坐标点集合
     * @param coordinateStr 指定格式的字符串; "116.3199,39.945255;116.386015,39.932863"
     * @return 坐标点集合
     */
    public static List<LatLng> getLatLngList(String coordinateStr) {
        List<LatLng> result = new ArrayList<LatLng>();
        if(isEmpty(coordinateStr)) {
            return  result;
        }
        String[] points = coordinateStr.split(";");
        for (String point : points) {
            String[] coordinate = point.split(",");
            if(coordinate.length == 2) {
                double longitude;
                double latitude;
                try {
                    longitude = Double.parseDouble(coordinate[0]);
                    latitude = Double.parseDouble(coordinate[1]);
                }catch (Exception e) {
                    TLog.error(e.toString());
                    continue;
                }
                result.add(new LatLng(latitude, longitude));
            }
        }
        return result;
    }

    /**
     * 获取区域中间点坐标
     * @param latLngs 区域点集合
     * @return 区域中间点坐标
     */
    public static LatLng getCenterLatLng(List<LatLng> latLngs) {
        if(latLngs == null || latLngs.size() == 0) {
            return null;
        }
        double longitudes = 0;
        double latitudes = 0;
        for (LatLng latLng : latLngs) {
            longitudes += latLng.longitude;
            latitudes += latLng.latitude;
        }
        return new LatLng(latitudes / latLngs.size(), longitudes / latLngs.size());
    }

}
