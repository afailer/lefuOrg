package com.lefuorgn.lefu.bean;

/**
 * 生日提醒具体日期的老人信息
 */

public class BirthdayDetails {

    private String floor_no; // 楼栋号
    private String floor_layer; // 楼层号
    private String room_no; // 房间号
    private String bed_no; // 床位号
    private String level; // 护理级别
    private int birthday_type; // 生日类型
    private long birthday_dt; // 生日日期
    private String elderly_name; // 老人姓名

    public String getFloor_no() {
        return floor_no;
    }

    public void setFloor_no(String floor_no) {
        this.floor_no = floor_no;
    }

    public String getFloor_layer() {
        return floor_layer;
    }

    public void setFloor_layer(String floor_layer) {
        this.floor_layer = floor_layer;
    }

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    public String getBed_no() {
        return bed_no;
    }

    public void setBed_no(String bed_no) {
        this.bed_no = bed_no;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getBirthday_type() {
        return birthday_type;
    }

    public void setBirthday_type(int birthday_type) {
        this.birthday_type = birthday_type;
    }

    public long getBirthday_dt() {
        return birthday_dt;
    }

    public void setBirthday_dt(long birthday_dt) {
        this.birthday_dt = birthday_dt;
    }

    public String getElderly_name() {
        return elderly_name;
    }

    public void setElderly_name(String elderly_name) {
        this.elderly_name = elderly_name;
    }

    @Override
    public String toString() {
        return "BirthdayDetails{" +
                "floor_no='" + floor_no + '\'' +
                ", floor_layer='" + floor_layer + '\'' +
                ", room_no='" + room_no + '\'' +
                ", bed_no='" + bed_no + '\'' +
                ", level='" + level + '\'' +
                ", birthday_type=" + birthday_type +
                ", birthday_dt=" + birthday_dt +
                ", elderly_name='" + elderly_name + '\'' +
                '}';
    }
}
