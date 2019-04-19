package com.lefuorgn.db.util;

import android.graphics.Color;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.DatabaseConnection;
import com.lefuorgn.AppContext;
import com.lefuorgn.bean.User;
import com.lefuorgn.db.base.BaseDao;
import com.lefuorgn.db.model.base.BaseDownload;
import com.lefuorgn.db.model.base.BaseMediaUpload;
import com.lefuorgn.db.model.base.BaseUpload;
import com.lefuorgn.db.model.basic.BatchEditingTask;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.model.basic.SignAndNursingTask;
import com.lefuorgn.db.model.basic.SignConfig;
import com.lefuorgn.db.model.download.BreathingDownload;
import com.lefuorgn.db.model.download.DailyLifeDownload;
import com.lefuorgn.db.model.download.DailyNursingDownload;
import com.lefuorgn.db.model.download.DefecationDownload;
import com.lefuorgn.db.model.download.DrinkingDownload;
import com.lefuorgn.db.model.download.EatDownload;
import com.lefuorgn.db.model.download.PressureDownload;
import com.lefuorgn.db.model.download.PulseDownload;
import com.lefuorgn.db.model.download.SleepingDownload;
import com.lefuorgn.db.model.download.SugarDownload;
import com.lefuorgn.db.model.download.TemperatureDownload;
import com.lefuorgn.db.model.interf.IDownloadable;
import com.lefuorgn.db.model.upload.BreathingUpload;
import com.lefuorgn.db.model.upload.DailyLifeMediaUpload;
import com.lefuorgn.db.model.upload.DailyLifeUpload;
import com.lefuorgn.db.model.upload.DailyNursingMediaUpload;
import com.lefuorgn.db.model.upload.DailyNursingUpload;
import com.lefuorgn.db.model.upload.DefecationUpload;
import com.lefuorgn.db.model.upload.DrinkingUpload;
import com.lefuorgn.db.model.upload.EatUpload;
import com.lefuorgn.db.model.upload.PressureUpload;
import com.lefuorgn.db.model.upload.PulseUpload;
import com.lefuorgn.db.model.upload.SleepingUpload;
import com.lefuorgn.db.model.upload.SugarUpload;
import com.lefuorgn.db.model.upload.TemperatureUpload;
import com.lefuorgn.lefu.bean.MultiMedia;
import com.lefuorgn.lefu.bean.NursingItemInfo;
import com.lefuorgn.lefu.bean.SignItemInfo;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 这个类是进行体征数据和护理信息保存、修改以及删除的操作
 */

class SignAndNursingManager {

    /**
     * 获取老人体征信息指定时间的所有条目信息
     * @param id 老人ID
     * @param time 当前时间
     * @param mItems 条目类型集合
     * @return 条目类型列表
     */
    static List<SignItemInfo> getSignItemInfoData(long id, long time, List<DisplaySignOrNursingItem> mItems) {
        List<SignItemInfo> list = new ArrayList<SignItemInfo>();
        long[] times = StringUtils.getSectionTime(time);
        for (DisplaySignOrNursingItem item : mItems) {
            list.add(getSignItemInfo(id, times, item));
        }
        return list;
    }

    /**
     * 分页加载指定老人的血糖信息
     * @param pageNo 当前页面
     * @param id 老人ID
     * @param type 血糖类型值
     * @return 血糖集合信息
     */
    static List<SignItemInfo> getSugarItemInfo(int pageNo, long id, int type) {
        // 数据缓存器
        List<SignItemInfo> data = new ArrayList<SignItemInfo>();
        // 从服务器中获取数据
        List<SugarDownload> list = getSignInfoData(SugarDownload.class, pageNo, id);
        // 获取配置文件
        SignConfig config = SignConfigManager.getSignConfig(type);
        // 比例原始数据, 并进行处理
        for (SugarDownload s : list) {
            data.add(getSignItemInfo(s, s.getBlood_sugar_id(), config, s.getBlood_sugar()));
        }
        return data;
    }

    /**
     * 分页加载指定老人的体温信息
     * @param pageNo 当前页面
     * @param id 老人ID
     * @return 体温集合信息
     */
    static List<SignItemInfo> getTemperatureItemInfo(int pageNo, long id, int type) {
        // 数据缓存器
        List<SignItemInfo> data = new ArrayList<SignItemInfo>();
        // 从服务器中获取数据
        List<TemperatureDownload> list = getSignInfoData(TemperatureDownload.class, pageNo, id);
        // 获取配置文件
        SignConfig config = SignConfigManager.getSignConfig(type);
        // 比例原始数据, 并进行处理
        for (TemperatureDownload t : list) {
            data.add(getSignItemInfo(t, t.getId(), config, t.getTemperature()));
        }
        return data;
    }

    /**
     * 分页加载指定老人的呼吸信息
     * @param pageNo 当前页面
     * @param id 老人ID
     * @return 呼吸集合信息
     */
    static List<SignItemInfo> getBreathingItemInfo(int pageNo, long id, int type) {
        // 数据缓存器
        List<SignItemInfo> data = new ArrayList<SignItemInfo>();
        // 从服务器中获取数据
        List<BreathingDownload> list = getSignInfoData(BreathingDownload.class, pageNo, id);
        // 获取配置文件
        SignConfig config = SignConfigManager.getSignConfig(type);
        // 比例原始数据, 并进行处理
        for (BreathingDownload b : list) {
            data.add(getSignItemInfo(b, b.getId(), config, b.getBreathing_times()));
        }
        return data;
    }

    /**
     * 分页加载指定老人的心率信息
     * @param pageNo 当前页面
     * @param id 老人ID
     * @return 心率集合信息
     */
    static List<SignItemInfo> getPulseItemInfo(int pageNo, long id, int type) {
        // 数据缓存器
        List<SignItemInfo> data = new ArrayList<SignItemInfo>();
        // 从服务器中获取数据
        List<PulseDownload> list = getSignInfoData(PulseDownload.class, pageNo, id);
        // 获取配置文件
        SignConfig config = SignConfigManager.getSignConfig(type);
        // 比例原始数据, 并进行处理
        for (PulseDownload p : list) {
            data.add(getSignItemInfo(p, p.getPulse_id(), config, p.getPulse_number()));
        }
        return data;
    }

    /**
     * 分页加载指定老人的排便信息
     * @param pageNo 当前页面
     * @param id 老人ID
     * @return 排便集合信息
     */
    static List<SignItemInfo> getDefecationItemInfo(int pageNo, long id, int type) {
        // 数据缓存器
        List<SignItemInfo> data = new ArrayList<SignItemInfo>();
        // 从服务器中获取数据
        List<DefecationDownload> list = getSignInfoData(DefecationDownload.class, pageNo, id);
        // 获取配置文件
        SignConfig config = SignConfigManager.getSignConfig(type);
        // 比例原始数据, 并进行处理
        for (DefecationDownload d : list) {
            data.add(getSignItemInfo(d, d.getId(), config, d.getDefecation_times()));
        }
        return data;
    }

    /**
     * 分页加载指定老人的饮水信息
     * @param pageNo 当前页面
     * @param id 老人ID
     * @return 饮水集合信息
     */
    static List<SignItemInfo> getDrinkingItemInfo(int pageNo, long id, int type) {
        // 数据缓存器
        List<SignItemInfo> data = new ArrayList<SignItemInfo>();
        // 从服务器中获取数据
        List<DrinkingDownload> list = getSignInfoData(DrinkingDownload.class, pageNo, id);
        // 获取配置文件
        SignConfig config = SignConfigManager.getSignConfig(type);
        // 比例原始数据, 并进行处理
        for (DrinkingDownload d : list) {
            data.add(getSignItemInfo(d, d.getId(), config, d.getWater_amount()));
        }
        return data;
    }

    /**
     * 分页加载指定老人的睡眠信息
     * @param pageNo 当前页面
     * @param id 老人ID
     * @return 睡眠集合信息
     */
    static List<SignItemInfo> getSleepingItemInfo(int pageNo, long id) {
        // 数据缓存器
        List<SignItemInfo> data = new ArrayList<SignItemInfo>();
        // 从服务器中获取数据
        List<SleepingDownload> list = getSignInfoData(SleepingDownload.class, pageNo, id);
        // 比例原始数据, 并进行处理
        for (SleepingDownload s : list) {
            SignItemInfo info = new SignItemInfo();
            info.set_id(s.get_id());
            info.setId(s.getId());
            info.setTime(s.getInspect_dt());
            info.setInspect_user_id(s.getInspect_user_id());
            info.setInspect_user_name(s.getInspect_user_name());
            info.setEntry_user_id(s.getEntry_user_id());
            info.setEntry_user_name(s.getEntry_user_name());
            info.setApprovalStatus(s.getApproval_status());
            info.setContent(s.getSleep_quality());
            info.setColor(Color.GRAY);
            info.setReserved(s.getReserved());
            data.add(info);
        }
        return data;
    }

