package com.lefuorgn.bean;

public class UseRateItem {
	String bedTotalNum;//总床位数
	String bedLeftNum;//剩余床位数
	String bedLiveRate;//入住率
	String time;//“时间title”
	
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getBedTotalNum() {
		return bedTotalNum;
	}
	public void setBedTotalNum(String bedTotalNum) {
		this.bedTotalNum = bedTotalNum;
	}
	public String getBedLeftNum() {
		return bedLeftNum;
	}
	public void setBedLeftNum(String bedLeftNum) {
		this.bedLeftNum = bedLeftNum;
	}
	public String getBedLiveRate() {
		return bedLiveRate;
	}
	public void setBedLiveRate(String bedLiveRate) {
		this.bedLiveRate = bedLiveRate;
	}
	public UseRateItem(String time,String bedTotalNum, String bedLeftNum,
			String bedLiveRate) {
		super();
		this.time=time;
		this.bedTotalNum = bedTotalNum;
		this.bedLeftNum = bedLeftNum;
		this.bedLiveRate = bedLiveRate;
	}
	public UseRateItem() {
		super();
	}
	
}
