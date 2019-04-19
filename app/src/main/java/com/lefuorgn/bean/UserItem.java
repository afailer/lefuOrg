package com.lefuorgn.bean;

import java.util.List;

public class UserItem {

	private long id; // 集团或者政府ID
	private String name; // 当前集团或者政府名称
	private int type; //
    private List<OrgInfo> tblOrganizeAgencyMapBeans; // 当前集团或政府所包含的机构集合信息

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<OrgInfo> getTblOrganizeAgencyMapBeans() {
		return tblOrganizeAgencyMapBeans;
	}

	public void setTblOrganizeAgencyMapBeans(List<OrgInfo> tblOrganizeAgencyMapBeans) {
		this.tblOrganizeAgencyMapBeans = tblOrganizeAgencyMapBeans;
	}

	@Override
	public String toString() {
		return "UserItem{" +
				"id=" + id +
				", name='" + name + '\'' +
				", type=" + type +
				", tblOrganizeAgencyMapBeans=" + tblOrganizeAgencyMapBeans +
				'}';
	}
}
