package com.lefuorgn.interactive.bean;

import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.model.basic.SignConfig;

import java.util.List;


/**
 * 数据从服务器下载到本地数据库过度类
 */
public class SignAndNursingConfig {

	private int approvalStatus; // 设置的审批状态
	private SignConfig bloodPressure; // 血压配置
	private SignConfig bloodSugar; // 血糖配置
	private SignConfig breathing; // 呼吸配置
	private SignConfig defecation; // 排便配置
	private SignConfig drinkWater; // 饮水配置
	private SignConfig pulse; // 心率配置
	private SignConfig temperature; // 体温配置
	private List<DisplaySignOrNursingItem> dailyNursing; // 可显示护理项条目
	private List<DisplaySignOrNursingItem> signsData; // 可显示体征项条目

    public int getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(int approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public SignConfig getBloodPressure() {
		return bloodPressure;
	}

	public void setBloodPressure(SignConfig bloodPressure) {
		this.bloodPressure = bloodPressure;
	}

	public SignConfig getBloodSugar() {
		return bloodSugar;
	}

	public void setBloodSugar(SignConfig bloodSugar) {
		this.bloodSugar = bloodSugar;
	}

	public SignConfig getBreathing() {
		return breathing;
	}

	public void setBreathing(SignConfig breathing) {
		this.breathing = breathing;
	}

	public SignConfig getDefecation() {
		return defecation;
	}

	public void setDefecation(SignConfig defecation) {
		this.defecation = defecation;
	}

	public SignConfig getDrinkWater() {
		return drinkWater;
	}

	public void setDrinkWater(SignConfig drinkWater) {
		this.drinkWater = drinkWater;
	}

	public SignConfig getPulse() {
		return pulse;
	}

	public void setPulse(SignConfig pulse) {
		this.pulse = pulse;
	}

	public SignConfig getTemperature() {
		return temperature;
	}

	public void setTemperature(SignConfig temperature) {
		this.temperature = temperature;
	}

	public List<DisplaySignOrNursingItem> getDailyNursing() {
		return dailyNursing;
	}

	public void setDailyNursing(List<DisplaySignOrNursingItem> dailyNursing) {
		this.dailyNursing = dailyNursing;
	}

	public List<DisplaySignOrNursingItem> getSignsData() {
		return signsData;
	}

	public void setSignsData(List<DisplaySignOrNursingItem> signsData) {
		this.signsData = signsData;
	}

    @Override
    public String toString() {
        return "SignAndNursingConfig{" +
                "approvalStatus=" + approvalStatus +
                ", bloodPressure=" + bloodPressure +
                ", bloodSugar=" + bloodSugar +
                ", breathing=" + breathing +
                ", defecation=" + defecation +
                ", drinkWater=" + drinkWater +
                ", pulse=" + pulse +
                ", temperature=" + temperature +
                ", dailyNursing=" + dailyNursing +
                ", signsData=" + signsData +
                '}';
    }
}
