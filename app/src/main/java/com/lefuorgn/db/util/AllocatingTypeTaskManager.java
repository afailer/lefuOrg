package com.lefuorgn.db.util;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.DatabaseConnection;
import com.lefuorgn.bean.User;
import com.lefuorgn.db.base.BaseDao;
import com.lefuorgn.db.model.basic.AllocatingTypeTask;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.lefu.bean.AllocatingTaskExecute;
import com.lefuorgn.lefu.bean.AllocatingTaskExecuteOption;
import com.lefuorgn.lefu.bean.MultiMedia;
import com.lefuorgn.lefu.bean.NursingOldPeople;
import com.lefuorgn.util.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 配单任务管理类
 */

public class AllocatingTypeTaskManager {

    private static final long PAGE_SIZE = 10;
    private static final String FORMAT = "yyyy-MM-dd";

    /**
     * 获取要执行的配单(分页加载数据)
     * @param agencyId 机构ID
     * @param pageNo 当前页面页号
     * @param local 是否要加载本地添加的数据
     * @return 执行中的配单
     */
    public static List<AllocatingTaskExecute> getAllocatingTaskExecute(long agencyId, int pageNo, boolean local) {
        // 用于存放结果的缓存
        List<AllocatingTaskExecute> resultData = new ArrayList<AllocatingTaskExecute>();
        // 获取配单的老人信息
        List<OldPeople> oldPeopleList = getOldPeople(pageNo);
        // 获取所有的配单
        List<AllocatingTypeTask> taskData = getAllocatingTypeTask(agencyId);
        // 过滤配单时间
        Set<String> taskTime = new HashSet<String>();
        for (AllocatingTypeTask task : taskData) {
            taskTime.add(StringUtils.getFormatData(task.getTask_time(), FORMAT));
        }
        // 获取并转换成要得到的数据
        for (String time : taskTime) {
            long[] times = StringUtils.getSectionTime(time, FORMAT);
            resultData.addAll(getAllocatingTaskExecute(agencyId, oldPeopleList, times, local));
        }
        return resultData;
    }

