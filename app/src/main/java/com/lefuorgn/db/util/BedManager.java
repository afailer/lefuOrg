package com.lefuorgn.db.util;

import com.lefuorgn.db.base.BaseDao;
import com.lefuorgn.db.model.basic.Bed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 床位管理类
 */
public class BedManager {

    /**
     * 获取所有的楼栋号
     *
     * @return 所有的楼栋号
     */
    public static List<String> findAllBuildingNos() {
        // 查找床位记录所有的楼栋号
        BaseDao<Bed, Long> dao = DaoHelper.getInstance().getDao(Bed.class);
        List<Bed> list = dao.queryAll("floor_no", true);
        if(list == null) {
            return new ArrayList<String>();
        }
        Set<String> buffer = new LinkedHashSet<String>();
        for (Bed bed : list) {
            buffer.add(bed.getFloor_no());
        }
        return new ArrayList<String>(buffer);
    }

    /**
     * 获取所有的楼层号
     *
     * @param buildingNo
     *            楼栋号
     * @return 指定楼栋号下的所有楼层号
     */
    public static List<String> findAllUnitNos(String buildingNo) {
        BaseDao<Bed, Long> dao = DaoHelper.getInstance().getDao(Bed.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("floor_no", buildingNo);
        List<Bed> list = dao.queryAll(map, "floor_layer", true);
        if(list == null) {
            return new ArrayList<String>();
        }
        Set<String> buffer = new LinkedHashSet<String>();
        for (Bed bed : list) {
            buffer.add(bed.getFloor_layer());
        }
        return new ArrayList<String>(buffer);
    }

    /**
     * 获取所有的房间号
     * @param buildingNo 楼栋号
     * @param unitNo 楼层号
     * @return 指定楼栋号下的楼层号中所有的房间号
     */
    public static List<String> findAllRoomNos(String buildingNo, String unitNo) {
        BaseDao<Bed, Long> dao = DaoHelper.getInstance().getDao(Bed.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("floor_no", buildingNo);
        map.put("floor_layer", unitNo);
        List<Bed> list = dao.queryAll(map, "room_no", true);
        if(list == null) {
            return new ArrayList<String>();
        }
        Set<String> buffer = new LinkedHashSet<String>();
        for (Bed bed : list) {
            buffer.add(bed.getRoom_no());
        }
        return new ArrayList<String>(buffer);
    }

}
