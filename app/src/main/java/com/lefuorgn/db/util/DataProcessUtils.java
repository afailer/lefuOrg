package com.lefuorgn.db.util;

import android.graphics.Color;
import android.text.TextUtils;

import com.j256.ormlite.dao.ForeignCollection;
import com.lefuorgn.db.model.basic.SignConfig;
import com.lefuorgn.db.model.basic.SignIntervalColor;
import com.lefuorgn.lefu.bean.MultiMedia;
import com.lefuorgn.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库中数据操作工具公共类
 */

public class DataProcessUtils {

    /**
     * 数据库数据分页加载每页的条目数
     */
    static final int PAGE_SIZE = 10;

    /**
     * 获取保存内容显示的颜色
     * @param value 值
     * @param config 配置
     * @return 颜色值
     */
    public static int getColor(double value, SignConfig config) {
        ForeignCollection<SignIntervalColor> colors = config.getfColor();
        for (SignIntervalColor color : colors) {
            if(value >= color.getLow() && value <= color.getHigh()) {
                // 返回指定的值
                return Color.parseColor(color.getFontColor());
            }
        }
        // 没有配置,则返回默认的颜色值
        return Color.GRAY;
    }

    /**
     * 获取血压的值
     * @param high 高血压值
     * @param low 低血压值
     * @param config 配置
     * @return 颜色值
     */
    public static int getPressureColor(int high, int low, SignConfig config) {
        ForeignCollection<SignIntervalColor> colors = config.getfColor();

        SignIntervalColor highColor = null;
        // 获取高血压颜色
        for (SignIntervalColor color : colors) {
            if(high >= color.getLow() && high <= color.getHigh()) {
                highColor = color;
                break;
            }
        }
        // 获取低血压颜色
        SignIntervalColor lowColor = null;
        for (SignIntervalColor color : colors) {
            if(low >= color.getLow() && low <= color.getHigh()) {
                lowColor = color;
            }
        }
        if(highColor != null && lowColor != null) {
            // 比较高血压和低血压的显示级别
            if(highColor.getShowLevel() >= lowColor.getShowLevel()) {
                return Color.parseColor(highColor.getFontColor());
            }else {
                return Color.parseColor(lowColor.getFontColor());
            }
        }else if(highColor == null && lowColor != null) {
            // 显示低血压的值
            return Color.parseColor(lowColor.getFontColor());
        }else if(highColor != null) {
            // 显示高血压的值
            return Color.parseColor(highColor.getFontColor());
        }
        // 不存在配置,显示默认的颜色值
        return Color.GRAY;
    }

    /**
     * 获取饮食类型
     * @param type 饭餐类型
     * @return 1: 早餐; 2: 午餐; 3:晚餐; 0:其他,异常
     */
    public static int getMealType(String type) {
        if("早餐".equals(type)) {
            return 1;
        }else if("午餐".equals(type)) {
            return 2;
        }else if("晚餐".equals(type)) {
            return 3;
        }
        return 0;
    }

    /**
     * 获取食量类型
     * @param amount 食量类型
     * @return 1: 偏少; 2: 正常; 3:偏多; 0:其他,异常
     */
    public static int getMealAmount(String amount) {
        if("偏少".equals(amount)) {
            return 1;
        }else if("正常".equals(amount)) {
            return 2;
        }else if("偏多".equals(amount)) {
            return 3;
        }
        return 0;
    }

    /**
     * 获取饮食组合信息
     * @param meal_type // 饮食类型 1：早餐 2：午餐 3：晚餐
     * @param meal_amount // 饮食量   1：偏少 2：正常 3：偏多
     * @return 饮食状况
     */
    public static String getDrinkInfo(int meal_type, int meal_amount) {
        StringBuilder info = new StringBuilder();
        switch (meal_type) {
            case 1:
                info.append("早餐");
                break;
            case 2:
                info.append("午餐");
                break;
            case 3:
                info.append("晚餐");
                break;
            default:
                info.append("");
                break;
        }
        switch (meal_amount) {
            case 1:
                info.append("偏少");
                break;
            case 2:
                info.append("正常");
                break;
            case 3:
                info.append("偏多");
                break;
            default:
                info.append("");
                break;
        }
        return info.toString();
    }

    /**
     * 切割血压字符串,此字符串必须符合123/79格式,中间的分割符可以为/ , . ， 。 、 \ -中的任意一种
     * @param str 需要切割的字符串
     * @return 返回结果中0下标是低血压,1下标是高血压
     */
    public static String[] getBloodPressureArray(String str) {
        String[] string = str.split("[/,.，。、\\-]");
        String[] request = new String[2];
        request[0] = string[1];
        request[1] = string[0];
        return request;
    }

    /**
     * 获取血压的校验值
     * @return 血压的校验规则
     */
    public static String getPressureRegex() {
        return "^(\\d)+[/,.，。、\\-](\\d)+$";
    }

