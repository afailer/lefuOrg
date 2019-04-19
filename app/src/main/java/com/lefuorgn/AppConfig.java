package com.lefuorgn;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * 应用程序配置类, 用于保存用户相关信息及设置
 */

public class AppConfig {

    /**
     * 开始同步, 发送广播标记
     */
    public final static String INTENT_ACTION_NOTICE_START = "intent_action_notice_start";
    /**
     * 所有数据同步完成, 发送广播标记
     */
    public final static String INTENT_ACTION_NOTICE_END = "intent_action_notice_end";
    /**
     * 配置文件同步完成, 发送广播标记
     */
    public final static String INTENT_ACTION_NOTICE_CONFIG = "intent_action_notice_config";
    /**
     * 所有下载表(不包括老人数据)同步完成, 发送广播标记
     */
    public final static String INTENT_ACTION_NOTICE_DATA = "intent_action_notice_data";
    /**
     * 老人相关下载表同步完成, 发送广播标记
     */
    public final static String INTENT_ACTION_NOTICE_ELDERLY = "intent_action_notice_elderly";
    /**
     * 用户登录成功广播
     */
    public final static String INTENT_ACTION_NOTICE_LOGIN = "intent_action_notice_login";
    /**
     * 用户查看通知, 首页面打开完成广播
     */
    public final static String INTENT_ACTION_NOTICE_MAIN_PAGE_OPEN = "intent_action_notice_main_page_open";

    private final static String APP_CONFIG = "config";

    /**
     * 默认存放图片的路径
     */
    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "lefuOrg"
            + File.separator + "lefu_img";

    /**
     * 默认存放视频的路径
     */
    public final static String DEFAULT_SAVE_VIDEO_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "lefuOrg"
            + File.separator + "lefu_video";

    /**
     * 默认存放音频的路径
     */
    public final static String DEFAULT_SAVE_AUDIO_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "lefuOrg"
            + File.separator + "lefu_audio";

    /**
     * 默认存放文件下载的路径
     */
    public final static String DEFAULT_SAVE_FILE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "lefuOrg"
            + File.separator + "download";

    /**
     * 同步配置文件的时间区间
     */
    public final static long SYNC_CONFIG_TIME = 14400000;

    private static AppConfig appConfig;

    static AppConfig getAppConfig() {
        if(appConfig == null) {
            synchronized (AppConfig.class) {
                if(appConfig == null) {
                    appConfig = new AppConfig();
                }
            }
        }
        return appConfig;
    }

    /**
     * 获取键对应的值
     * @param key 键
     * @return 与对应的值
     */
    public String get(String key) {
        Properties props = get();
        return (props != null) ? props.getProperty(key) : null;
    }

    /**
     * 将Properties对象保存到文件中
     * @param ps 配置对象
     */
    public void set(Properties ps) {
        Properties props = get();
        props.putAll(ps);
        setProps(props);
    }

    /**
     * 将键值对保存到配置文件中
     * @param key 键
     * @param value 值
     */
    public void set(String key, String value) {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    /**
     * 移除指定下的键的值
     * @param key 键组合
     */
    public void remove(String... key) {
        Properties props = get();
        for (String k : key)
            props.remove(k);
        setProps(props);
    }

    public Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();
       try {
            File dirConf = AppContext.getInstance().getDir(APP_CONFIG, Context.MODE_PRIVATE);
            File file = new File(dirConf, APP_CONFIG);
            fis = new FileInputStream(file);

            props.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return props;
    }

    /**
     * 保存配置文件
     * @param p Properties对象
     */
    private void setProps(Properties p) {
        FileOutputStream fos = null;
        try {
            File dirConf = AppContext.getInstance().getDir(APP_CONFIG, Context.MODE_PRIVATE);
            File conf = new File(dirConf, APP_CONFIG);
            fos = new FileOutputStream(conf);

            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
