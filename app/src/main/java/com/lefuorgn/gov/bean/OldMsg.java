package com.lefuorgn.gov.bean;

import java.util.ArrayList;
import java.util.List;

public class OldMsg {
	List<GraphicsData> sex=new ArrayList<GraphicsData>();
	List<GraphicsData> age=new ArrayList<GraphicsData>();
	List<GraphicsData> diseases=new ArrayList<GraphicsData>();
	public List<GraphicsData> getSex() {
		return sex;
	}
	public void setSex(List<GraphicsData> sex) {
		this.sex = sex;
	}
	public List<GraphicsData> getAge() {
		return age;
	}
	public void setAge(List<GraphicsData> age) {
		this.age = age;
	}
	public List<GraphicsData> getDiseases() {
		return diseases;
	}
	public void setDiseases(List<GraphicsData> diseases) {
		this.diseases = diseases;
	}
	
}