    /**
     * 从本地数据库中获取来自服务器的数据
     * @param isAll true: 所有数据(不包含本地添加的); false: 已经完成的数据
     * @return 配单信息集合
     */
    public static List<AllocatingTypeTask> getAllocatingTypeTask(boolean isAll) {
        BaseDao<AllocatingTypeTask, Long> dao = DaoHelper
                .getInstance().getDao(AllocatingTypeTask.class);
        List<AllocatingTypeTask> list = new ArrayList<AllocatingTypeTask>();
        try {
            QueryBuilder<AllocatingTypeTask, Long> queryBuilder = dao.getDao().queryBuilder();
            Where<AllocatingTypeTask, Long> where = queryBuilder.where();
            where.eq("save_type", AllocatingTypeTask.TYPE_FROM_SERVICE);
            if(!isAll) {
                // 加载已经完成的数据
                where.and().eq("task_state", 3);
            }
            list = dao.getDao().query(queryBuilder.prepare());
            return list != null ? list : new ArrayList<AllocatingTypeTask>();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     *  删除指定的配单信息包括多有的用户添加配单信息
     * @param list 指定的配单信息
     */
    public static void deleteAllocatingTypeTaskAndLocalTask(List<AllocatingTypeTask> list) {
        BaseDao<AllocatingTypeTask, Long> dao = DaoHelper
                .getInstance().getDao(AllocatingTypeTask.class);
        if(list != null && list.size() > 0) {
            dao.deleteList(list);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("save_type", AllocatingTypeTask.TYPE_FROM_DB);
        dao.deleteList(map);
    }

    /**
     * 删除配单信息中所有老人的数据
     * @param list 配单信息
     */
    public static void deleteAllocatingTypeTask(List<AllocatingTaskExecute> list) {
        try {
            BaseDao<AllocatingTypeTask, Long> dao = DaoHelper
                    .getInstance().getDao(AllocatingTypeTask.class);
            Dao<AllocatingTypeTask, Long> tDao = dao.getDao();
            DatabaseConnection databaseConnection = null;
            try {
                databaseConnection = tDao.startThreadConnection();
                tDao.setAutoCommit(databaseConnection, false);
                // 删除数据
                for (AllocatingTaskExecute task : list) {
                    DeleteBuilder<AllocatingTypeTask, Long> deleteBuilder = tDao.deleteBuilder();
                    deleteBuilder.where().eq("old_people_id", task.getOld_people_id());
                    PreparedDelete<AllocatingTypeTask> preparedDelete = deleteBuilder.prepare();
                    tDao.delete(preparedDelete);
                }
                // 提交事务
                tDao.commit(databaseConnection);
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
    }

    /**
     * 删除数据库中配单信息
     * @param list 数据集合
     */
    public static void deleteAllocatingTypeTaskById(List<AllocatingTypeTask> list) {
        BaseDao<AllocatingTypeTask, Long> dao = DaoHelper
                .getInstance().getDao(AllocatingTypeTask.class);
        dao.deleteList(list);
    }

    /**
     * 更新数据库中的配单信息, 并添加护理信息
     * @param user 用户信息
     * @param agencyId 机构ID
     * @param oldPeopleId 老人ID
     * @param oldPeopleName 老人名称
     * @param option 展示数据的配单数据
     * @param multiMedia 多媒体信息集合
     * @return true : 保存成功; false : 保存失败
     */
    public static boolean updateAllocatingTaskExecute(User user, long agencyId, long oldPeopleId, String oldPeopleName,
              AllocatingTaskExecuteOption option, List<MultiMedia> multiMedia) {
        BaseDao<AllocatingTypeTask, Long> dao = DaoHelper
                .getInstance().getDao(AllocatingTypeTask.class);
        AllocatingTypeTask task = switchAllocatingTypeTask(oldPeopleId, oldPeopleName, option);
        // 保存配单信息
        if(option.getId() > 0) {
            // 数据库原本存在数据
            dao.update(task);
        }else {
            // 数据库不存在数据
            dao.insert(task);
        }
        // 保存为护理信息
        List<NursingOldPeople> data = new ArrayList<NursingOldPeople>();
        NursingOldPeople people = new NursingOldPeople();
        people.setOld_people_id(oldPeopleId);
        people.setOld_people_name(oldPeopleName);
        data.add(people);
        return OldPeopleManager
                .saveNursingItemInfo(user, agencyId, option.getNursing_item_id(), option.getRemark(), multiMedia, data);
    }

    /**
     * 判断当前表是否存在过期数据
     * @return true 存在; false 不存在
     */
    public static boolean isExistExpiredData() {
        long[] times = StringUtils.getSectionTime(System.currentTimeMillis());
        BaseDao<AllocatingTypeTask, Long> dao = DaoHelper
                .getInstance().getDao(AllocatingTypeTask.class);
        try {
            QueryBuilder<AllocatingTypeTask, Long> queryBuilder = dao.getDao().queryBuilder();
            queryBuilder.where()
                    .eq("save_type", AllocatingTypeTask.TYPE_FROM_SERVICE)
                    .and()
                    .lt("task_time", times[0]);
            queryBuilder.offset((long) 0)
                    .limit((long) 2);
            List<AllocatingTypeTask> list = dao.getDao().query(queryBuilder.prepare());
            if(list != null && list.size() > 0) {
                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将配单信息转换成数据库保存的数据格式
     * @param oldPeopleId 老人ID
     * @param oldPeopleName 老人名称
     * @param option 展示数据的配单数据
     * @return 数据库配单数据格式
     */
    private static AllocatingTypeTask switchAllocatingTypeTask(
            long oldPeopleId, String oldPeopleName, AllocatingTaskExecuteOption option) {
        AllocatingTypeTask task = new AllocatingTypeTask();
        task.set_id(option.get_id());
        task.setId(option.getId());
        task.setAgency_id(option.getAgency_id());
        task.setHead_nurse_id(option.getHead_nurse_id());
        task.setOld_people_id(oldPeopleId);
        task.setOld_people_name(oldPeopleName);
        task.setNumber_nursing(option.getTotal());
        task.setNumber_current(option.getComplete());
        task.setCare_workers(option.getCare_workers());
        task.setCare_worker(option.getCare_worker());
        task.setWorker_name(option.getWorker_name());
        task.setNursing_item_id(option.getNursing_item_id());
        task.setContent(option.getNursing_item_name());
        task.setCreate_time(option.getCreate_time());
        task.setUpdate_time(option.getUpdate_time());
        task.setTask_time(option.getTask_time());
        // 完成数量大于等于指定的数量时设置为完成状态3
        task.setTask_state(option.getComplete() >= option.getTotal() ? 3 : 2);
        task.setSave_type(option.getSave_type());
        task.setRemark(option.getRemark());
        return task;
    }

    /**
     * 获取要执行的配单
     * @param agencyId 机构ID
     * @param oldPeoples 老人列表
     * @param times 时间区间
     * @param local 是否加载当前用户手动添加的数据
     * @return 执行的配单
     */
    private static List<AllocatingTaskExecute> getAllocatingTaskExecute(long agencyId
            , List<OldPeople> oldPeoples, long[] times, boolean local) {
        List<AllocatingTaskExecute> resultData = new ArrayList<AllocatingTaskExecute>();
        for (OldPeople people : oldPeoples) {
            List<AllocatingTypeTask> tasks = getAllocatingTypeTask(agencyId, people.getId(), times);
            if(tasks.size() > 0) {
                AllocatingTaskExecute task = new AllocatingTaskExecute();
                task.setOld_people_id(people.getId());
                task.setOld_people_name(people.getElderly_name());
                task.setTask_time(tasks.get(0).getTask_time());
                task.setOptions(new ArrayList<AllocatingTaskExecuteOption>());
                for (AllocatingTypeTask typeTask : tasks) {
                    if(typeTask.getSave_type() == AllocatingTypeTask.TYPE_FROM_DB && !local) {
                        // 不加载本地添加的数据
                        continue;
                    }
                    AllocatingTaskExecuteOption option = new AllocatingTaskExecuteOption();
                    option.set_id(typeTask.get_id());
                    option.setId(typeTask.getId());
                    option.setAgency_id(typeTask.getAgency_id());
                    option.setHead_nurse_id(typeTask.getHead_nurse_id());
                    option.setCare_workers(typeTask.getCare_workers());
                    option.setCare_worker(typeTask.getCare_worker());
                    option.setWorker_name(typeTask.getWorker_name());
                    option.setNursing_item_id(typeTask.getNursing_item_id());
                    option.setNursing_item_name(SignConfigManager.getNursingName(typeTask.getNursing_item_id()));
                    option.setTotal(typeTask.getNumber_nursing());
                    option.setComplete(typeTask.getNumber_current());
                    option.setPercentage(typeTask.getNumber_current() + "/" + typeTask.getNumber_nursing());
                    option.setCreate_time(typeTask.getCreate_time());
                    option.setUpdate_time(typeTask.getUpdate_time());
                    option.setTask_time(typeTask.getTask_time());
                    option.setRemark(typeTask.getRemark());
                    option.setSave_type(typeTask.getSave_type());
                    task.getOptions().add(option);
                }
                if(task.getOptions().size() != 0) {
                    resultData.add(task);
                }
            }
        }
        return resultData;
    }

    /**
     * 获取配单信息
     * @param id 机构ID
     * @return 本地数据库中配单信息
     */
    private static List<AllocatingTypeTask> getAllocatingTypeTask(long id) {
        BaseDao<AllocatingTypeTask, Long> dao = DaoHelper
                .getInstance().getDao(AllocatingTypeTask.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agency_id", id);
        List<AllocatingTypeTask> list = dao.queryAll(map, "task_time" , false);
        if(list == null) {
            list = new ArrayList<AllocatingTypeTask>();
        }
        return list;
    }

    /**
     * 获取配单信息
     * @param id 机构ID
     * @param oldPeopleId 老人ID
     * @param times 某天时间最小值和最大值
     * @return 本地数据库中配单信息
     */
    private static List<AllocatingTypeTask> getAllocatingTypeTask(long id, long oldPeopleId, long[] times) {
        BaseDao<AllocatingTypeTask, Long> dao = DaoHelper
                .getInstance().getDao(AllocatingTypeTask.class);
        List<AllocatingTypeTask> list = new ArrayList<AllocatingTypeTask>();
        try {
            QueryBuilder<AllocatingTypeTask, Long> queryBuilder = dao.getDao().queryBuilder();
            queryBuilder.where()
                    .eq("agency_id", id)
                    .and()
                    .eq("old_people_id", oldPeopleId)
                    .and()
                    .ge("task_time", times[0])
                    .and()
                    .le("task_time", times[1]);
            list = dao.getDao().query(queryBuilder.prepare());
            return list != null ? list : new ArrayList<AllocatingTypeTask>();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 分页获取老人信息
     */
    private static List<OldPeople> getOldPeople(int pageNo) {
        long offset = (pageNo - 1) * PAGE_SIZE;
        // 老人表操作对象
        BaseDao<OldPeople, Long> oDao = DaoHelper
                .getInstance().getDao(OldPeople.class);
        // 配单条目信息表操作对象
        BaseDao<AllocatingTypeTask, Long> aDao = DaoHelper
                .getInstance().getDao(AllocatingTypeTask.class);
        try {
            QueryBuilder<OldPeople, Long> oQueryBuilder = oDao.getDao().queryBuilder();
            QueryBuilder<AllocatingTypeTask, Long> aQueryBuilder = aDao.getDao().queryBuilder();
            oQueryBuilder.join("id", "old_people_id", aQueryBuilder)
                    .distinct()
                    .offset(offset)
                    .limit(PAGE_SIZE);
            List<OldPeople> list = oDao.queryList(oQueryBuilder.prepare());
            return list != null ? list : new ArrayList<OldPeople>();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<OldPeople>();
    }

}