    /**
     * 分页加载指定老人的饮食信息
     * @param pageNo 当前页面
     * @param id 老人ID
     * @return 饮食集合信息
     */
    static List<SignItemInfo> getEatItemInfo(int pageNo, long id) {
        // 数据缓存器
        List<SignItemInfo> data = new ArrayList<SignItemInfo>();
        // 从服务器中获取数据
        List<EatDownload> list = getSignInfoData(EatDownload.class, pageNo, id);
        // 比例原始数据, 并进行处理
        for (EatDownload e : list) {
            SignItemInfo info = new SignItemInfo();
            info.set_id(e.get_id());
            info.setId(e.getId());
            info.setTime(e.getInspect_dt());
            info.setInspect_user_id(e.getInspect_user_id());
            info.setInspect_user_name(e.getInspect_user_name());
            info.setEntry_user_id(e.getEntry_user_id());
            info.setEntry_user_name(e.getEntry_user_name());
            info.setApprovalStatus(e.getApproval_status());
            info.setContent(e.getDescriptiveInfo());
            info.setColor(Color.GRAY);
            info.setReserved(e.getReserved());
            data.add(info);
        }
        return data;
    }

    /**
     * 分页加载指定老人的血压信息
     * @param pageNo 当前页面
     * @param id 老人ID
     * @return 血压集合信息
     */
    static List<SignItemInfo> getPressureItemInfo(int pageNo, long id, int type) {
        // 数据缓存器
        List<SignItemInfo> data = new ArrayList<SignItemInfo>();
        // 从服务器中获取数据
        List<PressureDownload> list = getSignInfoData(PressureDownload.class, pageNo, id);
        // 获取配置文件
        SignConfig config = SignConfigManager.getSignConfig(type);
        int accurate = (int) config.getAccur();
        // 比例原始数据, 并进行处理
        for (PressureDownload p : list) {
            SignItemInfo info = new SignItemInfo();
            info.set_id(p.get_id());
            info.setId(id);
            info.setTime(p.getInspect_dt());
            info.setInspect_user_id(p.getInspect_user_id());
            info.setInspect_user_name(p.getInspect_user_name());
            info.setEntry_user_id(p.getEntry_user_id());
            info.setEntry_user_name(p.getEntry_user_name());
            info.setApprovalStatus(p.getApproval_status());
            info.setContent(StringUtils.numberFormat(accurate, p.getHigh_blood_pressure()) + "/"
                    + StringUtils.numberFormat(accurate, p.getLow_blood_pressure()));
            info.setColor(DataProcessUtils.getPressureColor(p.getHigh_blood_pressure(), p.getLow_blood_pressure(), config));
            info.setReserved(p.getReserved());
            data.add(info);
        }
        return data;
    }

    /**
     * 保存数据到血糖下载表和上传表中
     * @param user 用户信息
     * @param agencyId 用户信息
     * @param id 老人ID
     * @param name 老人名称
     * @param value 血糖值
     * @param reserved 备注
     * @return 大于0: 保存成功; 0: 保存失败
     */
    static long saveSugarItemInfo(User user, long agencyId, long id, String name, String value, String reserved) {
        long time = System.currentTimeMillis();
        SugarDownload sugarDownload = new SugarDownload();
        sugarDownload.setAgency_id(agencyId);
        sugarDownload.setOld_people_id(id);
        sugarDownload.setOld_people_name(name);
        sugarDownload.setApproval_status(AppContext.getApprovalStatus());
        sugarDownload.setInspect_user_id(user.getUser_id());
        sugarDownload.setInspect_user_name(user.getUser_name());
        sugarDownload.setInspect_dt(time);
        sugarDownload.setEntry_user_id(user.getUser_id());
        sugarDownload.setEntry_user_name(user.getUser_name());
        sugarDownload.setEntry_dt(time);
        sugarDownload.setBlood_sugar(Double.parseDouble(value));
        sugarDownload.setReserved(reserved);
        SugarUpload sugarUpload = new SugarUpload(sugarDownload);
        boolean success = saveSignInfoToDB(SugarDownload.class, SugarUpload.class,
                sugarDownload, sugarUpload);
        if(success) {
            return time;
        }
        return 0;
    }

    /**
     * 保存数据到体温下载表和上传表中
     * @param user 用户信息
     * @param agencyId 机构ID
     * @param id 老人ID
     * @param name 老人名称
     * @param value 体温值
     * @param reserved 备注
     * @return 大于0: 保存成功; 0: 保存失败
     */
    static long saveTemperatureItemInfo(User user, long agencyId, long id, String name, String value, String reserved) {
        long time = System.currentTimeMillis();
        TemperatureDownload temperatureDownload = new TemperatureDownload();
        temperatureDownload.setAgency_id(agencyId);
        temperatureDownload.setOld_people_id(id);
        temperatureDownload.setOld_people_name(name);
        temperatureDownload.setApproval_status(AppContext.getApprovalStatus());
        temperatureDownload.setInspect_user_id(user.getUser_id());
        temperatureDownload.setInspect_user_name(user.getUser_name());
        temperatureDownload.setInspect_dt(time);
        temperatureDownload.setEntry_user_id(user.getUser_id());
        temperatureDownload.setEntry_user_name(user.getUser_name());
        temperatureDownload.setEntry_dt(time);
        temperatureDownload.setTemperature(Double.parseDouble(value));
        temperatureDownload.setReserved(reserved);
        TemperatureUpload temperatureUpload = new TemperatureUpload(temperatureDownload);
        boolean success = saveSignInfoToDB(TemperatureDownload.class, TemperatureUpload.class,
                temperatureDownload, temperatureUpload);
        if(success) {
            return time;
        }
        return 0;
    }

    /**
     * 保存数据到呼吸下载表和上传表中
     * @param user 用户信息
     * @param agencyId 机构ID
     * @param id 老人ID
     * @param name 老人名称
     * @param value 呼吸值
     * @param reserved 备注
     * @return 大于0: 保存成功; 0: 保存失败
     */
    static long saveBreathingItemInfo(User user, long agencyId, long id, String name, String value, String reserved) {
        long time = System.currentTimeMillis();
        BreathingDownload breathingDownload = new BreathingDownload();
        breathingDownload.setAgency_id(agencyId);
        breathingDownload.setOld_people_id(id);
        breathingDownload.setOld_people_name(name);
        breathingDownload.setApproval_status(AppContext.getApprovalStatus());
        breathingDownload.setInspect_user_id(user.getUser_id());
        breathingDownload.setInspect_user_name(user.getUser_name());
        breathingDownload.setInspect_dt(time);
        breathingDownload.setEntry_user_id(user.getUser_id());
        breathingDownload.setEntry_user_name(user.getUser_name());
        breathingDownload.setEntry_dt(time);
        breathingDownload.setBreathing_times((int) Double.parseDouble(value));
        breathingDownload.setReserved(reserved);
        BreathingUpload breathingUpload = new BreathingUpload(breathingDownload);
        boolean success = saveSignInfoToDB(BreathingDownload.class, BreathingUpload.class,
                breathingDownload, breathingUpload);
        if(success) {
            return time;
        }
        return 0;
    }

    /**
     * 保存数据到血压下载表和上传表中
     * @param user 用户信息
     * @param agencyId 机构ID
     * @param id 老人ID
     * @param name 老人名称
     * @param value 血压值
     * @param reserved 备注
     * @return 大于0: 保存成功; 0: 保存失败
     */
    static long savePressureItemInfo(User user, long agencyId, long id, String name, String value, String reserved) {
        String[] array = DataProcessUtils.getBloodPressureArray(value);
        if(array.length != 2) {
            return 0;
        }
        int high, low;
        try {
            high = Integer.parseInt(array[1]);
            low = Integer.parseInt(array[0]);
        }catch (Exception ex) {
            return 0;
        }
        long time = System.currentTimeMillis();
        PressureDownload pressureDownload = new PressureDownload();
        pressureDownload.setAgency_id(agencyId);
        pressureDownload.setOld_people_id(id);
        pressureDownload.setOld_people_name(name);
        pressureDownload.setApproval_status(AppContext.getApprovalStatus());
        pressureDownload.setInspect_user_id(user.getUser_id());
        pressureDownload.setInspect_user_name(user.getUser_name());
        pressureDownload.setInspect_dt(time);
        pressureDownload.setEntry_user_id(user.getUser_id());
        pressureDownload.setEntry_user_name(user.getUser_name());
        pressureDownload.setEntry_dt(time);
        pressureDownload.setHigh_blood_pressure(high);
        pressureDownload.setLow_blood_pressure(low);
        pressureDownload.setReserved(reserved);
        PressureUpload pressureUpload = new PressureUpload(pressureDownload);
        boolean success = saveSignInfoToDB(PressureDownload.class, PressureUpload.class,
                pressureDownload, pressureUpload);
        if(success) {
            return time;
        }

        return 0;
    }

    /**
     * 保存数据到心率下载表和上传表中
     * @param user 用户信息
     * @param agencyId 机构ID
     * @param id 老人ID
     * @param name 老人名称
     * @param value 心率值
     * @param reserved 备注
     * @return 大于0: 保存成功; 0: 保存失败
     */
    static long savePulseItemInfo(User user, long agencyId, long id, String name, String value, String reserved) {
        long time = System.currentTimeMillis();
        PulseDownload pulseDownload = new PulseDownload();
        pulseDownload.setAgency_id(agencyId);
        pulseDownload.setOld_people_id(id);
        pulseDownload.setOld_people_name(name);
        pulseDownload.setApproval_status(AppContext.getApprovalStatus());
        pulseDownload.setInspect_user_id(user.getUser_id());
        pulseDownload.setInspect_user_name(user.getUser_name());
        pulseDownload.setInspect_dt(time);
        pulseDownload.setEntry_user_id(user.getUser_id());
        pulseDownload.setEntry_user_name(user.getUser_name());
        pulseDownload.setEntry_dt(time);
        pulseDownload.setPulse_number((int) Double.parseDouble(value));
        pulseDownload.setReserved(reserved);
        PulseUpload pulseUpload = new PulseUpload(pulseDownload);
        boolean success = saveSignInfoToDB(PulseDownload.class, PulseUpload.class,
                pulseDownload, pulseUpload);
        if(success) {
            return time;
        }
        return 0;
    }