    /**
     * 解析按规则封装的媒体路径字符转为媒体数据集合类型
     * @param mediaStr 按规则封装的媒体路径字符串
     * @return 多媒体集合
     */
    static List<MultiMedia> getMultiMediaList(String mediaStr) {
        List<MultiMedia> result = new ArrayList<MultiMedia>();
        if(TextUtils.isEmpty(mediaStr)) {
            return result;
        }
        // 获取不同类型的媒体信息
        String[] mediaTypes = mediaStr.split("\\|");

        if(mediaTypes.length == 0) {
            return result;
        }
        for (String mediaTypeStr : mediaTypes) {
            if(mediaTypeStr.length() <= 2) {
                continue;
            }
            // 获取标记位
            String start = mediaTypeStr.substring(0, 2);
            // 媒体类型
            int type = 0;
            if("P:".equals(start)) {
                // 图片
                type = MultiMedia.MULTI_MEDIA_TYPE_PICTURE;
            }else if("V:".equals(start)) {
                // 视频
                type = MultiMedia.MULTI_MEDIA_TYPE_VIDEO;
            }else if("A:".equals(start)) {
                // 音频
                type = MultiMedia.MULTI_MEDIA_TYPE_AUDIO;
            }
            // 分割同类型媒体uri
            String[] paths = mediaTypeStr.substring(2, mediaTypeStr.length()).split(",");
            for (String path : paths) {
                MultiMedia multiMedia = new MultiMedia();
                multiMedia.setUri(path);
                multiMedia.setType(type);
                multiMedia.setSourceType(MultiMedia.SOURCE_FROM_LOCAL);
                result.add(multiMedia);
            }
        }
        return result;
    }

    /**
     * 获取多媒体信息
     * <p>信息规则如下:</p>
     * <u>
     *      <li>图片信息是以<Strong color="red">P:</Strong>开头的</li>
     *      <li>视频信息是以<Strong color="red">V:</Strong>开头的</li>
     *      <li>音频信息是以<Strong color="red">A:</Strong>开头的</li>
     *      <li>图片、视频和音频多个信息之间是通过<Strong color="red">,</Strong>链接的</li>
     *      <li>图片、视频和音频之间是通过<Strong color="red">|</Strong>链接的</li>
     * </u>
     *
     * @param multiMedia 多媒体集合
     * @return 一定格式的字符串
     */
    static String getMultiMediaInfo(List<MultiMedia> multiMedia, boolean isAbsolutePath) {
        if(multiMedia == null || multiMedia.size() == 0) {
            return "";
        }
        // 存放最终的信息
        StringBuilder sb = new StringBuilder();
        // 存放图片信息
        StringBuilder sbPicture = new StringBuilder();
        // 存放视频信息
        StringBuilder sbVideo = new StringBuilder();
        // 存放音频信息
        StringBuilder sbAudio = new StringBuilder();
        boolean picFirst = true;
        boolean videoFirst = true;
        boolean audioFirst = true;
        for (MultiMedia media : multiMedia) {
            String uri;
            if(isAbsolutePath) {
                // 绝对路径
                uri = media.getUri();
            }else {
                // 文件名称
                uri = StringUtils.isEmpty(media.getAlias()) ? new File(media.getUri()).getName() : media.getAlias();
            }
            if(media.getType() == MultiMedia.MULTI_MEDIA_TYPE_PICTURE) {
                // 图片数据类型
                if(picFirst) {
                    sbPicture.append("P:").append(uri);
                    picFirst = false;
                }else {
                    sbPicture.append(",").append(uri);
                }
            }else if(media.getType() == MultiMedia.MULTI_MEDIA_TYPE_VIDEO) {
                // 视频数据类型
                if(videoFirst) {
                    sbVideo.append("V:").append(uri);
                    videoFirst = false;
                }else {
                    sbVideo.append(",").append(uri);
                }
            }else if(media.getType() == MultiMedia.MULTI_MEDIA_TYPE_AUDIO) {
                // 视频数据类型
                if(audioFirst) {
                    sbAudio.append("A:").append(uri);
                    audioFirst = false;
                }else {
                    sbAudio.append(",").append(uri);
                }
            }
        }
        if(!StringUtils.isEmpty(sbPicture.toString())) {
            // 拥有图片
            sb.append(sbPicture.toString());
        }
        if(!StringUtils.isEmpty(sbVideo.toString())) {
            // 拥有视频
            if(!StringUtils.isEmpty(sb.toString())) {
                sb.append("|").append(sbVideo.toString());
            }else {
                sb.append(sbVideo.toString());
            }
        }
        if(!StringUtils.isEmpty(sbAudio.toString())) {
            // 拥有音频
            if(!StringUtils.isEmpty(sb.toString())) {
                sb.append("|").append(sbAudio.toString());
            }else {
                sb.append(sbAudio.toString());
            }
        }
        return sb.toString();
    }

}
