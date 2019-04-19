package com.lefuorgn.gov.bean;

import java.util.List;

public class UseRate {
	List<GraphicsData> bedProportion;//从当前月开始往前六个月的入住率
	List<GraphicsData> bedProportion_last;//去年同期的入住率
	List<GraphicsData> bedTotal;//床位剩余数量
	List<GraphicsData> bedSurplus;//周食谱的id
	List<GraphicsData> bedUse;//床位已使用数量
	
	public List<GraphicsData> getBedProportion() {
		return bedProportion;
	}
	public void setBedProportion(List<GraphicsData> bedProportion) {
		this.bedProportion = bedProportion;
	}
	public List<GraphicsData> getBedProportion_last() {
		return bedProportion_last;
	}
	public void setBedProportion_last(List<GraphicsData> bedProportion_last) {
		this.bedProportion_last = bedProportion_last;
	}
	public List<GraphicsData> getBedTotal() {
		return bedTotal;
	}
	public void setBedTotal(List<GraphicsData> bedTotal) {
		this.bedTotal = bedTotal;
	}
	public List<GraphicsData> getBedSurplus() {
		return bedSurplus;
	}
	public void setBedSurplus(List<GraphicsData> bedSurplus) {
		this.bedSurplus = bedSurplus;
	}
	public List<GraphicsData> getBedUse() {
		return bedUse;
	}
	public void setBedUse(List<GraphicsData> bedUse) {
		this.bedUse = bedUse;
	}
	
}
