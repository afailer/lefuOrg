package com.lefuorgn.util;

import com.lefuorgn.db.model.basic.OldPeople;

import java.util.Comparator;

/**
 * 老人按房间号排序比较器
 */

public class RoomComparator implements Comparator<OldPeople> {

    @Override
    public int compare(OldPeople lhs, OldPeople rhs) {
        // 校验楼栋
        if(StringUtils.isEmpty(lhs.getFloor_no()) && StringUtils.isEmpty(rhs.getFloor_no())) {
            // 校验楼层
            return compareFloorLayer(lhs, rhs);
        }
        if(StringUtils.isEmpty(lhs.getFloor_no())) {
            return 1;
        }
        if(StringUtils.isEmpty(rhs.getFloor_no())) {
            return -1;
        }
        if(lhs.getFloor_no().equals(rhs.getFloor_no())) {
            // 校验楼层
            return compareFloorLayer(lhs, rhs);
        }
        return lhs.getFloor_no().compareTo(rhs.getFloor_no());
    }

    /**
     * 比较楼层号
     */
    private int compareFloorLayer(OldPeople lhs, OldPeople rhs) {
        // 校验楼层
        if(StringUtils.isEmpty(lhs.getFloor_layer()) && StringUtils.isEmpty(rhs.getFloor_layer())) {
            // 校验房间号
            return compareRoom(lhs, rhs);
        }
        if(StringUtils.isEmpty(lhs.getFloor_layer())) {
            return 1;
        }
        if(StringUtils.isEmpty(rhs.getFloor_layer())) {
            return -1;
        }
        if(lhs.getFloor_layer().equals(rhs.getFloor_layer())) {
            // 校验房间号
            return compareRoom(lhs, rhs);
        }
        return lhs.getFloor_layer().compareTo(rhs.getFloor_layer());
    }

    /**
     * 比较房间号
     */
    private int compareRoom(OldPeople lhs, OldPeople rhs) {
        if(StringUtils.isEmpty(lhs.getRoom_no()) && StringUtils.isEmpty(rhs.getRoom_no())) {
            return 0;
        }
        if(StringUtils.isEmpty(lhs.getRoom_no())) {
            return 1;
        }
        if(StringUtils.isEmpty(rhs.getRoom_no())) {
            return -1;
        }
        return lhs.getRoom_no().compareTo(rhs.getRoom_no());
    }

}
