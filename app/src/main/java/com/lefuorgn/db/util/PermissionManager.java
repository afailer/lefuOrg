package com.lefuorgn.db.util;

import com.lefuorgn.db.base.BaseDao;
import com.lefuorgn.db.model.basic.Permission;

/**
 * 权限管理类
 */

public class PermissionManager {

    /**
     * 显示权限
     */
    public static final String P_V = "_V";
    /**
     * 增加权限
     */
    public static final String P_C = "_C";
    /**
     * 更新权限
     */
    public static final String P_U = "_U";
    /**
     * 删除权限
     */
    public static final String P_D = "_D";

    /**
     * 交班记录操作权限
     */
    public static final String LOGBOOK = "handOver";
    /**
     * 健康信息操作权限
     */
    public static final String HEALTH = "health";
    /**
     * 院内评估权限
     */
    public static final String EXAM_ANSWER = "examAnswer";
    /**
     * 老人信息操作权限
     */
    public static final String OLDPEOPLE = "oldPeople";
    /**
     * 老人家属信息操作权限
     */
    public static final String FAMILY = "family";
    /**
     * 护理录入、批量录入
     */
    public static final String DAILY = "daily";
    /**
     * 随手拍
     */
    public static final String LIFE = "life";
    /**
     * 个人数据
     */
    public static final String SINGN_DATA_ENTRY = "singndataEntry";
    /**
     * 老人外出
     */
    public static final String LEAVE_OUT = "leaveOut";
    /**
     * 生日提醒
     */
    public static final String BIRTHDAY = "birthday";
    /**
     * 数据审核
     */
    public static final String VERIFY = "verify";
    /**
     * 今日工作
     */
    public static final String NURSING_TASK = "nursingTask";
    /**
     * 联系方式
     */
    public static final String CONTACTS = "contacts";

    /**
     * 血压(体征录入)
     */
    private static final String BLOOD_PRESSURE = "bpr";
    /**
     * 心率(体征录入)(查看权限字段)
     */
    private static final String HEART_RATE_PR = "pr";
    /**
     * 心率(体征录入)pr
     */
    private static final String HEART_RATE = "pulse";
    /**
     * 血糖(体征录入)
     */
    private static final String BLOOD_SUGAR = "bsr";
    /**
     * 体温(体征录入)
     */
    private static final String TEMPERATURE = "tr";
    /**
     * 呼吸(体征录入)
     */
    private static final String BREATHING = "breathing";
    /**
     * 饮水(体征录入)
     */
    private static final String DRINKING_WATER = "drinkwater";
    /**
     * 排便(体征录入)
     */
    private static final String DEFECATION = "defecation";
    /**
     * 睡眠(体征录入)
     */
    private static final String SLEEP = "sleep";
    /**
     * 进食(体征录入)
     */
    private static final String MEAL = "meal";
    /**
     * 我的排班
     */
    public static final String OA_STAFFPLAN = "oa_staffPlan";
    /**
     * 请假申请
     */
    public static final String OA_ASKFORLEAVE = "oa_askForLeave";
    /**
     * 请假申请撤销权限
     */
    public static final String OA_ASKFORLEAVEBACK = "oa_askForLeaveBack";
    /**
     * 考勤记录
     */
    public static final String OA_ATTENDANCEREPORTING = "oa_attendanceReporting";
    /**
     * 审批
     */
    public static final String OA_VERIFYASK = "oa_verifyAsk";
    /**
     * 审批添加权限
     */
    public static final String OA_SAVEASK = "oa_saveAsk";
    /**
     * 审核审批权限
     */
    public static final String OA_SAVEASKAUDIT = "oa_saveAskAudit";

    /**
     * 判断当前操作是否拥有权限
     * @param key key值
     * @return true : 拥有权限; false : 没有权限
     */
    public static boolean hasPermission(String key) {
        BaseDao<Permission, Long> mDao = DaoHelper
                .getInstance().getDao(Permission.class);
        Permission p = mDao.query("key", key);
        return p != null;
    }

