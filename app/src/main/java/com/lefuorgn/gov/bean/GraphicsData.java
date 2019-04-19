package com.lefuorgn.gov.bean;

public class GraphicsData {
	private String name;//坐标轴上刻度的名称
	private String color;//每段刻度的颜色
	private double y;//刻度对应的值
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public GraphicsData(String name, String color, double y) {
		super();
		this.name = name;
		this.color = color;
		this.y = y;
	}
	public GraphicsData() {
		super();
	}
	
	
}
