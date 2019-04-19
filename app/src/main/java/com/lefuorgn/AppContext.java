package com.lefuorgn;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.telephony.TelephonyManager;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.reflect.TypeToken;
import com.lefuorgn.api.ApiOkHttp;
import com.lefuorgn.api.common.Json;
import com.lefuorgn.api.http.config.HttpConfig;
import com.lefuorgn.bean.OrgInfo;
import com.lefuorgn.bean.User;
import com.lefuorgn.bean.UserConfig;
import com.lefuorgn.bean.UserItem;
import com.lefuorgn.db.util.DaoHelper;
import com.lefuorgn.util.NetworkUtils;
import com.lefuorgn.util.SPUtils;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import cn.jpush.android.api.JPushInterface;


/**
 * <p>全局应用程序类：用于保存和调用全局应用配置及访问网络数据</p>
 * <Strong color='red'>机构页面</Strong>使用的机构信息(id和name)
 * 必须使用方法{@link #getAgencyId()}和{@link #getAgencyName()}
 * <p>方法{@link #setAgencyId(long)}和{@link #setAgencyName(String)}
 * 只能在{@link com.lefuorgn.db.helper.DatabaseHelper}类中reset()使用; 其他地方严禁使用</p>
 *
 * <p>需要使用用户登录信息; 可以通过{@link #getUser()}获取</p>
 */
public class AppContext extends Application {

    private static AppContext instance;
    private long agencyId; // 当前页面机构的ID(只用于机构信息页面)
    private String agencyName; // 当前页面机构名称(只用于机构信息页面)
    private boolean login; // 记录当前用户是否登录
    private int netState; // 网络状态
    private String deviceId; // 当前设备号