    /**
     * 是否拥有体征录入权限(只要有一项存在权限则返回true)
     * @return true : 拥有权限; false : 没有权限
     */
    public static boolean hasSignPermission() {
        BaseDao<Permission, Long> mDao = DaoHelper
                .getInstance().getDao(Permission.class);
        Permission p = mDao.query("key", BLOOD_PRESSURE + P_V);
        if(p != null) {
            // 存在血压权限
            return true;
        }
        p = mDao.query("key", HEART_RATE_PR + P_V);
        if(p != null) {
            // 存在心率权限
            return true;
        }
        p = mDao.query("key", BLOOD_SUGAR + P_V);
        if(p != null) {
            // 存在血糖权限
            return true;
        }
        p = mDao.query("key", TEMPERATURE + P_V);
        if(p != null) {
            // 存在体温权限
            return true;
        }
        p = mDao.query("key", BREATHING + P_V);
        if(p != null) {
            // 存在呼吸权限
            return true;
        }
        p = mDao.query("key", DRINKING_WATER + P_V);
        if(p != null) {
            // 存在饮水权限
            return true;
        }
        p = mDao.query("key", DEFECATION + P_V);
        if(p != null) {
            // 存在排便权限
            return true;
        }
        p = mDao.query("key", SLEEP + P_V);
        if(p != null) {
            // 存在睡眠权限
            return true;
        }
        p = mDao.query("key", MEAL + P_V);
        // 返回true: 存在饮食权限; false:不存在
        return p != null;
    }

    /**
     * 体征录入信息修改权限
     * @param title 护理项名称
     * @return true: 存在权限; false: 不存在权限
     */
    public static boolean hasSignUpdatePermission(String title) {
        BaseDao<Permission, Long> mDao = DaoHelper.getInstance().getDao(Permission.class);
        Permission p = null;
        if("血压".equals(title)) {
            p = mDao.query("key", BLOOD_PRESSURE + P_U);
        }else if("体温".equals(title)) {
            p = mDao.query("key", TEMPERATURE + P_U);
        }else if("血糖".equals(title)) {
            p = mDao.query("key", BLOOD_SUGAR + P_U);
        }else if("心率".equals(title)) {
            p = mDao.query("key", HEART_RATE + P_U);
        }else if("排便".equals(title)) {
            p = mDao.query("key", DEFECATION + P_U);
        }else if("呼吸".equals(title)) {
            p = mDao.query("key", BREATHING + P_U);
        }else if("饮水".equals(title)) {
            p = mDao.query("key", DRINKING_WATER + P_U);
        }else if("进食".equals(title)) {
            p = mDao.query("key", MEAL + P_U);
        }else if("睡眠".equals(title)) {
            p = mDao.query("key", SLEEP + P_U);
        }
        return p != null;
    }

    /**
     * 获取数据审核条目是否拥有权限
     * @param itemTitle 体征项条目名称
     * @return true : 拥有权限; false : 没有权限
     */
    public static boolean hasVerifyPermission(String itemTitle) {
        BaseDao<Permission, Long> mDao = DaoHelper
                .getInstance().getDao(Permission.class);
        Permission p = mDao.query("key", getVerifyKey(itemTitle));
        return p != null;
    }

    /**
     * 获取数据审核条目KEY
     * @param itemTitle 体征项条目名称
     * @return key值
     */
    private static String getVerifyKey(String itemTitle) {
        StringBuilder sb = new StringBuilder();
        sb.append(VERIFY);
        if("血糖".equals(itemTitle)) {
            sb.append("bsr");
        }else if("体温".equals(itemTitle)) {
            sb.append("tr");
        }else if("呼吸".equals(itemTitle)) {
            sb.append("br");
        }else if("血压".equals(itemTitle)) {
            sb.append("bpr");
        }else if("心率".equals(itemTitle)) {
            sb.append("pr");
        }else if("排便".equals(itemTitle)) {
            sb.append("dr");
        }else if("饮水".equals(itemTitle)) {
            sb.append("dwr");
        }else if("睡眠".equals(itemTitle)) {
            sb.append("sleep");
        }else if("进食".equals(itemTitle)) {
            sb.append("meal");
        }
        sb.append(P_U);
        return sb.toString();
    }

}