    /**
     * 保存数据到排便下载表和上传表中
     * @param user 用户信息
     * @param agencyId 机构ID
     * @param id 老人ID
     * @param name 老人名称
     * @param value 排便值
     * @param reserved 备注
     * @return 大于0: 保存成功; 0: 保存失败
     */
    static long saveDefecationItemInfo(User user, long agencyId, long id, String name, String value, String reserved) {
        long time = System.currentTimeMillis();
        DefecationDownload defecationDownload = new DefecationDownload();
        defecationDownload.setAgency_id(agencyId);
        defecationDownload.setOld_people_id(id);
        defecationDownload.setOld_people_name(name);
        defecationDownload.setApproval_status(AppContext.getApprovalStatus());
        defecationDownload.setInspect_user_id(user.getUser_id());
        defecationDownload.setInspect_user_name(user.getUser_name());
        defecationDownload.setInspect_dt(time);
        defecationDownload.setEntry_user_id(user.getUser_id());
        defecationDownload.setEntry_user_name(user.getUser_name());
        defecationDownload.setEntry_dt(time);
        defecationDownload.setDefecation_times((int) Double.parseDouble(value));
        defecationDownload.setReserved(reserved);
        DefecationUpload defecationUpload = new DefecationUpload(defecationDownload);
        boolean success = saveSignInfoToDB(DefecationDownload.class, DefecationUpload.class,
                defecationDownload, defecationUpload);
        if(success) {
            return time;
        }
        return 0;
    }

    /**
     * 保存数据到饮水下载表和上传表中
     * @param user 用户信息
     * @param agencyId 机构ID
     * @param id 老人ID
     * @param name 老人名称
     * @param value 饮水值
     * @param reserved 备注
     * @return 大于0: 保存成功; 0: 保存失败
     */
    static long saveDrinkingItemInfo(User user, long agencyId, long id, String name, String value, String reserved) {
        long time = System.currentTimeMillis();
        DrinkingDownload drinkingDownload = new DrinkingDownload();
        drinkingDownload.setAgency_id(agencyId);
        drinkingDownload.setOld_people_id(id);
        drinkingDownload.setOld_people_name(name);
        drinkingDownload.setApproval_status(AppContext.getApprovalStatus());
        drinkingDownload.setInspect_user_id(user.getUser_id());
        drinkingDownload.setInspect_user_name(user.getUser_name());
        drinkingDownload.setInspect_dt(time);
        drinkingDownload.setEntry_user_id(user.getUser_id());
        drinkingDownload.setEntry_user_name(user.getUser_name());
        drinkingDownload.setEntry_dt(time);
        drinkingDownload.setWater_amount((int) Double.parseDouble(value));
        drinkingDownload.setReserved(reserved);
        DrinkingUpload drinkingUpload = new DrinkingUpload(drinkingDownload);
        boolean success = saveSignInfoToDB(DrinkingDownload.class, DrinkingUpload.class,
                drinkingDownload, drinkingUpload);
        if(success) {
            return time;
        }
        return 0;
    }

    /**
     * 保存数据到进食下载表和上传表中
     * @param user 用户信息
     * @param agencyId 机构ID
     * @param id 老人ID
     * @param name 老人名称
     * @param value 进食值
     * @return 大于0: 保存成功; 0: 保存失败
     */
    static long saveEatItemInfo(User user, long agencyId, long id, String name, String value) {
        EatDownload eatDownload = new EatDownload();
        eatDownload.setAgency_id(agencyId);
        eatDownload.setOld_people_id(id);
        eatDownload.setOld_people_name(name);
        eatDownload.setApproval_status(AppContext.getApprovalStatus());
        eatDownload.setInspect_user_id(user.getUser_id());
        eatDownload.setInspect_user_name(user.getUser_name());
        eatDownload.setEntry_user_id(user.getUser_id());
        eatDownload.setEntry_user_name(user.getUser_name());
        // 数据处理
        String[] data = value.split(";");
        String[] types = data[0].split(",");
        long time = 0;
        for (String type : types) {
            time = System.currentTimeMillis();
            eatDownload.setInspect_dt(time);
            eatDownload.setEntry_dt(time);
            eatDownload.setMeal_type(DataProcessUtils.getMealType(type));
            eatDownload.setMeal_amount(DataProcessUtils.getMealAmount(data[1]));
            eatDownload.setReserved(data[2]);
            EatUpload eatUpload = new EatUpload(eatDownload);
            saveSignInfoToDB(EatDownload.class, EatUpload.class, eatDownload, eatUpload);
        }
        return time;
    }



    /**
     * 保存数据到睡眠下载表和上传表中
     * @param user 用户信息
     * @param agencyId 机构ID
     * @param id 老人ID
     * @param name 老人名称
     * @param value 睡眠值
     * @param reserved 备注
     * @return 大于0: 保存成功; 0: 保存失败
     */
    static long saveSleepingItemInfo(User user, long agencyId, long id, String name, String value, String reserved) {
        long time = System.currentTimeMillis();
        SleepingDownload sleepingDownload = new SleepingDownload();
        sleepingDownload.setAgency_id(agencyId);
        sleepingDownload.setOld_people_id(id);
        sleepingDownload.setOld_people_name(name);
        sleepingDownload.setApproval_status(AppContext.getApprovalStatus());
        sleepingDownload.setInspect_user_id(user.getUser_id());
        sleepingDownload.setInspect_user_name(user.getUser_name());
        sleepingDownload.setInspect_dt(time);
        sleepingDownload.setEntry_user_id(user.getUser_id());
        sleepingDownload.setEntry_user_name(user.getUser_name());
        sleepingDownload.setEntry_dt(time);
        sleepingDownload.setSleep_quality(value);
        sleepingDownload.setReserved(reserved);
        SleepingUpload sleepingUpload = new SleepingUpload(sleepingDownload);
        boolean success = saveSignInfoToDB(SleepingDownload.class, SleepingUpload.class,
                sleepingDownload, sleepingUpload);
        if(success) {
            return time;
        }
        return 0;
    }

    /**
     * 更新血糖
     * @param info 更新的信息
     * @return true: 更新成功; false: 更新失败
     */
    static boolean updateSugarItemInfo(SignItemInfo info) {
        DaoHelper helper = DaoHelper.getInstance();
        BaseDao<SugarDownload, Long> dDao = helper.getDao(SugarDownload.class);
        BaseDao<SugarUpload, Long> uDao = helper.getDao(SugarUpload.class);
        // 从数据库中获取本条信息
        SugarDownload sugarDownload = dDao.queryById(info.get_id());
        if(sugarDownload == null) {
            // 数据库中不存在本条信息, 修改失败
            return false;
        }
        // 进行信息修改
        sugarDownload.setInspect_user_id(info.getInspect_user_id());
        sugarDownload.setInspect_user_name(info.getInspect_user_name());
        sugarDownload.setEntry_user_id(info.getEntry_user_id());
        sugarDownload.setEntry_user_name(info.getEntry_user_name());
        sugarDownload.setApproval_status(info.getApprovalStatus());
        sugarDownload.setBlood_sugar(Double.parseDouble(info.getContent()));
        // 修改数据库中的信息
        dDao.update(sugarDownload);

        // 操作上传表中的信息
        SugarUpload sugarUpload = uDao.query("dId", info.get_id());
        if(sugarUpload == null) {
            // 上传表中不存在本条信息
            sugarUpload = new SugarUpload(sugarDownload);
            // 插入当前信息
            uDao.insert(sugarUpload);
        }else {
            sugarUpload.setInspect_user_id(info.getInspect_user_id());
            sugarUpload.setInspect_user_name(info.getInspect_user_name());
            sugarUpload.setEntry_user_id(info.getEntry_user_id());
            sugarUpload.setEntry_user_name(info.getEntry_user_name());
            sugarUpload.setApproval_status(info.getApprovalStatus());
            sugarUpload.setBlood_sugar(Double.parseDouble(info.getContent()));
            // 修改当前上传表信息
            uDao.update(sugarUpload);
        }
        return true;
    }