    private User mUser; // 当前用户信息

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // 初始化基本信息,以及第三方类库
        init();
        // 初始化登录信息
        initLogin();
    }

    /**
     * 获取当前运行的AppContext
     * @return 当前实例对象
     */
    public static AppContext getInstance() {
        return instance;
    }

    private void init() {
        // 初始化网络请求
        ApiOkHttp.init(new HttpConfig(getApplicationContext()));
        // 初始化百度地图
        SDKInitializer.initialize(getApplicationContext());
        // 初始化当前网络状态
        netState = NetworkUtils.getNetWorkState(this);
        // 初始化设备号
        deviceId = getDeviceId();
        // Log控制器
        TLog.DEBUG = false;
        // 奔溃日志初始化
        CrashReport.initCrashReport(getApplicationContext(), "900009276", false);
        // 极光推送初始化
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
    }

    /**
     * 初始化登录信息
     */
    private void initLogin() {
        mUser = getLoginUser();
        if(mUser != null && mUser.getUser_id() > 0) {
            login = true;
            agencyId = mUser.getAgency_id();
            agencyName = mUser.getAgencyName();
            // 类型的获取在getLoginUser()方法中
        }else {
            agencyId = 0;
            login = false;
            // 清空配置信息
            SPUtils.clear(this);
        }
    }

    /**
     * 保存用户信息
     * @param user 当前用户对象
     */
    public void saveUserInfo(final User user) {
        setLastUserId(user.getUser_id());
        processingUser(user);
        this.mUser = user;
        this.agencyId = user.getAgency_id();
        this.agencyName = user.getAgencyName();
        this.login = true;
        setProperties(new Properties() {
            {
                setProperty("user.user_id", String.valueOf(user.getUser_id()));
                setProperty("user.name", StringUtils.toString(user.getUser_name()));
                setProperty("user.psw", StringUtils.toString(user.getPassword()));
                setProperty("user.gender", String.valueOf(user.getGender()));
                setProperty("user.icon", StringUtils.toString(user.getIcon()));
                setProperty("user.mobile", StringUtils.toString(user.getMobile()));
                setProperty("user.mailbox", StringUtils.toString(user.getMailbox()));
                setProperty("user.agency_id", String.valueOf(user.getAgency_id()));
                setProperty("user.agencyName", String.valueOf(user.getAgencyName()));
                setProperty("user.agencys", Json.getGson().toJson(user.getAgencys()));
                setProperty("user.document_number", String.valueOf(user.getDocument_number()));
                setProperty("user.birthday_dt", String.valueOf(user.getBirthday_dt()));
                setProperty("user.create_dt", String.valueOf(user.getCreate_dt()));
                setProperty("user.address", String.valueOf(user.getAddress()));
                setProperty("user.gov", String.valueOf(user.isGov()));
                setProperty("user.govOrg_id", String.valueOf(user.getGovOrg_id()));
                setProperty("user.govOrgName", String.valueOf(user.getGovOrgName()));
                setProperty("user.govOrg", Json.getGson().toJson(user.getGovOrg()));
                setProperty("user.group", String.valueOf(user.isGroup()));
                setProperty("user.groupOrg_id", String.valueOf(user.getGroupOrg_id()));
                setProperty("user.groupOrgName", String.valueOf(user.getGroupOrgName()));
                setProperty("user.groupOrg", Json.getGson().toJson(user.getGroupOrg()));
                setProperty("user.agencyInfo", Json.getGson().toJson(user.getAgencyInfo()));
            }
        });

    }

    /**
     * 处理用户数据
     */
    private void processingUser(User user) {
        // 获取当前用户类型
        if(user.isGroup()) {
            // 集团用户
            // 获取集团名称
            for (UserItem userItem : user.getGroupOrg()) {
                if(userItem.getId() == user.getGroupOrg_id()) {
                    user.setGroupOrgName(userItem.getName());
                    break;
                }
            }
        }
        if(user.getAgency_id() > 0) {
            // 机构用户
            // 获取机构名称
            for (OrgInfo orgInfo : user.getAgencys()) {
                if(orgInfo.getAgency_id() == user.getAgency_id()) {
                    user.setAgencyName(orgInfo.getAgency_name());
                    break;
                }
            }
        }
        if(user.isGov()) {
            // 政府用户
            // 获取政府名称
            for (UserItem userItem : user.getGovOrg()) {
                if(userItem.getId() == user.getGovOrg_id()) {
                    user.setGovOrgName(userItem.getName());
                    break;
                }
            }
        }
    }

    /**
     * 获取当前登录用户的信息
     * @return 当前用户, 如果用户不存在,其用户ID为0
     */
    private User getLoginUser() {
        // 用户信息保存在Properties
        User user = new User();
        long userId = StringUtils.toLong(getProperty("user.user_id") , 0);
        user.setUser_id(userId);
        if(userId <= 0) {
            return user;
        }
        user.setUser_name(getProperty("user.name"));
        user.setPassword(getProperty("user.psw"));
        user.setGender(StringUtils.toInt(getProperty("user.gender"), 0));
        user.setIcon(getProperty("user.icon"));
        user.setMobile(getProperty("user.mobile"));
        user.setMailbox(getProperty("user.mailbox"));
        user.setAgency_id(StringUtils.toLong(getProperty("user.agency_id"), 0));
        user.setAgencyName(getProperty("user.agencyName"));
        user.setAgencys(jsonToOrgInfo(getProperty("user.agencys")));
        user.setDocument_number(getProperty("user.document_number"));
        user.setBirthday_dt(StringUtils.toLong(getProperty("user.birthday_dt"), 0));
        user.setCreate_dt(StringUtils.toLong(getProperty("user.create_dt"), 0));
        user.setAddress(getProperty("user.address"));

        user.setGov(StringUtils.toBoolean(getProperty("user.gov"), false));
        user.setGovOrg_id(StringUtils.toLong(getProperty("user.govOrg_id"), 0));
        user.setGovOrgName(getProperty("user.govOrgName"));
        user.setGovOrg(jsonToUserItem(getProperty("user.govOrg")));

        user.setGroup(StringUtils.toBoolean(getProperty("user.group"), false));
        user.setGroupOrg_id(StringUtils.toLong(getProperty("user.groupOrg_id"), 0));
        user.setGroupOrgName(getProperty("user.groupOrgName"));
        user.setGroupOrg(jsonToUserItem(getProperty("user.groupOrg")));

        user.setAgencyInfo(Json.getGson().fromJson(getProperty("user.agencyInfo"), UserConfig.class));
        return user;
    }

    /**
     * json数据转换成OrgInfo数据集合
     */
    private List<OrgInfo> jsonToOrgInfo(String json) {
        if(StringUtils.isEmpty(json)) {
            return new ArrayList<OrgInfo>();
        }
        Type type = new TypeToken<List<OrgInfo>>(){}.getType();
        return Json.getGson().fromJson(json, type);
    }

    /**
     * json数据转换成OrgInfo数据集合
     */
    private List<UserItem> jsonToUserItem(String json) {
        if(StringUtils.isEmpty(json)) {
            return new ArrayList<UserItem>();
        }
        Type type = new TypeToken<List<UserItem>>(){}.getType();
        return Json.getGson().fromJson(json, type);
    }

    /**
     * 获取当前用户登录信息
     * @return 当前登录用户信息
     */
    public User getUser() {
        return mUser;
    }
    /**
     * 保存用户信息, 包括用户名称, 邮箱和电话号码
     * @param user 当前用户对象
     */
    public void updateUserInfo(final User user) {
        setProperties(new Properties() {
            {
                setProperty("user.name", user.getUser_name());
                setProperty("user.mailbox", user.getMailbox());
            }
        });
    }

    /**
     * 更新当前用户密码
     * @param psw 更新后的密码
     */
    public void updateUserPassword(String psw) {
        setProperty("user.psw", psw);
    }

    /**
     * 跳转到登录页面
     */
    public void skipLoginActivity() {
        cleanLoginInfo();
        Activity activity = AppManager.getAppManager().currentActivity();
        if(activity != null) {
            Intent intent = new Intent(activity, AppLogin.class);
            activity.startActivity(intent);
            AppManager.getAppManager().finishOtherActivity(activity);
        }
    }

    /**
     * 清除当前用户信息
     */
    public void cleanLoginInfo() {
        this.agencyId = 0;
        this.login = false;
        // 清空登录信息
        removeProperty("user.user_id", "user.name", "user.psw", "user.gender",
                "user.icon", "user.mobile", "user.mailbox",
                "user.agency_id", "user.agencyName", "user.agencys",
                "user.document_number", "user.birthday_dt", "user.create_dt",
                "user.address", "user.gov", "user.govOrg_id",
                "user.govOrgName", "user.govOrg", "user.group",
                "user.groupOrg_id", "user.groupOrgName", "user.groupOrg",
                "user.agencyInfo");
    }

    /**
     * 清空当前用户所有信息
     */
    public void cleanAllInfo() {
        cleanLoginInfo();
        // 保留同步设置信息
        String note = getSyncDataForNote();
        long time = getSyncDataForLongTime();
        // 流量情况下是否可以同步
        boolean allowed = isNetFlowAllowed();
        // 清空配置信息
        SPUtils.clear(this);
        setSyncDataForNote(note);
        setSyncDataForLongTime(time);
        setNetFlowAllowed(allowed);
        // 重置数据库状态
        DaoHelper.getInstance().reset();
        // 判断是否存在sd卡
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED
                .equals(android.os.Environment.getExternalStorageState());
        if(sdExist) {
            // SD卡挂载
            // 清空SD卡上保存的信息
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + File.separator + "lefuOrg");
            deleteFile(newPath);
        }else {
            // SD卡没有挂载
            deleteLocalFile("lefu_video");
            deleteLocalFile("lefu_audio");
            deleteLocalFile("lefu_img");
            deleteLocalFile("download");
        }
    }

    /**
     * 删除本地内存缓存
     * @param name 文件夹名称
     */
    private void deleteLocalFile(String name) {
        String path = getDir(name, Context.MODE_PRIVATE).getAbsolutePath();
        deleteFile(new File(path));
    }

    /**
     * 删除文件夹下的所有文件(包括目录)
     * @param file 要删除的文件File对象
     */
    private void deleteFile(File file) {
        if(file.isDirectory()){
            // 表示该文件是文件夹
            String[] files = file.list();
            for(String childFilePath : files){
                File childFile = new File(file.getAbsolutePath() + File.separator + childFilePath);
                deleteFile(childFile);
            }
        }else{
            // 是文件
            if(file.delete()){
                TLog.error("所有文件删除成功");
            }
        }
    }

    /**
     * 获取cookie时传AppConfig.CONF_COOKIE
     *
     * @param key 键
     * @return 指定键对应的值
     */
    private String getProperty(String key) {
        return AppConfig.getAppConfig().get(key);
    }
    /**
     * 将Properties保存到配置文件中
     * @param ps Properties对象
     */
    private void setProperties(Properties ps) {
        AppConfig.getAppConfig().set(ps);
    }

    /**
     * 保存当前配置
     * @param key 键
     * @param value 值
     */
    private void setProperty(String key, String value) {
        AppConfig.getAppConfig().set(key, value);
    }

    /**
     * 移除配置文件中指定的字段
     * @param key 字段集合
     */
    private void removeProperty(String... key) {
        AppConfig.getAppConfig().remove(key);
    }

    /**
     * 获取当前用户所在的机构ID(只用于机构信息页面)
     * @return 机构ID
     */
    public long getAgencyId() {
        return agencyId;
    }

    /**
     * 获取当前用户所在的机构名称(只用于机构信息页面)
     * @return 机构名称
     */
    public String getAgencyName() {
        return agencyName;
    }

    /**
     * 从集团或者政府版本跳转过来设置的机构ID
     * @param agencyId 机构ID
     */
    public void setAgencyId(long agencyId) {
        this.agencyId = agencyId;
    }

    /**
     * 从集团或者政府版本跳转过来设置的机构名称
     * @param agencyName 机构名称
     */
    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    /**
     * 获取当前网络状态
     * @return 当前网络的状态
     */
    public int getNetState() {
        if(netState == NetworkUtils.NETWORK_NONE) {
            // 无网络
            netState = NetworkUtils.getNetWorkState(this);
        }
        return netState;
    }

    /**
     * 设置当前的网络状态
     * @param netState 指定的网络状态
     */
    public void setNetState(int netState) {
        this.netState = netState;
    }

    /**
     * 判断当前用户是否登录
     * @return true : 已经登录; false : 用户未登录
     */
    public boolean isLogin() {
        return login;
    }

    /**
     * 获取App安装包信息
     *
     * @return App安装包信息实例对象
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 获取当前设备, 设备号码
     */
    public String getDeviceIMEI() {
        return deviceId;
    }

    /**
     * 获取当前设备的设备号
     */
    private String getDeviceId() {
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("HardwareIds")
        String deviceId = manager.getDeviceId();
        if(StringUtils.isEmpty(deviceId)){
            // 获取设备失败, 获取存储中的内容
            deviceId = getLocalDeviceId();
            if(StringUtils.isEmpty(deviceId)) {
                // 本地存储为空, 创建新的设备号
                UUID uuid = UUID.randomUUID();
                deviceId = uuid.toString().replace("-", "").substring(0, 20);
                // 将设备号存储着本地内存
                setLocalDeviceId(deviceId);
            }
        }
        return deviceId;
    }

    /**
     * 保存指定的设备号
     * @param deviceId 设备号
     */
    private void setLocalDeviceId(String deviceId) {
        SPUtils.put(getInstance(), "KEY_DEVICE_ID", deviceId);
    }

    /**
     * 获取设备号
     */
    private String getLocalDeviceId() {
        return (String) SPUtils.get(getInstance(), "KEY_DEVICE_ID", "");
    }

    /**
     * 判断当前数据同步是否是第一次
     * @param id 机构ID
     * @return true: 是; false: 不是
     */
    public static boolean isFirstLoadingData(long id) {
        return (Boolean) SPUtils.get(getInstance(), "KEY_FIRST_LOADING_" + id, true);
    }

    /**
     * 设置当前数据同步的状态, 是否同步过数据
     * @param id 机构ID
     * @param first true: 已同步; false: 未同步
     */
    public static void setFirstLoadingData(long id, boolean first) {
        SPUtils.put(getInstance(), "KEY_FIRST_LOADING_" + id, first);
    }

    /**
     * 当前网络应用请求是否被允许
     * @return true: 允许; false: 不被允许,要提示
     */
    public static boolean isNetFlowAllowed() {
        return (Boolean) SPUtils.get(getInstance(), "KEY_NET_ALLOW", false);
    }

    /**
     * 设置同步数据时网络请求时,是否要提示用户
     * @param allow true: 不用提示; false: 提示
     */
    public static void setNetFlowAllowed(boolean allow) {
        SPUtils.put(getInstance(), "KEY_NET_ALLOW", allow);
    }

    /**
     * 获取同步数据的时间段
     */
    public static long getSyncDataForLongTime() {
        // 默认返回前一个月的时间
        return (Long) SPUtils.get(getInstance(), "KEY_SYNC_DATA_LONG_TIME", StringUtils.getFirstNMonths(1));
    }

    /**
     * 设置同步数据的时间段
     */
    public static void setSyncDataForLongTime(long time) {
        SPUtils.put(getInstance(), "KEY_SYNC_DATA_LONG_TIME", time);
    }

    /**
     * 获取同步数据的时间段文本提示内容
     */
    public static String getSyncDataForNote() {
        return (String) SPUtils.get(getInstance(), "KEY_SYNC_DATA_NOTE", "近一个月");
    }

    /**
     * 设置同步数据的时间段文本提示内容
     */
    public static void setSyncDataForNote(String note) {
        SPUtils.put(getInstance(), "KEY_SYNC_DATA_NOTE", note);
    }

    /**
     * 获取审批状态
     * @return 当前配置的状态
     */
    public static int getApprovalStatus() {
        return (Integer) SPUtils.get(getInstance(), "APPROVAL_STATUS", 0);
    }

    /**
     * 保存审批状态
     * @param status 要设置的状态
     */
    public static void saveApprovalStatus(int status) {
        SPUtils.put(getInstance(), "APPROVAL_STATUS", status);
    }

    /**
     * 设置关注的老人是否修改
     * @param isUpload true: 修改; false: 未修改
     */
    public static void setCareForTheElderly(boolean isUpload) {
        SPUtils.put(getInstance(), "CARE_FOR_THE_ELDERLY", isUpload);
    }

    /**
     * 获取关注的老人是否修改
     * @return true: 修改; false: 未修改
     */
    public static boolean isCareForTheElderly() {
        return (Boolean) SPUtils.get(getInstance(), "CARE_FOR_THE_ELDERLY", false);
    }

    /**
     * 设置关注老人的ID集合,由","分割
     * @param ids 关注老人的ID; "12,1314,23"
     */
    public static void setCareForTheElderlyIds(String ids) {
        SPUtils.put(getInstance(), "CARE_FOR_THE_ELDERLY_IDS", ids);
    }

    /**
     * 获取关注老人的ID集合,由","分割
     * @return 关注老人的ID; "12,1314,23"
     */
    public static String getCareForTheElderlyIds() {
        return (String) SPUtils.get(getInstance(), "CARE_FOR_THE_ELDERLY_IDS", "");
    }

    /**
     * 设置最后一次同步配置文件时间
     * @param time 同步时间
     */
    public static void setSyncConfigTime(long time) {
        SPUtils.put(getInstance(), "SYNC_CONFIG_LAST_TIME" + AppContext.getInstance().getAgencyId(), time);
    }

    /**
     * 获取上次同步配置文件的时间
     * @return 上次同步配置文件的时间
     */
    public static long getSyncConfigTime() {
        return (Long) SPUtils.get(getInstance(), "SYNC_CONFIG_LAST_TIME" + AppContext.getInstance().getAgencyId(), 0L);
    }

    /**
     * 获取上一次登录用户的ID
     * @return 用户ID
     */
    public static long getLastUserId() {
        return (Long) SPUtils.get(getInstance(), "LAST_USER_ID", 0L);
    }

    /**
     * 设置当前用户的ID
     */
    public static void setLastUserId(long id) {
        SPUtils.put(getInstance(), "LAST_USER_ID", id);
    }

}
