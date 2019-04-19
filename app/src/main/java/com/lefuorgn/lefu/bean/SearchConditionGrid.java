package com.lefuorgn.lefu.bean;

import java.io.Serializable;

public class SearchConditionGrid implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final int NAME = 0; // 按名字排序
	public static final int ROOM = 1; // 按房间号排序
	
	private String buildingNo; // 楼栋号
	private String unitNo; // 楼层号
	private String roomNo; // 房间号
	private String date; // 日期
	private int sort; // 查询方式,名字排序或者房间号排序
	
	public SearchConditionGrid() {}
	
	public String getBuildingNo() {
		return buildingNo;
	}
	public void setBuildingNo(String buildingNo) {
		this.buildingNo = buildingNo;
	}
	public String getUnitNo() {
		return unitNo;
	}
	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}
	public String getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}

	public void copySearchCondition(SearchConditionGrid condition) {
		buildingNo = condition.getBuildingNo();
		unitNo = condition.getUnitNo();
		roomNo = condition.getRoomNo();
		date = condition.getDate();
		sort = condition.getSort();
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof SearchConditionGrid)) {
			return false;
		}
		SearchConditionGrid s = (SearchConditionGrid) o;
		if(!buildingNo.equals(s.getBuildingNo())) {
			return false;
		}else if(!unitNo.equals(s.getUnitNo())) {
			return false;
		}else if(!roomNo.equals(s.getRoomNo())) {
			return false;
		}else if(!date.equals(s.getDate())) {
			return false;
		}else if(!(sort == s.getSort())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "SearchConditionGrid [buildingNo=" + buildingNo + ", unitNo="
				+ unitNo + ", roomNo=" + roomNo + ", date=" + date + ", sort="
				+ sort + "]";
	}
	
}