    /**
     * 更新体温
     * @param info 更新的信息
     * @return true: 更新成功; false: 更新失败
     */
    static boolean updateTemperatureItemInfo(SignItemInfo info) {
        DaoHelper helper = DaoHelper.getInstance();
        BaseDao<TemperatureDownload, Long> dDao = helper.getDao(TemperatureDownload.class);
        BaseDao<TemperatureUpload, Long> uDao = helper.getDao(TemperatureUpload.class);
        // 从数据库中获取本条信息
        TemperatureDownload temperatureDownload = dDao.queryById(info.get_id());
        if(temperatureDownload == null) {
            // 数据库中不存在本条信息, 修改失败
            return false;
        }
        // 进行信息修改
        temperatureDownload.setInspect_user_id(info.getInspect_user_id());
        temperatureDownload.setInspect_user_name(info.getInspect_user_name());
        temperatureDownload.setEntry_user_id(info.getEntry_user_id());
        temperatureDownload.setEntry_user_name(info.getEntry_user_name());
        temperatureDownload.setApproval_status(info.getApprovalStatus());
        temperatureDownload.setTemperature(Double.parseDouble(info.getContent()));
        // 修改数据库中的信息
        dDao.update(temperatureDownload);

        // 操作上传表中的信息
        TemperatureUpload temperatureUpload = uDao.query("dId", info.get_id());
        if(temperatureUpload == null) {
            // 上传表中不存在本条信息
            temperatureUpload = new TemperatureUpload(temperatureDownload);
            // 插入当前信息
            uDao.insert(temperatureUpload);
        }else {
            temperatureUpload.setInspect_user_id(info.getInspect_user_id());
            temperatureUpload.setInspect_user_name(info.getInspect_user_name());
            temperatureUpload.setEntry_user_id(info.getEntry_user_id());
            temperatureUpload.setEntry_user_name(info.getEntry_user_name());
            temperatureUpload.setApproval_status(info.getApprovalStatus());
            temperatureUpload.setTemperature(Double.parseDouble(info.getContent()));
            // 修改当前上传表信息
            uDao.update(temperatureUpload);
        }
        return true;
    }

    /**
     * 更新呼吸
     * @param info 更新的信息
     * @return true: 更新成功; false: 更新失败
     */
    static boolean updateBreathingItemInfo(SignItemInfo info) {
        DaoHelper helper = DaoHelper.getInstance();
        BaseDao<BreathingDownload, Long> dDao = helper.getDao(BreathingDownload.class);
        BaseDao<BreathingUpload, Long> uDao = helper.getDao(BreathingUpload.class);
        // 从数据库中获取本条信息
        BreathingDownload breathingDownload = dDao.queryById(info.get_id());
        if(breathingDownload == null) {
            // 数据库中不存在本条信息, 修改失败
            return false;
        }
        // 进行信息修改
        breathingDownload.setInspect_user_id(info.getInspect_user_id());
        breathingDownload.setInspect_user_name(info.getInspect_user_name());
        breathingDownload.setEntry_user_id(info.getEntry_user_id());
        breathingDownload.setEntry_user_name(info.getEntry_user_name());
        breathingDownload.setApproval_status(info.getApprovalStatus());
        breathingDownload.setBreathing_times((int) Double.parseDouble(info.getContent()));
        // 修改数据库中的信息
        dDao.update(breathingDownload);

        // 操作上传表中的信息
        BreathingUpload breathingUpload = uDao.query("dId", info.get_id());
        if(breathingUpload == null) {
            // 上传表中不存在本条信息
            breathingUpload = new BreathingUpload(breathingDownload);
            // 插入当前信息
            uDao.insert(breathingUpload);
        }else {
            breathingUpload.setInspect_user_id(info.getInspect_user_id());
            breathingUpload.setInspect_user_name(info.getInspect_user_name());
            breathingUpload.setEntry_user_id(info.getEntry_user_id());
            breathingUpload.setEntry_user_name(info.getEntry_user_name());
            breathingUpload.setApproval_status(info.getApprovalStatus());
            breathingUpload.setBreathing_times((int) Double.parseDouble(info.getContent()));
            // 修改当前上传表信息
            uDao.update(breathingUpload);
        }
        return true;
    }

    /**
     * 更新血压
     * @param info 更新的信息
     * @return true: 更新成功; false: 更新失败
     */
    static boolean updatePressureItemInfo(SignItemInfo info) {
        // 解析出高低血压
        String[] array = DataProcessUtils.getBloodPressureArray(info.getContent());
        if(array.length != 2) {
            return false;
        }
        int high, low;
        try {
            high = Integer.parseInt(array[1]);
            low = Integer.parseInt(array[0]);
        }catch (Exception ex) {
            return false;
        }
        DaoHelper helper = DaoHelper.getInstance();
        BaseDao<PressureDownload, Long> dDao = helper.getDao(PressureDownload.class);
        BaseDao<PressureUpload, Long> uDao = helper.getDao(PressureUpload.class);
        // 从数据库中获取本条信息
        PressureDownload pressureDownload = dDao.queryById(info.get_id());
        if(pressureDownload == null) {
            // 数据库中不存在本条信息, 修改失败
            return false;
        }
        // 进行信息修改
        pressureDownload.setInspect_user_id(info.getInspect_user_id());
        pressureDownload.setInspect_user_name(info.getInspect_user_name());
        pressureDownload.setEntry_user_id(info.getEntry_user_id());
        pressureDownload.setEntry_user_name(info.getEntry_user_name());
        pressureDownload.setApproval_status(info.getApprovalStatus());
        pressureDownload.setHigh_blood_pressure(high);
        pressureDownload.setLow_blood_pressure(low);
        // 修改数据库中的信息
        dDao.update(pressureDownload);

        // 操作上传表中的信息
        PressureUpload pressureUpload = uDao.query("dId", info.get_id());
        if(pressureUpload == null) {
            // 上传表中不存在本条信息
            pressureUpload = new PressureUpload(pressureDownload);
            // 插入当前信息
            uDao.insert(pressureUpload);
        }else {
            pressureUpload.setInspect_user_id(info.getInspect_user_id());
            pressureUpload.setInspect_user_name(info.getInspect_user_name());
            pressureUpload.setEntry_user_id(info.getEntry_user_id());
            pressureUpload.setEntry_user_name(info.getEntry_user_name());
            pressureUpload.setApproval_status(info.getApprovalStatus());
            pressureUpload.setHigh_blood_pressure(high);
            pressureUpload.setLow_blood_pressure(low);
            // 修改当前上传表信息
            uDao.update(pressureUpload);
        }
        return true;
    }

    /**
     * 更新心率
     * @param info 更新的信息
     * @return true: 更新成功; false: 更新失败
     */
    static boolean updatePulseItemInfo(SignItemInfo info) {
        DaoHelper helper = DaoHelper.getInstance();
        BaseDao<PulseDownload, Long> dDao = helper.getDao(PulseDownload.class);
        BaseDao<PulseUpload, Long> uDao = helper.getDao(PulseUpload.class);
        // 从数据库中获取本条信息
        PulseDownload pulseDownload = dDao.queryById(info.get_id());
        if(pulseDownload == null) {
            // 数据库中不存在本条信息, 修改失败
            return false;
        }
        // 进行信息修改
        pulseDownload.setInspect_user_id(info.getInspect_user_id());
        pulseDownload.setInspect_user_name(info.getInspect_user_name());
        pulseDownload.setEntry_user_id(info.getEntry_user_id());
        pulseDownload.setEntry_user_name(info.getEntry_user_name());
        pulseDownload.setApproval_status(info.getApprovalStatus());
        pulseDownload.setPulse_number((int) Double.parseDouble(info.getContent()));
        // 修改数据库中的信息
        dDao.update(pulseDownload);

        // 操作上传表中的信息
        PulseUpload pulseUpload = uDao.query("dId", info.get_id());
        if(pulseUpload == null) {
            // 上传表中不存在本条信息
            pulseUpload = new PulseUpload(pulseDownload);
            // 插入当前信息
            uDao.insert(pulseUpload);
        }else {
            pulseUpload.setInspect_user_id(info.getInspect_user_id());
            pulseUpload.setInspect_user_name(info.getInspect_user_name());
            pulseUpload.setEntry_user_id(info.getEntry_user_id());
            pulseUpload.setEntry_user_name(info.getEntry_user_name());
            pulseUpload.setApproval_status(info.getApprovalStatus());
            pulseUpload.setPulse_number((int) Double.parseDouble(info.getContent()));
            // 修改当前上传表信息
            uDao.update(pulseUpload);
        }
        return true;
    }

    /**
     * 更新排便
     * @param info 更新的信息
     * @return true: 更新成功; false: 更新失败
     */
    static boolean updateDefecationItemInfo(SignItemInfo info) {
        DaoHelper helper = DaoHelper.getInstance();
        BaseDao<DefecationDownload, Long> dDao = helper.getDao(DefecationDownload.class);
        BaseDao<DefecationUpload, Long> uDao = helper.getDao(DefecationUpload.class);
        // 从数据库中获取本条信息
        DefecationDownload defecationDownload = dDao.queryById(info.get_id());
        if(defecationDownload == null) {
            // 数据库中不存在本条信息, 修改失败
            return false;
        }
        // 进行信息修改
        defecationDownload.setInspect_user_id(info.getInspect_user_id());
        defecationDownload.setInspect_user_name(info.getInspect_user_name());
        defecationDownload.setEntry_user_id(info.getEntry_user_id());
        defecationDownload.setEntry_user_name(info.getEntry_user_name());
        defecationDownload.setApproval_status(info.getApprovalStatus());
        defecationDownload.setDefecation_times((int) Double.parseDouble(info.getContent()));
        // 修改数据库中的信息
        dDao.update(defecationDownload);

        // 操作上传表中的信息
        DefecationUpload defecationUpload = uDao.query("dId", info.get_id());
        if(defecationUpload == null) {
            // 上传表中不存在本条信息
            defecationUpload = new DefecationUpload(defecationDownload);
            // 插入当前信息
            uDao.insert(defecationUpload);
        }else {
            defecationUpload.setInspect_user_id(info.getInspect_user_id());
            defecationUpload.setInspect_user_name(info.getInspect_user_name());
            defecationUpload.setEntry_user_id(info.getEntry_user_id());
            defecationUpload.setEntry_user_name(info.getEntry_user_name());
            defecationUpload.setApproval_status(info.getApprovalStatus());
            defecationUpload.setDefecation_times((int) Double.parseDouble(info.getContent()));
            // 修改当前上传表信息
            uDao.update(defecationUpload);
        }
        return true;
    }

