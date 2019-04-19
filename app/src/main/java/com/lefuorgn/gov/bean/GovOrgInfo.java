package com.lefuorgn.gov.bean;

import java.io.Serializable;

public class GovOrgInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String provinces;
	private String cities;
	private String areas;
	private String street;
	private String agencyInfos;

	public String getProvinces() {
		return provinces;
	}

	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}

	public String getCities() {
		return cities;
	}

	public void setCities(String cities) {
		this.cities = cities;
	}

	public String getAreas() {
		return areas;
	}

	public void setAreas(String areas) {
		this.areas = areas;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getAgencyInfos() {
		return agencyInfos;
	}

	public void setAgencyInfos(String agencyInfos) {
		this.agencyInfos = agencyInfos;
	}

	@Override
	public String toString() {
		return "GovOrgInfo [provinces=" + provinces + ", cities=" + cities
				+ ", areas=" + areas + ", street=" + street + ", agencyInfos="
				+ agencyInfos + "]";
	}

}
