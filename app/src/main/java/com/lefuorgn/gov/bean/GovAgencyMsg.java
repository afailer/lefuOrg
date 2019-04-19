package com.lefuorgn.gov.bean;

import java.io.Serializable;
import java.util.List;

public class GovAgencyMsg implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private List<Organization> agencyInfos;
	private AgencyCount agencyCountBean;
	
	public List<Organization> getAgencyInfos() {
		return agencyInfos;
	}
	
	public void setAgencyInfos(List<Organization> agencyInfos) {
		this.agencyInfos = agencyInfos;
	}
	
	public AgencyCount getAgencyCountBean() {
		return agencyCountBean;
	}
	
	public void setAgencyCountBean(AgencyCount agencyCountBean) {
		this.agencyCountBean = agencyCountBean;
	}

	@Override
	public String toString() {
		return "GovAgencyMsg [agencyInfos=" + agencyInfos
				+ ", agencyCountBean=" + agencyCountBean + "]";
	}
	
}