    /**
     * 更新饮水
     * @param info 更新的信息
     * @return true: 更新成功; false: 更新失败
     */
    static boolean updateDrinkingItemInfo(SignItemInfo info) {
        DaoHelper helper = DaoHelper.getInstance();
        BaseDao<DrinkingDownload, Long> dDao = helper.getDao(DrinkingDownload.class);
        BaseDao<DrinkingUpload, Long> uDao = helper.getDao(DrinkingUpload.class);
        // 从数据库中获取本条信息
        DrinkingDownload drinkingDownload = dDao.queryById(info.get_id());
        if(drinkingDownload == null) {
            // 数据库中不存在本条信息, 修改失败
            return false;
        }
        // 进行信息修改
        drinkingDownload.setInspect_user_id(info.getInspect_user_id());
        drinkingDownload.setInspect_user_name(info.getInspect_user_name());
        drinkingDownload.setEntry_user_id(info.getEntry_user_id());
        drinkingDownload.setEntry_user_name(info.getEntry_user_name());
        drinkingDownload.setApproval_status(info.getApprovalStatus());
        drinkingDownload.setWater_amount((int) Double.parseDouble(info.getContent()));
        // 修改数据库中的信息
        dDao.update(drinkingDownload);

        // 操作上传表中的信息
        DrinkingUpload drinkingUpload = uDao.query("dId", info.get_id());
        if(drinkingUpload == null) {
            // 上传表中不存在本条信息
            drinkingUpload = new DrinkingUpload(drinkingDownload);
            // 插入当前信息
            uDao.insert(drinkingUpload);
        }else {
            drinkingUpload.setInspect_user_id(info.getInspect_user_id());
            drinkingUpload.setInspect_user_name(info.getInspect_user_name());
            drinkingUpload.setEntry_user_id(info.getEntry_user_id());
            drinkingUpload.setEntry_user_name(info.getEntry_user_name());
            drinkingUpload.setApproval_status(info.getApprovalStatus());
            drinkingUpload.setWater_amount((int) Double.parseDouble(info.getContent()));
            // 修改当前上传表信息
            uDao.update(drinkingUpload);
        }
        return true;
    }

    /**
     * 更新饮水
     * @param info 更新的信息
     * @return true: 更新成功; false: 更新失败
     */
    static boolean updateEatItemInfo(SignItemInfo info) {
        // 获取饮食信息
        if(info.getContent().length() < 4) {
            return false;
        }
        DaoHelper helper = DaoHelper.getInstance();
        BaseDao<EatDownload, Long> dDao = helper.getDao(EatDownload.class);
        BaseDao<EatUpload, Long> uDao = helper.getDao(EatUpload.class);
        // 从数据库中获取本条信息
        EatDownload eatDownload = dDao.queryById(info.get_id());
        if(eatDownload == null) {
            // 数据库中不存在本条信息, 修改失败
            return false;
        }
        // 进行信息修改
        eatDownload.setInspect_user_id(info.getInspect_user_id());
        eatDownload.setInspect_user_name(info.getInspect_user_name());
        eatDownload.setEntry_user_id(info.getEntry_user_id());
        eatDownload.setEntry_user_name(info.getEntry_user_name());
        eatDownload.setApproval_status(info.getApprovalStatus());
        eatDownload.setMeal_type(DataProcessUtils.getMealType(info.getContent().substring(0, 2)));
        eatDownload.setMeal_amount(DataProcessUtils.getMealAmount(info.getContent().substring(2, 4)));
        eatDownload.setReserved(info.getReserved());
        // 修改数据库中的信息
        dDao.update(eatDownload);

        // 操作上传表中的信息
        EatUpload eatUpload = uDao.query("dId", info.get_id());
        if(eatUpload == null) {
            // 上传表中不存在本条信息
            eatUpload = new EatUpload(eatDownload);
            // 插入当前信息
            uDao.insert(eatUpload);
        }else {
            eatUpload.setInspect_user_id(info.getInspect_user_id());
            eatUpload.setInspect_user_name(info.getInspect_user_name());
            eatUpload.setEntry_user_id(info.getEntry_user_id());
            eatUpload.setEntry_user_name(info.getEntry_user_name());
            eatUpload.setApproval_status(info.getApprovalStatus());
            eatUpload.setMeal_type(DataProcessUtils.getMealType(info.getContent().substring(0, 2)));
            eatUpload.setMeal_amount(DataProcessUtils.getMealAmount(info.getContent().substring(2, 4)));
            eatUpload.setReserved(info.getReserved());
            // 修改当前上传表信息
            uDao.update(eatUpload);
        }
        return true;
    }

    /**
     * 更新饮水
     * @param info 更新的信息
     * @return true: 更新成功; false: 更新失败
     */
    static boolean updateSleepItemInfo(SignItemInfo info) {
        DaoHelper helper = DaoHelper.getInstance();
        BaseDao<SleepingDownload, Long> dDao = helper.getDao(SleepingDownload.class);
        BaseDao<SleepingUpload, Long> uDao = helper.getDao(SleepingUpload.class);
        // 从数据库中获取本条信息
        SleepingDownload sleepingDownload = dDao.queryById(info.get_id());
        if(sleepingDownload == null) {
            // 数据库中不存在本条信息, 修改失败
            return false;
        }
        // 进行信息修改
        sleepingDownload.setInspect_user_id(info.getInspect_user_id());
        sleepingDownload.setInspect_user_name(info.getInspect_user_name());
        sleepingDownload.setEntry_user_id(info.getEntry_user_id());
        sleepingDownload.setEntry_user_name(info.getEntry_user_name());
        sleepingDownload.setApproval_status(info.getApprovalStatus());
        sleepingDownload.setSleep_quality(info.getContent());
        // 修改数据库中的信息
        dDao.update(sleepingDownload);

        // 操作上传表中的信息
        SleepingUpload sleepingUpload = uDao.query("dId", info.get_id());
        if(sleepingUpload == null) {
            // 上传表中不存在本条信息
            sleepingUpload = new SleepingUpload(sleepingDownload);
            // 插入当前信息
            uDao.insert(sleepingUpload);
        }else {
            sleepingUpload.setInspect_user_id(info.getInspect_user_id());
            sleepingUpload.setInspect_user_name(info.getInspect_user_name());
            sleepingUpload.setEntry_user_id(info.getEntry_user_id());
            sleepingUpload.setEntry_user_name(info.getEntry_user_name());
            sleepingUpload.setApproval_status(info.getApprovalStatus());
            sleepingUpload.setSleep_quality(info.getContent());
            // 修改当前上传表信息
            uDao.update(sleepingUpload);
        }
        return true;
    }

    /**
     * 获取老人护理信息指定时间的所有条目信息
     * @param id 老人ID
     * @param time 当前时间
     * @param items 当前护理条目
     * @return 老人所有条目信息
     */
    static List<NursingItemInfo> getNursingItemInfoData(long id, long time, List<DisplaySignOrNursingItem> items) {
        List<NursingItemInfo> result = new ArrayList<NursingItemInfo>();
        // 获取当前老人相关的所有的数据
        List<DailyNursingDownload> list = getNursingInfoData(id, StringUtils.getSectionTime(time));
        // 记录当前所有指定护理信息的条目
        List<DailyNursingDownload> nursingItemList = new ArrayList<DailyNursingDownload>();
        for (DisplaySignOrNursingItem item : items) {
            NursingItemInfo nursingItemInfo = new NursingItemInfo();
            List<DailyNursingDownload> data = new ArrayList<DailyNursingDownload>();
            TLog.error("加载前... " + list.toString());
            nursingItemInfo.setType(item.getType());
            nursingItemInfo.setName(item.getTitle());
            nursingItemList.clear();
            for (DailyNursingDownload download : list) {
                if(download.getNurs_items_id() == item.getType()) {
                    nursingItemList.add(download);
                }
            }
            list.removeAll(nursingItemList);
            TLog.error("加载后... " + list.toString());
            nursingItemInfo.setNurse_times(nursingItemList.size());
            for (DailyNursingDownload d : nursingItemList) {
                if(d.getDaily_id() == -1) {
                    data.add(d);
                }
            }
            nursingItemInfo.setNotUploaded(data);
            result.add(nursingItemInfo);
        }
        return result;
    }

