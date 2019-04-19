package com.lefuorgn.lefu.bean;

import com.lefuorgn.db.model.base.BaseDownload;

/**
 * 体征项数据审核信息类
 */

public class SignDataAudit extends BaseDownload{

    private long id; // 当前条目ID(体温、)
    private long blood_pressure_id; // 血压ID
    private long pulse_id; // 心率ID
    private long blood_sugar_id; // 血糖ID
    private int breathing_times;// 呼吸次数
    private int defecation_times;// 排便次数
    private int water_amount; // 饮水量
    private int meal_amount; // 饮食量   1：偏少 2：正常 3：偏多
    private int meal_type; // 饮食类型 1：早餐 2：午餐 3：晚餐
    private int high_blood_pressure; // 高血压值
    private int low_blood_pressure; // 低血压值
    private int pulse_number; // 心率值
    private String sleep_quality; // 睡眠质量
    private double blood_sugar; // 血糖值
    private double temperature; // 体温值
    private boolean select; // 当前条目是否被选中

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBlood_pressure_id() {
        return blood_pressure_id;
    }

    public void setBlood_pressure_id(long blood_pressure_id) {
        this.blood_pressure_id = blood_pressure_id;
    }

    public long getPulse_id() {
        return pulse_id;
    }

    public void setPulse_id(long pulse_id) {
        this.pulse_id = pulse_id;
    }

    public long getBlood_sugar_id() {
        return blood_sugar_id;
    }

    public void setBlood_sugar_id(long blood_sugar_id) {
        this.blood_sugar_id = blood_sugar_id;
    }

    public int getBreathing_times() {
        return breathing_times;
    }

    public void setBreathing_times(int breathing_times) {
        this.breathing_times = breathing_times;
    }

    public int getDefecation_times() {
        return defecation_times;
    }

    public void setDefecation_times(int defecation_times) {
        this.defecation_times = defecation_times;
    }

    public int getWater_amount() {
        return water_amount;
    }

    public void setWater_amount(int water_amount) {
        this.water_amount = water_amount;
    }

    public int getMeal_amount() {
        return meal_amount;
    }

    public void setMeal_amount(int meal_amount) {
        this.meal_amount = meal_amount;
    }

    public int getMeal_type() {
        return meal_type;
    }

    public void setMeal_type(int meal_type) {
        this.meal_type = meal_type;
    }

    public int getHigh_blood_pressure() {
        return high_blood_pressure;
    }

    public void setHigh_blood_pressure(int high_blood_pressure) {
        this.high_blood_pressure = high_blood_pressure;
    }

    public int getLow_blood_pressure() {
        return low_blood_pressure;
    }

    public void setLow_blood_pressure(int low_blood_pressure) {
        this.low_blood_pressure = low_blood_pressure;
    }

    public int getPulse_number() {
        return pulse_number;
    }

    public void setPulse_number(int pulse_number) {
        this.pulse_number = pulse_number;
    }

    public String getSleep_quality() {
        return sleep_quality;
    }

    public void setSleep_quality(String sleep_quality) {
        this.sleep_quality = sleep_quality;
    }

    public double getBlood_sugar() {
        return blood_sugar;
    }

    public void setBlood_sugar(double blood_sugar) {
        this.blood_sugar = blood_sugar;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    @Override
    public String toString() {
        return "SignDataAudit{" +
                "id=" + id +
                ", blood_pressure_id=" + blood_pressure_id +
                ", pulse_id=" + pulse_id +
                ", blood_sugar_id=" + blood_sugar_id +
                ", breathing_times=" + breathing_times +
                ", defecation_times=" + defecation_times +
                ", water_amount=" + water_amount +
                ", meal_amount=" + meal_amount +
                ", meal_type=" + meal_type +
                ", high_blood_pressure=" + high_blood_pressure +
                ", low_blood_pressure=" + low_blood_pressure +
                ", pulse_number=" + pulse_number +
                ", sleep_quality='" + sleep_quality + '\'' +
                ", blood_sugar=" + blood_sugar +
                ", temperature=" + temperature +
                ", select=" + select +
                '}';
    }

}
