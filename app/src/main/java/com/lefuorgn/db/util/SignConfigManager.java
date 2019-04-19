package com.lefuorgn.db.util;

import com.lefuorgn.db.base.BaseDao;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.model.basic.SignConfig;

import java.util.ArrayList;
import java.util.List;


/**
 * 血压、血糖、呼吸、排便、饮水、心率和体温数据颜色配置管理类
 * 其中还包括服务器指定显示体征数据和护理数据的字段
 */
public class SignConfigManager {
	
	/**
	 * 血压
	 */
	public static final int SIGN_BLOOD_PRESSURE = 0x1;
	/**
	 * 血糖
	 */
	public static final int SIGN_BLOOD_SUGAR = 0x2;
	/**
	 * 呼吸
	 */
	public static final int SIGN_BREATHING = 0x3;
	/**
	 * 排便
	 */
	public static final int SIGN_DEFECATION = 0x4;
	/**
	 * 饮水
	 */
	public static final int SIGN_DRINK = 0x5;
	/**
	 * 心率
	 */
	public static final int SIGN_PULSE = 0x6;
	/**
	 * 体温
	 */
	public static final int SIGN_TEMPERATURE = 0x7;

    /**
     * 获取所有的配置文件
     * @return 所有配置文件的列表集合
     */
    public static List<SignConfig> getAllSignConfig() {
		BaseDao<SignConfig, Long> dao =DaoHelper
				.getInstance().getDao(SignConfig.class);
        List<SignConfig> configs = dao.queryAll("_id", true);
        if(configs == null) {
            configs = new ArrayList<SignConfig>();
        }
        return configs;
    }

	/**
	 * 获取配置文件
     */
	static SignConfig getSignConfig(int type) {
		BaseDao<SignConfig, Long> dao =DaoHelper
				.getInstance().getDao(SignConfig.class);
		return dao.query("type", type);
	}

	/**
	 * 获取配置文件
	 */
	public static SignConfig getSignConfig(String type) {
        if("血糖".equals(type)) {
            return getSignConfig(SignConfigManager.SIGN_BLOOD_SUGAR);
        }else if("体温".equals(type)) {
            return getSignConfig(SignConfigManager.SIGN_TEMPERATURE);
        }else if("呼吸".equals(type)) {
            return getSignConfig(SignConfigManager.SIGN_BREATHING);
        }else if("血压".equals(type)) {
            return getSignConfig(SignConfigManager.SIGN_BLOOD_PRESSURE);
        }else if("心率".equals(type)) {
            return getSignConfig(SignConfigManager.SIGN_PULSE);
        }else if("排便".equals(type)) {
            return getSignConfig(SignConfigManager.SIGN_DEFECATION);
        }else if("饮水".equals(type)) {
            return getSignConfig(SignConfigManager.SIGN_DRINK);
        }
        return new SignConfig();
	}

    /**
     * 通过类型值获取类型名称
     * @param type 类型值
     * @return 类型名称
     */
    public static String getTitle(int type) {
        String title;
        switch (type) {
            case SIGN_BLOOD_PRESSURE:
                title = "血压";
                break;
            case SIGN_BLOOD_SUGAR:
                title = "血糖";
                break;
            case SIGN_BREATHING:
                title = "呼吸";
                break;
            case SIGN_DEFECATION:
                title = "排便";
                break;
            case SIGN_DRINK:
                title = "饮水";
                break;
            case SIGN_PULSE:
                title = "心率";
                break;
            case SIGN_TEMPERATURE:
                title = "体温";
                break;
            default:
                title = "";
        }
        return title;
    }

    /**
     * 获取体征信息或者护理信息配置项
     * @param sign true : 体征信息条目; false : 护理信息条目
     * @return 指定类型的配置项
     */
    public static List<DisplaySignOrNursingItem> getSignOrNursingItem(boolean sign) {
        if(sign) {
            return getSignItem();
        }else {
            return getNursingItem();
        }
    }

	/**
	 * 获取体征项条目
	 * @return 服务器端配置的护理项
     */
	public static List<DisplaySignOrNursingItem> getSignItem() {
        BaseDao<DisplaySignOrNursingItem, Long> dao = DaoHelper
                .getInstance().getDao(DisplaySignOrNursingItem.class);
		List<DisplaySignOrNursingItem> item = dao.queryList("isSign", true);
		return item != null ? item : new ArrayList<DisplaySignOrNursingItem>();
	}

	/**
	 * 获取护理项条目
	 * @return 服务器端配置的护理项
     */
	public static List<DisplaySignOrNursingItem> getNursingItem() {
        BaseDao<DisplaySignOrNursingItem, Long> dao = DaoHelper
                .getInstance().getDao(DisplaySignOrNursingItem.class);
		List<DisplaySignOrNursingItem> item = dao.queryList("isSign", false);
		return item != null ? item : new ArrayList<DisplaySignOrNursingItem>();
	}

    /**
     * 根据护理项ID获取护理项名称
     * @param nursingId 护理项ID
     * @return 护理项名称
     */
	public static String getNursingName(long nursingId) {
        BaseDao<DisplaySignOrNursingItem, Long> dao = DaoHelper
                .getInstance().getDao(DisplaySignOrNursingItem.class);
        DisplaySignOrNursingItem item = dao.query("type", nursingId);
        if(item != null) {
            return item.getTitle();
        }
        return "";
    }
	
}