    /**
     * 获取老人护理信息指定时间的所有条目信息(包括配单信息)
     * @param oldPeople 老人信息
     * @param time 当前时间
     * @param items 当前护理条目
     * @return 老人所有条目信息
     */
    static List<NursingItemInfo> getBatchEditingNursingItemInfoData(
            OldPeople oldPeople, long time, List<DisplaySignOrNursingItem> items) {
        List<NursingItemInfo> list = new ArrayList<NursingItemInfo>();
        long[] times = StringUtils.getSectionTime(time);
        // 获取所有配单信息
        BaseDao<BatchEditingTask, Long> dao = DaoHelper
                .getInstance().getDao(BatchEditingTask.class);
        List<BatchEditingTask> tasks = dao.queryAll("_id", false);
        if(tasks == null) {
            tasks = new ArrayList<BatchEditingTask>();
        }
        BatchEditingTask task = null;
        // 获取指定老人的配单
        for (BatchEditingTask t : tasks) {
            if(oldPeople.getNursing_level_id() == t.getId()) {
                task = t;
                break;
            }
        }
        for (DisplaySignOrNursingItem item : items) {
            SignAndNursingTask nursingTask = null;
            if(task != null) {
                // 获取老人指定类型的配单
                for (SignAndNursingTask t : task.getNursingTask()) {
                    if(t.getId() == item.getType()) {
                        nursingTask = t;
                        break;
                    }
                }
            }
            list.add(getBatchEditingNursingItemInfo(oldPeople.getId(), nursingTask, times, item));
        }
        return list;
    }

