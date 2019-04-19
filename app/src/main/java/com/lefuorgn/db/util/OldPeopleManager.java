package com.lefuorgn.db.util;

import com.lefuorgn.bean.User;
import com.lefuorgn.db.base.BaseDao;
import com.lefuorgn.db.model.basic.Bed;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.model.basic.OldPeopleFamily;
import com.lefuorgn.db.model.download.DailyLifeDownload;
import com.lefuorgn.db.model.download.DailyNursingDownload;
import com.lefuorgn.lefu.bean.FamilyTelephone;
import com.lefuorgn.lefu.bean.MultiMedia;
import com.lefuorgn.lefu.bean.NursingInfo;
import com.lefuorgn.lefu.bean.NursingItemInfo;
import com.lefuorgn.lefu.bean.NursingOldPeople;
import com.lefuorgn.lefu.bean.SearchConditionGrid;
import com.lefuorgn.lefu.bean.SignInfo;
import com.lefuorgn.lefu.bean.SignItemInfo;
import com.lefuorgn.util.PinyinComparator;
import com.lefuorgn.util.PinyinUtils;
import com.lefuorgn.util.RoomComparator;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 老人表管理类
 */
public class OldPeopleManager {

    /**
     * 获取所有的老人, 老人是按姓的首字母进行的排序
     * @param attention true: 只被关注的老人; false: 所有的老人
     * @return 老人集合
     */
    public static List<OldPeople> getOldPeople(boolean attention) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("check_in_status", 0);
        if(attention) {
            map.put("cAttention", true);
        }
        BaseDao<OldPeople, Long> dao = DaoHelper.getInstance().getDao(OldPeople.class);
        List<OldPeople> list = dao.queryList(map);
        if(list == null) {
            list = new ArrayList<OldPeople>();
        }else {
            Collections.sort(list, new PinyinComparator<OldPeople>());
        }
        return list;
    }

    /**
     * 根据条件搜索老人
     * 当first为true时先去查找是否存在关注的老人,如果存在直接返回数据; 如果不存在则查找所有的老人,并返回
     * 当first为false时: 会根据attention进行数据加载;
     *          attention: true 直接加载关注的老人
     *          attention: false 直接加载所有的老人
     * @param condition 查询条件
     * @param first 是否是第一次加载数据
     * @param attention 除去第一次记录当前请求老人数据类型
     * @return 指定条件下的老人列表
     */
    private static List<OldPeople> getOldPeople(SearchConditionGrid condition, boolean first, boolean attention) {
        BaseDao<OldPeople, Long> dao = DaoHelper
                .getInstance().getDao(OldPeople.class);
        Map<String, Object> map = new HashMap<String, Object>();
        if(first) {
            // 首次加载数据, 先加载关注的老人
            map.put("cAttention", true);
        }else {
            // 不是首次加载
            if(attention) {
                map.put("cAttention", true);
            }
        }
        map.put("check_in_status", 0);
        if(!"-1".equals(condition.getBuildingNo())) {
            map.put("floor_no", condition.getBuildingNo());
            if(!"-1".equals(condition.getUnitNo())) {
                map.put("floor_layer", condition.getUnitNo());
                if(!"-1".equals(condition.getRoomNo())) {
                    map.put("room_no", condition.getRoomNo());
                }
            }
        }
        List<OldPeople> list = dao.queryList(map);
        if(first && (list == null || list.size() == 0)) {
            // 首次加载不存在关注的老人
            TLog.log("首次加载不存在....");
            map.remove("cAttention");
            list = dao.queryList(map);
        }
        if(list == null) {
            return new ArrayList<OldPeople>();
        }else {
            if(condition.getSort() == SearchConditionGrid.ROOM) {
                // 根据房间号
                Collections.sort(list, new RoomComparator());
            }else {
                // 根据姓名进行排序
                Collections.sort(list, new PinyinComparator<OldPeople>());
            }
            return list;
        }
    }

    /**
     * 处理老人信息,即转换老人名称为拼音以及创建老人床位信息
     */
    public static void convertedToPinyin() {
        BaseDao<OldPeople, Long> dao = DaoHelper.getInstance().getDao(OldPeople.class);
        List<OldPeople> list = dao.queryList("sortLetters", "");
        if(list != null && list.size() > 0) {
            PinyinUtils.convertedToPinyin(list);
            createBedInfo(list);
            dao.insertOrUpdate(list);
        }
    }

    /**
     * 创建老人床位信息; 根据床位表, 处理当前老人表中的数据
     * @param list 老人列表
     */
    private static void createBedInfo(List<OldPeople> list) {
        BaseDao<Bed, Long> dao = DaoHelper.getInstance().getDao(Bed.class);
        for (OldPeople oldPeople : list) {
            Bed bed = dao.query("id", oldPeople.getBed_id());
            if(bed != null) {
                oldPeople.setFloor_no(bed.getFloor_no());
                oldPeople.setFloor_layer(bed.getFloor_layer());
                oldPeople.setRoom_no(bed.getRoom_no());
                oldPeople.setFace(bed.getFace());
            }
        }
    }

    /**
     * 根据老人ID获取某一个老人的家属列表
     * @param oldPeopleId 老人ID
     * @return 家属列表
     */
    public static List<OldPeopleFamily> getOldPeopleFamily(long oldPeopleId) {
        BaseDao<OldPeopleFamily, Long> dao = DaoHelper
                .getInstance().getDao(OldPeopleFamily.class);
        List<OldPeopleFamily> list = dao.queryList("old_people_id", oldPeopleId);
        if(list == null) {
            list = new ArrayList<OldPeopleFamily>();
        }
        return list;
    }

    /**
     * 获取老人家属联系方式
     * @param oldPeopleId 老人ID
     * @return 老人家属联系信息集合
     */
    public static List<FamilyTelephone> getFamilyTelephone(long oldPeopleId) {
        List<FamilyTelephone> result = new ArrayList<FamilyTelephone>();
        List<OldPeopleFamily> family = getOldPeopleFamily(oldPeopleId);
        for (OldPeopleFamily peopleFamily : family) {
            if(!StringUtils.isEmpty(peopleFamily.getMobile())) {
                FamilyTelephone telephone = new FamilyTelephone();
                telephone.setName("家属: " + peopleFamily.getRelatives_name());
                telephone.setTelephone(peopleFamily.getMobile());
                result.add(telephone);
            }
            if(!StringUtils.isEmpty(peopleFamily.getTelephone())) {
                FamilyTelephone telephone = new FamilyTelephone();
                telephone.setName("家属: " + peopleFamily.getRelatives_name());
                telephone.setTelephone(peopleFamily.getTelephone());
                result.add(telephone);
            }
        }
        return result;
    }

    /**
     * 修改老人的关注状态
     * @param oldPeople 老人信息
     */
    public static void updateOldPeopleAttention(OldPeople oldPeople) {
        BaseDao<OldPeople, Long> dao = DaoHelper
                .getInstance().getDao(OldPeople.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cAttention", oldPeople.iscAttention());
        dao.update("id", oldPeople.getId(), map);
    }

    /**
     * 根据搜索条件获取体征信息, 每个类型条目只选择指定时间最近的一条数据; 如果没有数据则为空数据(不是null)
     * @param condition 搜索条件
     * @param items 当前要展示的条目类型
     * @param first 是否是第一次加载数据
     * @param attention 除去第一次记录当前请求老人数据类型
     * @return 数据集合
     */
    public static List<SignInfo> getSignInfoData(SearchConditionGrid condition
            , List<DisplaySignOrNursingItem> items, boolean first, boolean attention) {
        if(items == null || items.size() == 0) {
            return new ArrayList<SignInfo>();
        }
        List<OldPeople> list = getOldPeople(condition, first, attention);
        List<SignInfo> mList = new ArrayList<SignInfo>();
        long time = StringUtils.getFormatData(condition.getDate(), "yyyy-MM-dd");
        for (OldPeople oldPeople : list) {
            SignInfo signInfo = new SignInfo();
            signInfo.setOldPeopleId(oldPeople.getId());
            signInfo.setOldPeopleName(oldPeople.getElderly_name());
            signInfo.setLevel(oldPeople.getMonitor_level());
            signInfo.setcAttention(oldPeople.iscAttention());
            signInfo.setSignItemInfos(SignAndNursingManager
                    .getSignItemInfoData(oldPeople.getId(), time, items));
            mList.add(signInfo);
        }

        return mList;
    }

    /**
     * 分页加载某一类型体征数据
     * @param mItem 当前体征数据类型
     * @param pageNo 当前加载数据页号
     * @param id 当前加载数据的老人ID
     */
    public static List<SignItemInfo> getSignInfoDetails(DisplaySignOrNursingItem mItem, int pageNo, long id) {
        if("血糖".equals(mItem.getTitle())) {
            return SignAndNursingManager
                    .getSugarItemInfo(pageNo, id, SignConfigManager.SIGN_BLOOD_SUGAR);
        }else if("体温".equals(mItem.getTitle())) {
            return SignAndNursingManager
                    .getTemperatureItemInfo(pageNo, id, SignConfigManager.SIGN_TEMPERATURE);
        }else if("呼吸".equals(mItem.getTitle())) {
            return SignAndNursingManager
                    .getBreathingItemInfo(pageNo, id, SignConfigManager.SIGN_BREATHING);
        }else if("血压".equals(mItem.getTitle())) {
            return SignAndNursingManager
                    .getPressureItemInfo(pageNo, id, SignConfigManager.SIGN_BLOOD_PRESSURE);
        }else if("心率".equals(mItem.getTitle())) {
            return SignAndNursingManager
                    .getPulseItemInfo(pageNo, id, SignConfigManager.SIGN_PULSE);
        }else if("排便".equals(mItem.getTitle())) {
            return SignAndNursingManager
                    .getDefecationItemInfo(pageNo, id, SignConfigManager.SIGN_DEFECATION);
        }else if("饮水".equals(mItem.getTitle())) {
            return SignAndNursingManager
                    .getDrinkingItemInfo(pageNo, id, SignConfigManager.SIGN_DRINK);
        }else if("睡眠".equals(mItem.getTitle())) {
            return SignAndNursingManager
                    .getSleepingItemInfo(pageNo, id);
        }else if("进食".equals(mItem.getTitle())) {
            return SignAndNursingManager
                    .getEatItemInfo(pageNo, id);
        }else {
            return new ArrayList<SignItemInfo>();
        }
    }

    /**
     * 保存体征信息, 返回保存成功的时间
     * @param user 用户信息
     * @param agencyId 机构ID
     * @param id 老人ID
     * @param name 老人名称
     * @param value 对应的值
     * @param reserved 备注
     * @param type 当前条目保存类型
     * @return 大于0: 保存成功; 0: 保存失败
     */
    public static long saveSignItemInfo(User user, long agencyId, long id, String name, String value, String reserved, String type) {
        if("血糖".equals(type)) {
            // 保存血糖
            return SignAndNursingManager.saveSugarItemInfo(user, agencyId, id, name, value, reserved);
        }else if("体温".equals(type)) {
            // 保存体温
            return SignAndNursingManager.saveTemperatureItemInfo(user, agencyId, id, name, value, reserved);
        }else if("呼吸".equals(type)) {
            // 保存呼吸
            return SignAndNursingManager.saveBreathingItemInfo(user, agencyId, id, name, value, reserved);
        }else if("血压".equals(type)) {
            // 保存血压
            return SignAndNursingManager.savePressureItemInfo(user, agencyId, id, name, value, reserved);
        }else if("心率".equals(type)) {
            // 保存心率
            return SignAndNursingManager.savePulseItemInfo(user, agencyId, id, name, value, reserved);
        }else if("排便".equals(type)) {
            // 保存排便
            return SignAndNursingManager.saveDefecationItemInfo(user, agencyId, id, name, value, reserved);
        }else if("饮水".equals(type)) {
            // 保存饮水
            return SignAndNursingManager.saveDrinkingItemInfo(user, agencyId, id, name, value, reserved);
        }else if("进食".equals(type)) {
            // 保存进食
            return SignAndNursingManager.saveEatItemInfo(user, agencyId, id, name, value);
        }else if("睡眠".equals(type)) {
            // 保存睡眠
            return SignAndNursingManager.saveSleepingItemInfo(user, agencyId, id, name, value, reserved);
        }
        return 0;
    }

    /**
     * 通过设备保存血压和心率
     * @param user 用户信息
     * @param agencyId 机构ID
     * @param id 老人ID
     * @param name 老人名称
     * @param value 血压对应的值
     * @param heartRate  心率
     * @return true: 保存成功; false 保存失败
     */
    public static boolean saveSignItemInfoByDevice(User user, long agencyId, long id
            , String name, String value, String heartRate) {
        long num1 = SignAndNursingManager.savePressureItemInfo(user, agencyId, id, name, value, "");
        long num2 = SignAndNursingManager.savePulseItemInfo(user, agencyId, id, name, heartRate, "");
        return num1 > 0 && num2 > 0;
    }

    /**
     * 更新体征信息
     * @return true: 更新成功; false: 更新失败
     */
    public static boolean updateSignItemInfo(SignItemInfo info, String type) {
        if("血糖".equals(type)) {
            // 保存血糖
            return SignAndNursingManager.updateSugarItemInfo(info);
        }else if("体温".equals(type)) {
            // 保存体温
            return SignAndNursingManager.updateTemperatureItemInfo(info);
        }else if("呼吸".equals(type)) {
            // 保存呼吸
            return SignAndNursingManager.updateBreathingItemInfo(info);
        }else if("血压".equals(type)) {
            // 保存血压
            return SignAndNursingManager.updatePressureItemInfo(info);
        }else if("心率".equals(type)) {
            // 保存心率
            return SignAndNursingManager.updatePulseItemInfo(info);
        }else if("排便".equals(type)) {
            // 保存排便
            return SignAndNursingManager.updateDefecationItemInfo(info);
        }else if("饮水".equals(type)) {
            // 保存饮水
            return SignAndNursingManager.updateDrinkingItemInfo(info);
        }else if("进食".equals(type)) {
            // 保存进食
            return SignAndNursingManager.updateEatItemInfo(info);
        }else if("睡眠".equals(type)) {
            // 保存睡眠
            return SignAndNursingManager.updateSleepItemInfo(info);
        }
        return false;
    }

    /**
     * 根据搜索条件获取护理信息; 数据类型为当前时间段条目的个数
     * @param condition 搜索条件
     * @param items 当前要展示的条目类型
     * @param first 是否是第一次加载数据
     * @param attention 除去第一次记录当前请求老人数据类型
     * @return 数据集合
     */
    public static List<NursingInfo> getNursingData(SearchConditionGrid condition
            , List<DisplaySignOrNursingItem> items, boolean first, boolean attention) {
        if(items == null || items.size() == 0) {
            return new ArrayList<NursingInfo>();
        }
        List<OldPeople> list = getOldPeople(condition, first, attention);
        List<NursingInfo> mList = new ArrayList<NursingInfo>();
        long time = StringUtils.getFormatData(condition.getDate(), "yyyy-MM-dd");
        for (OldPeople oldPeople : list) {
            NursingInfo nursingInfo = new NursingInfo();
            nursingInfo.setOldPeopleId(oldPeople.getId());
            nursingInfo.setOldPeopleName(oldPeople.getElderly_name());
            nursingInfo.setLevel(oldPeople.getMonitor_level());
            nursingInfo.setcAttention(oldPeople.iscAttention());
            nursingInfo.setNursingItemInfoList(SignAndNursingManager
                    .getNursingItemInfoData(oldPeople.getId(), time, items));
            mList.add(nursingInfo);
        }
        return mList;
    }

    public static List<NursingInfo> getBatchEditingNursingData(SearchConditionGrid condition
            , List<DisplaySignOrNursingItem> items) {
        if(items == null || items.size() == 0) {
            return new ArrayList<NursingInfo>();
        }
        List<OldPeople> list = getOldPeople(condition, false, true);
        List<NursingInfo> mList = new ArrayList<NursingInfo>();
        long time = StringUtils.getFormatData(condition.getDate(), "yyyy-MM-dd");
        for (OldPeople oldPeople : list) {
            NursingInfo nursingInfo = new NursingInfo();
            nursingInfo.setOldPeopleId(oldPeople.getId());
            nursingInfo.setOldPeopleName(oldPeople.getElderly_name());
            nursingInfo.setLevel(oldPeople.getMonitor_level());
            nursingInfo.setcAttention(oldPeople.iscAttention());
            nursingInfo.setSelect(true);
            nursingInfo.setNursingItemInfoList(SignAndNursingManager
                    .getBatchEditingNursingItemInfoData(oldPeople, time, items));
            mList.add(nursingInfo);
        }
        return mList;
    }

    /**
     * 分页加载某一类型护理数据
     * @param item 当前条目类型
     * @param pageNo 当前页面
     * @param id 老人ID
     * @return 当前类型数据分页数据集合
     */
    public static List<NursingItemInfo> getNursingInfoDetails(DisplaySignOrNursingItem item, int pageNo, long id) {
        List<NursingItemInfo> data = new ArrayList<NursingItemInfo>();
        BaseDao<DailyNursingDownload, Long> dao = DaoHelper
                .getInstance().getDao(DailyNursingDownload.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("old_people_id", id);
        map.put("nurs_items_id", item.getType());
        List<DailyNursingDownload> list = dao.queryAll(map, "nursing_dt" , false,
                (pageNo - 1) * DataProcessUtils.PAGE_SIZE, DataProcessUtils.PAGE_SIZE);
        if(list == null) {
            list = new ArrayList<DailyNursingDownload>();
        }
        for (DailyNursingDownload download : list) {
            NursingItemInfo info = new NursingItemInfo();
            info.set_id(download.get_id());
            info.setId(download.getDaily_id());
            info.setType(item.getType());
            info.setName(item.getTitle());
            info.setReserved(download.getReserved());
            info.setMedia(DataProcessUtils.getMultiMediaList(download.getMedia()));
            info.setCaregiver_name(download.getCaregiver_name());
            info.setNursing_dt(download.getNursing_dt());
            data.add(info);
        }
        return data;
    }

    /**
     * 保存护理信息
     * @param user 用户信息
     * @param agencyId 机构ID
     * @param type 当前数据类型
     * @param remarks 备注
     * @param multiMedia 多媒体rul
     * @param data 老人信息
     * @return true: 保存成功; false: 保存失败
     */
    public static boolean saveNursingItemInfo(User user, long agencyId, long type, String remarks,
               List<MultiMedia> multiMedia, List<NursingOldPeople> data) {
        if(data == null || data.size() == 0) {
            return false;
        }
        long time = System.currentTimeMillis();
        for (NursingOldPeople oldPeople : data) {
            // 创建下载表信息
            DailyNursingDownload download = new DailyNursingDownload();
            download.setNurs_items_id(type);
            download.setOld_people_id(oldPeople.getOld_people_id());
            download.setNursing_dt(time);
            download.setEntry_staff(user.getUser_id());
            download.setAgency_id(agencyId);
            download.setCaregiver_id(user.getUser_id());
            download.setCaregiver_name(user.getUser_name());
            download.setMedia(DataProcessUtils.getMultiMediaInfo(multiMedia, true));
            download.setReserved(remarks);
            SignAndNursingManager.saveNursingItemInfo(download, multiMedia);
        }
        return true;
    }

    /**
     * 通过条目信息删除指定的护理信息
     * @param info 条目信息
     * @return true: 删除成功; false: 删除失败
     */
    public static boolean deleteNursingItemInfo(NursingItemInfo info) {
        return SignAndNursingManager.deleteNursingItemInfo(info);
    }

    /**
     * 批量操作用户护理信息
     * @param user 当前用户对象
     * @param agencyId 机构ID
     * @param data 批量操作的数据集合
     * @return true: 批量操作成功; false: 批量操作失败
     */
    public static boolean saveBatchEditingNursingData(User user, long agencyId, List<NursingInfo> data) {
        long time = System.currentTimeMillis();
        for (NursingInfo nursingInfo : data) {
            for (NursingItemInfo info : nursingInfo.getNursingItemInfoList()) {
                if(info.getCurrent_times() >= 0) {
                    // 数据增加
                    for (int i = 0; i < info.getCurrent_times(); i++) {
                        // 保存护理信息
                        DailyNursingDownload download = new DailyNursingDownload();
                        download.setNurs_items_id(info.getType());
                        download.setOld_people_id(nursingInfo.getOldPeopleId());
                        download.setNursing_dt(time);
                        download.setEntry_staff(user.getUser_id());
                        download.setAgency_id(agencyId);
                        download.setCaregiver_id(user.getUser_id());
                        download.setCaregiver_name(user.getUser_name());
                        download.setMedia("");
                        download.setReserved(info.getReserved());
                        SignAndNursingManager.saveNursingItemInfo(download, new ArrayList<MultiMedia>());
                    }

                }else {
                    // 删除本地数据库数据
                    List<DailyNursingDownload> download = info.getNotUploaded();
                    for (int k = 0; k < Math.abs(info.getCurrent_times()); k++) {
                        if(k < download.size()) {
                            SignAndNursingManager.deleteDailyNursingDownload(download.get(k));
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 更新护理信息
     * @param id 条目下载表主键ID
     * @param remarks 备注
     * @param multiMedia 媒体信息
     * @return true: 更新成功; false: 更新失败
     */
    public static boolean updateNursingItemInfo(long id, String remarks, List<MultiMedia> multiMedia) {
        return SignAndNursingManager.updateNursingItemInfo(id, remarks, multiMedia);
    }

    /**
     * 根据搜索条件获取随手拍信息; 数据类型为当前时间段条目的个数
     * @param condition 搜索条件
     * @param items 当前要展示的条目类型
     * @param first 是否是第一次加载数据
     * @param attention 除去第一次记录当前请求老人数据类型
     * @return 数据集合
     */
    public static List<NursingInfo> getReadilyShootData(SearchConditionGrid condition
            , List<DisplaySignOrNursingItem> items, boolean first, boolean attention) {
        if(items == null || items.size() == 0) {
            return new ArrayList<NursingInfo>();
        }
        List<OldPeople> list = getOldPeople(condition, first, attention);
        List<NursingInfo> mList = new ArrayList<NursingInfo>();
        long time = StringUtils.getFormatData(condition.getDate(), "yyyy-MM-dd");
        for (OldPeople oldPeople : list) {
            NursingInfo nursingInfo = new NursingInfo();
            nursingInfo.setOldPeopleId(oldPeople.getId());
            nursingInfo.setOldPeopleName(oldPeople.getElderly_name());
            nursingInfo.setLevel(oldPeople.getMonitor_level());
            nursingInfo.setcAttention(oldPeople.iscAttention());
            nursingInfo.setNursingItemInfoList(SignAndNursingManager
                    .getReadilyShootItemInfoData(oldPeople.getId(), time, items.get(0)));
            mList.add(nursingInfo);
        }
        return mList;
    }

    /**
     * 分页加载某一类型随手拍数据
     * @param pageNo 当前页面
     * @param id 老人ID
     * @return 当前类型数据分页数据集合
     */
    public static List<NursingItemInfo> getReadilyShootDetails(int pageNo, long id) {
        List<NursingItemInfo> data = new ArrayList<NursingItemInfo>();
        BaseDao<DailyLifeDownload, Long> dao = DaoHelper
                .getInstance().getDao(DailyLifeDownload.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("old_people_id", id);
        List<DailyLifeDownload> list = dao.queryAll(map, "nursing_dt" , false,
                (pageNo - 1) * DataProcessUtils.PAGE_SIZE, DataProcessUtils.PAGE_SIZE);
        if(list == null) {
            list = new ArrayList<DailyLifeDownload>();
        }
        for (DailyLifeDownload download : list) {
            NursingItemInfo info = new NursingItemInfo();
            info.set_id(download.get_id());
            info.setId(download.getDaily_id());
            info.setReserved(download.getReserved());
            info.setMedia(DataProcessUtils.getMultiMediaList(download.getMedia()));
            info.setCaregiver_name(download.getCaregiver_name());
            info.setNursing_dt(download.getNursing_dt());
            data.add(info);
        }
        return data;
    }

    /**
     * 保存随手拍信息
     * @param user 用户信息
     * @param agencyId 机构ID
     * @param remarks 备注
     * @param multiMedia 多媒体rul
     * @param data 老人信息
     * @return true: 保存成功; false: 保存失败
     */
    public static boolean saveReadilyShootItemInfo(User user, long agencyId, String remarks,
                    List<MultiMedia> multiMedia, List<NursingOldPeople> data) {
        if(data == null || data.size() == 0) {
            return false;
        }
        long time = System.currentTimeMillis();
        for (NursingOldPeople oldPeople : data) {
            // 创建下载表信息
            DailyLifeDownload download = new DailyLifeDownload();
            download.setOld_people_id(oldPeople.getOld_people_id());
            download.setNursing_dt(time);
            download.setEntry_staff(user.getUser_id());
            download.setAgency_id(agencyId);
            download.setCaregiver_id(user.getUser_id());
            download.setCaregiver_name(user.getUser_name());
            download.setMedia(DataProcessUtils.getMultiMediaInfo(multiMedia, true));
            download.setReserved(remarks);
            SignAndNursingManager.saveReadilyShootItemInfo(download, multiMedia);
        }
        return true;
    }

    /**
     * 通过条目信息删除指定的随手拍信息
     * @param info 条目信息
     * @return true: 删除成功; false: 删除失败
     */
    public static boolean deleteReadilyShootItemInfo(NursingItemInfo info) {
        return SignAndNursingManager.deleteReadilyShootItemInfo(info);
    }

    /**
     * 更新随手拍信息
     * @param id 条目下载表主键ID
     * @param remarks 备注
     * @param multiMedia 媒体信息
     * @return true: 更新成功; false: 更新失败
     */
    public static boolean updateReadilyShootItemInfo(long id, String remarks, List<MultiMedia> multiMedia) {
        return SignAndNursingManager.updateReadilyShootItemInfo(id, remarks, multiMedia);
    }

}
