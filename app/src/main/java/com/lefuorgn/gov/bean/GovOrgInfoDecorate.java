package com.lefuorgn.gov.bean;

import java.io.Serializable;
import java.util.List;

public class GovOrgInfoDecorate implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<RegionInfo> provinces;
	private List<RegionInfo> cities;
	private List<RegionInfo> areas;
	private List<Street> street;
	private List<Organization> agencyInfos;

	public List<RegionInfo> getProvinces() {
		return provinces;
	}

	public void setProvinces(List<RegionInfo> provinces) {
		this.provinces = provinces;
	}

	public List<RegionInfo> getCities() {
		return cities;
	}

	public void setCities(List<RegionInfo> cities) {
		this.cities = cities;
	}

	public List<RegionInfo> getAreas() {
		return areas;
	}

	public void setAreas(List<RegionInfo> areas) {
		this.areas = areas;
	}

	public List<Street> getStreet() {
		return street;
	}

	public void setStreet(List<Street> street) {
		this.street = street;
	}

	public List<Organization> getAgencyInfos() {
		return agencyInfos;
	}

	public void setAgencyInfos(List<Organization> agencyInfos) {
		this.agencyInfos = agencyInfos;
	}

	@Override
	public String toString() {
		return "GovOrgInfoDecorate [provinces=" + provinces + ", cities="
				+ cities + ", areas=" + areas + ", street=" + street
				+ ", agencyInfos=" + agencyInfos + "]";
	}

}