    /**
     * 将指定的护理信息保存到数据库中
     * @param d 下载表bean类信息
     * @return true: 保存成功; false: 保存失败
     */
    static boolean saveNursingItemInfo(DailyNursingDownload d, List<MultiMedia> multiMedia) {
        try {
            // 下载表操作类
            Dao<DailyNursingDownload, Long> dDao = DaoHelper
                    .getInstance().getDao(DailyNursingDownload.class).getDao();
            // 上传表操作类
            Dao<DailyNursingUpload, Long> uDao = DaoHelper
                    .getInstance().getDao(DailyNursingUpload.class).getDao();
            // 媒体信息操作类
            Dao<DailyNursingMediaUpload, Long> mDao = DaoHelper
                    .getInstance().getDao(DailyNursingMediaUpload.class).getDao();
            createAliasForImg(DailyNursingMediaUpload.class, multiMedia);
            DatabaseConnection databaseConnection = null;
            try {
                databaseConnection = dDao.startThreadConnection();
                dDao.setAutoCommit(databaseConnection, false);
                // 保存数据到下载表中
                dDao.create(d);
                // 将下载表中数据保存到上传表数据中
                DailyNursingUpload upload = new DailyNursingUpload(d, DataProcessUtils.getMultiMediaInfo(multiMedia, false));
                // 保存数据到上传表中
                uDao.create(upload);
                // 保存媒体文件
                if(multiMedia.size() > 0) {
                    mDao.create(getDailyNursingMediaUpload(upload.get_id(), multiMedia));
                }
                // 提交事务
                dDao.commit(databaseConnection);
                return true;
            } catch (SQLException ex) {
                dDao.rollBack(databaseConnection);
                ex.printStackTrace();
            } finally {
                try{
                    dDao.endThreadConnection(databaseConnection);
                }catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 更新护理信息
     * @param id 护理信息下载表ID
     * @param remarks 备注
     * @param multiMedia 要修改的媒体信息
     * @return true: 保存成功; false: 保存失败
     */
    static boolean updateNursingItemInfo(long id, String remarks, List<MultiMedia> multiMedia) {
        // 下载表操作类
        BaseDao<DailyNursingDownload, Long> dDao = DaoHelper
                .getInstance().getDao(DailyNursingDownload.class);
        // 上传表操作类
        BaseDao<DailyNursingUpload, Long> uDao = DaoHelper
                .getInstance().getDao(DailyNursingUpload.class);
        // 媒体信息操作类
        BaseDao<DailyNursingMediaUpload, Long> mDao = DaoHelper
                .getInstance().getDao(DailyNursingMediaUpload.class);

        DailyNursingDownload download = dDao.queryById(id);
        DailyNursingUpload upload = uDao.query("dId", id);
        if(download == null || upload == null) {
            return false;
        }
        createAliasForImg(DailyNursingMediaUpload.class, multiMedia);
        // 删除对应的多媒体信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uId", upload.get_id());
        mDao.deleteList(map);
        // 修改备注信息
        download.setReserved(remarks);
        upload.setReserved(remarks);
        // 修改媒体信息
        download.setMedia(DataProcessUtils.getMultiMediaInfo(multiMedia, true));
        upload.setMedia(DataProcessUtils.getMultiMediaInfo(multiMedia, false));
        // 修改数据到下载表中
        dDao.update(download);
        // 修改数据到上传表中
        uDao.update(upload);
        // 保存媒体文件
        if(multiMedia.size() > 0) {
            mDao.insertList(getDailyNursingMediaUpload(upload.get_id(), multiMedia));
        }
        return true;
    }

    /**
     * 通过护理信息下载表数据删除; 包括(下载表,上传表,多媒体上传表)
     * @param info 护理条目信息
     * @return true : 删除成功; false: 删除失败
     */
    static boolean deleteNursingItemInfo(NursingItemInfo info) {
        // 下载表操作类
        BaseDao<DailyNursingDownload, Long> dDao = DaoHelper
                .getInstance().getDao(DailyNursingDownload.class);
        DailyNursingDownload download = dDao.queryById(info.get_id());
        return deleteDailyNursingDownload(download);
    }

    /**
     * 通过护理信息下载表数据删除; 包括(下载表,上传表,多媒体上传表)
     * @param download 下载表信息
     * @return true : 删除成功; false: 删除失败
     */
    static boolean deleteDailyNursingDownload(DailyNursingDownload download) {
        if(download == null) {
            return false;
        }
        // 下载表操作类
        BaseDao<DailyNursingDownload, Long> dDao = DaoHelper
                .getInstance().getDao(DailyNursingDownload.class);
        // 上传表操作类
        BaseDao<DailyNursingUpload, Long> uDao = DaoHelper
                .getInstance().getDao(DailyNursingUpload.class);
        // 媒体信息操作类
        BaseDao<DailyNursingMediaUpload, Long> mDao = DaoHelper
                .getInstance().getDao(DailyNursingMediaUpload.class);
        // 获取上传表中的数据
        DailyNursingUpload upload = uDao.query("dId", download.get_id());
        if(upload == null) {
            return false;
        }
        // 删除下载表
        dDao.delete(download);
        // 删除上传表
        uDao.delete(upload);
        // 删除媒体信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uId", upload.get_id());
        mDao.deleteList(map);
        return true;
    }

    /**
     * 获取老人随手拍信息指定时间的所有条目信息
     * @param id 老人ID
     * @param time 当前时间
     * @param item 当前护理条目
     * @return 老人所有条目信息
     */
    static List<NursingItemInfo> getReadilyShootItemInfoData(long id, long time, DisplaySignOrNursingItem item) {
        List<NursingItemInfo> list = new ArrayList<NursingItemInfo>();
        long[] times = StringUtils.getSectionTime(time);
        list.add(getReadilyShootItemInfo(id, times, item));
        return list;
    }

    /**
     * 将指定的随手拍信息保存到数据库中
     * @param d 下载表bean类信息
     * @return true: 保存成功; false: 保存失败
     */
    static boolean saveReadilyShootItemInfo(DailyLifeDownload d, List<MultiMedia> multiMedia) {
        try {
            // 随手拍下载表操作类
            Dao<DailyLifeDownload, Long> dDao = DaoHelper
                    .getInstance().getDao(DailyLifeDownload.class).getDao();
            // 随手拍上传表操作类
            Dao<DailyLifeUpload, Long> uDao = DaoHelper
                    .getInstance().getDao(DailyLifeUpload.class).getDao();
            // 随手拍媒体信息上传表操作类
            Dao<DailyLifeMediaUpload, Long> mDao = DaoHelper
                    .getInstance().getDao(DailyLifeMediaUpload.class).getDao();
            createAliasForImg(DailyLifeMediaUpload.class, multiMedia);
            DatabaseConnection databaseConnection = null;
            try {
                databaseConnection = dDao.startThreadConnection();
                dDao.setAutoCommit(databaseConnection, false);
                // 保存数据到下载表中
                dDao.create(d);
                // 将下载表中数据保存到上传表数据中
                DailyLifeUpload upload = new DailyLifeUpload(d, DataProcessUtils.getMultiMediaInfo(multiMedia, false));
                // 保存数据到上传表中
                uDao.create(upload);
                // 保存媒体文件
                if(multiMedia.size() > 0) {
                    mDao.create(getDailyLifeMediaUpload(upload.get_id(), multiMedia));
                }
                // 提交事务
                dDao.commit(databaseConnection);
                return true;
            } catch (SQLException ex) {
                dDao.rollBack(databaseConnection);
                ex.printStackTrace();
            } finally {
                try{
                    dDao.endThreadConnection(databaseConnection);
                }catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 更新随手拍信息
     * @param id 护理信息下载表ID
     * @param remarks 备注
     * @param multiMedia 要修改的媒体信息
     * @return true: 保存成功; false: 保存失败
     */
    static boolean updateReadilyShootItemInfo(long id, String remarks, List<MultiMedia> multiMedia) {
        // 随手拍下载表操作类
        BaseDao<DailyLifeDownload, Long> dDao = DaoHelper
                .getInstance().getDao(DailyLifeDownload.class);
        // 随手拍上传表操作类
        BaseDao<DailyLifeUpload, Long> uDao = DaoHelper
                .getInstance().getDao(DailyLifeUpload.class);
        // 随手拍媒体信息上传表操作类
        BaseDao<DailyLifeMediaUpload, Long> mDao = DaoHelper
                .getInstance().getDao(DailyLifeMediaUpload.class);

        DailyLifeDownload download = dDao.queryById(id);
        DailyLifeUpload upload = uDao.query("dId", id);
        if(download == null || upload == null) {
            return false;
        }
        createAliasForImg(DailyLifeMediaUpload.class, multiMedia);
        // 删除对应的多媒体信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uId", upload.get_id());
        mDao.deleteList(map);
        // 修改备注信息
        download.setReserved(remarks);
        upload.setReserved(remarks);
        // 修改媒体信息
        download.setMedia(DataProcessUtils.getMultiMediaInfo(multiMedia, true));
        upload.setMedia(DataProcessUtils.getMultiMediaInfo(multiMedia, false));
        // 修改数据到下载表中
        dDao.update(download);
        // 修改数据到上传表中
        uDao.update(upload);
        // 保存媒体文件
        if(multiMedia.size() > 0) {
            mDao.insertList(getDailyLifeMediaUpload(upload.get_id(), multiMedia));
        }
        return true;
    }

    /**
     * 通过护理信息下载表数据删除; 包括(下载表,上传表,多媒体上传表)
     * @param info 护理条目信息
     * @return true : 删除成功; false: 删除失败
     */
    static boolean deleteReadilyShootItemInfo(NursingItemInfo info) {
        // 下载表操作类
        BaseDao<DailyLifeDownload, Long> dDao = DaoHelper
                .getInstance().getDao(DailyLifeDownload.class);
        DailyLifeDownload download = dDao.queryById(info.get_id());
        return deleteReadilyShootItemInfo(download);
    }

    /**
     * 通过护理信息下载表数据删除; 包括(下载表,上传表,多媒体上传表)
     * @param download 下载表信息
     * @return true : 删除成功; false: 删除失败
     */
    static boolean deleteReadilyShootItemInfo(DailyLifeDownload download) {
        if(download == null) {
            return false;
        }
        // 随手拍下载表操作类
        BaseDao<DailyLifeDownload, Long> dDao = DaoHelper
                .getInstance().getDao(DailyLifeDownload.class);
        // 随手拍上传表操作类
        BaseDao<DailyLifeUpload, Long> uDao = DaoHelper
                .getInstance().getDao(DailyLifeUpload.class);
        // 随手拍媒体信息上传表操作类
        BaseDao<DailyLifeMediaUpload, Long> mDao = DaoHelper
                .getInstance().getDao(DailyLifeMediaUpload.class);
        // 获取上传表中的数据
        DailyLifeUpload upload = uDao.query("dId", download.get_id());
        if(upload == null) {
            return false;
        }
        // 删除下载表
        dDao.delete(download);
        // 删除上传表
        uDao.delete(upload);
        // 删除媒体信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uId", upload.get_id());
        mDao.deleteList(map);
        return true;
    }

    /**
     * 通过类型获取指定老人的类型条目
     * @param id 老人ID
     * @param times 当前时间段
     * @param item 护理条目
     * @return 指定老人类型条目
     */
    private static SignItemInfo getSignItemInfo(long id, long[] times, DisplaySignOrNursingItem item) {
        SignItemInfo itemInfo = new SignItemInfo();
        if("血压".equals(item.getTitle())) {
            PressureDownload p = getSignInfo(PressureDownload.class, id, times);
            if(p != null) {
                SignConfig config = SignConfigManager.getSignConfig(SignConfigManager.SIGN_BLOOD_PRESSURE);
                int accurate = (int) config.getAccur();
                itemInfo.setContent(
                        StringUtils.numberFormat(accurate, p.getHigh_blood_pressure()) + "/"
                                + StringUtils.numberFormat(accurate, p.getLow_blood_pressure()));
                itemInfo.setColor(DataProcessUtils
                        .getPressureColor(p.getHigh_blood_pressure(), p.getLow_blood_pressure(), config));
                itemInfo.setTime(p.getInspect_dt());
            }
        }else if("体温".equals(item.getTitle())) {
            TemperatureDownload t = getSignInfo(TemperatureDownload.class, id, times);
            if(t != null) {
                // 获取配置文件
                SignConfig config = SignConfigManager.getSignConfig(SignConfigManager.SIGN_TEMPERATURE);
                itemInfo = getSignItemInfo(t, t.getId(), config, t.getTemperature());
            }
        }else if("血糖".equals(item.getTitle())) {
            SugarDownload s = getSignInfo(SugarDownload.class, id, times);
            if(s != null) {
                // 获取配置文件
                SignConfig config = SignConfigManager.getSignConfig(SignConfigManager.SIGN_BLOOD_SUGAR);
                itemInfo = getSignItemInfo(s, s.getBlood_sugar_id(), config, s.getBlood_sugar());
            }
        }else if("心率".equals(item.getTitle())) {
            PulseDownload p = getSignInfo(PulseDownload.class, id, times);
            if(p != null) {
                // 获取配置文件
                SignConfig config = SignConfigManager.getSignConfig(SignConfigManager.SIGN_PULSE);
                itemInfo = getSignItemInfo(p, p.getPulse_id(), config, p.getPulse_number());
            }
        }else if("排便".equals(item.getTitle())) {
            DefecationDownload d = getSignInfo(DefecationDownload.class, id, times);
            if(d != null) {
                // 获取配置文件
                SignConfig config = SignConfigManager.getSignConfig(SignConfigManager.SIGN_DEFECATION);
                itemInfo = getSignItemInfo(d, d.getId(), config, d.getDefecation_times());
            }
        }else if("呼吸".equals(item.getTitle())) {
            BreathingDownload b = getSignInfo(BreathingDownload.class, id, times);
            if(b != null) {
                // 获取配置文件
                SignConfig config = SignConfigManager.getSignConfig(SignConfigManager.SIGN_BREATHING);
                itemInfo = getSignItemInfo(b, b.getId(), config, b.getBreathing_times());
            }
        }else if("饮水".equals(item.getTitle())) {
            DrinkingDownload b = getSignInfo(DrinkingDownload.class, id, times);
            if(b != null) {
                // 获取配置文件
                SignConfig config = SignConfigManager.getSignConfig(SignConfigManager.SIGN_DRINK);
                itemInfo = getSignItemInfo(b, b.getId(), config, b.getWater_amount());
            }
        }else if("进食".equals(item.getTitle())) {
            EatDownload b = getSignInfo(EatDownload.class, id, times);
            if(b != null) {
                itemInfo.setContent(b.getDescriptiveInfo());
                itemInfo.setColor(Color.GRAY);
                itemInfo.setTime(b.getInspect_dt());
            }
        }else if("睡眠".equals(item.getTitle())) {
            SleepingDownload b = getSignInfo(SleepingDownload.class, id, times);
            if(b != null) {
                itemInfo.setContent(b.getSleep_quality());
                itemInfo.setColor(Color.GRAY);
                itemInfo.setTime(b.getInspect_dt());
            }
        }
        itemInfo.setType(item.getType());
        itemInfo.setName(item.getTitle());
        return itemInfo;
    }

    /**
     * 获取指定条目的护理信息, 不包括条目类型ID以及条目名称
     * @param t 要处理信息条目
     * @param id 当前条目id
     * @param config 当前条目类型配置文件
     * @param value 当前要处理的值
     * @param <T> 泛型,实现BaseDownload类
     * @return 处理后的信息条目
     */
    private static <T extends BaseDownload> SignItemInfo getSignItemInfo(T t, long id , SignConfig config, double value) {
        SignItemInfo info = new SignItemInfo();
        info.set_id(t.get_id());
        info.setId(id);
        info.setTime(t.getInspect_dt());
        info.setInspect_user_id(t.getInspect_user_id());
        info.setInspect_user_name(t.getInspect_user_name());
        info.setEntry_user_id(t.getEntry_user_id());
        info.setEntry_user_name(t.getEntry_user_name());
        info.setApprovalStatus(t.getApproval_status());
        info.setContent(StringUtils.numberFormat((int) config.getAccur(), value));
        info.setColor(DataProcessUtils.getColor(value, config));
        info.setReserved(t.getReserved());
        return info;
    }

    /**
     * 获取某一个条目的类型(包含指定的配单信息)
     * @param id 老人ID
     * @param task 配单任务
     * @param times 某一天时间段; 一天的开始时间和结束时间
     * @param item 当前类型的条目类型
     * @return 条目类型
     */
    private static NursingItemInfo getBatchEditingNursingItemInfo(
            long id, SignAndNursingTask task ,long[] times, DisplaySignOrNursingItem item) {
        NursingItemInfo nursingItemInfo = new NursingItemInfo();
        // 添加护理类型信息
        nursingItemInfo.setType(item.getType());
        nursingItemInfo.setName(item.getTitle());
        // 创建未添加容器
        nursingItemInfo.setNotUploaded(new ArrayList<DailyNursingDownload>());
        // 添加指定的配单信息
        if(task == null) {
            // 没有指定此项配单
            nursingItemInfo.setSelect(false);
            nursingItemInfo.setNurse_times(0);
            return nursingItemInfo;
        }else {
            nursingItemInfo.setSelect(true);
            nursingItemInfo.setMax_times(task.getNumber_nursing());
            nursingItemInfo.setPeriod_type(task.getPeriod_type());
        }
        // 获取护理信息
        List<DailyNursingDownload> list = getNursingInfoData(id, times, item.getType());
        if(list == null) {
            list = new ArrayList<DailyNursingDownload>();
        }
        nursingItemInfo.setNurse_times(list.size());
        for (DailyNursingDownload d : list) {
            if(d.getDaily_id() == -1) {
                nursingItemInfo.getNotUploaded().add(d);
            }
        }
        return nursingItemInfo;
    }

    /**
     * 为媒体上传表中的相册图片起别名
     * @param clazz 多媒体表类字节码
     * @param multiMedia 多媒体信息集合
     */
    private static <T extends BaseMediaUpload> void createAliasForImg(Class<T> clazz, List<MultiMedia> multiMedia) {
        BaseDao<T, Long> dao = DaoHelper.getInstance().getDao(clazz);
        for (MultiMedia media : multiMedia) {
            if(media.getType() == MultiMedia.MULTI_MEDIA_TYPE_PICTURE
                    && media.getSourceType() == MultiMedia.SOURCE_FROM_LOCAL) {
                // 图片且来自本地相册
                if(StringUtils.isEmpty(media.getUri())) {
                    continue;
                }
                int index = media.getUri().lastIndexOf(".");
                if(index == -1) {
                    continue;
                }
                BaseMediaUpload upload = dao.query("path", media.getUri());
                if(upload == null) {
                    // 设置别名
                    media.setAlias(UUID.randomUUID().toString() + media.getUri().substring(index, media.getUri().length()));
                }else {
                    // 图片已经存在, 并且取得了别名
                    media.setAlias(upload.getAlias());
                }
            }
        }

    }

    /**
     * 获取护理信息媒体信息
     * @param uId 上传表信息主键值
     * @param multiMedia 媒体信息
     */
    private static List<DailyNursingMediaUpload> getDailyNursingMediaUpload(long uId, List<MultiMedia> multiMedia) {
        List<DailyNursingMediaUpload> result = new ArrayList<DailyNursingMediaUpload>();
        for (MultiMedia media : multiMedia) {
            DailyNursingMediaUpload mUpload = new DailyNursingMediaUpload();
            mUpload.setuId(uId);
            mUpload.setPath(media.getUri());
            mUpload.setType(media.getType());
            mUpload.setAlias(media.getAlias());
            result.add(mUpload);
        }
        return result;
    }

    /**
     * 获取某一个条目的类型
     * @param id 老人ID
     * @param times 某一天时间段; 一天的开始时间和结束时间
     * @param item 当前类型的条目类型
     * @return 条目类型
     */
    private static NursingItemInfo getReadilyShootItemInfo(long id, long[] times, DisplaySignOrNursingItem item) {
        NursingItemInfo nursingItemInfo = new NursingItemInfo();
        List<DailyLifeDownload> list = getReadilyShootInfoData(id, times);
        if(list == null) {
            list = new ArrayList<DailyLifeDownload>();
        }
        nursingItemInfo.setType(item.getType());
        nursingItemInfo.setName(item.getTitle());
        nursingItemInfo.setNurse_times(list.size());
        return nursingItemInfo;
    }

    /**
     * 获取随手拍媒体信息
     * @param uId 上传表信息主键值
     * @param multiMedia 媒体信息
     */
    private static List<DailyLifeMediaUpload> getDailyLifeMediaUpload(long uId, List<MultiMedia> multiMedia) {
        List<DailyLifeMediaUpload> result = new ArrayList<DailyLifeMediaUpload>();
        for (MultiMedia media : multiMedia) {
            DailyLifeMediaUpload mUpload = new DailyLifeMediaUpload();
            mUpload.setuId(uId);
            mUpload.setPath(media.getUri());
            mUpload.setType(media.getType());
            mUpload.setAlias(media.getAlias());
            result.add(mUpload);
        }
        return result;
    }

    /**
     * 从数据库中获取指定的体征信息
     * @param clazz 体征项类字节码
     * @param id 指定老人ID
     * @param times 指定的时间段
     * @param <T> 下载表
     * @return 体征项集合, 异常则返回null
     */
    private static <T extends IDownloadable> T getSignInfo(Class<T> clazz, long id, long[] times) {
        BaseDao<T, Long> dao = DaoHelper.getInstance().getDao(clazz);
        try {
            QueryBuilder<T, Long> queryBuilder = dao.getDao().queryBuilder();
            Where<T, Long> wheres = queryBuilder.where();
            wheres.eq("old_people_id", id)
                    .and()
                    .ge("inspect_dt", times[0])
                    .and()
                    .le("inspect_dt", times[1]);
            queryBuilder.orderBy("inspect_dt", false);
            List<T> list = dao.queryList(queryBuilder.prepare());
            if(list != null && list.size() > 0) {
                return list.get(0);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 分页加载体征数据详情信息
     * @param clazz 数据字节码
     * @param pageNo 当前请求数据页面
     * @param id 当前老人ID
     * @return 详情信息
     */
    private static <T extends BaseDownload> List<T> getSignInfoData(Class<T> clazz, int pageNo, long id) {
        BaseDao<T, Long> dao = DaoHelper.getInstance().getDao(clazz);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("old_people_id", id);
        List<T> list = dao.queryAll(map, "inspect_dt" , false,
                (pageNo - 1) * DataProcessUtils.PAGE_SIZE, DataProcessUtils.PAGE_SIZE);
        if(list == null) {
            list = new ArrayList<T>();
        }
        return list;
    }

    /**
     * 将数据保存到指定的下载表和上传表中
     * @param dClass 下载表类字节码
     * @param uClass 上传表类字节码
     * @param t 下载表数据
     * @param e 上床表数据
     * @param <T> 继承BaseDownload类的bean类
     * @param <E> 继承BaseUpload类的bean类
     * @return true: 保存成功; false: 保存失败
     */
    private static<T extends BaseDownload, E extends BaseUpload> boolean saveSignInfoToDB
    (Class<T> dClass, Class<E> uClass, T t, E e) {
        try {
            BaseDao<T, Long> dDao = DaoHelper.getInstance().getDao(dClass);
            BaseDao<E, Long> uDao = DaoHelper.getInstance().getDao(uClass);
            Dao<T, Long> tDao = dDao.getDao();
            DatabaseConnection databaseConnection = null;
            try {
                databaseConnection = tDao.startThreadConnection();
                tDao.setAutoCommit(databaseConnection, false);
                // 保存数据到下载表中
                tDao.create(t);
                // 将下载表中数据主键值保存到上传表数据中
                e.setdId(t.get_id());
                // 保存数据到上传表中
                uDao.insert(e);
                // 提交事务
                tDao.commit(databaseConnection);
                return true;
            } catch (SQLException ex) {
                tDao.rollBack(databaseConnection);
                ex.printStackTrace();
            } finally {
                try{
                    tDao.endThreadConnection(databaseConnection);
                }catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 获取指定时间段中所有的护理信息
     * @param id 老人ID
     * @param times 时间段
     * @return 当前时间段所有的护理信息集合
     */
    private static List<DailyNursingDownload> getNursingInfoData(long id, long[] times) {
        BaseDao<DailyNursingDownload, Long> dao = DaoHelper.getInstance()
                .getDao(DailyNursingDownload.class);
        try {
            QueryBuilder<DailyNursingDownload, Long> queryBuilder = dao.getDao().queryBuilder();
            Where<DailyNursingDownload, Long> wheres = queryBuilder.where();
            wheres.eq("old_people_id", id)
                    .and()
                    .ge("nursing_dt", times[0])
                    .and()
                    .le("nursing_dt", times[1]);
            return dao.queryList(queryBuilder.prepare());
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<DailyNursingDownload>();
    }

    /**
     * 获取指定时间段中所有的护理信息
     * @param id 老人ID
     * @param times 时间段
     * @param type 条目类型
     * @return 当前时间段所有的护理信息集合
     */
    private static List<DailyNursingDownload> getNursingInfoData(long id, long[] times, long type) {
        BaseDao<DailyNursingDownload, Long> dao = DaoHelper.getInstance()
                .getDao(DailyNursingDownload.class);
        try {
            QueryBuilder<DailyNursingDownload, Long> queryBuilder = dao.getDao().queryBuilder();
            Where<DailyNursingDownload, Long> wheres = queryBuilder.where();
            wheres.eq("old_people_id", id)
                    .and()
                    .eq("nurs_items_id", type)
                    .and()
                    .ge("nursing_dt", times[0])
                    .and()
                    .le("nursing_dt", times[1]);
            return dao.queryList(queryBuilder.prepare());
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定时间段中所有的随手拍信息
     * @param id 老人ID
     * @param times 时间段
     * @return 当前时间段所有的随手拍信息集合
     */
    private static List<DailyLifeDownload> getReadilyShootInfoData(long id, long[] times) {
        BaseDao<DailyLifeDownload, Long> dao = DaoHelper
                .getInstance().getDao(DailyLifeDownload.class);
        try {
            QueryBuilder<DailyLifeDownload, Long> queryBuilder = dao.getDao().queryBuilder();
            Where<DailyLifeDownload, Long> wheres = queryBuilder.where();
            wheres.eq("old_people_id", id)
                    .and()
                    .ge("nursing_dt", times[0])
                    .and()
                    .le("nursing_dt", times[1]);
            return dao.queryList(queryBuilder.prepare());
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
