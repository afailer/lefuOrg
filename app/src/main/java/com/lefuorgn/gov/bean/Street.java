package com.lefuorgn.gov.bean;

import java.io.Serializable;

public class Street implements Serializable {

	private static final long serialVersionUID = 1L;

	private long pid;
	private String region_name;

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	@Override
	public String toString() {
		return "Street [pid=" + pid + ", region_name=" + region_name + "]";
	}

}
