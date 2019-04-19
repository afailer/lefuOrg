package com.lefuorgn.gov.bean;

import java.util.List;

public class MedicalMsg {
	List<GraphicsData> isInsurance;
	List<GraphicsData> insuranceType;
	List<InsuranceHistory> agencyCountBeans;
	
	public List<GraphicsData> getIsInsurance() {
		return isInsurance;
	}
	public void setIsInsurance(List<GraphicsData> isInsurance) {
		this.isInsurance = isInsurance;
	}
	public List<GraphicsData> getInsuranceType() {
		return insuranceType;
	}
	public void setInsuranceType(List<GraphicsData> insuranceType) {
		this.insuranceType = insuranceType;
	}
	public List<InsuranceHistory> getAgencyCountBeans() {
		return agencyCountBeans;
	}
	public void setAgencyCountBeans(List<InsuranceHistory> agencyCountBeans) {
		this.agencyCountBeans = agencyCountBeans;
	}
	
}
